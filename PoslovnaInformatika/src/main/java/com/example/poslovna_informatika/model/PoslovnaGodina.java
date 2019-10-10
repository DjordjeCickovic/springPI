package com.example.poslovna_informatika.model;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

@Entity
@Table(name = "poslovna_godina")
public class PoslovnaGodina {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "poslovna_godina_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false, unique = true)
	private int godina;

	@Column
	private boolean zakljucena;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "poslovna_godina")
	private List<Faktura> fakture;

	public PoslovnaGodina() {
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

	public List<Faktura> getFakture() {
		return fakture;
	}

	public void setFakture(List<Faktura> fakture) {
		this.fakture = fakture;
	}
}