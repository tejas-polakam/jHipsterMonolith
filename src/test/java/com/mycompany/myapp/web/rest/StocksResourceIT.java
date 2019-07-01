package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FirstmonolithApp;
import com.mycompany.myapp.domain.Stocks;
import com.mycompany.myapp.repository.StocksRepository;
import com.mycompany.myapp.service.StocksService;
import com.mycompany.myapp.service.dto.StocksDTO;
import com.mycompany.myapp.service.mapper.StocksMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.StocksCriteria;
import com.mycompany.myapp.service.StocksQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link StocksResource} REST controller.
 */
@SpringBootTest(classes = FirstmonolithApp.class)
public class StocksResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_OPEN = new BigDecimal(1);
    private static final BigDecimal UPDATED_OPEN = new BigDecimal(2);

    private static final BigDecimal DEFAULT_HIGH = new BigDecimal(1);
    private static final BigDecimal UPDATED_HIGH = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CLOSE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CLOSE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LOW = new BigDecimal(1);
    private static final BigDecimal UPDATED_LOW = new BigDecimal(2);

    private static final Integer DEFAULT_VOLUME = 1;
    private static final Integer UPDATED_VOLUME = 2;

    @Autowired
    private StocksRepository stocksRepository;

    @Autowired
    private StocksMapper stocksMapper;

    @Autowired
    private StocksService stocksService;

    @Autowired
    private StocksQueryService stocksQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restStocksMockMvc;

    private Stocks stocks;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StocksResource stocksResource = new StocksResource(stocksService, stocksQueryService);
        this.restStocksMockMvc = MockMvcBuilders.standaloneSetup(stocksResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stocks createEntity(EntityManager em) {
        Stocks stocks = new Stocks()
            .name(DEFAULT_NAME)
            .open(DEFAULT_OPEN)
            .high(DEFAULT_HIGH)
            .close(DEFAULT_CLOSE)
            .low(DEFAULT_LOW)
            .volume(DEFAULT_VOLUME);
        return stocks;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stocks createUpdatedEntity(EntityManager em) {
        Stocks stocks = new Stocks()
            .name(UPDATED_NAME)
            .open(UPDATED_OPEN)
            .high(UPDATED_HIGH)
            .close(UPDATED_CLOSE)
            .low(UPDATED_LOW)
            .volume(UPDATED_VOLUME);
        return stocks;
    }

    @BeforeEach
    public void initTest() {
        stocks = createEntity(em);
    }

    @Test
    @Transactional
    public void createStocks() throws Exception {
        int databaseSizeBeforeCreate = stocksRepository.findAll().size();

        // Create the Stocks
        StocksDTO stocksDTO = stocksMapper.toDto(stocks);
        restStocksMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stocksDTO)))
            .andExpect(status().isCreated());

        // Validate the Stocks in the database
        List<Stocks> stocksList = stocksRepository.findAll();
        assertThat(stocksList).hasSize(databaseSizeBeforeCreate + 1);
        Stocks testStocks = stocksList.get(stocksList.size() - 1);
        assertThat(testStocks.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStocks.getOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testStocks.getHigh()).isEqualTo(DEFAULT_HIGH);
        assertThat(testStocks.getClose()).isEqualTo(DEFAULT_CLOSE);
        assertThat(testStocks.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testStocks.getVolume()).isEqualTo(DEFAULT_VOLUME);
    }

    @Test
    @Transactional
    public void createStocksWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stocksRepository.findAll().size();

        // Create the Stocks with an existing ID
        stocks.setId(1L);
        StocksDTO stocksDTO = stocksMapper.toDto(stocks);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStocksMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stocksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stocks in the database
        List<Stocks> stocksList = stocksRepository.findAll();
        assertThat(stocksList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList
        restStocksMockMvc.perform(get("/api/stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stocks.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.intValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.intValue())))
            .andExpect(jsonPath("$.[*].close").value(hasItem(DEFAULT_CLOSE.intValue())))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.intValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)));
    }
    
    @Test
    @Transactional
    public void getStocks() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get the stocks
        restStocksMockMvc.perform(get("/api/stocks/{id}", stocks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stocks.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.intValue()))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH.intValue()))
            .andExpect(jsonPath("$.close").value(DEFAULT_CLOSE.intValue()))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW.intValue()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME));
    }

    @Test
    @Transactional
    public void getAllStocksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where name equals to DEFAULT_NAME
        defaultStocksShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the stocksList where name equals to UPDATED_NAME
        defaultStocksShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStocksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStocksShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the stocksList where name equals to UPDATED_NAME
        defaultStocksShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStocksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where name is not null
        defaultStocksShouldBeFound("name.specified=true");

        // Get all the stocksList where name is null
        defaultStocksShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByOpenIsEqualToSomething() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where open equals to DEFAULT_OPEN
        defaultStocksShouldBeFound("open.equals=" + DEFAULT_OPEN);

        // Get all the stocksList where open equals to UPDATED_OPEN
        defaultStocksShouldNotBeFound("open.equals=" + UPDATED_OPEN);
    }

    @Test
    @Transactional
    public void getAllStocksByOpenIsInShouldWork() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where open in DEFAULT_OPEN or UPDATED_OPEN
        defaultStocksShouldBeFound("open.in=" + DEFAULT_OPEN + "," + UPDATED_OPEN);

        // Get all the stocksList where open equals to UPDATED_OPEN
        defaultStocksShouldNotBeFound("open.in=" + UPDATED_OPEN);
    }

    @Test
    @Transactional
    public void getAllStocksByOpenIsNullOrNotNull() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where open is not null
        defaultStocksShouldBeFound("open.specified=true");

        // Get all the stocksList where open is null
        defaultStocksShouldNotBeFound("open.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByHighIsEqualToSomething() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where high equals to DEFAULT_HIGH
        defaultStocksShouldBeFound("high.equals=" + DEFAULT_HIGH);

        // Get all the stocksList where high equals to UPDATED_HIGH
        defaultStocksShouldNotBeFound("high.equals=" + UPDATED_HIGH);
    }

    @Test
    @Transactional
    public void getAllStocksByHighIsInShouldWork() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where high in DEFAULT_HIGH or UPDATED_HIGH
        defaultStocksShouldBeFound("high.in=" + DEFAULT_HIGH + "," + UPDATED_HIGH);

        // Get all the stocksList where high equals to UPDATED_HIGH
        defaultStocksShouldNotBeFound("high.in=" + UPDATED_HIGH);
    }

    @Test
    @Transactional
    public void getAllStocksByHighIsNullOrNotNull() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where high is not null
        defaultStocksShouldBeFound("high.specified=true");

        // Get all the stocksList where high is null
        defaultStocksShouldNotBeFound("high.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByCloseIsEqualToSomething() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where close equals to DEFAULT_CLOSE
        defaultStocksShouldBeFound("close.equals=" + DEFAULT_CLOSE);

        // Get all the stocksList where close equals to UPDATED_CLOSE
        defaultStocksShouldNotBeFound("close.equals=" + UPDATED_CLOSE);
    }

    @Test
    @Transactional
    public void getAllStocksByCloseIsInShouldWork() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where close in DEFAULT_CLOSE or UPDATED_CLOSE
        defaultStocksShouldBeFound("close.in=" + DEFAULT_CLOSE + "," + UPDATED_CLOSE);

        // Get all the stocksList where close equals to UPDATED_CLOSE
        defaultStocksShouldNotBeFound("close.in=" + UPDATED_CLOSE);
    }

    @Test
    @Transactional
    public void getAllStocksByCloseIsNullOrNotNull() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where close is not null
        defaultStocksShouldBeFound("close.specified=true");

        // Get all the stocksList where close is null
        defaultStocksShouldNotBeFound("close.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByLowIsEqualToSomething() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where low equals to DEFAULT_LOW
        defaultStocksShouldBeFound("low.equals=" + DEFAULT_LOW);

        // Get all the stocksList where low equals to UPDATED_LOW
        defaultStocksShouldNotBeFound("low.equals=" + UPDATED_LOW);
    }

    @Test
    @Transactional
    public void getAllStocksByLowIsInShouldWork() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where low in DEFAULT_LOW or UPDATED_LOW
        defaultStocksShouldBeFound("low.in=" + DEFAULT_LOW + "," + UPDATED_LOW);

        // Get all the stocksList where low equals to UPDATED_LOW
        defaultStocksShouldNotBeFound("low.in=" + UPDATED_LOW);
    }

    @Test
    @Transactional
    public void getAllStocksByLowIsNullOrNotNull() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where low is not null
        defaultStocksShouldBeFound("low.specified=true");

        // Get all the stocksList where low is null
        defaultStocksShouldNotBeFound("low.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where volume equals to DEFAULT_VOLUME
        defaultStocksShouldBeFound("volume.equals=" + DEFAULT_VOLUME);

        // Get all the stocksList where volume equals to UPDATED_VOLUME
        defaultStocksShouldNotBeFound("volume.equals=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    public void getAllStocksByVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where volume in DEFAULT_VOLUME or UPDATED_VOLUME
        defaultStocksShouldBeFound("volume.in=" + DEFAULT_VOLUME + "," + UPDATED_VOLUME);

        // Get all the stocksList where volume equals to UPDATED_VOLUME
        defaultStocksShouldNotBeFound("volume.in=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    public void getAllStocksByVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where volume is not null
        defaultStocksShouldBeFound("volume.specified=true");

        // Get all the stocksList where volume is null
        defaultStocksShouldNotBeFound("volume.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where volume greater than or equals to DEFAULT_VOLUME
        defaultStocksShouldBeFound("volume.greaterOrEqualThan=" + DEFAULT_VOLUME);

        // Get all the stocksList where volume greater than or equals to UPDATED_VOLUME
        defaultStocksShouldNotBeFound("volume.greaterOrEqualThan=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    public void getAllStocksByVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        // Get all the stocksList where volume less than or equals to DEFAULT_VOLUME
        defaultStocksShouldNotBeFound("volume.lessThan=" + DEFAULT_VOLUME);

        // Get all the stocksList where volume less than or equals to UPDATED_VOLUME
        defaultStocksShouldBeFound("volume.lessThan=" + UPDATED_VOLUME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStocksShouldBeFound(String filter) throws Exception {
        restStocksMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stocks.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.intValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.intValue())))
            .andExpect(jsonPath("$.[*].close").value(hasItem(DEFAULT_CLOSE.intValue())))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.intValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)));

        // Check, that the count call also returns 1
        restStocksMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStocksShouldNotBeFound(String filter) throws Exception {
        restStocksMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStocksMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStocks() throws Exception {
        // Get the stocks
        restStocksMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStocks() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        int databaseSizeBeforeUpdate = stocksRepository.findAll().size();

        // Update the stocks
        Stocks updatedStocks = stocksRepository.findById(stocks.getId()).get();
        // Disconnect from session so that the updates on updatedStocks are not directly saved in db
        em.detach(updatedStocks);
        updatedStocks
            .name(UPDATED_NAME)
            .open(UPDATED_OPEN)
            .high(UPDATED_HIGH)
            .close(UPDATED_CLOSE)
            .low(UPDATED_LOW)
            .volume(UPDATED_VOLUME);
        StocksDTO stocksDTO = stocksMapper.toDto(updatedStocks);

        restStocksMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stocksDTO)))
            .andExpect(status().isOk());

        // Validate the Stocks in the database
        List<Stocks> stocksList = stocksRepository.findAll();
        assertThat(stocksList).hasSize(databaseSizeBeforeUpdate);
        Stocks testStocks = stocksList.get(stocksList.size() - 1);
        assertThat(testStocks.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStocks.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testStocks.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testStocks.getClose()).isEqualTo(UPDATED_CLOSE);
        assertThat(testStocks.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testStocks.getVolume()).isEqualTo(UPDATED_VOLUME);
    }

    @Test
    @Transactional
    public void updateNonExistingStocks() throws Exception {
        int databaseSizeBeforeUpdate = stocksRepository.findAll().size();

        // Create the Stocks
        StocksDTO stocksDTO = stocksMapper.toDto(stocks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStocksMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stocksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stocks in the database
        List<Stocks> stocksList = stocksRepository.findAll();
        assertThat(stocksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStocks() throws Exception {
        // Initialize the database
        stocksRepository.saveAndFlush(stocks);

        int databaseSizeBeforeDelete = stocksRepository.findAll().size();

        // Delete the stocks
        restStocksMockMvc.perform(delete("/api/stocks/{id}", stocks.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Stocks> stocksList = stocksRepository.findAll();
        assertThat(stocksList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stocks.class);
        Stocks stocks1 = new Stocks();
        stocks1.setId(1L);
        Stocks stocks2 = new Stocks();
        stocks2.setId(stocks1.getId());
        assertThat(stocks1).isEqualTo(stocks2);
        stocks2.setId(2L);
        assertThat(stocks1).isNotEqualTo(stocks2);
        stocks1.setId(null);
        assertThat(stocks1).isNotEqualTo(stocks2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StocksDTO.class);
        StocksDTO stocksDTO1 = new StocksDTO();
        stocksDTO1.setId(1L);
        StocksDTO stocksDTO2 = new StocksDTO();
        assertThat(stocksDTO1).isNotEqualTo(stocksDTO2);
        stocksDTO2.setId(stocksDTO1.getId());
        assertThat(stocksDTO1).isEqualTo(stocksDTO2);
        stocksDTO2.setId(2L);
        assertThat(stocksDTO1).isNotEqualTo(stocksDTO2);
        stocksDTO1.setId(null);
        assertThat(stocksDTO1).isNotEqualTo(stocksDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stocksMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stocksMapper.fromId(null)).isNull();
    }
}
