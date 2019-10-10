package com.example.poslovna_informatika.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "faktura")
public class Faktura {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "faktura_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false)
	private String datumFakture;

	@Column(nullable = false)
	private String datumValute;

	@Column(nullable = false)
	private double osnovica;

	@Column(nullable = false)
	private double ukupanPDV;

	@Column(nullable = false)
	private double iznosZaPlacanje;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "faktura")
	private List<StavkaFakture> stavkeFakture;

	@ManyToOne
	@JoinColumn(name = "preduzece_id", referencedColumnName = "preduzece_id")
	private Preduzece preduzece;

	@ManyToOne
	@JoinColumn(name = "poslovni_partner_id", referencedColumnName = "poslovni_partner_id")
	private PoslovniPartner poslovniPartner;

	@ManyToOne
	@JoinColumn(name = "poslovna_godina_id", referencedColumnName = "poslovna_godina_id")
	private PoslovnaGodina poslovnaGodina;

	public Faktura() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDatumFakture() {
		return datumFakture;
	}

	public void setDatumFakture(String datumFakture) {
		this.datumFakture = datumFakture;
	}

	public String getDatumValute() {
		return datumValute;
	}

	public void setDatumValute(String datumValute) {
		this.datumValute = datumValute;
	}

	public double getOsnovica() {
		return osnovica;
	}

	public void setOsnovica(double osnovica) {
		this.osnovica = osnovica;
	}

	public double getUkupanPDV() {
		return ukupanPDV;
	}

	public void setUkupanPDV(double ukupanPDV) {
		this.ukupanPDV = ukupanPDV;
	}

	public double getIznosZaPlacanje() {
		return iznosZaPlacanje;
	}

	public void setIznosZaPlacanje(double iznosZaPlacanje) {
		this.iznosZaPlacanje = iznosZaPlacanje;
	}

	public List<StavkaFakture> getStavkeFakture() {
		return stavkeFakture;
	}

	public void setStavkeFakture(List<StavkaFakture> stavkeFakture) {
		this.stavkeFakture = stavkeFakture;
	}

	public Preduzece getPreduzece() {
		return preduzece;
	}

	public void setPreduzece(Preduzece preduzece) {
		this.preduzece = preduzece;
	}

	public PoslovniPartner getPoslovniPartner() {
		return poslovniPartner;
	}

	public void setPoslovniPartner(PoslovniPartner poslovniPartner) {
		this.poslovniPartner = poslovniPartner;
	}

	public PoslovnaGodina getPoslovnaGodina() {
		return poslovnaGodina;
	}

	public void setPoslovnaGodina(PoslovnaGodina poslovnaGodina) {
		this.poslovnaGodina = poslovnaGodina;
	}
}
