package com.example.poslovna_informatika.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.example.poslovna_informatika.model.JedinicaMere;

public class JedinicaMereDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	@Pattern(regexp="^[A-Za-z]*$", message="Polje sme sadrzati samo slova")
	private String naziv;
	private List<RobaDTO> robeDTO;

	public JedinicaMereDTO() {
		super();
	}

	public JedinicaMereDTO(long id, String naziv) {
		this.id = id;
		this.naziv = naziv;

	}

	public JedinicaMereDTO(JedinicaMere jedinicaMere) {
		this(jedinicaMere.getId(), jedinicaMere.getNaziv());
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

	public List<RobaDTO> getRobeDTO() {
		return robeDTO;
	}

	public void setRobeDTO(List<RobaDTO> robeDTO) {
		this.robeDTO = robeDTO;
	}

}
