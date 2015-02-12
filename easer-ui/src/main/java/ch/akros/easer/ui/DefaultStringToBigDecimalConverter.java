package ch.akros.easer.ui;

import com.vaadin.data.util.converter.StringToBigDecimalConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by lukovics on 12.02.2015.
 */
public class DefaultStringToBigDecimalConverter extends StringToBigDecimalConverter {

    protected NumberFormat getFormat(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        NumberFormat numberInstance = DecimalFormat.getNumberInstance(locale);
        numberInstance.setMaximumIntegerDigits(2);
        numberInstance.setMinimumFractionDigits(2);
        numberInstance.setGroupingUsed(true);
        return numberInstance;
    }
}
