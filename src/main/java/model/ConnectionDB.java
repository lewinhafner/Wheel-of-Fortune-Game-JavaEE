/*
 *  Lewin Hafner
 *  Lewin.Hafner4@stud.bbbaden.ch
 */
package model;

import entities.Score;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lewin Hafner
 */
public class ConnectionDB {

    private static Connection connection;
    private final static Logger LOGGER = Logger.getLogger(ConnectionDB.class.getName());

    /**
     * Creates a new instance of CustomerBean
     */
    public ConnectionDB() {
    }

    private static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                return DriverManager
                        .getConnection("jdbc:mysql://localhost:3306/wheeloffortune?serverTimezone=UTC", "gamehost", "wheeloffortune");
            } catch (SQLException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "BBB InsecureApp Cannot connect to database, giving up.", e);
            }
        }

        return connection;
    }

    public static boolean doLogin(String username, String password) {
        String sql = "SELECT password FROM user WHERE username=? AND password=?";

        byte[] saltDB = getSalt(username);
        byte[] hashedPassword = Password.hashPassword(password, saltDB);
        try (Connection con = getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, Base64.getEncoder().encodeToString(hashedPassword));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String passwordDB = rs.getString("password");
                System.out.println("Password from DB: " + passwordDB);
                if (passwordDB.equals(Base64.getEncoder().encodeToString(hashedPassword))) {
                    return true;
                }
            }

            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    private static byte[] getSalt(String username) {
        String sql = "SELECT salt FROM user WHERE username=?";
        PreparedStatement pstmt;
        String salt;

        try (Connection con = getConnection()) {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                salt = rs.getString("salt");
                return Base64.getDecoder().decode(salt);
            }
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static List<Score> getScoreList() {
        List<Score> ranks = new ArrayList<>();

        String sql = "SELECT scoreID, username, playedOn, moneyAmount, roundsAmount, RANK() OVER ( ORDER BY moneyAmount DESC ) sorted_rank FROM score";
        PreparedStatement pstmt;
        Score score;
        try (Connection con = getConnection()) {
            pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("scoreID");
                int rank = rs.getInt("sorted_rank");
                String playername = rs.getString("username");
                Date playedOn = rs.getDate("playedOn");
                long wager = rs.getLong("moneyAmount");
                int rounds = rs.getInt("roundsAmount");

                System.out.println("-----------------------");
                System.out.println(id);
                System.out.println(rank);
                System.out.println(playername);
                System.out.println(playedOn);
                System.out.println(wager);
                System.out.println(rounds);
                System.out.println("-----------------------");
                score = new Score(id, rank, playername, playedOn, wager, rounds);
                ranks.add(score);
            }
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ranks;
    }

}
