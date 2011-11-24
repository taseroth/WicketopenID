package org.faboo.openIdTest.ui.login;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.faboo.openIdTest.service.LoginService;
import org.faboo.openIdTest.ui.WicketApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Simple HTML Page to display the login form.
 * There is no wicket logic / components here, only some html
 * to display a few OpenID provider logos to click on.
 */
public class LoginPage extends WebPage {

    private static Logger logger = LoggerFactory.getLogger(LoginPage.class);

    private String openIDIdentifier;
    public LoginPage() {

        final TextField<String> oid_identifier
                =  new TextField<String>("openid_identifier", new PropertyModel<String>(this, "openIDIdentifier"));
        add(new Form("loginForm") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                logger.trace("onSubmit: {}", openIDIdentifier);
                LoginService loginService = ((WicketApplication) WicketApplication.get()).getLoginService();

                HttpServletRequest req = (HttpServletRequest)(RequestCycle.get().getRequest()).getContainerRequest();

                String relativePagePath = RequestCycle.get().mapUrlFor(OpenIdCallbackPage.class,null).toString();

                String requestURL = req.getRequestURL().toString();

                // wicket 1.5 can 't cope with requestURLs that contain more than one /
                String callbackURL = RequestUtils.toAbsolutePath(requestURL, relativePagePath);

                loginService.startLogin(openIDIdentifier, callbackURL
                        , (HttpServletRequest) getRequest().getContainerRequest());

            }
        }.add(oid_identifier));
    }
}
