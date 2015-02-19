package ch.akros.easer.business.domain.car;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by lukovics on 09.02.2015.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = TankFuellung.FIND_ALL,
                query = TankFuellung.FIND_ALL_QUERY),
        @NamedQuery(name = TankFuellung.FIND_BY_FAHRER,
                query = TankFuellung.FIND_BY_FAHRER_QUERY),
        @NamedQuery(name = TankFuellung.FIND_ALL_IN_YEAR,
                query = TankFuellung.FIND_ALL_IN_YEAR_QUERY),
})
public class TankFuellung {

    public final static String FIND_ALL = "TankFuellung.findAll";
    public static final String FIND_BY_FAHRER = "TankFuellung.findByFahrer";
    public static final String FIND_BY_FAHRER_QUERY = "SELECT t FROM TankFuellung t WHERE t.fahrer = :fahrer";
    public static final String FIND_ALL_IN_YEAR = "TankFuellung.findAllInYear";
    public static final String FIND_ALL_IN_YEAR_QUERY = "SELECT t FROM TankFuellung t WHERE FUNCTION('date_part','year', t.datum)=:datum";
    protected final static String FIND_ALL_QUERY = "SELECT t FROM TankFuellung t";
    @Id
    @SequenceGenerator(name = "tankfuellung_id_seq", sequenceName = "tankfuellung_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tankfuellung_id_seq")
    private Integer id;
    @Version
    private Long version;
    @Digits(integer = 2, fraction = 2, message = "{ch.akros.easer.business.domain.car.tankfuellung.bigdecimal}")
    private BigDecimal menge;
    @Digits(integer = 2, fraction = 2, message = "{ch.akros.easer.business.domain.car.tankfuellung.bigdecimal}")
    private BigDecimal preisProLiter;
    @NotNull
    @Digits(integer = 4, fraction = 2, message = "{ch.akros.easer.business.domain.car.tankfuellung.bigdecima.preistotal}")
    private BigDecimal preisTotal;
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate datum = LocalDate.now();
    @Size(min = 0, max = 100)
    private String bemerkung;
    @NotNull

    private String fahrer;

    public TankFuellung() {
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getMenge() {
        return menge;
    }

    public void setMenge(BigDecimal menge) {
        this.menge = menge;
    }

    public BigDecimal getPreisProLiter() {
        return preisProLiter;
    }

    public void setPreisProLiter(BigDecimal preisProLiter) {
        this.preisProLiter = preisProLiter;
    }

    public BigDecimal getPreisTotal() {
        return preisTotal;
    }

    public void setPreisTotal(BigDecimal preisTotal) {
        this.preisTotal = preisTotal;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    public String getFahrer() {
        return fahrer;
    }

    public void setFahrer(String fahrer) {
        this.fahrer = fahrer;
    }

    public Long getVersion() {
        return version;
    }

    public TankFuellung copy() {
        TankFuellung entity = new TankFuellung();
        entity.setFahrer(this.getFahrer());
        entity.setBemerkung(this.getBemerkung());
        entity.setDatum(this.getDatum());
        entity.setMenge(this.getMenge());
        entity.setPreisProLiter(this.getPreisProLiter());
        entity.setPreisTotal(this.getPreisTotal());
        return entity;
    }

    public enum Properties {
        menge,
        preisProLiter,
        preisTotal,
        datum,
        bemerkung,
        fahrer
    }
}
