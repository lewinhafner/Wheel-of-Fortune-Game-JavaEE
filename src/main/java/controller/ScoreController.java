package controller;

import entities.Score;
import beans.ScoreFacade;
import entities.util.JsfUtil;
import entities.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("scoreController")
@SessionScoped
public class ScoreController implements Serializable {

    @EJB
    private beans.ScoreFacade ejbFacade;
    private List<Score> items = null;
    List<Score> topten = new ArrayList<>();
    private Score selected;
    private DateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    public ScoreController() {
    }

    public Score getSelected() {
        return selected;
    }

    public void setSelected(Score selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ScoreFacade getFacade() {
        return ejbFacade;
    }

    public Score prepareCreate() {
        selected = new Score();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ScoreCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ScoreUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ScoreDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Score> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Score> getTopTen() {
        if (items == null) {
            items = getFacade().findAll();
        }
        this.topten.clear();

        // custom comparator for ranking based on money won
        items.sort(new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                if (o1.getMoneyAmount() > o2.getMoneyAmount()) {
                    return -1;
                }
                if (o1.getMoneyAmount() < o2.getMoneyAmount()) {
                    return 1;
                }
                return 0;
            }
        });

        // manual ranking 
        for (int i = 1; i <= items.size(); i++) {
            items.get(i - 1).setRank(i);
        }

        // cut highscore entries if more than 10
        int max;
        if (items.size() < 10) {
            max = items.size();
        } else {
            max = 10;
        }

        // highscore with top ten (or top n)
        for (int y = 0; y < max; y++) {
            topten.add(items.get(y));
        }

        return topten;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Score getScore(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Score> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Score> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Score.class)
    public static class ScoreControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ScoreController controller = (ScoreController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "scoreController");
            return controller.getScore(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Score) {
                Score o = (Score) object;
                return getStringKey(o.getScoreID());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Score.class.getName()});
                return null;
            }
        }

    }

}
