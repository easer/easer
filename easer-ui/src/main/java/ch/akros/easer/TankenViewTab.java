package ch.akros.easer;

import ch.akros.easer.business.domain.car.TankFuellung;
import ch.akros.easer.ui.CurrencyStringToBigDecimalConverter;
import ch.akros.easer.ui.DefaultStringToBigDecimalConverter;
import ch.akros.easer.ui.EaserTab;
import ch.akros.easer.ui.LocalDateField;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lukovics on 17.02.2015.
 */
public class TankenViewTab extends CustomComponent implements EaserTab {

    private final static String DATUM = "Datum";
    private final static String FAHRER = "Fahrer";
    private final static String MENGE = "Tankfüllung in Liter";
    private final static String PREIS_PRO_LITER = "Kosten/Liter";
    private final static String KOSTEN_TOTAL = "Kosten Total";
    private Properties propValidationMessages;

    @Inject
    private JPAContainer<TankFuellung> tankFuellungJPAContainer;

    private Button btnNeueTankfuellung = new Button("neue Tankfüllung...", FontAwesome.CAR);
    private VerticalLayout tabTanken = new VerticalLayout();
    private Table tblTankfuellung = new Table();

    @PostConstruct
    private void init() {
        readProperties();
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
        tabTanken.setSpacing(true);
        setCompositionRoot(tabTanken);
    }

    private void readProperties() {
        propValidationMessages = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("ValidationMessages.properties");
        try {
            propValidationMessages.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FieldGroup buildAndBindFieldGroup(EntityItem<TankFuellung> entityItem) {
        final FieldGroup fieldGroup = new FieldGroup();
        fieldGroup.setBuffered(true);
        fieldGroup.setItemDataSource(entityItem);

        // create Fields
        LocalDateField fldDatum = new LocalDateField(DATUM);
        fldDatum.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.datum.name()));

        TextField fldFahrer = new TextField(FAHRER);
        fldFahrer.setNullRepresentation("");
        fldFahrer.setInputPrompt("Elvis Presley");
        fldFahrer.setImmediate(true);
        fldFahrer.setRequired(true);
        fldFahrer.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.fahrer.name()));

        TextField fldMenge = new TextField(MENGE);
        fldMenge.setNullRepresentation("");
        fldMenge.setInputPrompt("61.25");
        fldMenge.setImmediate(true);
        fldMenge.setConversionError(propValidationMessages.getProperty("ch.akros.easer.business.domain.car.tankfuellung.bigdecimal"));
        fldMenge.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.menge.name()));

        TextField fldPreisProLiter = new TextField(PREIS_PRO_LITER);

        fldPreisProLiter.setNullRepresentation("");
        fldPreisProLiter.setInputPrompt("1.45");
        fldPreisProLiter.setImmediate(true);
        fldPreisProLiter.setConversionError(propValidationMessages.getProperty("ch.akros.easer.business.domain.car.tankfuellung.bigdecimal"));
        fldPreisProLiter.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.preisProLiter.name()));

        TextField fldPreisTotal = new TextField(KOSTEN_TOTAL);
        fldPreisTotal.setRequired(true);
        fldPreisTotal.setInputPrompt("88.81");
        fldPreisTotal.setNullRepresentation("");
        fldPreisTotal.setImmediate(true);
        fldPreisTotal.setConversionError(propValidationMessages.getProperty("ch.akros.easer.business.domain.car.tankfuellung.bigdecimal.preistotal"));
        fldPreisTotal.addValidator(new BeanValidator(TankFuellung.class, TankFuellung.Properties.preisTotal.name()));

        fieldGroup.bind(fldDatum, TankFuellung.Properties.datum.name());
        fieldGroup.bind(fldFahrer, TankFuellung.Properties.fahrer.name());
        fieldGroup.bind(fldMenge, TankFuellung.Properties.menge.name());
        fieldGroup.bind(fldPreisProLiter, TankFuellung.Properties.preisProLiter.name());
        fieldGroup.bind(fldPreisTotal, TankFuellung.Properties.preisTotal.name());
        return fieldGroup;
    }

    private FormLayout buildForm(final FieldGroup fieldGroup, Window window) {
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSizeUndefined();
        Button speichernButton = new Button("Speichern", FontAwesome.SAVE);
        speichernButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        fieldGroup.getFields().forEach(formLayout::addComponent);
        formLayout.addComponent(speichernButton);

        speichernButton.addClickListener(saveEvent -> {
            try {
                fieldGroup.commit();
                EntityItem entityItem = (EntityItem) fieldGroup.getItemDataSource();
                TankFuellung tankFuellung = (TankFuellung) entityItem.getEntity();
                if (!entityItem.isPersistent()) {
                    tankFuellungJPAContainer.addEntity(tankFuellung);
                }
                tankFuellungJPAContainer.refresh();
                window.close();
            } catch (FieldGroup.CommitException commitException) {
                Notification.show("Die Speicherung ist fehlgeschlagen. Bitte Mussfelder korrekt ausfüllen.",
                        Notification.Type.WARNING_MESSAGE);

                return;
            }
        });

        return formLayout;
    }

    private Table buildTable() {

        final String CRUD_COLUMN = "crud";

        tblTankfuellung.setContainerDataSource(tankFuellungJPAContainer);
        tblTankfuellung.addGeneratedColumn(CRUD_COLUMN, (source, itemId, columnId) -> {

            Button deleteButton = new Button("", FontAwesome.MINUS);
            deleteButton.setDescription("Löschen");
            Button editButton = new Button("", FontAwesome.EDIT);
            editButton.setDescription("Bearbeiten");
            Button copyButton = new Button("", FontAwesome.COPY);
            copyButton.setDescription("Kopieren");
            HorizontalLayout crudLayout = new HorizontalLayout(deleteButton, editButton, copyButton);
            crudLayout.setSpacing(true);
            deleteButton.addClickListener(event -> MessageBox.showPlain(Icon.QUESTION,
                    "Tankfüllung löschen...",
                    "Wollen Sie die Tankfüllung wirklich löschen?",
                    buttonId -> {
                        if (buttonId == ButtonId.YES) {
                            source.getContainerDataSource().removeItem(itemId);
                        } else {

                        }
                    },
                    ButtonId.YES,
                    ButtonId.NO));

            editButton.addClickListener(event -> {
                EntityItem item = (EntityItem) source.getContainerDataSource().getItem(itemId);
                Window detailWindow = buildDetailWindow();
                FieldGroup fieldGroup = buildAndBindFieldGroup(item);
                FormLayout formLayout = buildForm(fieldGroup, detailWindow);
                detailWindow.setContent(formLayout);
                UI.getCurrent().addWindow(detailWindow);
            });
            copyButton.addClickListener(event -> {
                EntityItem item = (EntityItem) source.getContainerDataSource().getItem(itemId);
                TankFuellung tankFuellung = (TankFuellung) item.getEntity();
                tankFuellungJPAContainer.addEntity(tankFuellung.copy());
            });
            return crudLayout;
        });


        tblTankfuellung.setConverter(TankFuellung.Properties.menge.name(), new DefaultStringToBigDecimalConverter());
        tblTankfuellung.setConverter(TankFuellung.Properties.preisTotal.name(), new CurrencyStringToBigDecimalConverter());
        tblTankfuellung.setConverter(TankFuellung.Properties.preisProLiter.name(), new CurrencyStringToBigDecimalConverter());

        tblTankfuellung.setVisibleColumns(CRUD_COLUMN, TankFuellung.Properties.datum.name(),
                TankFuellung.Properties.fahrer.name(),
                TankFuellung.Properties.menge.name(),
                TankFuellung.Properties.preisProLiter.name(),
                TankFuellung.Properties.preisTotal.name()
        );

        tblTankfuellung.setColumnWidth(CRUD_COLUMN, 185);
        tblTankfuellung.setColumnHeaders("", DATUM, FAHRER, MENGE, PREIS_PRO_LITER, KOSTEN_TOTAL);
        tblTankfuellung.setColumnAlignment(TankFuellung.Properties.menge.name(), Table.Align.RIGHT);
        tblTankfuellung.setColumnAlignment(TankFuellung.Properties.preisProLiter.name(), Table.Align.RIGHT);
        tblTankfuellung.setColumnAlignment(TankFuellung.Properties.preisTotal.name(), Table.Align.RIGHT);

        tblTankfuellung.setColumnAlignment(CRUD_COLUMN, Table.Align.CENTER);
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

    @Override
    public void reload() {

    }
}
