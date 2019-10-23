package com.example.poslovna_informatika.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.poslovna_informatika.model.GrupaRobe;

import scala.Console;

public class GrupaRobeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	@Pattern(regexp="^[A-Za-z]*$", message="Polje sme sadrzati samo slova")
	private String naziv;
	private PreduzeceDTO preduzece;
	private PdvDTO pdv;

	public GrupaRobeDTO() {
		super();
	}

	public GrupaRobeDTO(long id, String naziv, PreduzeceDTO preduzece, PdvDTO pdv) {
		this.id = id;
		this.naziv = naziv;
		this.preduzece = preduzece;
		this.pdv = pdv;
	}

	public GrupaRobeDTO(GrupaRobe grupaRobe) {
		this(grupaRobe.getId(), grupaRobe.getNaziv(), new PreduzeceDTO(grupaRobe.getPreduzece()),
				new PdvDTO(grupaRobe.getPdv()));
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

	public PreduzeceDTO getPreduzece() {
		return preduzece;
	}

	public void setPreduzece(PreduzeceDTO preduzece) {
		this.preduzece = preduzece;
	}

	public PdvDTO getPdv() {
		return pdv;
	}

	public void setPdv(PdvDTO pdv) {
		this.pdv = pdv;
	}

}
