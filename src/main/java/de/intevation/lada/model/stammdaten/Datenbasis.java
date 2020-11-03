package de.intevation.lada.model.stammdaten;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the datenbasis database table.
 *
 */
@Entity
@Table(name="datenbasis")
public class Datenbasis implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    private String beschreibung;

    private String datenbasis;

    public Datenbasis() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBeschreibung() {
        return this.beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getDatenbasis() {
        return this.datenbasis;
    }

    public void setDatenbasis(String datenbasis) {
        this.datenbasis = datenbasis;
    }
}
