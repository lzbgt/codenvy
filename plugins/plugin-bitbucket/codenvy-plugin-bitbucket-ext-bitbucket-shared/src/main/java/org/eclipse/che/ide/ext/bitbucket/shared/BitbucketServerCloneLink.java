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
package org.eclipse.che.ide.ext.bitbucket.shared;

import org.eclipse.che.dto.shared.DTO;

/**
 * Represents a link in Bitbucket Server rest API.
 *
 * @author Igor Vinokur
 */
@DTO
public interface BitbucketServerCloneLink {
  String getName();

  void setName(String name);

  BitbucketServerCloneLink withName(String name);

  String getHref();

  void setHref(String href);

  BitbucketServerCloneLink withHref(String href);
}
