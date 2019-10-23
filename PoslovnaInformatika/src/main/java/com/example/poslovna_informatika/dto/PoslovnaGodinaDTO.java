package com.example.poslovna_informatika.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.example.poslovna_informatika.model.PoslovnaGodina;

public class PoslovnaGodinaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	@Pattern(regexp="^[0-9]{4}*$", message="Polje sme sadrzati samo brojeve i 4 cifre")
	private int godina;
	private boolean zakljucena;

	public PoslovnaGodinaDTO() {
		super();
	}

	public PoslovnaGodinaDTO(long id, int godina, boolean zakljucena) {
		this.id = id;
		this.godina = godina;
		this.zakljucena = zakljucena;

	}

	public PoslovnaGodinaDTO(PoslovnaGodina poslovnaGodina) {
		this(poslovnaGodina.getId(), poslovnaGodina.getGodina(), poslovnaGodina.isZakljucena());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getGodina() {
		return godina;
	}

	public void setGodina(int godina) {
		this.godina = godina;
	}

	public boolean isZakljucena() {
		return zakljucena;
	}

	public void setZakljucena(boolean zakljucena) {
		this.zakljucena = zakljucena;
	}
}