package org.faboo.openIdTest.ui;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.component.IRequestableComponent;
import org.faboo.openIdTest.ui.login.Consumer;
import org.faboo.openIdTest.ui.login.LoginPage;
import org.faboo.openIdTest.ui.login.OpenIdCallbackPage;


public class AuthStrategy implements IAuthorizationStrategy {

	public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
			Class<T> componentClass) {

		if (!Page.class.isAssignableFrom(componentClass)) {
			return true;
		}

		if (LoginPage.class.isAssignableFrom(componentClass)) {
			return true;
		}

        if (Consumer.class.isAssignableFrom(componentClass)) {
            return true;
        }

		if (OpenIdCallbackPage.class.isAssignableFrom(componentClass)) {
			return true;
		}

		if ((SampleSession.get()).getUserEmail() == null) {
			throw new RestartResponseAtInterceptPageException(LoginPage.class);
		}

		return true;
	}

	public boolean isActionAuthorized(Component component, Action action) {
		return true;
	}

}
