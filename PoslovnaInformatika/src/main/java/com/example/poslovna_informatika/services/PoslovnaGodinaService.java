package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.PoslovnaGodina;
import com.example.poslovna_informatika.repositories.PoslovnaGodinaRepository;
import com.example.poslovna_informatika.serviceInterfaces.PoslovnaGodinaServiceInterface;

@Service
public class PoslovnaGodinaService implements PoslovnaGodinaServiceInterface {

	@Autowired
	PoslovnaGodinaRepository poslovnaGodinaRepository;

	@Override
	public List<PoslovnaGodina> findAll() {
		return poslovnaGodinaRepository.findAll();

	}

	@Override
	public Page<PoslovnaGodina> findAll(Pageable pageable) {
		return poslovnaGodinaRepository.findAll(pageable);

	}

	@Override
	public PoslovnaGodina findOne(long id) {
		return poslovnaGodinaRepository.findOne(id);
	}

	@Override
	public List<PoslovnaGodina> findAllByGodinaBetween(int pocetnaGodina, int krajnjaGodina) {
		return poslovnaGodinaRepository.findAllByGodinaBetween(pocetnaGodina, krajnjaGodina);
	}

	@Override
	public List<PoslovnaGodina> findAllByZakljucena(boolean zakljucena) {
		return poslovnaGodinaRepository.findAllByZakljucena(zakljucena);
	}

	@Override
	public PoslovnaGodina save(PoslovnaGodina poslovnaGodina) {
		return poslovnaGodinaRepository.save(poslovnaGodina);
	}

	@Override
	public void remove(long id) {
		poslovnaGodinaRepository.delete(id);
	}

}