package com.sample.shop.config.audit;

import com.sample.shop.domain.CustomRevisionEntity;
import com.sample.shop.security.SecurityUtils;
import java.util.UUID;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity entity = (CustomRevisionEntity) revisionEntity;
        entity.setAuditor(SecurityUtils.getCurrentUserLogin().orElse(null));
        Pair<String, String> userDetails = SecurityUtils.extractPrincipalUserDetails();
        if (null != userDetails) {
            entity.setRevisedBy(UUID.fromString(userDetails.getKey()));
            entity.setRevisedByType(userDetails.getValue());
        }
    }
}
