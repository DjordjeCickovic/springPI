package com.example.poslovna_informatika.model;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

@Entity
@Table(name = "pdv")
public class PDV {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "pdv_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false, unique = true, length = 30)
	private String naziv;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "pdv")
	private List<GrupaRobe> grupaRobe;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "pdv")
	private List<StopaPDV> stopePDV;

	public PDV() {
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

	public List<GrupaRobe> getGrupaRobe() {
		return grupaRobe;
	}

	public void setGrupaRobe(List<GrupaRobe> grupaRobe) {
		this.grupaRobe = grupaRobe;
	}

	public List<StopaPDV> getStopePDV() {
		return stopePDV;
	}

	public void setStopePDV(List<StopaPDV> stopePDV) {
		this.stopePDV = stopePDV;
	}
}
