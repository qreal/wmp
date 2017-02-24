package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.exceptions.WrongAuthException;
import com.qreal.wmp.uitesting.services.Auther;
import com.qreal.wmp.uitesting.services.Opener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(PageLoader.class);
	
	private final PageFactory pageFactory;
	
	private final Opener opener;
	
	private final Auther auther;
	
	public PageLoader(PageFactory pageFactory, Opener opener, Auther auther) {
		this.pageFactory = pageFactory;
		this.opener = opener;
		this.auther = auther;
	}
	
	public <T> T load(Page page) {
		opener.open(page.getIdentify());
		// wait for load
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
		return getPage(page);
	}
	
	public <T> T load(Page page, String username, String password) throws WrongAuthException {
		auther.auth(username, password);
		opener.open(page.getIdentify());
		// wait for load
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
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
