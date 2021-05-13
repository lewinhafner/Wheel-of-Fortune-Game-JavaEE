/*
 *  Lewin Hafner
 *  Lewin.Hafner4@stud.bbbaden.ch
 */
package controller;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Lewin Hafner
 */
@Named(value = "directionController")
@SessionScoped
public class DirectionController implements Serializable {

    /**
     * Creates a new instance of DirectionController
     */
    public DirectionController() {
    }

    public String goHome() {
        System.out.println("redirecting..");
        return "/index?faces-redirect=true";
    }

}
