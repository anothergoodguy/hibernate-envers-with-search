package com.sample.shop.version.conf.search.identifier.bridge;

import com.sample.shop.version.conf.search.AuditIdentity;
import com.sample.shop.version.domain.identity.CustomEntityId;
import org.hibernate.search.mapper.pojo.bridge.IdentifierBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeFromDocumentIdentifierContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeToDocumentIdentifierContext;

public class AuditEntityIdentifierBridge implements IdentifierBridge<AuditIdentity<CustomEntityId, Integer>> {

    @Override
    public String toDocumentIdentifier(AuditIdentity value, IdentifierBridgeToDocumentIdentifierContext context) {
        return value == null ? null : value.getStringValue();
    }

    @Override
    public AuditIdentity fromDocumentIdentifier(String documentIdentifier, IdentifierBridgeFromDocumentIdentifierContext context) {
        // Note: this is incomplete and needs implementation
        return documentIdentifier == null ? null : AuditIdentity.parse(documentIdentifier);
    }
}
