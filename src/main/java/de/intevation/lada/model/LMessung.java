package de.intevation.lada.model;

// Generated 21.05.2013 16:58:30 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * LMessung generated by hbm2java
 */
@Entity
@Table(name = "l_messung", schema = "public")
@SequenceGenerator(name = "MESSUNG_ID_STORE", sequenceName = "messung_id_seq")
public class LMessung implements java.io.Serializable {

	private LMessungId id;
	private Integer messungsId;
	private String probeId;
	private String mmtId;
	private String nebenprobenNr;
	private Integer messdauer;
	private Date messzeitpunkt;
	private boolean fertig;
	private Date letzteAenderung;
	private boolean geplant;

	public LMessung() {
	}

	public LMessung(LMessungId id, String probeId, String mmtId,
			boolean fertig, boolean geplant) {
		this.id = id;
		this.probeId = probeId;
		this.mmtId = mmtId;
		this.fertig = fertig;
		this.geplant = geplant;
	}

	public LMessung(LMessungId id, String probeId, String mmtId,
			String nebenprobenNr, Integer messdauer, Date messzeitpunkt,
			boolean fertig, Date letzteAenderung, boolean geplant) {
		this.id = id;
		this.probeId = probeId;
		this.mmtId = mmtId;
		this.nebenprobenNr = nebenprobenNr;
		this.messdauer = messdauer;
		this.messzeitpunkt = messzeitpunkt;
		this.fertig = fertig;
		this.letzteAenderung = letzteAenderung;
		this.geplant = geplant;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "probeId", column = @Column(name = "probe_id", nullable = false, length = 20)),
			@AttributeOverride(name = "messungsId", column = @Column(name = "messungs_id", nullable = false)) })
	public LMessungId getId() {
		return this.id;
	}

	public void setId(LMessungId id) {
		this.id = id;
	}

    @Column(name = "messungs_id", nullable = false, insertable = false, updatable = false)
	public Integer getMessungsId() {
        return messungsId;
    }

    public void setMessungsId(Integer messungsId) {
        this.messungsId = messungsId;
    }

    @Column(name = "probe_id", nullable = false, insertable = false, updatable = false)
	public String getProbeId() {
		return this.probeId;
	}

	public void setProbeId(String probeId) {
		this.probeId = probeId;
	}

	@Column(name = "mmt_id", nullable = false)
	public String getMmtId() {
		return this.mmtId;
	}

	public void setMmtId(String mmtId) {
		this.mmtId = mmtId;
	}

	@Column(name = "nebenproben_nr", length = 10)
	public String getNebenprobenNr() {
		return this.nebenprobenNr;
	}

	public void setNebenprobenNr(String nebenprobenNr) {
		this.nebenprobenNr = nebenprobenNr;
	}

	@Column(name = "messdauer")
	public Integer getMessdauer() {
		return this.messdauer;
	}

	public void setMessdauer(Integer messdauer) {
		this.messdauer = messdauer;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "messzeitpunkt", length = 35)
	public Date getMesszeitpunkt() {
		return this.messzeitpunkt;
	}

	public void setMesszeitpunkt(Date messzeitpunkt) {
		this.messzeitpunkt = messzeitpunkt;
	}

	@Column(name = "fertig", nullable = false)
	public boolean isFertig() {
		return this.fertig;
	}

	public void setFertig(boolean fertig) {
		this.fertig = fertig;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "letzte_aenderung", length = 29)
	public Date getLetzteAenderung() {
		return this.letzteAenderung;
	}

	public void setLetzteAenderung(Date letzteAenderung) {
		this.letzteAenderung = letzteAenderung;
	}

	@Column(name = "geplant", nullable = false)
	public boolean isGeplant() {
		return this.geplant;
	}

	public void setGeplant(boolean geplant) {
		this.geplant = geplant;
	}

}
