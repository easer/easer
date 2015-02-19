package ch.akros.easer;

import ch.akros.easer.business.domain.car.TankFuellung;
import ch.akros.easer.ui.EaserTab;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lukovics on 19.02.2015.
 */
public class DchartsViewTab extends CustomComponent implements EaserTab {

    @Inject
    private JPAContainer<TankFuellung> tankFuellungJPAContainer;


    @PostConstruct
    private void init() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setSpacing(true);

        // get all Entities first
        TypedQuery<TankFuellung> namedQuery = tankFuellungJPAContainer.getEntityProvider().getEntityManager().
                createNamedQuery(TankFuellung.FIND_ALL, TankFuellung.class);

        // we create for every year a bar chart, so we filter all possible years
        List<Integer> years = namedQuery.getResultList().stream().map(e -> e.getDatum().getYear()).distinct().collect(Collectors.toList());

        // iterate over all years
        for (int year : years) {

            Map<Month, List<TankFuellung>> yearMap = new HashMap<>();
            List<TankFuellung> tankfuellungenInYear = namedQuery.getResultList().stream().filter(e -> e.getDatum().getYear() == year).collect(Collectors.toList());

            List<TankFuellung> tankfulInJanuary = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.JANUARY).collect(Collectors.toList());
            List<TankFuellung> tankfulInFebruary = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.FEBRUARY).collect(Collectors.toList());
            List<TankFuellung> tankfulInMarch = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.MARCH).collect(Collectors.toList());
            List<TankFuellung> tankfulInApril = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.APRIL).collect(Collectors.toList());
            List<TankFuellung> tankfulInMay = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.MAY).collect(Collectors.toList());
            List<TankFuellung> tankfulInJune = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.JUNE).collect(Collectors.toList());
            List<TankFuellung> tankfulInJuly = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.JULY).collect(Collectors.toList());
            List<TankFuellung> tankfulInAugust = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.AUGUST).collect(Collectors.toList());
            List<TankFuellung> tankfulInSeptember = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.SEPTEMBER).collect(Collectors.toList());
            List<TankFuellung> tankfulInOctober = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.OCTOBER).collect(Collectors.toList());
            List<TankFuellung> tankfulInNovmeber = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.NOVEMBER).collect(Collectors.toList());
            List<TankFuellung> tankfulInDecember = namedQuery.getResultList().stream().filter(e -> e.getDatum().getMonth() == Month.DECEMBER).collect(Collectors.toList());

            yearMap.put(Month.JANUARY, tankfulInJanuary);
            yearMap.put(Month.FEBRUARY, tankfulInFebruary);
            yearMap.put(Month.MARCH, tankfulInMarch);
            yearMap.put(Month.APRIL, tankfulInApril);
            yearMap.put(Month.MAY, tankfulInMay);
            yearMap.put(Month.JUNE, tankfulInJune);
            yearMap.put(Month.JULY, tankfulInJuly);
            yearMap.put(Month.AUGUST, tankfulInAugust);
            yearMap.put(Month.SEPTEMBER, tankfulInSeptember);
            yearMap.put(Month.OCTOBER, tankfulInOctober);
            yearMap.put(Month.NOVEMBER, tankfulInNovmeber);
            yearMap.put(Month.DECEMBER, tankfulInDecember);

            Month monthWithMostTankfuellungen = getMonthWithMostTankfuellungen(yearMap);
            int sizeOfThisMonth = yearMap.get(monthWithMostTankfuellungen).size();

            DataSeries dataSeries = new DataSeries();
            for (int i = 0; i < sizeOfThisMonth; i++) {
                List<Double> data = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
                for (Map.Entry<Month, List<TankFuellung>> entry : yearMap.entrySet()) {
                    if (entry.getValue().size() > i) {
                        switch (entry.getKey()) {
                            case JANUARY:
                                data.set(0, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case FEBRUARY:
                                data.set(1, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case MARCH:
                                data.set(2, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case APRIL:
                                data.set(3, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case MAY:
                                data.set(4, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case JUNE:
                                data.set(5, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case JULY:
                                data.set(6, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case AUGUST:
                                data.set(7, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case SEPTEMBER:
                                data.set(8, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case OCTOBER:
                                data.set(9, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case NOVEMBER:
                                data.set(10, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                            case DECEMBER:
                                data.set(11, entry.getValue().get(i).getMenge().doubleValue());
                                break;
                        }
                    }
                }
                dataSeries.add(data.toArray());
            }

            SeriesDefaults seriesDefaults = new SeriesDefaults().setRenderer(SeriesRenderers.BAR);
            Axes axes = new Axes().addAxis(new XYaxis()
                    .setRenderer(AxisRenderers.CATEGORY)
                    .setTicks(new Ticks().add("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember")));
            Highlighter highlighter = new Highlighter().setShow(false);

            Options options = new Options()
                    .setSeriesDefaults(seriesDefaults)
                    .setAxes(axes)
                    .setHighlighter(highlighter);

            DCharts chart = new DCharts();
            chart.setSizeFull();
            chart.setDescription(String.valueOf(year));
            chart.setDataSeries(dataSeries)
                    .setOptions(options)
                    .show();

            layout.addComponent(chart);
        }

        setCompositionRoot(layout);
    }

    private Month getMonthWithMostTankfuellungen(Map<Month, List<TankFuellung>> yearMap) {

        Month month = Month.JANUARY;
        int size = 0;
        for (Map.Entry<Month, List<TankFuellung>> entry : yearMap.entrySet()) {
            if (entry.getValue().size() > size) {
                size = entry.getValue().size();
                month = entry.getKey();
            }
        }
        return month;
    }

    @Override
    public void reload() {
        init();
    }
}
