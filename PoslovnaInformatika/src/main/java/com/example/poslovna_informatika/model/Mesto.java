package com.example.poslovna_informatika.model;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

@Entity
@Table(name = "mesto")
public class Mesto {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "mesto_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false, unique = true, length = 150)
	private String grad;

	@Column(nullable = false)
	private String drzava;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "mesto")
	private List<Preduzece> preduzeca;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "mesto")
	private List<PoslovniPartner> poslovniPartneri;

	public Mesto() {
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

	public List<Preduzece> getPreduzeca() {
		return preduzeca;
	}

	public void setPreduzeca(List<Preduzece> preduzeca) {
		this.preduzeca = preduzeca;
	}

	public List<PoslovniPartner> getPoslovniPartneri() {
		return poslovniPartneri;
	}

	public void setPoslovniPartneri(List<PoslovniPartner> poslovniPartneri) {
		this.poslovniPartneri = poslovniPartneri;
	}
}
