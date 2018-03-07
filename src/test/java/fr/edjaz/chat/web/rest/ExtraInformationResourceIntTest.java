package fr.edjaz.chat.web.rest;

import fr.edjaz.chat.ChatApp;

import fr.edjaz.chat.domain.ExtraInformation;
import fr.edjaz.chat.repository.ExtraInformationRepository;
import fr.edjaz.chat.service.ExtraInformationService;
import fr.edjaz.chat.repository.search.ExtraInformationSearchRepository;
import fr.edjaz.chat.service.dto.ExtraInformationDTO;
import fr.edjaz.chat.service.mapper.ExtraInformationMapper;
import fr.edjaz.chat.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static fr.edjaz.chat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExtraInformationResource REST controller.
 *
 * @see ExtraInformationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatApp.class)
public class ExtraInformationResourceIntTest {

    private static final String DEFAULT_EXTRAS = "AAAAAAAAAA";
    private static final String UPDATED_EXTRAS = "BBBBBBBBBB";

    @Autowired
    private ExtraInformationRepository extraInformationRepository;

    @Autowired
    private ExtraInformationMapper extraInformationMapper;

    @Autowired
    private ExtraInformationService extraInformationService;

    @Autowired
    private ExtraInformationSearchRepository extraInformationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExtraInformationMockMvc;

    private ExtraInformation extraInformation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExtraInformationResource extraInformationResource = new ExtraInformationResource(extraInformationService);
        this.restExtraInformationMockMvc = MockMvcBuilders.standaloneSetup(extraInformationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraInformation createEntity(EntityManager em) {
        ExtraInformation extraInformation = new ExtraInformation()
            .extras(DEFAULT_EXTRAS);
        return extraInformation;
    }

    @Before
    public void initTest() {
        extraInformationSearchRepository.deleteAll();
        extraInformation = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtraInformation() throws Exception {
        int databaseSizeBeforeCreate = extraInformationRepository.findAll().size();

        // Create the ExtraInformation
        ExtraInformationDTO extraInformationDTO = extraInformationMapper.toDto(extraInformation);
        restExtraInformationMockMvc.perform(post("/api/extra-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraInformationDTO)))
            .andExpect(status().isCreated());

        // Validate the ExtraInformation in the database
        List<ExtraInformation> extraInformationList = extraInformationRepository.findAll();
        assertThat(extraInformationList).hasSize(databaseSizeBeforeCreate + 1);
        ExtraInformation testExtraInformation = extraInformationList.get(extraInformationList.size() - 1);
        assertThat(testExtraInformation.getExtras()).isEqualTo(DEFAULT_EXTRAS);

        // Validate the ExtraInformation in Elasticsearch
        ExtraInformation extraInformationEs = extraInformationSearchRepository.findOne(testExtraInformation.getId());
        assertThat(extraInformationEs).isEqualToIgnoringGivenFields(testExtraInformation);
    }

    @Test
    @Transactional
    public void createExtraInformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extraInformationRepository.findAll().size();

        // Create the ExtraInformation with an existing ID
        extraInformation.setId(1L);
        ExtraInformationDTO extraInformationDTO = extraInformationMapper.toDto(extraInformation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraInformationMockMvc.perform(post("/api/extra-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraInformationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExtraInformation in the database
        List<ExtraInformation> extraInformationList = extraInformationRepository.findAll();
        assertThat(extraInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExtraInformations() throws Exception {
        // Initialize the database
        extraInformationRepository.saveAndFlush(extraInformation);

        // Get all the extraInformationList
        restExtraInformationMockMvc.perform(get("/api/extra-informations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].extras").value(hasItem(DEFAULT_EXTRAS.toString())));
    }

    @Test
    @Transactional
    public void getExtraInformation() throws Exception {
        // Initialize the database
        extraInformationRepository.saveAndFlush(extraInformation);

        // Get the extraInformation
        restExtraInformationMockMvc.perform(get("/api/extra-informations/{id}", extraInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extraInformation.getId().intValue()))
            .andExpect(jsonPath("$.extras").value(DEFAULT_EXTRAS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExtraInformation() throws Exception {
        // Get the extraInformation
        restExtraInformationMockMvc.perform(get("/api/extra-informations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtraInformation() throws Exception {
        // Initialize the database
        extraInformationRepository.saveAndFlush(extraInformation);
        extraInformationSearchRepository.save(extraInformation);
        int databaseSizeBeforeUpdate = extraInformationRepository.findAll().size();

        // Update the extraInformation
        ExtraInformation updatedExtraInformation = extraInformationRepository.findOne(extraInformation.getId());
        // Disconnect from session so that the updates on updatedExtraInformation are not directly saved in db
        em.detach(updatedExtraInformation);
        updatedExtraInformation
            .extras(UPDATED_EXTRAS);
        ExtraInformationDTO extraInformationDTO = extraInformationMapper.toDto(updatedExtraInformation);

        restExtraInformationMockMvc.perform(put("/api/extra-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraInformationDTO)))
            .andExpect(status().isOk());

        // Validate the ExtraInformation in the database
        List<ExtraInformation> extraInformationList = extraInformationRepository.findAll();
        assertThat(extraInformationList).hasSize(databaseSizeBeforeUpdate);
        ExtraInformation testExtraInformation = extraInformationList.get(extraInformationList.size() - 1);
        assertThat(testExtraInformation.getExtras()).isEqualTo(UPDATED_EXTRAS);

        // Validate the ExtraInformation in Elasticsearch
        ExtraInformation extraInformationEs = extraInformationSearchRepository.findOne(testExtraInformation.getId());
        assertThat(extraInformationEs).isEqualToIgnoringGivenFields(testExtraInformation);
    }

    @Test
    @Transactional
    public void updateNonExistingExtraInformation() throws Exception {
        int databaseSizeBeforeUpdate = extraInformationRepository.findAll().size();

        // Create the ExtraInformation
        ExtraInformationDTO extraInformationDTO = extraInformationMapper.toDto(extraInformation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExtraInformationMockMvc.perform(put("/api/extra-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraInformationDTO)))
            .andExpect(status().isCreated());

        // Validate the ExtraInformation in the database
        List<ExtraInformation> extraInformationList = extraInformationRepository.findAll();
        assertThat(extraInformationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExtraInformation() throws Exception {
        // Initialize the database
        extraInformationRepository.saveAndFlush(extraInformation);
        extraInformationSearchRepository.save(extraInformation);
        int databaseSizeBeforeDelete = extraInformationRepository.findAll().size();

        // Get the extraInformation
        restExtraInformationMockMvc.perform(delete("/api/extra-informations/{id}", extraInformation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean extraInformationExistsInEs = extraInformationSearchRepository.exists(extraInformation.getId());
        assertThat(extraInformationExistsInEs).isFalse();

        // Validate the database is empty
        List<ExtraInformation> extraInformationList = extraInformationRepository.findAll();
        assertThat(extraInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchExtraInformation() throws Exception {
        // Initialize the database
        extraInformationRepository.saveAndFlush(extraInformation);
        extraInformationSearchRepository.save(extraInformation);

        // Search the extraInformation
        restExtraInformationMockMvc.perform(get("/api/_search/extra-informations?query=id:" + extraInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].extras").value(hasItem(DEFAULT_EXTRAS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtraInformation.class);
        ExtraInformation extraInformation1 = new ExtraInformation();
        extraInformation1.setId(1L);
        ExtraInformation extraInformation2 = new ExtraInformation();
        extraInformation2.setId(extraInformation1.getId());
        assertThat(extraInformation1).isEqualTo(extraInformation2);
        extraInformation2.setId(2L);
        assertThat(extraInformation1).isNotEqualTo(extraInformation2);
        extraInformation1.setId(null);
        assertThat(extraInformation1).isNotEqualTo(extraInformation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtraInformationDTO.class);
        ExtraInformationDTO extraInformationDTO1 = new ExtraInformationDTO();
        extraInformationDTO1.setId(1L);
        ExtraInformationDTO extraInformationDTO2 = new ExtraInformationDTO();
        assertThat(extraInformationDTO1).isNotEqualTo(extraInformationDTO2);
        extraInformationDTO2.setId(extraInformationDTO1.getId());
        assertThat(extraInformationDTO1).isEqualTo(extraInformationDTO2);
        extraInformationDTO2.setId(2L);
        assertThat(extraInformationDTO1).isNotEqualTo(extraInformationDTO2);
        extraInformationDTO1.setId(null);
        assertThat(extraInformationDTO1).isNotEqualTo(extraInformationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(extraInformationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(extraInformationMapper.fromId(null)).isNull();
    }
}
