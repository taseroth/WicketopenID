package org.faboo.openIdTest.ui;

import org.apache.wicket.protocol.http.WebApplication;
import org.faboo.openIdTest.service.LoginService;
import org.faboo.openIdTest.ui.login.Consumer;
import org.faboo.openIdTest.ui.login.LoginPage;
import org.faboo.openIdTest.ui.login.OpenIdCallbackPage;

public class WicketApplication extends WebApplication {

    private LoginService loginService;

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<HomePage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

		// add your configuration here
        mountPage("loginCallback", OpenIdCallbackPage.class);
        mountPage("loginDo", LoginPage.class);
        mountPage("loginConsumer", Consumer.class);

        loginService = new LoginService();

	}

    public LoginService getLoginService() {
        return loginService;
    }
}
