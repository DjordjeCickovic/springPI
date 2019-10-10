package com.example.poslovna_informatika.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;

@Entity
@Table(name = "stopa_pdv")
public class StopaPDV {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "stopa_pdv_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false)
	private double procenat;

	@Column(nullable = false,unique= true)
	private String datumVazenja;

	@ManyToOne
	@JoinColumn(name = "pdv_id", referencedColumnName = "pdv_id")
	private PDV pdv;

	public StopaPDV() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getProcenat() {
		return procenat;
	}

	public void setProcenat(double procenat) {
		this.procenat = procenat;
	}

	public String getDatumVazenja() {
		return datumVazenja;
	}

	public void setDatumVazenja(String datumVazenja) {
		this.datumVazenja = datumVazenja;
	}

	public PDV getPdv() {
		return pdv;
	}

	public void setPdv(PDV pdv) {
		this.pdv = pdv;
	}
}
