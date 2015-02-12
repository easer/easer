package ch.akros.easer;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by lukovics on 12.02.2015.
 */
@WebServlet(urlPatterns = "/*", name = "EaserUIServlet", asyncSupported = true, initParams = {
        @WebInitParam(name = "UIProvider", value = "ch.akros.easer.Jee6UIProvider")})
@VaadinServletConfiguration(ui = EaserUI.class, productionMode = false)
public class EaserUIServlet extends VaadinServlet {
    @Inject
    private UI ui;

    public UI getUI() {
        return ui;
    }
}
