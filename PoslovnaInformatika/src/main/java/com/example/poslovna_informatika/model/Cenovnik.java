package com.example.poslovna_informatika.model;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

@Entity
@Table(name = "cenovnik")
public class Cenovnik {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cenovnik_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false)
	private String datumVazenja;

	@Transient
	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "cenovnik")
	private List<StavkaCenovnika> stavkeCenovnika;

	@ManyToOne
	@JoinColumn(name = "preduzece_id", referencedColumnName = "preduzece_id")
	private Preduzece preduzece;

	public Cenovnik() {
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

	public List<StavkaCenovnika> getStavkeCenovnika() {
		return stavkeCenovnika;
	}

	public void setStavkeCenovnika(List<StavkaCenovnika> stavkeCenovnika) {
		this.stavkeCenovnika = stavkeCenovnika;
	}

	public Preduzece getPreduzece() {
		return preduzece;
	}

	public void setPreduzece(Preduzece preduzece) {
		this.preduzece = preduzece;
	}
}
