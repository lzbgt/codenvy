/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2015] Codenvy, S.A.
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
package com.codenvy.api.analytics;

import com.codenvy.api.account.billing.CreditCardRegistrationEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.api.core.notification.EventService;
import org.eclipse.che.api.core.notification.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Anatoliy Bazko
 */
@Singleton
public class CreditCardRegistrationSubscriber implements EventSubscriber<CreditCardRegistrationEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(CreditCardRegistrationSubscriber.class);

    private final EventService eventService;

    @Inject
    public CreditCardRegistrationSubscriber(EventService eventService) {
        this.eventService = eventService;
    }

    @PostConstruct
    private void subscribe() {
        eventService.subscribe(this);
    }

    @PreDestroy
    private void unsubscribe() {
        eventService.unsubscribe(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onEvent(CreditCardRegistrationEvent event) {
        switch (event.getType()) {
            case CREDIT_CARD_ADDED:
                LOG.info("EVENT#credit-card-added# USER#{}# ACCOUNT#{}#", event.getUserId(), event.getAccount());
                break;

            case CREDIT_CARD_REMOVED:
                LOG.info("EVENT#credit-card-removed# USER#{}# ACCOUNT#{}#", event.getUserId(), event.getAccount());
                break;
            default:
        }
    }
}
