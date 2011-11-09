package org.faboo.openIdTest.ui.login;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.faboo.openIdTest.service.LoginService;
import org.faboo.openIdTest.ui.WicketApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * This is the page that the LoginPage html submits the form to.
 * Starts the login process. No html or Wicket components.
 */
public class Consumer extends WebPage {

    private static Logger logger = LoggerFactory.getLogger(Consumer.class);

    /**
     * Receives the OpenID provider URL in the parameters.
     * Starts the login process.
     * @param parameters Contains the OpenID provider URL in a parameter with name 'openid_identifier'
     */
    public Consumer(PageParameters parameters) {
        super(parameters);

        String openIDIdentifier = parameters.get("openid_identifier").toString();

        LoginService loginService = ((WicketApplication) WicketApplication.get()).getLoginService();

        HttpServletRequest req = (HttpServletRequest)(RequestCycle.get().getRequest()).getContainerRequest();

        String relativePagePath = RequestCycle.get().mapUrlFor(OpenIdCallbackPage.class,null).toString();

        String requestURL = req.getRequestURL().toString();

        // wicket 1.5 can 't cope with requestURLs that contain more

        String callbackURL = RequestUtils.toAbsolutePath(requestURL, relativePagePath);

        logger.trace("relativePagePath: {}, callbackURL: {}", relativePagePath, callbackURL);

        logger.trace("***: {}", req.getRequestURL().toString());

        logger.trace("###: {}", RequestCycle.get().mapUrlFor(OpenIdCallbackPage.class, null).toAbsoluteString());
        logger.trace("+++: {}", RequestUtils.toAbsolutePath(
                req.getRequestURL().toString()
                , RequestCycle.get().mapUrlFor(OpenIdCallbackPage.class, null).toAbsoluteString()));
        loginService.startLogin(openIDIdentifier, callbackURL);
    }
}
