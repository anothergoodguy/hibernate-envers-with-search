package com.sample.shop.version.repository.impl;

import com.sample.shop.domain.Address;
import com.sample.shop.service.util.JPAUtils;
import com.sample.shop.version.domain.AddressVersion;
import com.sample.shop.version.repository.IAddressVersionRepository;
import com.sample.shop.version.repository.query.AuditQueryResult;
import com.sample.shop.version.repository.query.AuditQueryUtils;
import com.sample.shop.version.service.mapper.AddressVersionMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddressVersionRepository implements IAddressVersionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AddressVersionMapper mMapper;

    private static AddressVersion getAddressVersion(AuditQueryResult<Address> auditQueryResult) {
        return new AddressVersion(
            auditQueryResult.getRevision().getId(),
            auditQueryResult.getType(),
            auditQueryResult.getRevision().getRevisedBy(),
            auditQueryResult.getRevision().getRevisedByType(),
            auditQueryResult.getRevision().getAuditor()
        );
    }

    private static AddressVersion getAddressVersion(AuditQueryResult<Address> auditQueryResult, AddressVersionMapper entityManager) {
        return new AddressVersion(
            entityManager.toDto(auditQueryResult.getEntity()),
            auditQueryResult.getRevision().getId(),
            auditQueryResult.getType(),
            auditQueryResult.getRevision().getRevisedBy(),
            auditQueryResult.getRevision().getRevisedByType(),
            auditQueryResult.getRevision().getAuditor()
        );
    }

    @Transactional(readOnly = true)
    public List<AddressVersion> listAddressRevisions(UUID addressId) {
        // Create the Audit Reader. It uses the EntityManager, which will be opened when
        // starting the new Transation and closed when the Transaction finishes.
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        // Create the Query:
        AuditQuery auditQuery = auditReader
            .createQuery()
            .forRevisionsOfEntity(Address.class, false, true)
            .add(AuditEntity.id().eq(addressId));

        // We don't operate on the untyped Results, but cast them into a List of AuditQueryResult:
        return AuditQueryUtils
            .getAuditQueryResults(auditQuery, Address.class)
            .stream()
            // Turn into the AddressVersion Domain Object:
            .peek(x -> {
                JPAUtils.initialize(entityManager, x.getEntity(), 1);
            })
            .map(x -> getAddressVersion(x, mMapper))
            // And collect the Results:
            .collect(Collectors.toList());
    }
}
