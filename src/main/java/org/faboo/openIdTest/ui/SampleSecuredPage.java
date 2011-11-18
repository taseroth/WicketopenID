package org.faboo.openIdTest.ui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 * User: br
 */
public class SampleSecuredPage extends WebPage {


    public SampleSecuredPage() {

        add(new Label("email", SampleSession.get().getUserEmail()));

    }
}
