package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Stocks;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.StocksRepository;
import com.mycompany.myapp.service.dto.StocksCriteria;
import com.mycompany.myapp.service.dto.StocksDTO;
import com.mycompany.myapp.service.mapper.StocksMapper;

/**
 * Service for executing complex queries for {@link Stocks} entities in the database.
 * The main input is a {@link StocksCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StocksDTO} or a {@link Page} of {@link StocksDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StocksQueryService extends QueryService<Stocks> {

    private final Logger log = LoggerFactory.getLogger(StocksQueryService.class);

    private final StocksRepository stocksRepository;

    private final StocksMapper stocksMapper;

    public StocksQueryService(StocksRepository stocksRepository, StocksMapper stocksMapper) {
        this.stocksRepository = stocksRepository;
        this.stocksMapper = stocksMapper;
    }

    /**
     * Return a {@link List} of {@link StocksDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StocksDTO> findByCriteria(StocksCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Stocks> specification = createSpecification(criteria);
        return stocksMapper.toDto(stocksRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StocksDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StocksDTO> findByCriteria(StocksCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Stocks> specification = createSpecification(criteria);
        return stocksRepository.findAll(specification, page)
            .map(stocksMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StocksCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Stocks> specification = createSpecification(criteria);
        return stocksRepository.count(specification);
    }

    /**
     * Function to convert StocksCriteria to a {@link Specification}.
     */
    private Specification<Stocks> createSpecification(StocksCriteria criteria) {
        Specification<Stocks> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Stocks_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Stocks_.name));
            }
            if (criteria.getOpen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpen(), Stocks_.open));
            }
            if (criteria.getHigh() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHigh(), Stocks_.high));
            }
            if (criteria.getClose() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClose(), Stocks_.close));
            }
            if (criteria.getLow() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLow(), Stocks_.low));
            }
            if (criteria.getVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVolume(), Stocks_.volume));
            }
        }
        return specification;
    }
}
