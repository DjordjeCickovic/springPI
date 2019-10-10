package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.StavkaFakture;

public interface StavkaFaktureServiceInterface {

	List<StavkaFakture> findAll();

	Page<StavkaFakture> findAll(Pageable pageable);

	StavkaFakture findOne(long id);

	List<StavkaFakture> findAllByFakturaId(long fakturaId);

	List<StavkaFakture> findAllByRobaIn(long robaId);

	List<StavkaFakture> findAllByKolicinaBetween(int pocetnaKolicina, int krajnjaKolicina);

	List<StavkaFakture> findAllByJedinicnaCenaBetween(double pocetnaJedCena, double krajnjaJedCena);

	List<StavkaFakture> findAllByRabatBetween(double pocetniRabat, double krajnjiRabat);

	List<StavkaFakture> findAllByOsnovicaZaPDVBetween(double pocetnaOsnovica, double krajnjaOsnovica);

	List<StavkaFakture> findAllByProcenatPDVBetween(double pocetniProcPdv, double krajnjiProcPdv);

	List<StavkaFakture> findAllByIznosPDVBetween(double pocetniPdv, double krajnjiPdv);

	List<StavkaFakture> findAllByIznosStavkeBetween(double pocetniIznosStavke, double krajnjiIznosStavke);

	StavkaFakture save(StavkaFakture stavkaFakture);

	void remove(long id);

}
