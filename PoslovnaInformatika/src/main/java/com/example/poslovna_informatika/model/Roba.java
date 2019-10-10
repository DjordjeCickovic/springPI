package com.example.poslovna_informatika.model;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

@Entity
@Table(name = "roba")
public class Roba {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "roba_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false)
	private String naziv;

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stavka_cenovnika_id")
	private StavkaCenovnika stavkaCenovnika;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "roba")
	private List<StavkaFakture> stavkaFakture;

	@ManyToOne
	@JoinColumn(name = "jedinica_mere_id", referencedColumnName = "jedinica_mere_id")
	private JedinicaMere jediniceMere;

	@ManyToOne
	@JoinColumn(name = "grupa_robe_id", referencedColumnName = "grupa_robe_id")
	private GrupaRobe grupaRobe;

	public Roba() {
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

	public StavkaCenovnika getStavkaCenovnika() {
		return stavkaCenovnika;
	}

	public void setStavkaCenovnika(StavkaCenovnika stavkaCenovnika) {
		this.stavkaCenovnika = stavkaCenovnika;
	}

	public JedinicaMere getJediniceMere() {
		return jediniceMere;
	}

	public void setJediniceMere(JedinicaMere jediniceMere) {
		this.jediniceMere = jediniceMere;
	}

	public GrupaRobe getGrupaRobe() {
		return grupaRobe;
	}

	public void setGrupaRobe(GrupaRobe grupaRobe) {
		this.grupaRobe = grupaRobe;
	}
}
