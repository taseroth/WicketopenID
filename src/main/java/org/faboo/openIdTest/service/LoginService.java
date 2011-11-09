package org.faboo.openIdTest.service;

import org.apache.wicket.request.flow.RedirectToUrlException;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Service to process OpenID login requests.
 * Uses the openid4java lib (http://http://code.google.com/p/openid4java/) to handle the nifty details.
 * The flow is sampled after the expample at http://http://code.google.com/p/openid4java/wiki/SampleConsumer
 */
public class LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    private ConsumerManager consumerManager;

    private DiscoveryInformation dirtyDiscovery;

    public LoginService() {
        this.consumerManager = new ConsumerManager();
    }

    /**
     * Establishes a relation with the OpenID provider and constructs the auth requests.
     * @param openIDProvider URL of the OpenID provider.
     * @param callbackURL URL the provider calls us under to finish the auth process.
     */
    @SuppressWarnings("unchecked")
    public void startLogin(String openIDProvider, String callbackURL) {

        logger.trace("startLogin() openIDProvider:{}, callbackURL:{} ", openIDProvider ,callbackURL);

        try {
            List<DiscoveryInformation> discoveries = consumerManager.discover(openIDProvider);

            for(DiscoveryInformation information : discoveries) {
                logger.trace("DiscoveryInformation: {}" ,information.toString());
            }

            DiscoveryInformation discovered = consumerManager.associate(discoveries);
            logger.trace("discovered: {}" ,discovered.toString());
            dirtyDiscovery = discovered;

            AuthRequest authReq = consumerManager.authenticate(discovered, callbackURL);
            FetchRequest fetch = FetchRequest.createFetchRequest();
            fetch.addAttribute("email",
                    // attribute alias
                    "http://schema.openid.net/contact/email",   // type URI
                    true);                                      // required

            // attach the extension to the authentication request
            authReq.addExtension(fetch);

            throw new RedirectToUrlException(authReq.getDestinationUrl(true));

        } catch (OpenIDException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process of the callback from the provider.
     * 2. part of the auth process. The provider calls us back at the callback url and the
     * request gets processed here.
     * The email of the user gets retrieved and returned.
     * @param request the request containing the parameters from the callback.
     * @return the email of the user
     * TODO: hanlde login errors in some way (exception?)
     */
    public String finishLogin(HttpServletRequest request)  {

        try {

            logger.trace("finishLogin: {}" ,request.getParameterMap());

            ParameterList response = new ParameterList(request.getParameterMap());

            // extract the receiving URL from the HTTP request
            StringBuffer receivingURL = request.getRequestURL();
            logger.trace("receivingURL: {}" ,receivingURL);
            String queryString = request.getQueryString();
            if (queryString != null && queryString.length() > 0)
                    receivingURL.append("?").append(request.getQueryString());

            // verify the response; ConsumerManager needs to be the same
            // (static) instance used to place the authentication request
            VerificationResult verification = consumerManager.verify(
                    receivingURL.toString(),
                    response, dirtyDiscovery);

            // examine the verification result and extract the verified identifier
            Identifier verified = verification.getVerifiedId();
            if (verified != null) {
                AuthSuccess authSuccess =
                        (AuthSuccess) verification.getAuthResponse();

                if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {

                    FetchResponse fetchResp = (FetchResponse) authSuccess
                            .getExtension(AxMessage.OPENID_NS_AX);

                    List emails = fetchResp.getAttributeValues("email");
                    String email = (String) emails.get(0);
                    logger.debug("email: {}" ,email);
                }


            }

        } catch (OpenIDException e) {
            logger.error("error processing the callback", e);
        }

        return null;
    }

}
