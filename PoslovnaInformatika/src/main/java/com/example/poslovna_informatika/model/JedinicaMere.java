package com.example.poslovna_informatika.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "jedinica_mere")
public class JedinicaMere {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "jedinica_mere_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false, unique = true, length = 7)
	private String naziv;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "jedinica_mere")
	private List<Roba> roba;

	public JedinicaMere() {
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

	public List<Roba> getRoba() {
		return roba;
	}

	public void setRoba(List<Roba> roba) {
		this.roba = roba;
	}
}
