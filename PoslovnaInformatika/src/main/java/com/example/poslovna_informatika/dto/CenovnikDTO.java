package com.example.poslovna_informatika.dto;

import com.example.poslovna_informatika.model.Cenovnik;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class CenovnikDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	private String datumVazenja;
	private PreduzeceDTO preduzece;
	private List<StavkaCenovnikaDTO> stavkeCenovnikaDTO;

	public CenovnikDTO() {
		super();
	}

	public CenovnikDTO(long id, String datumVazenja, PreduzeceDTO preduzece) {
		super();
		this.id = id;
		this.datumVazenja = datumVazenja;
		this.preduzece = preduzece;

	}

	public CenovnikDTO(Cenovnik cenovnik) {
		this(cenovnik.getId(), cenovnik.getDatumVazenja(), new PreduzeceDTO(cenovnik.getPreduzece()));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDatumVazenja() {
		return datumVazenja;
	}

	public void setDatumVazenja(String datumVazenja) {
		this.datumVazenja = datumVazenja;
	}

	public List<StavkaCenovnikaDTO> getStavkeCenovnikaDTO() {
		return stavkeCenovnikaDTO;
	}

	public void setStavkeCenovnikaDTO(List<StavkaCenovnikaDTO> stavkeCenovnikaDTO) {
		this.stavkeCenovnikaDTO = stavkeCenovnikaDTO;
	}

	public PreduzeceDTO getPreduzece() {
		return preduzece;
	}

	public void setPreduzece(PreduzeceDTO preduzece) {
		this.preduzece = preduzece;
	}

}
