package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.StocksDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stocks} and its DTO {@link StocksDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StocksMapper extends EntityMapper<StocksDTO, Stocks> {



    default Stocks fromId(Long id) {
        if (id == null) {
            return null;
        }
        Stocks stocks = new Stocks();
        stocks.setId(id);
        return stocks;
    }
}
