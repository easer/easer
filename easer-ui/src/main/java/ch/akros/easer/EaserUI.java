package ch.akros.easer;

import ch.akros.easer.ui.EaserTab;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

import javax.inject.Inject;

/**
 *
 */
@Theme("mytheme")
@Widgetset("ch.akros.MyAppWidgetset")
@Title("easer")
@CDIUI("")
@SuppressWarnings("serial")
public class EaserUI extends UI {

    @Inject
    private TankenViewTab tankenView;
    @Inject
    private ChartsViewTab chartsViewTab;


    private TabSheet tsMain = new TabSheet();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        tsMain.setStyleName("ts-main");
        tsMain.addTab(tankenView, "tanken");
        tsMain.addTab(chartsViewTab, "charts");
        tsMain.addSelectedTabChangeListener(changeEvent -> ((EaserTab) changeEvent.getTabSheet().getSelectedTab()).reload());
        setContent(tsMain);
        setSizeFull();
    }

}
