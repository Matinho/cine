package ar.edu.um.programacion2.cine.web.rest;

import ar.edu.um.programacion2.cine.CineApp;

import ar.edu.um.programacion2.cine.domain.Pelicula;
import ar.edu.um.programacion2.cine.repository.PeliculaRepository;
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
 * Test class for the PeliculaResource REST controller.
 *
 * @see PeliculaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CineApp.class)
public class PeliculaResourceIntTest {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ESTRENO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ESTRENO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeliculaMockMvc;

    private Pelicula pelicula;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeliculaResource peliculaResource = new PeliculaResource(peliculaRepository);
        this.restPeliculaMockMvc = MockMvcBuilders.standaloneSetup(peliculaResource)
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
    public static Pelicula createEntity(EntityManager em) {
        Pelicula pelicula = new Pelicula()
            .titulo(DEFAULT_TITULO)
            .estreno(DEFAULT_ESTRENO)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return pelicula;
    }

    @Before
    public void initTest() {
        pelicula = createEntity(em);
    }

    @Test
    @Transactional
    public void createPelicula() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // Create the Pelicula
        restPeliculaMockMvc.perform(post("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isCreated());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate + 1);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testPelicula.getEstreno()).isEqualTo(DEFAULT_ESTRENO);
        assertThat(testPelicula.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPelicula.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createPeliculaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // Create the Pelicula with an existing ID
        pelicula.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeliculaMockMvc.perform(post("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        pelicula.setTitulo(null);

        // Create the Pelicula, which fails.

        restPeliculaMockMvc.perform(post("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstrenoIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        pelicula.setEstreno(null);

        // Create the Pelicula, which fails.

        restPeliculaMockMvc.perform(post("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        pelicula.setCreated(null);

        // Create the Pelicula, which fails.

        restPeliculaMockMvc.perform(post("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        pelicula.setUpdated(null);

        // Create the Pelicula, which fails.

        restPeliculaMockMvc.perform(post("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeliculas() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList
        restPeliculaMockMvc.perform(get("/api/peliculas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pelicula.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())))
            .andExpect(jsonPath("$.[*].estreno").value(hasItem(sameInstant(DEFAULT_ESTRENO))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }
    
    @Test
    @Transactional
    public void getPelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get the pelicula
        restPeliculaMockMvc.perform(get("/api/peliculas/{id}", pelicula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pelicula.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO.toString()))
            .andExpect(jsonPath("$.estreno").value(sameInstant(DEFAULT_ESTRENO)))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)));
    }

    @Test
    @Transactional
    public void getNonExistingPelicula() throws Exception {
        // Get the pelicula
        restPeliculaMockMvc.perform(get("/api/peliculas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula
        Pelicula updatedPelicula = peliculaRepository.findById(pelicula.getId()).get();
        // Disconnect from session so that the updates on updatedPelicula are not directly saved in db
        em.detach(updatedPelicula);
        updatedPelicula
            .titulo(UPDATED_TITULO)
            .estreno(UPDATED_ESTRENO)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restPeliculaMockMvc.perform(put("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPelicula)))
            .andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testPelicula.getEstreno()).isEqualTo(UPDATED_ESTRENO);
        assertThat(testPelicula.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPelicula.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Create the Pelicula

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeliculaMockMvc.perform(put("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeDelete = peliculaRepository.findAll().size();

        // Get the pelicula
        restPeliculaMockMvc.perform(delete("/api/peliculas/{id}", pelicula.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pelicula.class);
        Pelicula pelicula1 = new Pelicula();
        pelicula1.setId(1L);
        Pelicula pelicula2 = new Pelicula();
        pelicula2.setId(pelicula1.getId());
        assertThat(pelicula1).isEqualTo(pelicula2);
        pelicula2.setId(2L);
        assertThat(pelicula1).isNotEqualTo(pelicula2);
        pelicula1.setId(null);
        assertThat(pelicula1).isNotEqualTo(pelicula2);
    }
}
