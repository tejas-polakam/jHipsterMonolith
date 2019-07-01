package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.StocksDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Stocks}.
 */
public interface StocksService {

    /**
     * Save a stocks.
     *
     * @param stocksDTO the entity to save.
     * @return the persisted entity.
     */
    StocksDTO save(StocksDTO stocksDTO);

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StocksDTO> findAll(Pageable pageable);


    /**
     * Get the "id" stocks.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StocksDTO> findOne(Long id);

    /**
     * Delete the "id" stocks.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
