package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Stocks} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.StocksResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StocksCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BigDecimalFilter open;

    private BigDecimalFilter high;

    private BigDecimalFilter close;

    private BigDecimalFilter low;

    private IntegerFilter volume;

    public StocksCriteria(){
    }

    public StocksCriteria(StocksCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.open = other.open == null ? null : other.open.copy();
        this.high = other.high == null ? null : other.high.copy();
        this.close = other.close == null ? null : other.close.copy();
        this.low = other.low == null ? null : other.low.copy();
        this.volume = other.volume == null ? null : other.volume.copy();
    }

    @Override
    public StocksCriteria copy() {
        return new StocksCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BigDecimalFilter getOpen() {
        return open;
    }

    public void setOpen(BigDecimalFilter open) {
        this.open = open;
    }

    public BigDecimalFilter getHigh() {
        return high;
    }

    public void setHigh(BigDecimalFilter high) {
        this.high = high;
    }

    public BigDecimalFilter getClose() {
        return close;
    }

    public void setClose(BigDecimalFilter close) {
        this.close = close;
    }

    public BigDecimalFilter getLow() {
        return low;
    }

    public void setLow(BigDecimalFilter low) {
        this.low = low;
    }

    public IntegerFilter getVolume() {
        return volume;
    }

    public void setVolume(IntegerFilter volume) {
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
        final StocksCriteria that = (StocksCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(open, that.open) &&
            Objects.equals(high, that.high) &&
            Objects.equals(close, that.close) &&
            Objects.equals(low, that.low) &&
            Objects.equals(volume, that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        open,
        high,
        close,
        low,
        volume
        );
    }

    @Override
    public String toString() {
        return "StocksCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (open != null ? "open=" + open + ", " : "") +
                (high != null ? "high=" + high + ", " : "") +
                (close != null ? "close=" + close + ", " : "") +
                (low != null ? "low=" + low + ", " : "") +
                (volume != null ? "volume=" + volume + ", " : "") +
            "}";
    }

}
