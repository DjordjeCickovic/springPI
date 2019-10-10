package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.PoslovnaGodina;

public interface PoslovnaGodinaServiceInterface {

	List<PoslovnaGodina> findAll();
	
	Page<PoslovnaGodina> findAll(Pageable pageable);

	PoslovnaGodina findOne(long id);

	List<PoslovnaGodina> findAllByGodinaBetween(int pocetnaGodina, int krajnjaGodina);

	List<PoslovnaGodina> findAllByZakljucena(boolean zakljucena);

	PoslovnaGodina save(PoslovnaGodina poslovnaGodina);

	void remove(long id);

}