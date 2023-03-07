package com.sample.shop.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.sample.shop.domain.Address;
import com.sample.shop.repository.AddressRepository;
import com.sample.shop.repository.search.AddressSearchRepository;
import com.sample.shop.service.dto.AddressDTO;
import com.sample.shop.service.mapper.AddressMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Address}.
 */
@Service
@Transactional
public class AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    private final AddressSearchRepository addressSearchRepository;

    public AddressService(
        AddressRepository addressRepository,
        AddressMapper addressMapper,
        AddressSearchRepository addressSearchRepository
    ) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.addressSearchRepository = addressSearchRepository;
    }

    /**
     * Save a address.
     *
     * @param addressDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public AddressDTO save(AddressDTO addressDTO) {
        log.debug("Request to save Address : {}", addressDTO);
        Address address = addressMapper.toEntity(addressDTO);
        address = addressRepository.save(address);
        AddressDTO result = addressMapper.toDto(address);
        // addressSearchRepository.index(address);
        return result;
    }

    /**
     * Update a address.
     *
     * @param addressDTO the entity to save.
     * @return the persisted entity.
     */
    public AddressDTO update(AddressDTO addressDTO) {
        log.debug("Request to update Address : {}", addressDTO);
        Address address = addressMapper.toEntity(addressDTO);
        address = addressRepository.save(address);
        AddressDTO result = addressMapper.toDto(address);
        // addressSearchRepository.index(address);
        return result;
    }

    /**
     * Partially update a address.
     *
     * @param addressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AddressDTO> partialUpdate(AddressDTO addressDTO) {
        log.debug("Request to partially update Address : {}", addressDTO);

        return addressRepository
            .findById(addressDTO.getId())
            .map(existingAddress -> {
                addressMapper.partialUpdate(existingAddress, addressDTO);

                return existingAddress;
            })
            .map(addressRepository::save)
            .map(savedAddress -> {
                // addressSearchRepository.save(savedAddress);

                return savedAddress;
            })
            .map(addressMapper::toDto);
    }

    /**
     * Get all the addresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll(pageable).map(addressMapper::toDto);
    }

    /**
     * Get one address by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AddressDTO> findOne(UUID id) {
        log.debug("Request to get Address : {}", id);
        return addressRepository.findById(id).map(addressMapper::toDto);
    }

    /**
     * Delete the address by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.deleteById(id);
        // addressSearchRepository.deleteById(id);
    }

    /**
     * Search for the address corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Addresses for query {}", query);
        return addressSearchRepository.search(query, pageable).map(addressMapper::toDto);
    }
}
