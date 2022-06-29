package com.adobe.aem.pirates.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class HeaderModelTest {

    @Mock
    Resource resource;

    @Mock
    PageManager pageManager;

    @Mock
    Page page;

    @Mock
    ValueMap mockProperty;

    @InjectMocks
    HeaderModel headerModel;


    @Before
    public void setUp() throws Exception {

        when(page.getDepth()).thenReturn(5);
    }

    @Test(expected = Exception.class)
    public void no_page_has_resource(){
        headerModel.init();
    }

    @Test
    public void resourceType_is_landing_page() {
        when(pageManager.getContainingPage((Resource) any())).thenReturn(page);
        when(page.getProperties()).thenReturn(mockProperty);
        when(page.getProperties().get(any())).thenReturn("xyz/components/structure/landing-page");
        when(page.getProperties().get(anyString(),anyString())).thenReturn("","Mock Nav title");

        headerModel.init();
        assertEquals("Mock Nav title",headerModel.getLandingPageTitle());
    }

    @Test
    public void currentPath_not_tmpPath() {
        when(pageManager.getContainingPage((Resource) any())).thenReturn(page);
        when(page.getProperties()).thenReturn(mockProperty);
        when(page.getProperties().get(any())).thenReturn("xyz/components/structure/page","xyz/components/structure/landing-page");
        when(page.getPath()).thenReturn("/content/cl-ma-sp");
        when(page.getParent()).thenReturn(page,null);
        when(page.getProperties().get(anyString(),anyString())).thenReturn("Mock Internal Title");

        headerModel.init();
        assertEquals("Mock Internal Title",headerModel.getLandingPageTitle());
    }

    @Test
    public void not_landing_is_tmp_path() {
        when(pageManager.getContainingPage((Resource) any())).thenReturn(page);
        when(page.getProperties()).thenReturn(mockProperty);
        when(page.getProperties().get(any())).thenReturn("xyz/components/structure/page");
        when(page.getPath()).thenReturn("/tmp/versionhistory/mock");

        headerModel.init();
        assertEquals(StringUtils.EMPTY, headerModel.getLandingPageTitle());
    }
}