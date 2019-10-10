package com.example.poslovna_informatika.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;

@Entity
@Table(name = "stavka_cenovnika")
public class StavkaCenovnika {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "stavka_cenovnika_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false)
	private double cena;

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roba_id")
	private Roba roba;

	@ManyToOne
	@JoinColumn(name = "cenovnik_id", referencedColumnName = "cenovnik_id")
	private Cenovnik cenovnik;

	public StavkaCenovnika() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public Roba getRoba() {
		return roba;
	}

	public void setRoba(Roba roba) {
		this.roba = roba;
	}

	public Cenovnik getCenovnik() {
		return cenovnik;
	}

	public void setCenovnik(Cenovnik cenovnik) {
		this.cenovnik = cenovnik;
	}
}
