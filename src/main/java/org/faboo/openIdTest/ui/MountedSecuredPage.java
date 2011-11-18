package org.faboo.openIdTest.ui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 * User: br
 */
public class MountedSecuredPage extends WebPage {


    public MountedSecuredPage() {

        add(new Label("email", SampleSession.get().getUserEmail()));
    }
}
