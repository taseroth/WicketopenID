package org.faboo.openIdTest.ui;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.faboo.openIdTest.service.LoginService;
import org.faboo.openIdTest.ui.login.LoginPage;

public class WicketApplication extends WebApplication {

    private LoginService loginService;

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<HomePage> getHomePage() {
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		super.init();

        getSecuritySettings().setAuthorizationStrategy(new AuthStrategy());

		// add your configuration here
        mountPage("loginDo", LoginPage.class);
        mountPage("SampleSecuredPage", MountedSecuredPage.class);

        loginService = new LoginService();

	}

    @Override
    public Session newSession(Request request, Response response) {
        return new SampleSession(request);
    }

    public LoginService getLoginService() {
        return loginService;
    }
}
