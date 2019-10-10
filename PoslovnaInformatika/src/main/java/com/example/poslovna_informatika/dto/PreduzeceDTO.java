package com.example.poslovna_informatika.dto;

import java.io.Serializable;

import com.example.poslovna_informatika.model.Preduzece;

public class PreduzeceDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String naziv;
	private String adresa;
	private int pib;
	private String telefon;
	private String email;
	private String logoPath;
	private MestoDTO mesto;

	public PreduzeceDTO() {
	}

	public PreduzeceDTO(long id, String naziv, String adresa, int pib, String telefon, String email, String logoPath,
			MestoDTO mesto) {
		this.id = id;
		this.naziv = naziv;
		this.adresa = adresa;
		this.pib = pib;
		this.telefon = telefon;
		this.email = email;
		this.logoPath = logoPath;
		this.mesto = mesto;
	}

	public PreduzeceDTO(Preduzece preduzece) {
		this(preduzece.getId(), preduzece.getNaziv(), preduzece.getAdresa(), preduzece.getPib(), preduzece.getTelefon(),
				preduzece.getEmail(), preduzece.getLogoPath(), new MestoDTO(preduzece.getMesto()));
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

	public MestoDTO getMesto() {
		return mesto;
	}

	public void setMesto(MestoDTO mesto) {
		this.mesto = mesto;
	}

}