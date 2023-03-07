package com.sample.shop.version.repository;

import com.sample.shop.version.domain.AddressVersion;
import java.util.List;
import java.util.UUID;

public interface IAddressVersionRepository {
    List<AddressVersion> listAddressRevisions(UUID addressId);
}
