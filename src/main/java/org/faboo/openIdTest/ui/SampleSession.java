package org.faboo.openIdTest.ui;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * User: taseroth
 */
public class SampleSession extends WebSession {

    private static final long serialVersionUID = 1L;


    private String userEmail;


    public SampleSession(Request request) {
        super(request);
    }

    public static SampleSession get() {
        return (SampleSession) Session.get();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
