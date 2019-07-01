package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.StocksService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.StocksDTO;
import com.mycompany.myapp.service.dto.StocksCriteria;
import com.mycompany.myapp.service.StocksQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Stocks}.
 */
@RestController
@RequestMapping("/api")
public class StocksResource {

    private final Logger log = LoggerFactory.getLogger(StocksResource.class);

    private static final String ENTITY_NAME = "stocks";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StocksService stocksService;

    private final StocksQueryService stocksQueryService;

    public StocksResource(StocksService stocksService, StocksQueryService stocksQueryService) {
        this.stocksService = stocksService;
        this.stocksQueryService = stocksQueryService;
    }

    /**
     * {@code POST  /stocks} : Create a new stocks.
     *
     * @param stocksDTO the stocksDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stocksDTO, or with status {@code 400 (Bad Request)} if the stocks has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stocks")
    public ResponseEntity<StocksDTO> createStocks(@RequestBody StocksDTO stocksDTO) throws URISyntaxException {
        log.debug("REST request to save Stocks : {}", stocksDTO);
        if (stocksDTO.getId() != null) {
            throw new BadRequestAlertException("A new stocks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StocksDTO result = stocksService.save(stocksDTO);
        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stocks} : Updates an existing stocks.
     *
     * @param stocksDTO the stocksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stocksDTO,
     * or with status {@code 400 (Bad Request)} if the stocksDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stocksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stocks")
    public ResponseEntity<StocksDTO> updateStocks(@RequestBody StocksDTO stocksDTO) throws URISyntaxException {
        log.debug("REST request to update Stocks : {}", stocksDTO);
        if (stocksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StocksDTO result = stocksService.save(stocksDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stocksDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /stocks} : get all the stocks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stocks in body.
     */
    @GetMapping("/stocks")
    public ResponseEntity<List<StocksDTO>> getAllStocks(StocksCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Stocks by criteria: {}", criteria);
        Page<StocksDTO> page = stocksQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /stocks/count} : count all the stocks.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/stocks/count")
    public ResponseEntity<Long> countStocks(StocksCriteria criteria) {
        log.debug("REST request to count Stocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(stocksQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /stocks/:id} : get the "id" stocks.
     *
     * @param id the id of the stocksDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stocksDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stocks/{id}")
    public ResponseEntity<StocksDTO> getStocks(@PathVariable Long id) {
        log.debug("REST request to get Stocks : {}", id);
        Optional<StocksDTO> stocksDTO = stocksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stocksDTO);
    }

    /**
     * {@code DELETE  /stocks/:id} : delete the "id" stocks.
     *
     * @param id the id of the stocksDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> deleteStocks(@PathVariable Long id) {
        log.debug("REST request to delete Stocks : {}", id);
        stocksService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
