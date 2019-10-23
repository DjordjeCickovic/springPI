package com.example.poslovna_informatika.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.example.poslovna_informatika.model.PDV;

public class PdvDTO implements Serializable {

 
	private static final long serialVersionUID = 1L;
	private long id;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	@Pattern(regexp="^[A-Za-z]*$", message="Polje sme sadrzati samo slova")
	private String naziv;

	public PdvDTO() {
		super();
	}

	public PdvDTO(long id, String naziv) {
		this.id = id;
		this.naziv = naziv;

	}

	public PdvDTO(PDV pdv) {
		this(pdv.getId(), pdv.getNaziv());
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

}
