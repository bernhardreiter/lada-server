/* Copyright (C) 2018 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.model.stammdaten;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the ort database table.
 *
 */
@Entity
@Table(name = "gemeindeuntergliederung", schema = SchemaName.LEGACY_NAME)
public class GemeindeUntergliederung implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "netzbetreiber_id")
    private String netzbetreiberId;

    @Column(name = "gem_id")
    private String gemId;

    @Column(name = "ozk_id")
    private Integer ozkId;

    @Column(name = "gemeindeuntergliederung")
    private String gemeindeUntergliederung;

    @Column(name = "letzte_aenderung", insertable = false)
    private Timestamp letzteAenderung;

    @Transient
    private boolean readonly;

    public GemeindeUntergliederung() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNetzbetreiberId() {
        return this.netzbetreiberId;
    }

    public void setNetzbetreiberId(String netzbetreiberId) {
        this.netzbetreiberId = netzbetreiberId;
    }

    public String getGemId() {
        return this.gemId;
    }

    public void setGemId(String gemId) {
        this.gemId = gemId;
    }

    public Integer getOzkId() {
        return this.ozkId;
    }

    public void setOzkId(Integer ozkId) {
        this.ozkId = ozkId;
    }

    public String getGemeindeUntergliederung() {
        return this.gemeindeUntergliederung;
    }

    public void setGemeindeUntergliederung(String gemeindeUntergliederung) {
        this.gemeindeUntergliederung = gemeindeUntergliederung;
    }

    public Timestamp getLetzteAenderung() {
        return this.letzteAenderung;
    }

    public void setLetzteAenderung(Timestamp letzteAenderung) {
        this.letzteAenderung = letzteAenderung;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

}
