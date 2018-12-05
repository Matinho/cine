package ar.edu.um.programacion2.cine.web.rest;

import ar.edu.um.programacion2.cine.CineApp;

import ar.edu.um.programacion2.cine.domain.Funcion;
import ar.edu.um.programacion2.cine.repository.FuncionRepository;
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
 * Test class for the FuncionResource REST controller.
 *
 * @see FuncionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CineApp.class)
public class FuncionResourceIntTest {

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(0);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(1);

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private FuncionRepository funcionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFuncionMockMvc;

    private Funcion funcion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FuncionResource funcionResource = new FuncionResource(funcionRepository);
        this.restFuncionMockMvc = MockMvcBuilders.standaloneSetup(funcionResource)
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
    public static Funcion createEntity(EntityManager em) {
        Funcion funcion = new Funcion()
            .fecha(DEFAULT_FECHA)
            .valor(DEFAULT_VALOR)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return funcion;
    }

    @Before
    public void initTest() {
        funcion = createEntity(em);
    }

    @Test
    @Transactional
    public void createFuncion() throws Exception {
        int databaseSizeBeforeCreate = funcionRepository.findAll().size();

        // Create the Funcion
        restFuncionMockMvc.perform(post("/api/funcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(funcion)))
            .andExpect(status().isCreated());

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll();
        assertThat(funcionList).hasSize(databaseSizeBeforeCreate + 1);
        Funcion testFuncion = funcionList.get(funcionList.size() - 1);
        assertThat(testFuncion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testFuncion.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testFuncion.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFuncion.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createFuncionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = funcionRepository.findAll().size();

        // Create the Funcion with an existing ID
        funcion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuncionMockMvc.perform(post("/api/funcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(funcion)))
            .andExpect(status().isBadRequest());

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll();
        assertThat(funcionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionRepository.findAll().size();
        // set the field null
        funcion.setFecha(null);

        // Create the Funcion, which fails.

        restFuncionMockMvc.perform(post("/api/funcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(funcion)))
            .andExpect(status().isBadRequest());

        List<Funcion> funcionList = funcionRepository.findAll();
        assertThat(funcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionRepository.findAll().size();
        // set the field null
        funcion.setValor(null);

        // Create the Funcion, which fails.

        restFuncionMockMvc.perform(post("/api/funcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(funcion)))
            .andExpect(status().isBadRequest());

        List<Funcion> funcionList = funcionRepository.findAll();
        assertThat(funcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionRepository.findAll().size();
        // set the field null
        funcion.setCreated(null);

        // Create the Funcion, which fails.

        restFuncionMockMvc.perform(post("/api/funcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(funcion)))
            .andExpect(status().isBadRequest());

        List<Funcion> funcionList = funcionRepository.findAll();
        assertThat(funcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionRepository.findAll().size();
        // set the field null
        funcion.setUpdated(null);

        // Create the Funcion, which fails.

        restFuncionMockMvc.perform(post("/api/funcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(funcion)))
            .andExpect(status().isBadRequest());

        List<Funcion> funcionList = funcionRepository.findAll();
        assertThat(funcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFuncions() throws Exception {
        // Initialize the database
        funcionRepository.saveAndFlush(funcion);

        // Get all the funcionList
        restFuncionMockMvc.perform(get("/api/funcions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }
    
    @Test
    @Transactional
    public void getFuncion() throws Exception {
        // Initialize the database
        funcionRepository.saveAndFlush(funcion);

        // Get the funcion
        restFuncionMockMvc.perform(get("/api/funcions/{id}", funcion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(funcion.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)));
    }

    @Test
    @Transactional
    public void getNonExistingFuncion() throws Exception {
        // Get the funcion
        restFuncionMockMvc.perform(get("/api/funcions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFuncion() throws Exception {
        // Initialize the database
        funcionRepository.saveAndFlush(funcion);

        int databaseSizeBeforeUpdate = funcionRepository.findAll().size();

        // Update the funcion
        Funcion updatedFuncion = funcionRepository.findById(funcion.getId()).get();
        // Disconnect from session so that the updates on updatedFuncion are not directly saved in db
        em.detach(updatedFuncion);
        updatedFuncion
            .fecha(UPDATED_FECHA)
            .valor(UPDATED_VALOR)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restFuncionMockMvc.perform(put("/api/funcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFuncion)))
            .andExpect(status().isOk());

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
        Funcion testFuncion = funcionList.get(funcionList.size() - 1);
        assertThat(testFuncion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testFuncion.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testFuncion.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFuncion.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingFuncion() throws Exception {
        int databaseSizeBeforeUpdate = funcionRepository.findAll().size();

        // Create the Funcion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionMockMvc.perform(put("/api/funcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(funcion)))
            .andExpect(status().isBadRequest());

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFuncion() throws Exception {
        // Initialize the database
        funcionRepository.saveAndFlush(funcion);

        int databaseSizeBeforeDelete = funcionRepository.findAll().size();

        // Get the funcion
        restFuncionMockMvc.perform(delete("/api/funcions/{id}", funcion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Funcion> funcionList = funcionRepository.findAll();
        assertThat(funcionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Funcion.class);
        Funcion funcion1 = new Funcion();
        funcion1.setId(1L);
        Funcion funcion2 = new Funcion();
        funcion2.setId(funcion1.getId());
        assertThat(funcion1).isEqualTo(funcion2);
        funcion2.setId(2L);
        assertThat(funcion1).isNotEqualTo(funcion2);
        funcion1.setId(null);
        assertThat(funcion1).isNotEqualTo(funcion2);
    }
}
