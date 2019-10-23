package com.example.poslovna_informatika.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.poslovna_informatika.model.StopaPDV;

import scala.Console;

public class StopaPdvDTO implements Serializable {

 
	private static final long serialVersionUID = 1L;
	private long id;
	private double procenat;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	private String datumVazenja;
	private PdvDTO pdv;

	public StopaPdvDTO() {
		super();
	}

	public StopaPdvDTO(long id, double procenat, String datumVazenja, PdvDTO pdv) {
		this.id = id;
		this.procenat = procenat;
		this.datumVazenja = datumVazenja;
		this.pdv = pdv;
	}

	public StopaPdvDTO(StopaPDV stopaPdv) {
		this(stopaPdv.getId(), stopaPdv.getProcenat(), stopaPdv.getDatumVazenja(), new PdvDTO(stopaPdv.getPdv()));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getProcenat() {
		return procenat;
	}

	public void setProcenat(double procenat) {
		this.procenat = procenat;
	}

	public String getDatumVazenja() {
		return datumVazenja;
	}

	public void setDatumVazenja(String datumVazenja) {
		this.datumVazenja = datumVazenja;
	}

	public PdvDTO getPdv() {
		return pdv;
	}

	public void setPdv(PdvDTO pdv) {
		this.pdv = pdv;
	}

}
