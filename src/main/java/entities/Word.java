/*
 *  Lewin Hafner
 *  Lewin.Hafner4@stud.bbbaden.ch
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lewin Hafner
 */
@Entity
@Table(name = "word")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Word.findAll", query = "SELECT w FROM Word w")
    , @NamedQuery(name = "Word.findByWordID", query = "SELECT w FROM Word w WHERE w.wordID = :wordID")
    , @NamedQuery(name = "Word.findByWord", query = "SELECT w FROM Word w WHERE w.word = :word")})
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "wordID")
    private Integer wordID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "word")
    private String word;
    @JoinColumn(name = "categoryID", referencedColumnName = "categoryID")
    @ManyToOne(optional = false)
    private Category categoryID;

    public Word() {
    }

    public Word(Integer wordID) {
        this.wordID = wordID;
    }

    public Word(Integer wordID, String word) {
        this.wordID = wordID;
        this.word = word;
    }

    public Integer getWordID() {
        return wordID;
    }

    public void setWordID(Integer wordID) {
        this.wordID = wordID;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Category getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Category categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wordID != null ? wordID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Word)) {
            return false;
        }
        Word other = (Word) object;
        if ((this.wordID == null && other.wordID != null) || (this.wordID != null && !this.wordID.equals(other.wordID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return word;
    }

}
