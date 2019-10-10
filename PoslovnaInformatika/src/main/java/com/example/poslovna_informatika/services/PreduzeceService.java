package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.Preduzece;
import com.example.poslovna_informatika.repositories.PreduzeceRepository;
import com.example.poslovna_informatika.serviceInterfaces.PreduzeceServiceInterface;

@Service
public class PreduzeceService implements PreduzeceServiceInterface {

	@Autowired
	PreduzeceRepository preduzeceRepository;

	@Override
	public List<Preduzece> findAll() {
		return preduzeceRepository.findAll();
	}

	@Override
	public Page<Preduzece> findAll(Pageable pageable) {
		return preduzeceRepository.findAll(pageable);
	}

	@Override
	public Preduzece findOne(long id) {
		return preduzeceRepository.findOne(id);
	}

	@Override
	public Preduzece findByNaziv(String naziv) {
		return preduzeceRepository.findByNaziv(naziv);
	}

	@Override
	public Preduzece findByPib(int pib) {
		return preduzeceRepository.findByPib(pib);
	}

	@Override
	public List<Preduzece> findAllByAdresa(String adresa) {
		return preduzeceRepository.findAllByAdresa(adresa);
	}

	@Override
	public List<Preduzece> findAllByTelefon(String telefon) {
		return preduzeceRepository.findAllByTelefon(telefon);
	}

	@Override
	public List<Preduzece> findAllByEmail(String email) {
		return preduzeceRepository.findAllByEmail(email);
	}

	@Override
	public List<Preduzece> findAllByMestoId(long mestoId) {
		return preduzeceRepository.findAllByMestoId(mestoId);
	}

	@Override
	public Preduzece save(Preduzece preduzece) {
		return preduzeceRepository.save(preduzece);
	}

	@Override
	public void remove(long id) {
		preduzeceRepository.delete(id);
	}

	@Override
	public Preduzece findOne(Preduzece preduzece) {
		// TODO Auto-generated method stub
		return null;
	}
}