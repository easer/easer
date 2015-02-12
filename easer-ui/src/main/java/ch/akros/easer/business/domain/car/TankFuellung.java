package ch.akros.easer.business.domain.car;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by lukovics on 09.02.2015.
 */
@Entity
public class TankFuellung {

    @Id
    @SequenceGenerator(name = "tankfuellung_id_seq", sequenceName = "tankfuellung_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tankfuellung_id_seq")
    private Integer id;
    @Version
    private Long version;
    @Digits(integer = 2, fraction = 2)
    private BigDecimal menge;
    @Digits(integer = 2, fraction = 2)
    private BigDecimal preisProLiter;
    @Digits(integer = 4, fraction = 2)
    private BigDecimal preisTotal;
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate datum = LocalDate.now();
    @Size(min = 0, max = 100)
    private String bemerkung;
    @Size(min = 2, max = 100)
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

    public enum Properties {
        menge,
        preisProLiter,
        preisTotal,
        datum,
        bemerkung,
        fahrer
    }
}
