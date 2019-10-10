package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.Faktura;

public interface FakturaServiceInterface {

	List<Faktura> findAll();

	Page<Faktura> findAll(Pageable pageable);

	Faktura findOne(long id);

	Faktura findByBrFakture(long id);

	List<Faktura> findAllByDatumFaktureBetween(String pocetniDatum, String krajnjiDatum);

	List<Faktura> findAllByDatumValuteBetween(String pocetniDatum, String krajnjiDatum);

	List<Faktura> findAllByOsnovicaBetween(double pocetnaOsnovica, double krajnjaOsnovica);

	List<Faktura> findAllByUkupanPDVBetween(double pocetniUkPdv, double krajnjiUkPdv);

	List<Faktura> findAllByIznosZaPlacanjeBetween(double pocetniIznos, double krajnjiIznos);

	List<Faktura> findAllByPreduzeceId(long preduzeceId);

	List<Faktura> findAllByPoslovniPartnerId(long poslovniPartnerId);

	List<Faktura> findAllByPoslovnaGodinaId(long poslovnaGodinaId);

	Faktura save(Faktura faktura);

	void remove(long id);

}
