package ch.akros.easer;

import ch.akros.easer.business.domain.car.TankFuellung;
import ch.akros.easer.business.domain.car.TankFuellungProviderBean;
import ch.akros.easer.ui.CurrencyStringToBigDecimalConverter;
import ch.akros.easer.ui.DefaultStringToBigDecimalConverter;
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
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
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

    private final static String DATUM = "Datum";
    private final static String FAHRER = "Fahrer";
    private final static String MENGE = "Tankfüllung in Liter";
    private final static String PREIS_PRO_LITER = "Kosten/Liter";
    private final static String KOSTEN_TOTAL = "Kosten Total";
    @Inject
    private TankFuellungProviderBean tankFuellungProviderBean;
    private JPAContainer<TankFuellung> tankFuellungJPAContainer = new JPAContainer<>(
            TankFuellung.class);
    private Button btnNeueTankfuellung = new Button("neue Tankfüllung...", FontAwesome.CAR);
    private TabSheet tsMain = new TabSheet();
    private VerticalLayout tabTanken = new VerticalLayout();
    private Table tblTankfuellung = new Table();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // set the provider first, we use it everywhere
        tankFuellungJPAContainer.setEntityProvider(tankFuellungProviderBean);

        btnNeueTankfuellung.addClickListener(windowEvent -> {
                    EntityItem<TankFuellung> entityItem = tankFuellungJPAContainer.createEntityItem(new TankFuellung());
                    Window detailWindow = buildDetailWindow();
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

    private FieldGroup buildAndBindFieldGroup(EntityItem entityItem) {
        final FieldGroup fieldGroup = new FieldGroup();
        fieldGroup.setItemDataSource(entityItem);

        // create Fields
        LocalDateField fldDatum = new LocalDateField(DATUM);
        fldDatum.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.datum.name()));

        TextField fldFahrer = new TextField(FAHRER);
        fldFahrer.setInputPrompt("Elvis Presley");
        fldFahrer.setRequired(true);
        fldFahrer.setNullRepresentation("");
        fldFahrer.setImmediate(true);
        fldFahrer.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.fahrer.name()));

        TextField fldMenge = new TextField(MENGE);
        fldMenge.setNullRepresentation("");
        fldMenge.setInputPrompt("61.25");
        fldMenge.setImmediate(true);
        fldMenge.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.menge.name()));
        fldMenge.setConverter(new StringToBigDecimalConverter());

        TextField fldPreisProLiter = new TextField(PREIS_PRO_LITER);
        fldPreisProLiter.setNullRepresentation("");
        fldPreisProLiter.setInputPrompt("1.45");
        fldPreisProLiter.setImmediate(true);
        fldPreisProLiter.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.preisProLiter.name()));
        fldPreisProLiter.setConverter(new StringToBigDecimalConverter());

        TextField fldPreisTotal = new TextField(KOSTEN_TOTAL);
        fldPreisTotal.setRequired(true);
        fldPreisTotal.setInputPrompt("88.81");
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

        formLayout.setMargin(true);
        formLayout.setSizeUndefined();

        Button speichernButton = new Button("Speichern", FontAwesome.SAVE);
        speichernButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);


        speichernButton.addClickListener(saveEvent -> {
            try {
                fieldGroup.commit();
                EntityItem entityItem = (EntityItem) fieldGroup.getItemDataSource();
                TankFuellung tankFuellung = (TankFuellung) entityItem.getEntity();
                if (!entityItem.isPersistent()) {
                    tankFuellungJPAContainer.addEntity(tankFuellung);
                } else {
                    tankFuellungJPAContainer.refreshItem(entityItem.getItemId());
                }
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

        tblTankfuellung.setContainerDataSource(tankFuellungJPAContainer);
        tblTankfuellung.addGeneratedColumn("d", (source, itemId, columnId) -> {

            Button deleteButton = new Button("", FontAwesome.MINUS);
            Button editButton = new Button("", FontAwesome.EDIT);
            HorizontalLayout crudLayout = new HorizontalLayout(deleteButton, editButton);
            crudLayout.setSpacing(true);
            deleteButton.addClickListener(event -> source.getContainerDataSource().removeItem(itemId));

            editButton.addClickListener(event -> {
                EntityItem item = (EntityItem) source.getContainerDataSource().getItem(itemId);
                Window detailWindow = buildDetailWindow();
                FieldGroup fieldGroup = buildAndBindFieldGroup(item);
                FormLayout formLayout = buildForm(fieldGroup, detailWindow);
                detailWindow.setContent(formLayout);
                UI.getCurrent().addWindow(detailWindow);
            });
            return crudLayout;
        });


        tblTankfuellung.setConverter(TankFuellung.Properties.menge.name(), new DefaultStringToBigDecimalConverter());
        tblTankfuellung.setConverter(TankFuellung.Properties.preisTotal.name(), new CurrencyStringToBigDecimalConverter());
        tblTankfuellung.setConverter(TankFuellung.Properties.preisProLiter.name(), new CurrencyStringToBigDecimalConverter());

        tblTankfuellung.setVisibleColumns("d", TankFuellung.Properties.datum.name(),
                TankFuellung.Properties.fahrer.name(),
                TankFuellung.Properties.menge.name(),
                TankFuellung.Properties.preisProLiter.name(),
                TankFuellung.Properties.preisTotal.name()
        );

        tblTankfuellung.setColumnWidth("d", 130);
        tblTankfuellung.setColumnHeaders("", DATUM, FAHRER, MENGE, PREIS_PRO_LITER, KOSTEN_TOTAL);
        tblTankfuellung.setColumnAlignment(TankFuellung.Properties.menge.name(), Table.Align.RIGHT);
        tblTankfuellung.setColumnAlignment(TankFuellung.Properties.preisProLiter.name(), Table.Align.RIGHT);
        tblTankfuellung.setColumnAlignment(TankFuellung.Properties.preisTotal.name(), Table.Align.RIGHT);

        tblTankfuellung.setColumnAlignment("d", Table.Align.CENTER);
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
