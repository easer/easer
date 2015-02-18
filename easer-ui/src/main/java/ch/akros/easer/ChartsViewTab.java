package ch.akros.easer;

import ch.akros.easer.business.domain.car.TankFuellung;
import ch.akros.easer.business.domain.car.TankFuellungProviderBean;
import ch.akros.easer.ui.EaserTab;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lukovics on 17.02.2015.
 */
public class ChartsViewTab extends CustomComponent implements EaserTab {

    private VerticalLayout tabCharts = new VerticalLayout();

    @Inject
    private TankenViewTab tankenView;

    @Inject
    private TankFuellungProviderBean tankFuellungProviderBean;

    private Chart chart = new Chart(ChartType.BAR);
    private ListSeries series = new ListSeries("Preis pro Liter");


    @PostConstruct
    private void init() {
        chart.setSizeUndefined();

// Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setSeries(series);
        conf.setTitle("Tankfüllungen / Preis pro Liter");
        conf.getLegend().setEnabled(false);

// The data
        TypedQuery<TankFuellung> namedQuery = tankFuellungProviderBean.getEntityManager().
                createNamedQuery(TankFuellung.FIND_ALL, TankFuellung.class);


// Set the Y axis title
        YAxis yaxis = new YAxis();
        yaxis.setTitle("Preis pro Liter");
        yaxis.getLabels().setFormatter("function() {return Math.floor(this.value) + \' CHF\';}");
        yaxis.setAllowDecimals(true);
        yaxis.setMin(0.0);
        yaxis.setMax(2);
        conf.addyAxis(yaxis);
        tabCharts.addComponent(chart);
        tabCharts.setSpacing(true);
        setCompositionRoot(tabCharts);
    }

    private void reloadSeries(List<TankFuellung> list) {
        List<Number> data = list.stream().map(t -> t.getPreisProLiter()).collect(Collectors.toList());
        list.forEach(t -> series.setData(data));
        List<String> categories = list.stream().map(t -> t.getDatum().toString()).collect(Collectors.toList());

        XAxis xaxis = new XAxis();
        xaxis.setTitle("Datum");
        chart.getConfiguration().addxAxis(xaxis);
        xaxis.setCategories(categories.toArray(new String[categories.size()]));
    }

    @Override
    public void reload() {
        reloadSeries(tankFuellungProviderBean.getEntityManager().
                createNamedQuery(TankFuellung.FIND_ALL, TankFuellung.class).getResultList());
    }
}
