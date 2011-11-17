package org.faboo.openIdTest.ui.login;

import org.apache.wicket.markup.html.WebPage;
import org.faboo.openIdTest.service.LoginService;
import org.faboo.openIdTest.ui.SampleSession;
import org.faboo.openIdTest.ui.WicketApplication;

import javax.servlet.http.HttpServletRequest;

/**
 * Callback page (endpoint) the OpenID provider calls to finish the auth process.
 * No HTML / Wicket components needed.
 */
public class OpenIdCallbackPage extends WebPage {

    public OpenIdCallbackPage() {

        LoginService loginService = ((WicketApplication) WicketApplication.get()).getLoginService();

        SampleSession.get().setUserEmail(
                loginService.finishLogin((HttpServletRequest) getRequest().getContainerRequest()));
    }
}
