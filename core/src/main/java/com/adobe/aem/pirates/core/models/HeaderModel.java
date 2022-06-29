package com.adobe.aem.pirates.core.models;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class HeaderModel {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderModel.class);

    private static final String PN_NAVIGATION_TITLE = "navTitle";

    private static final String PN_INTERNAL_TITLE = "internalTitle";

    private static final String	PN_SLING_RESOURCETYPE =	"sling:resourceType";

    private static final String RT_LANDING_PAGE = "xyz/components/structure/landing-page";

    private static final String TEMP_VERSION = "/tmp/versionhistory";

    @Self
    private Resource resource;

    @Inject
    private PageManager pageManager;

    private String landingPageTitle = StringUtils.EMPTY;

    @PostConstruct
    protected void init() {
        Page landingPage = findLandingPage(pageManager.getContainingPage(resource));
        if(landingPage != null) {
            landingPageTitle = landingPage.getProperties().get(PN_NAVIGATION_TITLE, landingPage.getProperties().get(PN_INTERNAL_TITLE, StringUtils.EMPTY));
        }
    }

    protected Page findLandingPage(Page currentPage) {
        if(RT_LANDING_PAGE.equals(currentPage.getProperties().get(PN_SLING_RESOURCETYPE))) {
            return currentPage;
        }
        if (!currentPage.getPath().contains(TEMP_VERSION)) {
            while (currentPage.getDepth() > 2) {
                currentPage = currentPage.getParent();
                if (RT_LANDING_PAGE.equals(currentPage.getProperties().get(PN_SLING_RESOURCETYPE))) {
                    return currentPage;
                }
            }
        }
        return null;
    }

    public String getLandingPageTitle() {
        return landingPageTitle;
    }
}