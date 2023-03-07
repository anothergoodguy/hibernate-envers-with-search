package com.sample.shop.version.repository.impl;

import com.sample.shop.domain.WishList;
import com.sample.shop.service.util.JPAUtils;
import com.sample.shop.version.domain.WishListVersion;
import com.sample.shop.version.repository.IWishListVersionRepository;
import com.sample.shop.version.repository.query.AuditQueryResult;
import com.sample.shop.version.repository.query.AuditQueryUtils;
import com.sample.shop.version.service.mapper.WishListVersionMapper;
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
public class WishListVersionRepository implements IWishListVersionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WishListVersionMapper mMapper;

    private static WishListVersion getWishListVersion(AuditQueryResult<WishList> auditQueryResult) {
        return new WishListVersion(
            auditQueryResult.getRevision().getId(),
            auditQueryResult.getType(),
            auditQueryResult.getRevision().getRevisedBy(),
            auditQueryResult.getRevision().getRevisedByType(),
            auditQueryResult.getRevision().getAuditor()
        );
    }

    private static WishListVersion getWishListVersion(AuditQueryResult<WishList> auditQueryResult, WishListVersionMapper entityManager) {
        return new WishListVersion(
            entityManager.toDto(auditQueryResult.getEntity()),
            auditQueryResult.getRevision().getId(),
            auditQueryResult.getType(),
            auditQueryResult.getRevision().getRevisedBy(),
            auditQueryResult.getRevision().getRevisedByType(),
            auditQueryResult.getRevision().getAuditor()
        );
    }

    @Transactional(readOnly = true)
    public List<WishListVersion> listWishListRevisions(UUID wishListId) {
        // Create the Audit Reader. It uses the EntityManager, which will be opened when
        // starting the new Transation and closed when the Transaction finishes.
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        // Create the Query:
        AuditQuery auditQuery = auditReader
            .createQuery()
            .forRevisionsOfEntity(WishList.class, false, true)
            .add(AuditEntity.id().eq(wishListId));

        // We don't operate on the untyped Results, but cast them into a List of AuditQueryResult:
        return AuditQueryUtils
            .getAuditQueryResults(auditQuery, WishList.class)
            .stream()
            // Turn into the WishListVersion Domain Object:
            .peek(x -> {
                JPAUtils.initialize(entityManager, x.getEntity(), 1);
            })
            .map(x -> getWishListVersion(x, mMapper))
            // And collect the Results:
            .collect(Collectors.toList());
    }
}
