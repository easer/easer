package ch.akros.easer.business.domain.car;

import com.vaadin.addon.jpacontainer.provider.MutableLocalEntityProvider;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by lukovics on 09.02.2015.
 */
@Stateless
@TransactionManagement
public class TankFuellungProviderBean extends MutableLocalEntityProvider<TankFuellung> {

    @PersistenceContext
    private EntityManager em;

    protected TankFuellungProviderBean() {
        super(TankFuellung.class);
        setTransactionsHandledByProvider(false);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected void runInTransaction(Runnable operation) {
        super.runInTransaction(operation);
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*
         * The entity manager is transaction-scoped, which means
         * that the entities will be automatically detached when
         * the transaction is closed. Therefore, we do not need
         * to explicitly detach them.
         */
        setEntitiesDetached(false);
    }
}
