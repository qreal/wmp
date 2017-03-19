package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.exceptions.WrongAuthException;
import com.qreal.wmp.uitesting.services.Auther;
import com.qreal.wmp.uitesting.services.Opener;

/** Loads page.
 * It means, firstly, it opens uri by Opener service.
 * Secondly, it returns page by PageFactory.
 * */
public class PageLoader {
    
    private final PageFactory pageFactory;
    
    private final Opener opener;
    
    private final Auther auther;
    
    public PageLoader(PageFactory pageFactory, Opener opener, Auther auther) {
        this.pageFactory = pageFactory;
        this.opener = opener;
        this.auther = auther;
    }
    
    /** Loads and returns requested page with default authentication. */
    public <T> T load(Page page) {
        opener.open(page.getIdentify());
        return getPage(page);
    }
    
    /** Loads and returns requested page with login and password. */
    public <T> T load(Page page, String username, String password) throws WrongAuthException {
        auther.auth(username, password);
        opener.open(page.getIdentify());
        return getPage(page);
    }
    
    @SuppressWarnings("unchecked")
    private <T> T getPage(Page page) {
        switch (page) {
            case Auth: return (T) pageFactory.getAuthPage();
            case Dashboard: return (T) pageFactory.getDashboardPage();
            case EditorBPMN: return (T) pageFactory.getEditorPage();
            case EditorRobots: return (T) pageFactory.getEditorPage();
            default: return null;
        }
    }
}