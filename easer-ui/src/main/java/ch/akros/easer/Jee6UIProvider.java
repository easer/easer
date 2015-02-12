package ch.akros.easer;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * Created by lukovics on 09.02.2015.
 */
public class Jee6UIProvider extends UIProvider {

    @Override
    public UI createInstance(UICreateEvent event) {
        return ((EaserUIServlet) VaadinServlet.getCurrent()).getUI();
    }

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return ((EaserUIServlet) VaadinServlet.getCurrent()).getUI().getClass();
    }
}
