package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.StocksService;
import com.mycompany.myapp.domain.Stocks;
import com.mycompany.myapp.repository.StocksRepository;
import com.mycompany.myapp.service.dto.StocksDTO;
import com.mycompany.myapp.service.mapper.StocksMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Stocks}.
 */
@Service
@Transactional
public class StocksServiceImpl implements StocksService {

    private final Logger log = LoggerFactory.getLogger(StocksServiceImpl.class);

    private final StocksRepository stocksRepository;

    private final StocksMapper stocksMapper;

    public StocksServiceImpl(StocksRepository stocksRepository, StocksMapper stocksMapper) {
        this.stocksRepository = stocksRepository;
        this.stocksMapper = stocksMapper;
    }

    /**
     * Save a stocks.
     *
     * @param stocksDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StocksDTO save(StocksDTO stocksDTO) {
        log.debug("Request to save Stocks : {}", stocksDTO);
        Stocks stocks = stocksMapper.toEntity(stocksDTO);
        stocks = stocksRepository.save(stocks);
        return stocksMapper.toDto(stocks);
    }

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StocksDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stocksRepository.findAll(pageable)
            .map(stocksMapper::toDto);
    }


    /**
     * Get one stocks by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StocksDTO> findOne(Long id) {
        log.debug("Request to get Stocks : {}", id);
        return stocksRepository.findById(id)
            .map(stocksMapper::toDto);
    }

    /**
     * Delete the stocks by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stocks : {}", id);
        stocksRepository.deleteById(id);
    }
}
