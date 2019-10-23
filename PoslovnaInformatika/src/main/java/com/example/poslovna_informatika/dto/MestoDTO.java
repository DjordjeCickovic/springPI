package com.example.poslovna_informatika.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.example.poslovna_informatika.model.Mesto;

public class MestoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	@Pattern(regexp="^[A-Za-z]*$", message="Polje sme sadrzati samo slova")
	private String grad;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	@Pattern(regexp="^[A-Za-z]*$", message="Polje sme sadrzati samo slova")
	private String drzava;

	public MestoDTO() {
		super();
	}

	public MestoDTO(long id, String grad, String drzava) {
		this.id = id;
		this.grad = grad;
		this.drzava = drzava;

	}

	public MestoDTO(Mesto mesto) {
		this(mesto.getId(), mesto.getGrad(), mesto.getDrzava());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getDrzava() {
		return drzava;
	}

	public void setDrzava(String drzava) {
		this.drzava = drzava;

	}

}
