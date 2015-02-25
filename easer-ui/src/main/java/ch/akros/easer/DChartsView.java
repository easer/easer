package ch.akros.easer;

import ch.akros.easer.business.domain.car.TankFuellung;
import ch.akros.easer.ui.EaserTab;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.base.elements.XYseries;
import org.dussan.vaadin.dcharts.canvasoverlays.DashedHorizontalLine;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.metadata.TooltipAxes;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
import org.dussan.vaadin.dcharts.metadata.locations.TooltipLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.*;
import org.dussan.vaadin.dcharts.renderers.tick.AxisTickRenderer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.text.DecimalFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lukovics on 19.02.2015.
 */
public class DChartsView extends CustomComponent implements EaserTab {

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
        List<Integer> years = namedQuery.getResultList().stream().map(e -> e.getDatum().getYear()).distinct().sorted().collect(Collectors.toList());

        // iterate over all years
        for (int year : years) {

            Map<Month, List<TankFuellung>> yearMap = new HashMap<>();
            List<TankFuellung> tankfuellungenInYear = namedQuery.getResultList().stream().filter(e -> e.getDatum().getYear() == year).collect(Collectors.toList());

            // calculate some values a year
            double averageMenge = tankfuellungenInYear.stream().mapToDouble(t -> t.getMenge().doubleValue()).average().getAsDouble();
            double averagePreisProLiter = tankfuellungenInYear.stream().mapToDouble(t -> t.getPreisProLiter().doubleValue()).average().getAsDouble();
            double averagePreisProLiterTransformedForY2 = averagePreisProLiter * 50;
            String formattedAveragePreisProLiter = new DecimalFormat("#.##").format(averagePreisProLiter);
            String formattedAveragePreisProLiterTransformedForY2 = new DecimalFormat("#.##").format(averagePreisProLiterTransformedForY2);
            String formattedAverageMenge = new DecimalFormat("#.##").format(averageMenge);

            List<TankFuellung> tankfulInJanuary = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.JANUARY).collect(Collectors.toList());
            List<TankFuellung> tankfulInFebruary = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.FEBRUARY).collect(Collectors.toList());
            List<TankFuellung> tankfulInMarch = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.MARCH).collect(Collectors.toList());
            List<TankFuellung> tankfulInApril = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.APRIL).collect(Collectors.toList());
            List<TankFuellung> tankfulInMay = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.MAY).collect(Collectors.toList());
            List<TankFuellung> tankfulInJune = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.JUNE).collect(Collectors.toList());
            List<TankFuellung> tankfulInJuly = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.JULY).collect(Collectors.toList());
            List<TankFuellung> tankfulInAugust = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.AUGUST).collect(Collectors.toList());
            List<TankFuellung> tankfulInSeptember = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.SEPTEMBER).collect(Collectors.toList());
            List<TankFuellung> tankfulInOctober = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.OCTOBER).collect(Collectors.toList());
            List<TankFuellung> tankfulInNovmeber = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.NOVEMBER).collect(Collectors.toList());
            List<TankFuellung> tankfulInDecember = tankfuellungenInYear.stream().filter(e -> e.getDatum().getMonth() == Month.DECEMBER).collect(Collectors.toList());

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
            int numberOfTankfuellungen = yearMap.get(monthWithMostTankfuellungen).size();

            DataSeries dataSeries = new DataSeries();
            Series series = new Series();
            // the numberOfTankfuellungen is the number of iterations(one iteration is one dataserie)
            for (int i = 0; i < numberOfTankfuellungen; i++) {
                List<Double> data = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
                // iterate over every month
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

                series.addSeries(new XYseries().setLabel("Tankfüllung"));
                dataSeries.add(data.toArray());

            }

            SeriesDefaults barSeriesDefaults = new SeriesDefaults().setRenderer(SeriesRenderers.BAR);

            Locale locale = this.getLocale();
            if (locale == null) {
                locale = Locale.GERMAN;
            }
            Axes axes = new Axes().addAxis(new XYaxis()
                    .setRenderer(AxisRenderers.CATEGORY)
                    .setTicks(new Ticks().add(Month.JANUARY.getDisplayName(TextStyle.FULL, locale),
                            Month.FEBRUARY.getDisplayName(TextStyle.FULL, locale),
                            Month.MARCH.getDisplayName(TextStyle.FULL, locale),
                            Month.APRIL.getDisplayName(TextStyle.FULL, locale),
                            Month.MAY.getDisplayName(TextStyle.FULL, locale),
                            Month.JUNE.getDisplayName(TextStyle.FULL, locale),
                            Month.JULY.getDisplayName(TextStyle.FULL, locale),
                            Month.AUGUST.getDisplayName(TextStyle.FULL, locale),
                            Month.SEPTEMBER.getDisplayName(TextStyle.FULL, locale),
                            Month.OCTOBER.getDisplayName(TextStyle.FULL, locale),
                            Month.NOVEMBER.getDisplayName(TextStyle.FULL, locale),
                            Month.DECEMBER.getDisplayName(TextStyle.FULL, locale))))
                    .addAxis(new XYaxis(XYaxes.Y)
                            .setTickOptions(new AxisTickRenderer().setFormatString("%d Liter"))
                            .setMax(100)
                            .setTickInterval(20))
                    .addAxis(new XYaxis(XYaxes.Y2)
                            .setTickOptions(new AxisTickRenderer().setFormatString("%f SFr./Liter"))
                            .setTicks(new Ticks().add(0.0, 0.4, 0.8, 1.2, 1.6, 2.0))
                            .setBorderColor("rgb(255,165,0)"));

            Highlighter highlighter = new Highlighter()
                    .setShow(true)
                    .setShowTooltip(true)
                    .setTooltipAlwaysVisible(true)
                    .setKeepTooltipInsideChart(true)
                    .setTooltipLocation(TooltipLocations.NORTH)
                    .setTooltipAxes(TooltipAxes.XY_BAR);


            String titleAsString = "Tankfüllungen im Jahr " + year + " ( Ø " + formattedAveragePreisProLiter + " SFr./Liter)";
            Title title = new Title(titleAsString);


            CanvasOverlay canvasOverlay = new CanvasOverlay()
                    .setShow(true)
                    .setObject(
                            new DashedHorizontalLine()
                                    .setY(averageMenge)
                                    .setTooltipFormatString("Durchschnittliche Betankung " + formattedAverageMenge + " Liter")
                                    .setShowTooltip(true)
                                    .setLineWidth(2)
                                    .setColor("rgb(0, 0, 0)")
                                    .setShadow(false))
                    .setObject(new DashedHorizontalLine()
                            .setY(averagePreisProLiterTransformedForY2)
                            .setTooltipFormatString("Durchschnittlicher Literpreis " + formattedAveragePreisProLiter + " SFr./Liter")
                            .setShowTooltip(true)
                            .setLineWidth(2)
                            .setColor("rgb(255,165,0)")
                            .setShadow(false));

            Options options = new Options()
                    .setSeriesDefaults(barSeriesDefaults)
                    .setTitle(title)
                    .setSeries(series)
                    .setAxes(axes)
                    .setCanvasOverlay(canvasOverlay)
                    .setHighlighter(highlighter);


            DCharts chart = new DCharts();
            chart.setSizeFull();
            chart.setMarginRight(10);
            chart.setDescription(titleAsString);
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
