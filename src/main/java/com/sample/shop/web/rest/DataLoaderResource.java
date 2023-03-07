package com.sample.shop.web.rest;

import com.sample.shop.service.AddressService;
import com.sample.shop.service.CategoryService;
import com.sample.shop.service.CustomerService;
import com.sample.shop.service.ProductService;
import com.sample.shop.service.WishListService;
import com.sample.shop.service.dto.AddressDTO;
import com.sample.shop.service.dto.CategoryDTO;
import com.sample.shop.service.dto.CustomerDTO;
import com.sample.shop.service.dto.ProductDTO;
import com.sample.shop.service.dto.WishListDTO;
import com.sample.shop.utils.GenericUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DataLoaderResource {

    private static final String ENTITY_NAME = "Data Loader";
    private final Logger log = LoggerFactory.getLogger(DataLoaderResource.class);

    private final CategoryService mCategoryService;
    private final CustomerService mCustomerService;
    private final AddressService mAddressService;
    private final WishListService mWishListService;
    private final ProductService mProductService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public DataLoaderResource(
        CategoryService categoryService,
        CustomerService customerService,
        AddressService addressService,
        WishListService wishListService,
        ProductService productService
    ) {
        mCategoryService = categoryService;
        mCustomerService = customerService;
        mAddressService = addressService;
        mWishListService = wishListService;
        mProductService = productService;
    }

    @GetMapping("/load")
    public ResponseEntity<String> getCustomer() throws InterruptedException {
        log.debug("REST request to Load Data :");
        File file = new File(getClass().getResource("/config/liquibase/fake-data").getFile());
        List<AddressDTO> addresses = Arrays
            .stream(file.listFiles())
            .filter(file1 -> file1.getName().equals("address.csv"))
            .findAny()
            .map(file1 -> {
                return GenericUtils.loadObjectList(AddressDTO.class, file1);
            })
            .orElse(new ArrayList());

        List<CategoryDTO> categories = Arrays
            .stream(file.listFiles())
            .filter(file1 -> file1.getName().equals("category.csv"))
            .findAny()
            .map(file1 -> {
                return GenericUtils.loadObjectList(CategoryDTO.class, file1);
            })
            .orElse(new ArrayList());

        List<CustomerDTO> customers = Arrays
            .stream(file.listFiles())
            .filter(file1 -> file1.getName().equals("customer.csv"))
            .findAny()
            .map(file1 -> {
                return GenericUtils.loadObjectList(CustomerDTO.class, file1);
            })
            .orElse(new ArrayList());

        List<ProductDTO> products = Arrays
            .stream(file.listFiles())
            .filter(file1 -> file1.getName().equals("product.csv"))
            .findAny()
            .map(file1 -> {
                return GenericUtils.loadObjectList(ProductDTO.class, file1);
            })
            .orElse(new ArrayList());

        List<WishListDTO> wishLists = Arrays
            .stream(file.listFiles())
            .filter(file1 -> file1.getName().equals("wish_list.csv"))
            .findAny()
            .map(file1 -> {
                return GenericUtils.loadObjectList(WishListDTO.class, file1);
            })
            .orElse(new ArrayList());

        Set<CategoryDTO> cats = categories
            .stream()
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(CategoryDTO::getSortOrder))
            .map(mCategoryService::save)
            .collect(Collectors.toSet());
        Set<CustomerDTO> custs = customers.stream().filter(Objects::nonNull).map(mCustomerService::save).collect(Collectors.toSet());
        Set<AddressDTO> adds = addresses.stream().map(mAddressService::save).collect(Collectors.toSet());
        Set<WishListDTO> wls = wishLists.stream().map(mWishListService::save).collect(Collectors.toSet());
        Set<ProductDTO> prods = products.stream().map(mProductService::save).collect(Collectors.toSet());

        Set<UUID> catSet = Arrays
            .asList(
                "3bc9f847-69d3-404f-b9fa-5b84a1e41f7d",
                "32775c3f-2f3c-4d1d-a6d0-6902eedde862",
                "3c0ab1a6-06ec-4976-9b79-e54c83224afe",
                "2e556830-2839-4e0f-a846-f83b592b3789"
            )
            .stream()
            .map(UUID::fromString)
            .collect(Collectors.toSet());

        cats
            .stream()
            .filter(Objects::nonNull)
            .filter(cat -> catSet.contains(cat.getId()))
            .peek(cat -> {
                cat.setProducts(prods);
            })
            .forEach(mCategoryService::update);
        return null;
    }
}
