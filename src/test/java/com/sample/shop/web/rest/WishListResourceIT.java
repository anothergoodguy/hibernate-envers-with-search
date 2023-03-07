package com.sample.shop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sample.shop.IntegrationTest;
import com.sample.shop.domain.WishList;
import com.sample.shop.repository.WishListRepository;
import com.sample.shop.repository.search.WishListSearchRepository;
import com.sample.shop.service.dto.WishListDTO;
import com.sample.shop.service.mapper.WishListMapper;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WishListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WishListResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RESTRICTED = false;
    private static final Boolean UPDATED_RESTRICTED = true;

    private static final String ENTITY_API_URL = "/api/wish-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/wish-lists";

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private WishListMapper wishListMapper;

    @Autowired
    private WishListSearchRepository wishListSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWishListMockMvc;

    private WishList wishList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WishList createEntity(EntityManager em) {
        WishList wishList = new WishList().title(DEFAULT_TITLE).restricted(DEFAULT_RESTRICTED);
        return wishList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WishList createUpdatedEntity(EntityManager em) {
        WishList wishList = new WishList().title(UPDATED_TITLE).restricted(UPDATED_RESTRICTED);
        return wishList;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        wishListSearchRepository.deleteAll();
        assertThat(wishListSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        wishList = createEntity(em);
    }

    @Test
    @Transactional
    void createWishList() throws Exception {
        int databaseSizeBeforeCreate = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);
        restWishListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishListDTO)))
            .andExpect(status().isCreated());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWishList.getRestricted()).isEqualTo(DEFAULT_RESTRICTED);
    }

    @Test
    @Transactional
    void createWishListWithExistingId() throws Exception {
        // Create the WishList with an existing ID
        wishListRepository.saveAndFlush(wishList);
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        int databaseSizeBeforeCreate = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        // set the field null
        wishList.setTitle(null);

        // Create the WishList, which fails.
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        restWishListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishListDTO)))
            .andExpect(status().isBadRequest());

        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllWishLists() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        // Get all the wishListList
        restWishListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishList.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].restricted").value(hasItem(DEFAULT_RESTRICTED.booleanValue())));
    }

    @Test
    @Transactional
    void getWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        // Get the wishList
        restWishListMockMvc
            .perform(get(ENTITY_API_URL_ID, wishList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wishList.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.restricted").value(DEFAULT_RESTRICTED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingWishList() throws Exception {
        // Get the wishList
        restWishListMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        wishListSearchRepository.save(wishList);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());

        // Update the wishList
        WishList updatedWishList = wishListRepository.findById(wishList.getId()).get();
        // Disconnect from session so that the updates on updatedWishList are not directly saved in db
        em.detach(updatedWishList);
        updatedWishList.title(UPDATED_TITLE).restricted(UPDATED_RESTRICTED);
        WishListDTO wishListDTO = wishListMapper.toDto(updatedWishList);

        restWishListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wishListDTO))
            )
            .andExpect(status().isOk());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWishList.getRestricted()).isEqualTo(UPDATED_RESTRICTED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<WishList> wishListSearchList = IterableUtils.toList(wishListSearchRepository.findAll());
                WishList testWishListSearch = wishListSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testWishListSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testWishListSearch.getRestricted()).isEqualTo(UPDATED_RESTRICTED);
            });
    }

    @Test
    @Transactional
    void putNonExistingWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        wishList.setId(UUID.randomUUID());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wishListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        wishList.setId(UUID.randomUUID());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wishListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        wishList.setId(UUID.randomUUID());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateWishListWithPatch() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();

        // Update the wishList using partial update
        WishList partialUpdatedWishList = new WishList();
        partialUpdatedWishList.setId(wishList.getId());

        partialUpdatedWishList.restricted(UPDATED_RESTRICTED);

        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWishList))
            )
            .andExpect(status().isOk());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWishList.getRestricted()).isEqualTo(UPDATED_RESTRICTED);
    }

    @Test
    @Transactional
    void fullUpdateWishListWithPatch() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();

        // Update the wishList using partial update
        WishList partialUpdatedWishList = new WishList();
        partialUpdatedWishList.setId(wishList.getId());

        partialUpdatedWishList.title(UPDATED_TITLE).restricted(UPDATED_RESTRICTED);

        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWishList))
            )
            .andExpect(status().isOk());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWishList.getRestricted()).isEqualTo(UPDATED_RESTRICTED);
    }

    @Test
    @Transactional
    void patchNonExistingWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        wishList.setId(UUID.randomUUID());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wishListDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wishListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        wishList.setId(UUID.randomUUID());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wishListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        wishList.setId(UUID.randomUUID());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wishListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);
        wishListRepository.save(wishList);
        wishListSearchRepository.save(wishList);

        int databaseSizeBeforeDelete = wishListRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the wishList
        restWishListMockMvc
            .perform(delete(ENTITY_API_URL_ID, wishList.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(wishListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchWishList() throws Exception {
        // Initialize the database
        wishList = wishListRepository.saveAndFlush(wishList);
        wishListSearchRepository.save(wishList);

        // Search the wishList
        restWishListMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + wishList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishList.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].restricted").value(hasItem(DEFAULT_RESTRICTED.booleanValue())));
    }
}
