/*
 *  Lewin Hafner
 *  Lewin.Hafner4@stud.bbbaden.ch
 */
package beans;

import beans.AbstractFacade;
import entities.Score;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Lewin Hafner
 */
@Stateless
public class ScoreFacade extends AbstractFacade<Score> {

    @PersistenceContext(unitName = "HafLew_LB_M151_Applikation_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScoreFacade() {
        super(Score.class);
    }
    
}
