package ch.akros.easer.ui;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by lukovics on 11.02.2015.
 */
public class LocalDateField extends CustomField<LocalDate> {

    private final DateField legacyDateField = new DateField();


    public LocalDateField(String caption) {
        setCaption(caption);
    }

    @Override
    protected void setInternalValue(LocalDate newValue) {
        super.setInternalValue(newValue);
        if (newValue == null) {
            legacyDateField.setValue(null);
        } else {
            Instant instant = newValue.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            legacyDateField.setValue(Date.from(instant));
        }
    }

    /**
     * Sets the resolution for UI.
     * <p>
     * The default resolution is {@link Resolution#SECOND}.
     *
     * @param resolution the resolution to set.
     */
    public void setResolution(Resolution resolution) {
        legacyDateField.setResolution(resolution);
    }

    public void setDateFormat(String dateFormat) {
        legacyDateField.setDateFormat(dateFormat);
    }

    @Override
    protected Component initContent() {
        legacyDateField.addValueChangeListener(e -> {
            Date value = legacyDateField.getValue();
            if (value == null) {
                setValue(null);
            } else {
                setValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), ZoneId.systemDefault()).toLocalDate());
            }
        });
        legacyDateField.setImmediate(true);
        legacyDateField.setResolution(Resolution.DAY);
        return legacyDateField;
    }

    @Override
    public Class<? extends LocalDate> getType() {
        return LocalDate.class;
    }

}
