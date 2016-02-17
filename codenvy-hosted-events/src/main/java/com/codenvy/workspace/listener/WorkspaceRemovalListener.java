/*
 *  [2012] - [2016] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.workspace.listener;

import org.eclipse.che.api.core.BadRequestException;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.notification.EventService;
import org.eclipse.che.api.user.server.dao.MembershipDao;
import org.eclipse.che.api.user.server.dao.PreferenceDao;
import org.eclipse.che.api.user.server.dao.UserDao;
import org.eclipse.che.api.user.shared.model.Membership;
import org.eclipse.che.api.workspace.server.WorkspaceManager;
import com.codenvy.workspace.event.StopWsEvent;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;

/**
 * Cache removal listener that remove temporary workspace and its memberships, temporary users.
 *
 * @author Alexander Garagatyi
 */
public class WorkspaceRemovalListener implements RemovalListener<String, Boolean> {
    private static final Logger LOG = LoggerFactory.getLogger(WorkspaceRemovalListener.class);

    private final EventService     eventService;
    private final WorkspaceManager workspaceManager;
    private final MembershipDao    membershipDao;
    private final UserDao          userDao;
    private final PreferenceDao    preferenceDao;

    @Inject
    public WorkspaceRemovalListener(EventService eventService,
                                    WorkspaceManager workspaceManager,
                                    MembershipDao membershipDao,
                                    UserDao userDao,
                                    PreferenceDao preferenceDao) {
        this.eventService = eventService;
        this.workspaceManager = workspaceManager;
        this.membershipDao = membershipDao;
        this.userDao = userDao;
        this.preferenceDao = preferenceDao;
    }

    @Override
    public void onRemoval(RemovalNotification<String, Boolean> notification) {
        try {
            if (notification.getValue()) {
                try {
                    String wsId = notification.getKey();
                    final List<Membership> memberships = new ArrayList<>();
                    try {
                        memberships.addAll(membershipDao.getAllMemberships("workspace", wsId));
                        workspaceManager.removeWorkspace(wsId);
                    } catch (BadRequestException e) {
                        LOG.error("Cannot remove workspace {}", wsId);
                    }

                    for (Membership member : memberships) {
                        final Map<String, String> preferences = preferenceDao.getPreferences(member.getUserId());
                        if (parseBoolean(preferences.get("temporary")) && membershipDao.getMemberships(member.getUserId(), "workspace")
                                                                                       .isEmpty()) {
                            userDao.remove(member.getUserId());
                        }
                    }
                } catch (ConflictException | NotFoundException | ServerException e) {
                    LOG.warn(e.getLocalizedMessage());
                }
            }
        } finally {
            eventService.publish(new StopWsEvent(notification.getKey(), notification.getValue()));
            LOG.info("Workspace is stopped. Id#{}# Temporary#{}#", notification.getKey(), notification.getValue());
        }
    }
}