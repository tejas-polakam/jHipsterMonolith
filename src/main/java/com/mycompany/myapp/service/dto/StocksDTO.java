package com.mycompany.myapp.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Stocks} entity.
 */
public class StocksDTO implements Serializable {

    private Long id;

    private String name;

    private BigDecimal open;

    private BigDecimal high;

    private BigDecimal close;

    private BigDecimal low;

    private Integer volume;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StocksDTO stocksDTO = (StocksDTO) o;
        if (stocksDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stocksDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StocksDTO{" +
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
