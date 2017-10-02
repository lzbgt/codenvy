/*
 * Copyright (c) [2012] - [2017] Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package com.codenvy.selenium.core.requestfactory;

import com.google.inject.Provider;
import com.google.inject.name.Named;
import javax.inject.Inject;
import org.eclipse.che.selenium.core.client.TestAuthServiceClient;
import org.eclipse.che.selenium.core.requestfactory.TestUserHttpJsonRequestFactory;

/** @author Dmytro Nochevnov */
public class TestAdminHttpJsonRequestFactory extends TestUserHttpJsonRequestFactory {

  @Inject
  public TestAdminHttpJsonRequestFactory(
      Provider<TestAuthServiceClient> authServiceClientProvider,
      @Named("codenvy.admin.name") String adminName,
      @Named("codenvy.admin.password") String adminPassword) {
    super(authServiceClientProvider.get(), adminName, adminPassword);
  }
}
