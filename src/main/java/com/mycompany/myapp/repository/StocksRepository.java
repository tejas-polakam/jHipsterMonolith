package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Stocks;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Stocks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StocksRepository extends JpaRepository<Stocks, Long>, JpaSpecificationExecutor<Stocks> {

}
