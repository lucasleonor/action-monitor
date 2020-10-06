package com.betvictor.test.messaging.actionmonitor.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

@Component
public class ActiveConnectionsInfoContributor implements InfoContributor {
    private final SimpUserRegistry simpUserRegistry;

    @Autowired
    public ActiveConnectionsInfoContributor(final SimpUserRegistry simpUserRegistry) {
        this.simpUserRegistry = simpUserRegistry;
    }

    @Override
    public void contribute(final Info.Builder builder) {
        builder.withDetail("numberActiveUsers", simpUserRegistry.getUserCount());
    }
}
