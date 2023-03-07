package com.sample.shop.version.repository;

import com.sample.shop.version.domain.CustomerVersion;
import java.util.List;
import java.util.UUID;

public interface ICustomerVersionRepository {
    List<CustomerVersion> listCustomerRevisions(UUID customerId);
}
