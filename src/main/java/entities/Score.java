/*
 *  Lewin Hafner
 *  Lewin.Hafner4@stud.bbbaden.ch
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lewin Hafner
 */
@Entity
@Table(name = "score")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Score.findAll", query = "SELECT s FROM Score s")
    , @NamedQuery(name = "Score.findByScoreID", query = "SELECT s FROM Score s WHERE s.scoreID = :scoreID")
    , @NamedQuery(name = "Score.findByRank", query = "SELECT s FROM Score s WHERE s.rank = :rank")
    , @NamedQuery(name = "Score.findByUsername", query = "SELECT s FROM Score s WHERE s.username = :username")
    , @NamedQuery(name = "Score.findByPlayedOn", query = "SELECT s FROM Score s WHERE s.playedOn = :playedOn")
    , @NamedQuery(name = "Score.findByMoneyAmount", query = "SELECT s FROM Score s WHERE s.moneyAmount = :moneyAmount")
    , @NamedQuery(name = "Score.findByRoundsAmount", query = "SELECT s FROM Score s WHERE s.roundsAmount = :roundsAmount")})
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "scoreID")
    private Integer scoreID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rank")
    private int rank;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Column(name = "playedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date playedOn;
    @Basic(optional = false)
    @NotNull
    @Column(name = "moneyAmount")
    private long moneyAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "roundsAmount")
    private int roundsAmount;

    public Score() {
    }

    public Score(Integer scoreID) {
        this.scoreID = scoreID;
    }

    public Score(Integer scoreID, int rank, String username, Date playedOn, long moneyAmount, int roundsAmount) {
        this.scoreID = scoreID;
        this.rank = rank;
        this.username = username;
        this.playedOn = playedOn;
        this.moneyAmount = moneyAmount;
        this.roundsAmount = roundsAmount;
    }

    public Integer getScoreID() {
        return scoreID;
    }

    public void setScoreID(Integer scoreID) {
        this.scoreID = scoreID;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }

    public long getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public int getRoundsAmount() {
        return roundsAmount;
    }

    public void setRoundsAmount(int roundsAmount) {
        this.roundsAmount = roundsAmount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scoreID != null ? scoreID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Score)) {
            return false;
        }
        Score other = (Score) object;
        if ((this.scoreID == null && other.scoreID != null) || (this.scoreID != null && !this.scoreID.equals(other.scoreID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Score[ scoreID=" + scoreID + " ]";
    }
    
}
