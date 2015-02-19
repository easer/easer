package ch.akros.easer.business.domain.car;

import com.vaadin.addon.jpacontainer.JPAContainer;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Created by lukovics on 19.02.2015.
 */
public class TankFuellungJPAFactory {

    @Inject
    private TankFuellungProviderBean tankFuellungProviderBean;

    @Produces
    public JPAContainer<TankFuellung> create() {
        JPAContainer<TankFuellung> tankFuellungJPAContainer = new JPAContainer<>(
                TankFuellung.class);
        tankFuellungJPAContainer.setEntityProvider(tankFuellungProviderBean);
        return tankFuellungJPAContainer;
    }
}
