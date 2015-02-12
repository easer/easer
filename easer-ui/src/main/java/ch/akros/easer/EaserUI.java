package ch.akros.easer;

import ch.akros.easer.business.domain.car.TankFuellung;
import ch.akros.easer.business.domain.car.TankFuellungProviderBean;
import ch.akros.easer.ui.LocalDateField;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 */
@Theme("mytheme")
@Widgetset("ch.akros.MyAppWidgetset")
@Title("easer")
@SessionScoped
@PreserveOnRefresh
public class EaserUI extends UI {

    @Inject
    private TankFuellungProviderBean tankFuellungProviderBean;

    private JPAContainer<TankFuellung> tankFuellungJPAContainer = new JPAContainer<>(
            TankFuellung.class);

    private Button btnNeueTankfuellung = new Button("neue Tankfüllung");
    private TabSheet tsMain = new TabSheet();
    private VerticalLayout tabTanken = new VerticalLayout();


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // set the provider first, we use it everywhere
        tankFuellungJPAContainer.setEntityProvider(tankFuellungProviderBean);

        btnNeueTankfuellung.addClickListener(windowEvent -> {
                    Window detailWindow = buildDetailWindow();
                    EntityItem<TankFuellung> entityItem = tankFuellungJPAContainer.createEntityItem(new TankFuellung());
                    FieldGroup fieldGroup = buildAndBindFieldGroup(entityItem);
                    FormLayout formLayout = buildForm(fieldGroup, detailWindow);
                    detailWindow.setContent(formLayout);
                    UI.getCurrent().addWindow(detailWindow);
                }
        );

        tabTanken.addComponent(btnNeueTankfuellung);
        tabTanken.addComponent(buildTable());
        tabTanken.setMargin(true);
        tabTanken.setSpacing(true);
        tsMain.setStyleName("ts-main");
        tsMain.addTab(tabTanken, "tanken");
        setContent(tsMain);
        setSizeFull();
    }

    private FieldGroup buildAndBindFieldGroup(EntityItem<TankFuellung> entityItem) {
        final FieldGroup fieldGroup = new FieldGroup();
        fieldGroup.setItemDataSource(entityItem);

        // create Fields
        LocalDateField fldDatum = new LocalDateField("Datum");
        fldDatum.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.datum.name()));

        TextField fldFahrer = new TextField("Fahrer");
        fldFahrer.setNullRepresentation("");
        fldFahrer.setImmediate(true);
        fldFahrer.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.fahrer.name()));

        TextField fldMenge = new TextField("Menge");
        fldMenge.setNullRepresentation("");
        fldMenge.setImmediate(true);
        fldMenge.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.menge.name()));
        fldMenge.setConverter(new StringToBigDecimalConverter());

        TextField fldPreisProLiter = new TextField("CHF/Liter");
        fldPreisProLiter.setNullRepresentation("");
        fldPreisProLiter.setImmediate(true);
        fldPreisProLiter.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.preisProLiter.name()));
        fldPreisProLiter.setConverter(new StringToBigDecimalConverter());

        TextField fldPreisTotal = new TextField("Total");
        fldPreisTotal.setNullRepresentation("");
        fldPreisTotal.setImmediate(true);
        fldPreisTotal.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.preisTotal.name()));

        fieldGroup.bind(fldDatum, TankFuellung.Properties.datum.name());
        fieldGroup.bind(fldFahrer, TankFuellung.Properties.fahrer.name());
        fieldGroup.bind(fldMenge, TankFuellung.Properties.menge.name());
        fieldGroup.bind(fldPreisProLiter, TankFuellung.Properties.preisProLiter.name());
        fieldGroup.bind(fldPreisTotal, TankFuellung.Properties.preisTotal.name());
        return fieldGroup;
    }

    private FormLayout buildForm(FieldGroup fieldGroup, Window window) {
        FormLayout formLayout = new FormLayout();
        formLayout.setSizeUndefined();
        formLayout.setMargin(true);
        Button speichernButton = new Button("Speichern");

        speichernButton.addClickListener(saveEvent -> {
            try {
                fieldGroup.commit();
                TankFuellung tankFuellung = (TankFuellung) ((EntityItem) fieldGroup.getItemDataSource()).getEntity();
                tankFuellungJPAContainer.addEntity(tankFuellung);
                window.close();
            } catch (FieldGroup.CommitException e) {
                // validateException
            }
        });
        fieldGroup.getFields().forEach(formLayout::addComponent);
        formLayout.addComponent(speichernButton);
        return formLayout;
    }

    private Table buildTable() {
        final Table tblTankfuellung = new Table();
        tblTankfuellung.setContainerDataSource(tankFuellungJPAContainer);
        tblTankfuellung.setVisibleColumns("datum", "fahrer", "menge", "preisProLiter", "preisTotal");
        tblTankfuellung.setColumnHeaders("Datum", "Fahrer", "Menge", "Preis pro Liter", "Preis Total");
        tblTankfuellung.setColumnAlignment("menge", Table.Align.RIGHT);
        tblTankfuellung.setColumnAlignment("preisProLiter", Table.Align.RIGHT);
        tblTankfuellung.setColumnAlignment("preisTotal", Table.Align.RIGHT);
        tblTankfuellung.setSizeFull();
        tblTankfuellung.setPageLength(0);
        return tblTankfuellung;
    }

    private Window buildDetailWindow() {
        Window detailWindow = new Window("neue Tankfüllung");
        detailWindow.setSizeUndefined();
        detailWindow.setWindowMode(WindowMode.NORMAL);
        detailWindow.setResizable(false);
        detailWindow.setModal(true);
        detailWindow.center();
        return detailWindow;
    }
}
