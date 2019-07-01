package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Stocks.
 */
@Entity
@Table(name = "stocks")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stocks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "open", precision = 21, scale = 2)
    private BigDecimal open;

    @Column(name = "high", precision = 21, scale = 2)
    private BigDecimal high;

    @Column(name = "close", precision = 21, scale = 2)
    private BigDecimal close;

    @Column(name = "low", precision = 21, scale = 2)
    private BigDecimal low;

    @Column(name = "volume")
    private Integer volume;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Stocks name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public Stocks open(BigDecimal open) {
        this.open = open;
        return this;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public Stocks high(BigDecimal high) {
        this.high = high;
        return this;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getClose() {
        return close;
    }

    public Stocks close(BigDecimal close) {
        this.close = close;
        return this;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getLow() {
        return low;
    }

    public Stocks low(BigDecimal low) {
        this.low = low;
        return this;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public Integer getVolume() {
        return volume;
    }

    public Stocks volume(Integer volume) {
        this.volume = volume;
        return this;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stocks)) {
            return false;
        }
        return id != null && id.equals(((Stocks) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Stocks{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", open=" + getOpen() +
            ", high=" + getHigh() +
            ", close=" + getClose() +
            ", low=" + getLow() +
            ", volume=" + getVolume() +
            "}";
    }
}
