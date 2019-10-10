package com.example.poslovna_informatika.model;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

@Entity
@Table(name = "grupa_robe")
public class GrupaRobe {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "grupa_robe_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false, length = 100)
	private String naziv;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "grupa_robe")
	private List<Roba> robe;

	@ManyToOne
	@JoinColumn(name = "preduzece_id", referencedColumnName = "preduzece_id")
	private Preduzece preduzece;

	@ManyToOne
	@JoinColumn(name = "pdv_id", referencedColumnName = "pdv_id")
	private PDV pdv;

	public GrupaRobe() {
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

	public List<Roba> getRobe() {
		return robe;
	}

	public void setRobe(List<Roba> robe) {
		this.robe = robe;
	}

	public Preduzece getPreduzece() {
		return preduzece;
	}

	public void setPreduzece(Preduzece preduzece) {
		this.preduzece = preduzece;
	}

	public PDV getPdv() {
		return pdv;
	}

	public void setPdv(PDV pdv) {
		this.pdv = pdv;
	}
}
