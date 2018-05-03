package com.reporting.mocks.model.underlying;

import java.util.UUID;

public class SecurityStatic {
    protected UUID securityId;
    protected String securityName;

    public SecurityStatic(String securityName) {
        this.securityId = UUID.randomUUID();
        this.securityName = securityName;
    }

    public UUID getSecurityId() {
        return securityId;
    }

    public String getSecurityName() {
        return securityName;
    }
}
