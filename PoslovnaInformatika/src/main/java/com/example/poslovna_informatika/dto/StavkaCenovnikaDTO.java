package com.example.poslovna_informatika.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;

import com.example.poslovna_informatika.model.StavkaCenovnika;

public class StavkaCenovnikaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	@Min(value=1, message = "cena mora biti veca od nula")
	private double cena;
	private RobaDTO roba;
	private CenovnikDTO cenovnik;

	public StavkaCenovnikaDTO() {
		super();
	}

	public StavkaCenovnikaDTO(long id, double cena, RobaDTO roba, CenovnikDTO cenovnik) {
		super();
		this.id = id;
		this.cena = cena;
		this.roba = roba;
		this.cenovnik = cenovnik;
	}

	public StavkaCenovnikaDTO(StavkaCenovnika stavkaCenovnika) {
		this(stavkaCenovnika.getId(), stavkaCenovnika.getCena(), new RobaDTO(stavkaCenovnika.getRoba()),
				new CenovnikDTO(stavkaCenovnika.getCenovnik()));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public RobaDTO getRoba() {
		return roba;
	}

	public void setRoba(RobaDTO roba) {
		this.roba = roba;
	}

	public CenovnikDTO getCenovnik() {
		return cenovnik;
	}

	public void setCenovnik(CenovnikDTO cenovnik) {
		this.cenovnik = cenovnik;
	}

}
