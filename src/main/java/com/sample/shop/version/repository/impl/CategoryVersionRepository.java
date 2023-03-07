package com.sample.shop.version.repository.impl;

import com.sample.shop.domain.Category;
import com.sample.shop.service.util.JPAUtils;
import com.sample.shop.version.domain.CategoryVersion;
import com.sample.shop.version.repository.ICategoryVersionRepository;
import com.sample.shop.version.repository.query.AuditQueryResult;
import com.sample.shop.version.repository.query.AuditQueryUtils;
import com.sample.shop.version.service.mapper.CategoryVersionMapper;
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
public class CategoryVersionRepository implements ICategoryVersionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CategoryVersionMapper mMapper;

    private static CategoryVersion getCategoryVersion(AuditQueryResult<Category> auditQueryResult) {
        return new CategoryVersion(
            auditQueryResult.getRevision().getId(),
            auditQueryResult.getType(),
            auditQueryResult.getRevision().getRevisedBy(),
            auditQueryResult.getRevision().getRevisedByType(),
            auditQueryResult.getRevision().getAuditor()
        );
    }

    private static CategoryVersion getCategoryVersion(AuditQueryResult<Category> auditQueryResult, CategoryVersionMapper entityManager) {
        return new CategoryVersion(
            entityManager.toDto(auditQueryResult.getEntity()),
            auditQueryResult.getRevision().getId(),
            auditQueryResult.getType(),
            auditQueryResult.getRevision().getRevisedBy(),
            auditQueryResult.getRevision().getRevisedByType(),
            auditQueryResult.getRevision().getAuditor()
        );
    }

    @Transactional(readOnly = true)
    public List<CategoryVersion> listCategoryRevisions(UUID categoryId) {
        // Create the Audit Reader. It uses the EntityManager, which will be opened when
        // starting the new Transation and closed when the Transaction finishes.
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        // Create the Query:
        AuditQuery auditQuery = auditReader
            .createQuery()
            .forRevisionsOfEntity(Category.class, false, true)
            .add(AuditEntity.id().eq(categoryId));

        // We don't operate on the untyped Results, but cast them into a List of AuditQueryResult:
        return AuditQueryUtils
            .getAuditQueryResults(auditQuery, Category.class)
            .stream()
            // Turn into the CategoryVersion Domain Object:
            .peek(x -> {
                JPAUtils.initialize(entityManager, x.getEntity(), 1);
            })
            .map(x -> getCategoryVersion(x, mMapper))
            // And collect the Results:
            .collect(Collectors.toList());
    }
}
