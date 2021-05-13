/*
 *  Lewin Hafner
 *  Lewin.Hafner4@stud.bbbaden.ch
 */
package controller;

import model.ConnectionDB;
import model.LoggerHelper;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Lewin Hafner
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    private String username = "Account";
    private String password;
    private boolean isAdmin = false;

    private final static Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    /**
     * Creates a new instance of LoginController
     */
    public LoginController() {
    }

    public void onload() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.invalidate();
        isAdmin = false;
    }

    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        LOGGER.log(Level.INFO, "Wheel of Fortune Administrator logged out, invalidating session. IP:{0}, username:{1}, user agent:{2}", new Object[]{LoggerHelper.getRemoteAddr(), username, LoggerHelper.getUserAgent()});
        session.invalidate();
        isAdmin = false;
        return "/index?faces-redirect=true";
    }

    public String access() {
        if (ConnectionDB.doLogin(username, password)) {
            System.out.println("right");
            this.isAdmin = true;
            LOGGER.log(Level.WARNING, "Wheel of Fortune login attempt successful, access granted. IP:{0}, username:{1}, user agent:{2}", new Object[]{LoggerHelper.getRemoteAddr(), username, LoggerHelper.getUserAgent()});
            return "/secured/adminPage?faces-redirect=true";
        } else {
            System.out.println("wrong");
            LOGGER.log(Level.WARNING, "Wheel of Fortune login attempt failed, access denied. IP:{0}, username:{1}, user agent:{2}", new Object[]{LoggerHelper.getRemoteAddr(), username, LoggerHelper.getUserAgent()});
            return "/index?faces-redirect=true";
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }
}
