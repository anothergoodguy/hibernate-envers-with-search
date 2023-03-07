package com.sample.shop.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.sample.shop.repository.CustomerRepository;
import com.sample.shop.service.CustomerService;
import com.sample.shop.service.dto.CustomerDTO;
import com.sample.shop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sample.shop.domain.Customer}.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "customer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    public CustomerResource(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    /**
     * {@code POST  /customers} : Create a new customer.
     *
     * @param customerDTO the customerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerDTO, or with status {@code 400 (Bad Request)} if the customer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customers")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customerDTO);
        if (customerDTO.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerDTO result = customerService.save(customerDTO);
        return ResponseEntity
            .created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customers/:id} : Updates an existing customer.
     *
     * @param id the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Customer : {}, {}", id, customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerDTO result = customerService.update(customerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customers/:id} : Partial updates given fields of an existing customer, field will ignore if it is null
     *
     * @param id the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerDTO> partialUpdateCustomer(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Customer partially : {}, {}", id, customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerDTO> result = customerService.partialUpdate(customerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customers} : get all the customers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Customers");
        Page<CustomerDTO> page = customerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customers/:id} : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable UUID id) {
        log.debug("REST request to get Customer : {}", id);
        Optional<CustomerDTO> customerDTO = customerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerDTO);
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customer.
     *
     * @param id the id of the customerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        log.debug("REST request to delete Customer : {}", id);
        customerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/customers?query=:query} : search for the customer corresponding
     * to the query.
     *
     * @param query the query of the customer search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/customers")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Customers for query {}", query);
        Page<CustomerDTO> page = customerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
