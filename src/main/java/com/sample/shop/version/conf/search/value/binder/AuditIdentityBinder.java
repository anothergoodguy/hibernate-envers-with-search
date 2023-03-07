package com.sample.shop.version.conf.search.value.binder;

import com.sample.shop.version.conf.search.AuditIdentity;
import com.sample.shop.version.conf.search.EntityId;
import com.sample.shop.version.conf.search.identifier.bridge.AuditEntityIdentifierBridge;
import org.hibernate.search.mapper.pojo.bridge.IdentifierBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.IdentifierBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.IdentifierBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeFromDocumentIdentifierContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeToDocumentIdentifierContext;

public class AuditIdentityBinder implements IdentifierBinder {

    @Override
    public void bind(IdentifierBindingContext<?> context) {
        context.bridge(AuditIdentity.class, new Bridge());
    }

    private static class Bridge implements IdentifierBridge<AuditIdentity> { // <6>

        @Override
        public String toDocumentIdentifier(AuditIdentity value, IdentifierBridgeToDocumentIdentifierContext context) {
            return value == null ? null : value.getStringValue();
        }

        @Override
        public AuditIdentity fromDocumentIdentifier(String documentIdentifier, IdentifierBridgeFromDocumentIdentifierContext context) {
            return documentIdentifier == null ? null : AuditIdentity.parse(documentIdentifier);
        }
    }
}
