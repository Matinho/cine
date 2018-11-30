package ar.edu.um.programacion2.cine.web.rest;

import ar.edu.um.programacion2.cine.CineApp;

import ar.edu.um.programacion2.cine.domain.Ocupacion;
import ar.edu.um.programacion2.cine.repository.OcupacionRepository;
import ar.edu.um.programacion2.cine.web.rest.errors.ExceptionTranslator;

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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static ar.edu.um.programacion2.cine.web.rest.TestUtil.sameInstant;
import static ar.edu.um.programacion2.cine.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OcupacionResource REST controller.
 *
 * @see OcupacionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CineApp.class)
public class OcupacionResourceIntTest {

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(0);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(1);

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private OcupacionRepository ocupacionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOcupacionMockMvc;

    private Ocupacion ocupacion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OcupacionResource ocupacionResource = new OcupacionResource(ocupacionRepository);
        this.restOcupacionMockMvc = MockMvcBuilders.standaloneSetup(ocupacionResource)
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
    public static Ocupacion createEntity(EntityManager em) {
        Ocupacion ocupacion = new Ocupacion()
            .valor(DEFAULT_VALOR)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return ocupacion;
    }

    @Before
    public void initTest() {
        ocupacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createOcupacion() throws Exception {
        int databaseSizeBeforeCreate = ocupacionRepository.findAll().size();

        // Create the Ocupacion
        restOcupacionMockMvc.perform(post("/api/ocupacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ocupacion)))
            .andExpect(status().isCreated());

        // Validate the Ocupacion in the database
        List<Ocupacion> ocupacionList = ocupacionRepository.findAll();
        assertThat(ocupacionList).hasSize(databaseSizeBeforeCreate + 1);
        Ocupacion testOcupacion = ocupacionList.get(ocupacionList.size() - 1);
        assertThat(testOcupacion.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testOcupacion.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOcupacion.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createOcupacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ocupacionRepository.findAll().size();

        // Create the Ocupacion with an existing ID
        ocupacion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOcupacionMockMvc.perform(post("/api/ocupacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ocupacion)))
            .andExpect(status().isBadRequest());

        // Validate the Ocupacion in the database
        List<Ocupacion> ocupacionList = ocupacionRepository.findAll();
        assertThat(ocupacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocupacionRepository.findAll().size();
        // set the field null
        ocupacion.setValor(null);

        // Create the Ocupacion, which fails.

        restOcupacionMockMvc.perform(post("/api/ocupacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ocupacion)))
            .andExpect(status().isBadRequest());

        List<Ocupacion> ocupacionList = ocupacionRepository.findAll();
        assertThat(ocupacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocupacionRepository.findAll().size();
        // set the field null
        ocupacion.setCreated(null);

        // Create the Ocupacion, which fails.

        restOcupacionMockMvc.perform(post("/api/ocupacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ocupacion)))
            .andExpect(status().isBadRequest());

        List<Ocupacion> ocupacionList = ocupacionRepository.findAll();
        assertThat(ocupacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocupacionRepository.findAll().size();
        // set the field null
        ocupacion.setUpdated(null);

        // Create the Ocupacion, which fails.

        restOcupacionMockMvc.perform(post("/api/ocupacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ocupacion)))
            .andExpect(status().isBadRequest());

        List<Ocupacion> ocupacionList = ocupacionRepository.findAll();
        assertThat(ocupacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOcupacions() throws Exception {
        // Initialize the database
        ocupacionRepository.saveAndFlush(ocupacion);

        // Get all the ocupacionList
        restOcupacionMockMvc.perform(get("/api/ocupacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocupacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }
    
    @Test
    @Transactional
    public void getOcupacion() throws Exception {
        // Initialize the database
        ocupacionRepository.saveAndFlush(ocupacion);

        // Get the ocupacion
        restOcupacionMockMvc.perform(get("/api/ocupacions/{id}", ocupacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ocupacion.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)));
    }

    @Test
    @Transactional
    public void getNonExistingOcupacion() throws Exception {
        // Get the ocupacion
        restOcupacionMockMvc.perform(get("/api/ocupacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOcupacion() throws Exception {
        // Initialize the database
        ocupacionRepository.saveAndFlush(ocupacion);

        int databaseSizeBeforeUpdate = ocupacionRepository.findAll().size();

        // Update the ocupacion
        Ocupacion updatedOcupacion = ocupacionRepository.findById(ocupacion.getId()).get();
        // Disconnect from session so that the updates on updatedOcupacion are not directly saved in db
        em.detach(updatedOcupacion);
        updatedOcupacion
            .valor(UPDATED_VALOR)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restOcupacionMockMvc.perform(put("/api/ocupacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOcupacion)))
            .andExpect(status().isOk());

        // Validate the Ocupacion in the database
        List<Ocupacion> ocupacionList = ocupacionRepository.findAll();
        assertThat(ocupacionList).hasSize(databaseSizeBeforeUpdate);
        Ocupacion testOcupacion = ocupacionList.get(ocupacionList.size() - 1);
        assertThat(testOcupacion.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testOcupacion.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOcupacion.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingOcupacion() throws Exception {
        int databaseSizeBeforeUpdate = ocupacionRepository.findAll().size();

        // Create the Ocupacion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcupacionMockMvc.perform(put("/api/ocupacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ocupacion)))
            .andExpect(status().isBadRequest());

        // Validate the Ocupacion in the database
        List<Ocupacion> ocupacionList = ocupacionRepository.findAll();
        assertThat(ocupacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOcupacion() throws Exception {
        // Initialize the database
        ocupacionRepository.saveAndFlush(ocupacion);

        int databaseSizeBeforeDelete = ocupacionRepository.findAll().size();

        // Get the ocupacion
        restOcupacionMockMvc.perform(delete("/api/ocupacions/{id}", ocupacion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ocupacion> ocupacionList = ocupacionRepository.findAll();
        assertThat(ocupacionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ocupacion.class);
        Ocupacion ocupacion1 = new Ocupacion();
        ocupacion1.setId(1L);
        Ocupacion ocupacion2 = new Ocupacion();
        ocupacion2.setId(ocupacion1.getId());
        assertThat(ocupacion1).isEqualTo(ocupacion2);
        ocupacion2.setId(2L);
        assertThat(ocupacion1).isNotEqualTo(ocupacion2);
        ocupacion1.setId(null);
        assertThat(ocupacion1).isNotEqualTo(ocupacion2);
    }
}
