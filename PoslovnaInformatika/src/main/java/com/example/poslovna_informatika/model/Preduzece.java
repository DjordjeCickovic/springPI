package com.example.poslovna_informatika.model;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

@Entity
@Table(name = "preduzece")
public class Preduzece {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "preduzece_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false, length = 12)
	private String naziv;

	@Column(nullable = false)
	private String adresa;

	@Column(nullable = false, unique = true, length = 10)
	private int pib;

	@Column(length = 10)
	private String telefon;

	@Column
	private String email;

	@Column
	private String logoPath;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "preduzece")
	private List<GrupaRobe> grupeRoba;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "preduzece")
	private List<Faktura> fakture;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "preduzece")
	private List<PoslovniPartner> poslovniPartneri;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "preduzece")
	private List<Cenovnik> cenovnici;

	@ManyToOne
	@JoinColumn(name = "mesto_id", referencedColumnName = "mesto_id")
	private Mesto mesto;

	public Preduzece() {
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

	public int getPib() {
		return pib;
	}

	public void setPib(int pib) {
		this.pib = pib;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public List<GrupaRobe> getGrupeRoba() {
		return grupeRoba;
	}

	public void setGrupeRoba(List<GrupaRobe> grupeRoba) {
		this.grupeRoba = grupeRoba;
	}

	public List<Faktura> getFakture() {
		return fakture;
	}

	public void setFakture(List<Faktura> fakture) {
		this.fakture = fakture;
	}

	public List<PoslovniPartner> getPoslovniPartneri() {
		return poslovniPartneri;
	}

	public void setPoslovniPartneri(List<PoslovniPartner> poslovniPartneri) {
		this.poslovniPartneri = poslovniPartneri;
	}

	public List<Cenovnik> getCenovnici() {
		return cenovnici;
	}

	public void setCenovnici(List<Cenovnik> cenovnici) {
		this.cenovnici = cenovnici;
	}

	public Mesto getMesto() {
		return mesto;
	}

	public void setMesto(Mesto mesto) {
		this.mesto = mesto;
	}
}
