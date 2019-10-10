package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.Faktura;

public interface FakturaRepository extends JpaRepository<Faktura, Long> {

	List<Faktura> findAll();

	Page<Faktura> findAll(Pageable pageable);

	Faktura findById(long id);

	List<Faktura> findAllByDatumFaktureBetween(String pocetniDatum, String krajnjiDatum);

	List<Faktura> findAllByDatumValuteBetween(String pocetniDatum, String krajnjiDatum);

	List<Faktura> findAllByOsnovicaBetween(double pocetnaOsnovica, double krajnjaOsnovica);

	List<Faktura> findAllByUkupanPDVBetween(double pocetniUkPdv, double krajnjiUkPdv);

	List<Faktura> findAllByIznosZaPlacanjeBetween(double pocetniIznos, double krajnjiIznos);

	List<Faktura> findAllByPreduzeceId(long preduzeceId);

	List<Faktura> findAllByPoslovniPartnerId(long poslovniPartnerId);

	List<Faktura> findAllByPoslovnaGodinaId(long poslovnaGodinaId);

}
