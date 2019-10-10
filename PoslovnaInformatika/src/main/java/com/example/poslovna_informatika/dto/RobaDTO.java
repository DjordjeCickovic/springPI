package com.example.poslovna_informatika.dto;

import com.example.poslovna_informatika.model.Roba;

import java.io.Serializable;

public class RobaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String naziv;
	private JedinicaMereDTO jedinicaMere;
	private GrupaRobeDTO grupaRobe;

	public RobaDTO() {
		super();
	}

	public RobaDTO(long id, String naziv, JedinicaMereDTO jedinicaMere, GrupaRobeDTO grupaRobe) {
		this.id = id;
		this.naziv = naziv;
		this.jedinicaMere = jedinicaMere;
		this.grupaRobe = grupaRobe;
	}

	public RobaDTO(Roba roba) {
		this(roba.getId(), roba.getNaziv(), new JedinicaMereDTO(roba.getJediniceMere()),
				new GrupaRobeDTO(roba.getGrupaRobe()));
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

	public JedinicaMereDTO getJedinicaMere() {
		return jedinicaMere;
	}

	public void setJedinicaMere(JedinicaMereDTO jedinicaMere) {
		this.jedinicaMere = jedinicaMere;
	}

	public GrupaRobeDTO getGrupaRobe() {
		return grupaRobe;
	}

	public void setGrupaRobe(GrupaRobeDTO grupaRobe) {
		this.grupaRobe = grupaRobe;
	}

}
