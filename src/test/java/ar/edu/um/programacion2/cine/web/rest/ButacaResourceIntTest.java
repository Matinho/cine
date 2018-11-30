package ar.edu.um.programacion2.cine.web.rest;

import ar.edu.um.programacion2.cine.CineApp;

import ar.edu.um.programacion2.cine.domain.Butaca;
import ar.edu.um.programacion2.cine.repository.ButacaRepository;
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
 * Test class for the ButacaResource REST controller.
 *
 * @see ButacaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CineApp.class)
public class ButacaResourceIntTest {

    private static final String DEFAULT_FILA = "A";
    private static final String UPDATED_FILA = "B";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ButacaRepository butacaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restButacaMockMvc;

    private Butaca butaca;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ButacaResource butacaResource = new ButacaResource(butacaRepository);
        this.restButacaMockMvc = MockMvcBuilders.standaloneSetup(butacaResource)
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
    public static Butaca createEntity(EntityManager em) {
        Butaca butaca = new Butaca()
            .fila(DEFAULT_FILA)
            .numero(DEFAULT_NUMERO)
            .descripcion(DEFAULT_DESCRIPCION)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return butaca;
    }

    @Before
    public void initTest() {
        butaca = createEntity(em);
    }

    @Test
    @Transactional
    public void createButaca() throws Exception {
        int databaseSizeBeforeCreate = butacaRepository.findAll().size();

        // Create the Butaca
        restButacaMockMvc.perform(post("/api/butacas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(butaca)))
            .andExpect(status().isCreated());

        // Validate the Butaca in the database
        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeCreate + 1);
        Butaca testButaca = butacaList.get(butacaList.size() - 1);
        assertThat(testButaca.getFila()).isEqualTo(DEFAULT_FILA);
        assertThat(testButaca.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testButaca.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testButaca.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testButaca.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createButacaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = butacaRepository.findAll().size();

        // Create the Butaca with an existing ID
        butaca.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restButacaMockMvc.perform(post("/api/butacas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(butaca)))
            .andExpect(status().isBadRequest());

        // Validate the Butaca in the database
        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFilaIsRequired() throws Exception {
        int databaseSizeBeforeTest = butacaRepository.findAll().size();
        // set the field null
        butaca.setFila(null);

        // Create the Butaca, which fails.

        restButacaMockMvc.perform(post("/api/butacas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(butaca)))
            .andExpect(status().isBadRequest());

        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = butacaRepository.findAll().size();
        // set the field null
        butaca.setNumero(null);

        // Create the Butaca, which fails.

        restButacaMockMvc.perform(post("/api/butacas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(butaca)))
            .andExpect(status().isBadRequest());

        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = butacaRepository.findAll().size();
        // set the field null
        butaca.setDescripcion(null);

        // Create the Butaca, which fails.

        restButacaMockMvc.perform(post("/api/butacas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(butaca)))
            .andExpect(status().isBadRequest());

        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = butacaRepository.findAll().size();
        // set the field null
        butaca.setCreated(null);

        // Create the Butaca, which fails.

        restButacaMockMvc.perform(post("/api/butacas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(butaca)))
            .andExpect(status().isBadRequest());

        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = butacaRepository.findAll().size();
        // set the field null
        butaca.setUpdated(null);

        // Create the Butaca, which fails.

        restButacaMockMvc.perform(post("/api/butacas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(butaca)))
            .andExpect(status().isBadRequest());

        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllButacas() throws Exception {
        // Initialize the database
        butacaRepository.saveAndFlush(butaca);

        // Get all the butacaList
        restButacaMockMvc.perform(get("/api/butacas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(butaca.getId().intValue())))
            .andExpect(jsonPath("$.[*].fila").value(hasItem(DEFAULT_FILA.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }
    
    @Test
    @Transactional
    public void getButaca() throws Exception {
        // Initialize the database
        butacaRepository.saveAndFlush(butaca);

        // Get the butaca
        restButacaMockMvc.perform(get("/api/butacas/{id}", butaca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(butaca.getId().intValue()))
            .andExpect(jsonPath("$.fila").value(DEFAULT_FILA.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)));
    }

    @Test
    @Transactional
    public void getNonExistingButaca() throws Exception {
        // Get the butaca
        restButacaMockMvc.perform(get("/api/butacas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateButaca() throws Exception {
        // Initialize the database
        butacaRepository.saveAndFlush(butaca);

        int databaseSizeBeforeUpdate = butacaRepository.findAll().size();

        // Update the butaca
        Butaca updatedButaca = butacaRepository.findById(butaca.getId()).get();
        // Disconnect from session so that the updates on updatedButaca are not directly saved in db
        em.detach(updatedButaca);
        updatedButaca
            .fila(UPDATED_FILA)
            .numero(UPDATED_NUMERO)
            .descripcion(UPDATED_DESCRIPCION)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restButacaMockMvc.perform(put("/api/butacas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedButaca)))
            .andExpect(status().isOk());

        // Validate the Butaca in the database
        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeUpdate);
        Butaca testButaca = butacaList.get(butacaList.size() - 1);
        assertThat(testButaca.getFila()).isEqualTo(UPDATED_FILA);
        assertThat(testButaca.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testButaca.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testButaca.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testButaca.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingButaca() throws Exception {
        int databaseSizeBeforeUpdate = butacaRepository.findAll().size();

        // Create the Butaca

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restButacaMockMvc.perform(put("/api/butacas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(butaca)))
            .andExpect(status().isBadRequest());

        // Validate the Butaca in the database
        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteButaca() throws Exception {
        // Initialize the database
        butacaRepository.saveAndFlush(butaca);

        int databaseSizeBeforeDelete = butacaRepository.findAll().size();

        // Get the butaca
        restButacaMockMvc.perform(delete("/api/butacas/{id}", butaca.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Butaca> butacaList = butacaRepository.findAll();
        assertThat(butacaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Butaca.class);
        Butaca butaca1 = new Butaca();
        butaca1.setId(1L);
        Butaca butaca2 = new Butaca();
        butaca2.setId(butaca1.getId());
        assertThat(butaca1).isEqualTo(butaca2);
        butaca2.setId(2L);
        assertThat(butaca1).isNotEqualTo(butaca2);
        butaca1.setId(null);
        assertThat(butaca1).isNotEqualTo(butaca2);
    }
}
