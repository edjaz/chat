package fr.edjaz.chat.web.rest;

import fr.edjaz.chat.ChatApp;

import fr.edjaz.chat.domain.Conseiller;
import fr.edjaz.chat.repository.ConseillerRepository;
import fr.edjaz.chat.service.ConseillerService;
import fr.edjaz.chat.repository.search.ConseillerSearchRepository;
import fr.edjaz.chat.service.dto.ConseillerDTO;
import fr.edjaz.chat.service.mapper.ConseillerMapper;
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
 * Test class for the ConseillerResource REST controller.
 *
 * @see ConseillerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatApp.class)
public class ConseillerResourceIntTest {

    @Autowired
    private ConseillerRepository conseillerRepository;

    @Autowired
    private ConseillerMapper conseillerMapper;

    @Autowired
    private ConseillerService conseillerService;

    @Autowired
    private ConseillerSearchRepository conseillerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConseillerMockMvc;

    private Conseiller conseiller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConseillerResource conseillerResource = new ConseillerResource(conseillerService);
        this.restConseillerMockMvc = MockMvcBuilders.standaloneSetup(conseillerResource)
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
    public static Conseiller createEntity(EntityManager em) {
        Conseiller conseiller = new Conseiller();
        return conseiller;
    }

    @Before
    public void initTest() {
        conseillerSearchRepository.deleteAll();
        conseiller = createEntity(em);
    }

    @Test
    @Transactional
    public void createConseiller() throws Exception {
        int databaseSizeBeforeCreate = conseillerRepository.findAll().size();

        // Create the Conseiller
        ConseillerDTO conseillerDTO = conseillerMapper.toDto(conseiller);
        restConseillerMockMvc.perform(post("/api/conseillers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conseillerDTO)))
            .andExpect(status().isCreated());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeCreate + 1);
        Conseiller testConseiller = conseillerList.get(conseillerList.size() - 1);

        // Validate the Conseiller in Elasticsearch
        Conseiller conseillerEs = conseillerSearchRepository.findOne(testConseiller.getId());
        assertThat(conseillerEs).isEqualToIgnoringGivenFields(testConseiller);
    }

    @Test
    @Transactional
    public void createConseillerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = conseillerRepository.findAll().size();

        // Create the Conseiller with an existing ID
        conseiller.setId(1L);
        ConseillerDTO conseillerDTO = conseillerMapper.toDto(conseiller);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConseillerMockMvc.perform(post("/api/conseillers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conseillerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllConseillers() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);

        // Get all the conseillerList
        restConseillerMockMvc.perform(get("/api/conseillers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conseiller.getId().intValue())));
    }

    @Test
    @Transactional
    public void getConseiller() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);

        // Get the conseiller
        restConseillerMockMvc.perform(get("/api/conseillers/{id}", conseiller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conseiller.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConseiller() throws Exception {
        // Get the conseiller
        restConseillerMockMvc.perform(get("/api/conseillers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConseiller() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);
        conseillerSearchRepository.save(conseiller);
        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();

        // Update the conseiller
        Conseiller updatedConseiller = conseillerRepository.findOne(conseiller.getId());
        // Disconnect from session so that the updates on updatedConseiller are not directly saved in db
        em.detach(updatedConseiller);
        ConseillerDTO conseillerDTO = conseillerMapper.toDto(updatedConseiller);

        restConseillerMockMvc.perform(put("/api/conseillers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conseillerDTO)))
            .andExpect(status().isOk());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
        Conseiller testConseiller = conseillerList.get(conseillerList.size() - 1);

        // Validate the Conseiller in Elasticsearch
        Conseiller conseillerEs = conseillerSearchRepository.findOne(testConseiller.getId());
        assertThat(conseillerEs).isEqualToIgnoringGivenFields(testConseiller);
    }

    @Test
    @Transactional
    public void updateNonExistingConseiller() throws Exception {
        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();

        // Create the Conseiller
        ConseillerDTO conseillerDTO = conseillerMapper.toDto(conseiller);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConseillerMockMvc.perform(put("/api/conseillers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conseillerDTO)))
            .andExpect(status().isCreated());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConseiller() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);
        conseillerSearchRepository.save(conseiller);
        int databaseSizeBeforeDelete = conseillerRepository.findAll().size();

        // Get the conseiller
        restConseillerMockMvc.perform(delete("/api/conseillers/{id}", conseiller.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean conseillerExistsInEs = conseillerSearchRepository.exists(conseiller.getId());
        assertThat(conseillerExistsInEs).isFalse();

        // Validate the database is empty
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchConseiller() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);
        conseillerSearchRepository.save(conseiller);

        // Search the conseiller
        restConseillerMockMvc.perform(get("/api/_search/conseillers?query=id:" + conseiller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conseiller.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conseiller.class);
        Conseiller conseiller1 = new Conseiller();
        conseiller1.setId(1L);
        Conseiller conseiller2 = new Conseiller();
        conseiller2.setId(conseiller1.getId());
        assertThat(conseiller1).isEqualTo(conseiller2);
        conseiller2.setId(2L);
        assertThat(conseiller1).isNotEqualTo(conseiller2);
        conseiller1.setId(null);
        assertThat(conseiller1).isNotEqualTo(conseiller2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConseillerDTO.class);
        ConseillerDTO conseillerDTO1 = new ConseillerDTO();
        conseillerDTO1.setId(1L);
        ConseillerDTO conseillerDTO2 = new ConseillerDTO();
        assertThat(conseillerDTO1).isNotEqualTo(conseillerDTO2);
        conseillerDTO2.setId(conseillerDTO1.getId());
        assertThat(conseillerDTO1).isEqualTo(conseillerDTO2);
        conseillerDTO2.setId(2L);
        assertThat(conseillerDTO1).isNotEqualTo(conseillerDTO2);
        conseillerDTO1.setId(null);
        assertThat(conseillerDTO1).isNotEqualTo(conseillerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(conseillerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(conseillerMapper.fromId(null)).isNull();
    }
}
