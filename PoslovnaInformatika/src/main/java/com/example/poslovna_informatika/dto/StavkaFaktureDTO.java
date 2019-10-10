package com.example.poslovna_informatika.dto;

import java.io.Serializable;

import com.example.poslovna_informatika.model.StavkaFakture;

public class StavkaFaktureDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private int kolicina;
	private double jedinicnaCena;
	private double rabat;
	private double osnovicaZaPDV;
	private double procenatPDV;
	private double iznosPDV;
	private double iznosStavke;
	private RobaDTO roba;
	private FakturaDTO faktura;

	public StavkaFaktureDTO() {
		super();
	}

	public StavkaFaktureDTO(long id, int kolicina, double jedinicnaCena, double rabat, double osnovicaZaPDV,
			double procenatPDV, double iznosPDV, double iznosStavke, RobaDTO roba, FakturaDTO faktura) {
		super();
		this.id = id;
		this.kolicina = kolicina;
		this.jedinicnaCena = jedinicnaCena;
		this.rabat = rabat;
		this.osnovicaZaPDV = osnovicaZaPDV;
		this.procenatPDV = procenatPDV;
		this.iznosPDV = iznosPDV;
		this.iznosStavke = iznosStavke;
		this.roba = roba;
		this.faktura = faktura;
	}

	public StavkaFaktureDTO(StavkaFakture stavkaFakture) {
		this(stavkaFakture.getId(), stavkaFakture.getKolicina(), stavkaFakture.getJedinicnaCena(),
				stavkaFakture.getRabat(), stavkaFakture.getOsnovicaZaPDV(), stavkaFakture.getProcenatPDV(),
				stavkaFakture.getIznosPDV(), stavkaFakture.getIznosStavke(), new RobaDTO(stavkaFakture.getRoba()),
				new FakturaDTO(stavkaFakture.getFaktura()));
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

	public RobaDTO getRoba() {
		return roba;
	}

	public void setRoba(RobaDTO roba) {
		this.roba = roba;
	}

	public FakturaDTO getFaktura() {
		return faktura;
	}

	public void setFaktura(FakturaDTO faktura) {
		this.faktura = faktura;
	}

}