/*
 *  Lewin Hafner
 *  Lewin.Hafner4@stud.bbbaden.ch
 */
package controller;

import entities.Datarow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Lewin Hafner
 */
@Named(value = "dataController")
@SessionScoped
public class DatatableDataController implements Serializable {

    private List<Datarow> datarows = new ArrayList<>();

    /**
     * Creates a new instance of DatatableDataController
     */
    public DatatableDataController() {
    }

    public void createDataRow(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8,
            String col9, String col10, String col11, String col12, String col13, String col14) {

        Datarow temp = new Datarow(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14);
        this.datarows.add(temp);
    }

    public void updateDataRows(Datarow d1, Datarow d2, Datarow d3) {
        this.datarows.clear();
        this.datarows.add(d1);
        this.datarows.add(d2);
        this.datarows.add(d3);
    }

    public List<Datarow> getDataRows() {
        return this.datarows;
    }
    
    public void clearDatarows(){
        this.datarows.clear();
    }
}
