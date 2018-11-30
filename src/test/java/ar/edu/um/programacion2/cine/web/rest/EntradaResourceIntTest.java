package ar.edu.um.programacion2.cine.web.rest;

import ar.edu.um.programacion2.cine.CineApp;

import ar.edu.um.programacion2.cine.domain.Entrada;
import ar.edu.um.programacion2.cine.repository.EntradaRepository;
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
 * Test class for the EntradaResource REST controller.
 *
 * @see EntradaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CineApp.class)
public class EntradaResourceIntTest {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(0);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(1);

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntradaMockMvc;

    private Entrada entrada;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EntradaResource entradaResource = new EntradaResource(entradaRepository);
        this.restEntradaMockMvc = MockMvcBuilders.standaloneSetup(entradaResource)
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
    public static Entrada createEntity(EntityManager em) {
        Entrada entrada = new Entrada()
            .descripcion(DEFAULT_DESCRIPCION)
            .valor(DEFAULT_VALOR)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return entrada;
    }

    @Before
    public void initTest() {
        entrada = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntrada() throws Exception {
        int databaseSizeBeforeCreate = entradaRepository.findAll().size();

        // Create the Entrada
        restEntradaMockMvc.perform(post("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrada)))
            .andExpect(status().isCreated());

        // Validate the Entrada in the database
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeCreate + 1);
        Entrada testEntrada = entradaList.get(entradaList.size() - 1);
        assertThat(testEntrada.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testEntrada.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testEntrada.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testEntrada.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createEntradaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entradaRepository.findAll().size();

        // Create the Entrada with an existing ID
        entrada.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntradaMockMvc.perform(post("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrada)))
            .andExpect(status().isBadRequest());

        // Validate the Entrada in the database
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = entradaRepository.findAll().size();
        // set the field null
        entrada.setDescripcion(null);

        // Create the Entrada, which fails.

        restEntradaMockMvc.perform(post("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrada)))
            .andExpect(status().isBadRequest());

        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = entradaRepository.findAll().size();
        // set the field null
        entrada.setValor(null);

        // Create the Entrada, which fails.

        restEntradaMockMvc.perform(post("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrada)))
            .andExpect(status().isBadRequest());

        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = entradaRepository.findAll().size();
        // set the field null
        entrada.setCreated(null);

        // Create the Entrada, which fails.

        restEntradaMockMvc.perform(post("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrada)))
            .andExpect(status().isBadRequest());

        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = entradaRepository.findAll().size();
        // set the field null
        entrada.setUpdated(null);

        // Create the Entrada, which fails.

        restEntradaMockMvc.perform(post("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrada)))
            .andExpect(status().isBadRequest());

        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEntradas() throws Exception {
        // Initialize the database
        entradaRepository.saveAndFlush(entrada);

        // Get all the entradaList
        restEntradaMockMvc.perform(get("/api/entradas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entrada.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }
    
    @Test
    @Transactional
    public void getEntrada() throws Exception {
        // Initialize the database
        entradaRepository.saveAndFlush(entrada);

        // Get the entrada
        restEntradaMockMvc.perform(get("/api/entradas/{id}", entrada.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entrada.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)));
    }

    @Test
    @Transactional
    public void getNonExistingEntrada() throws Exception {
        // Get the entrada
        restEntradaMockMvc.perform(get("/api/entradas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntrada() throws Exception {
        // Initialize the database
        entradaRepository.saveAndFlush(entrada);

        int databaseSizeBeforeUpdate = entradaRepository.findAll().size();

        // Update the entrada
        Entrada updatedEntrada = entradaRepository.findById(entrada.getId()).get();
        // Disconnect from session so that the updates on updatedEntrada are not directly saved in db
        em.detach(updatedEntrada);
        updatedEntrada
            .descripcion(UPDATED_DESCRIPCION)
            .valor(UPDATED_VALOR)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restEntradaMockMvc.perform(put("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntrada)))
            .andExpect(status().isOk());

        // Validate the Entrada in the database
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeUpdate);
        Entrada testEntrada = entradaList.get(entradaList.size() - 1);
        assertThat(testEntrada.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testEntrada.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testEntrada.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testEntrada.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingEntrada() throws Exception {
        int databaseSizeBeforeUpdate = entradaRepository.findAll().size();

        // Create the Entrada

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntradaMockMvc.perform(put("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrada)))
            .andExpect(status().isBadRequest());

        // Validate the Entrada in the database
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEntrada() throws Exception {
        // Initialize the database
        entradaRepository.saveAndFlush(entrada);

        int databaseSizeBeforeDelete = entradaRepository.findAll().size();

        // Get the entrada
        restEntradaMockMvc.perform(delete("/api/entradas/{id}", entrada.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entrada.class);
        Entrada entrada1 = new Entrada();
        entrada1.setId(1L);
        Entrada entrada2 = new Entrada();
        entrada2.setId(entrada1.getId());
        assertThat(entrada1).isEqualTo(entrada2);
        entrada2.setId(2L);
        assertThat(entrada1).isNotEqualTo(entrada2);
        entrada1.setId(null);
        assertThat(entrada1).isNotEqualTo(entrada2);
    }
}
