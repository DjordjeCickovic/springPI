package com.example.poslovna_informatika.dto;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.example.poslovna_informatika.model.Faktura;

public class FakturaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	private String datumFakture;
	@NotEmpty(message = "Polje ne sme biti prazno!")
	private String datumValute;
	private double osnovica;
	private double ukupanPDV;
	private double iznosZaPlacanje;
	private List<StavkaFaktureDTO> stavkeFaktureDTO;
	private PreduzeceDTO preduzece;
	private PoslovnaGodinaDTO poslovnaGodina;
	private PoslovniPartnerDTO poslovniPartner;

	public FakturaDTO() {

	}

	public FakturaDTO(long id, String datumFakture, String datumValute, double osnovica, double ukupanPDV,
			double iznosZaPlacanje, PreduzeceDTO preduzece, PoslovnaGodinaDTO poslovnaGodina,
			PoslovniPartnerDTO poslovniPartner) {

		this.id = id;
		this.datumFakture = datumFakture;
		this.datumValute = datumValute;
		this.osnovica = osnovica;
		this.ukupanPDV = ukupanPDV;
		this.iznosZaPlacanje = iznosZaPlacanje;
		this.preduzece = preduzece;
		this.poslovnaGodina = poslovnaGodina;
		this.poslovniPartner = poslovniPartner;
	}

	public FakturaDTO(Faktura faktura) {
		this(faktura.getId(), faktura.getDatumFakture(), faktura.getDatumValute(), faktura.getOsnovica(),
				faktura.getUkupanPDV(), faktura.getIznosZaPlacanje(), new PreduzeceDTO(faktura.getPreduzece()),
				new PoslovnaGodinaDTO(faktura.getPoslovnaGodina()),
				new PoslovniPartnerDTO(faktura.getPoslovniPartner()));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDatumFakture() {
		return datumFakture;
	}

	public void setDatumFakture(String datumFakture) {
		this.datumFakture = datumFakture;
	}

	public String getDatumValute() {
		return datumValute;
	}

	public void setDatumValute(String datumValute) {
		this.datumValute = datumValute;
	}

	public double getOsnovica() {
		return osnovica;
	}

	public void setOsnovica(double osnovica) {
		this.osnovica = osnovica;
	}

	public double getUkupanPDV() {
		return ukupanPDV;
	}

	public void setUkupanPDV(double ukupanPDV) {
		this.ukupanPDV = ukupanPDV;
	}

	public double getIznosZaPlacanje() {
		return iznosZaPlacanje;
	}

	public void setIznosZaPlacanje(double iznosZaPlacanje) {
		this.iznosZaPlacanje = iznosZaPlacanje;
	}

	public List<StavkaFaktureDTO> getStavkeFaktureDTO() {
		return stavkeFaktureDTO;
	}

	public void setStavkeFaktureDTO(List<StavkaFaktureDTO> stavkeFaktureDTO) {
		this.stavkeFaktureDTO = stavkeFaktureDTO;
	}

	public PreduzeceDTO getPreduzece() {
		return preduzece;
	}

	public void setPreduzece(PreduzeceDTO preduzece) {
		this.preduzece = preduzece;
	}

	public PoslovnaGodinaDTO getPoslovnaGodina() {
		return poslovnaGodina;
	}

	public void setPoslovnaGodina(PoslovnaGodinaDTO poslovnaGodina) {
		this.poslovnaGodina = poslovnaGodina;
	}

	public PoslovniPartnerDTO getPoslovniPartner() {
		return poslovniPartner;
	}

	public void setPoslovniPartner(PoslovniPartnerDTO poslovniPartner) {
		this.poslovniPartner = poslovniPartner;
	}

}