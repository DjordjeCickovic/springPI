package com.example.poslovna_informatika.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "stavka_fakture")
public class StavkaFakture {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "stavka_fakture_id", unique = true, nullable = false)
	private long id;
	
	@Column(nullable = false)
	private int kolicina;

	@Column(nullable = false)
	private double jedinicnaCena;

	@Column(nullable = false)
	private double rabat;

	@Column(nullable = false)
	private double osnovicaZaPDV;

	@Column(nullable = false)
	private double procenatPDV;

	@Column(nullable = false)
	private double iznosPDV;

	@Column(nullable = false)
	private double iznosStavke;

	@ManyToOne
	@JoinColumn(name = "roba_id", referencedColumnName = "roba_id")
	private Roba roba;

	@ManyToOne
	@JoinColumn(name = "faktura_id", referencedColumnName = "faktura_id")
	private Faktura faktura;
	
	public StavkaFakture() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public double getJedinicnaCena() {
		return jedinicnaCena;
	}

	public void setJedinicnaCena(double jedinicnaCena) {
		this.jedinicnaCena = jedinicnaCena;
	}

	public double getRabat() {
		return rabat;
	}

	public void setRabat(double rabat) {
		this.rabat = rabat;
	}

	public double getOsnovicaZaPDV() {
		return osnovicaZaPDV;
	}

	public void setOsnovicaZaPDV(double osnovicaZaPDV) {
		this.osnovicaZaPDV = osnovicaZaPDV;
	}

	public double getProcenatPDV() {
		return procenatPDV;
	}

	public void setProcenatPDV(double procenatPDV) {
		this.procenatPDV = procenatPDV;
	}

	public double getIznosPDV() {
		return iznosPDV;
	}

	public void setIznosPDV(double iznosPDV) {
		this.iznosPDV = iznosPDV;
	}

	public double getIznosStavke() {
		return iznosStavke;
	}

	public void setIznosStavke(double iznosStavke) {
		this.iznosStavke = iznosStavke;
	}

	public Roba getRoba() {
		return roba;
	}

	public void setRoba(Roba roba) {
		this.roba = roba;
	}

	public Faktura getFaktura() {
		return faktura;
	}

	public void setFaktura(Faktura faktura) {
		this.faktura = faktura;
	}
}
