package com.example.poslovna_informatika.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.poslovna_informatika.model.PoslovniPartner;

import scala.Console;

public class PoslovniPartnerDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	private String naziv;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	private String adresa;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	private String vrsta;
	private MestoDTO mesto;
	private PreduzeceDTO preduzece;

	public PoslovniPartnerDTO() {
		super();
	}

	public PoslovniPartnerDTO(long id, String naziv, String adresa, String vrsta, MestoDTO mesto,
			PreduzeceDTO preduzece) {
		this.id = id;
		this.naziv = naziv;
		this.adresa = adresa;
		this.vrsta = vrsta;
		this.mesto = mesto;
		this.preduzece = preduzece;
	}

	public PoslovniPartnerDTO(PoslovniPartner poslovniPartner) {
		this(poslovniPartner.getId(), poslovniPartner.getNaziv(), poslovniPartner.getAdresa(),
				poslovniPartner.getVrsta(), new MestoDTO(poslovniPartner.getMesto()),
				new PreduzeceDTO(poslovniPartner.getPreduzece()));
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

	public MestoDTO getMesto() {
		return mesto;
	}

	public void setMesto(MestoDTO mesto) {
		this.mesto = mesto;
	}

	public PreduzeceDTO getPreduzece() {
		return preduzece;
	}

	public void setPreduzece(PreduzeceDTO preduzece) {
		this.preduzece = preduzece;
	}

}