package com.example.poslovna_informatika.model;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

@Entity
@Table(name = "poslovni_partner")
public class PoslovniPartner {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "poslovni_partner_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false, unique = true, length = 150)
	private String naziv;

	@Column(nullable = false)
	private String adresa;

	@Column(nullable = false)
	private String vrsta;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "poslovni_partner")
	private List<Faktura> fakture;

	@ManyToOne
	@JoinColumn(name = "mesto_id", referencedColumnName = "mesto_id")
	private Mesto mesto;

	@ManyToOne
	@JoinColumn(name = "preduzece_id", referencedColumnName = "preduzece_id")
	private Preduzece preduzece;

	public PoslovniPartner() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getVrsta() {
		return vrsta;
	}

	public void setVrsta(String vrsta) {
		this.vrsta = vrsta;
	}

	public List<Faktura> getFakture() {
		return fakture;
	}

	public void setFakture(List<Faktura> fakture) {
		this.fakture = fakture;
	}

	public Mesto getMesto() {
		return mesto;
	}

	public void setMesto(Mesto mesto) {
		this.mesto = mesto;
	}

	public Preduzece getPreduzece() {
		return preduzece;
	}

	public void setPreduzece(Preduzece preduzece) {
		this.preduzece = preduzece;
	}
}