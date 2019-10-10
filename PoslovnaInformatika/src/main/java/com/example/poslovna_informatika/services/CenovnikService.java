package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.Cenovnik;
import com.example.poslovna_informatika.repositories.CenovnikRepository;
import com.example.poslovna_informatika.serviceInterfaces.CenovnikServiceInterface;

@Service
public class CenovnikService implements CenovnikServiceInterface {

	@Autowired
	CenovnikRepository cenovnikRepository;

	@Override
	public List<Cenovnik> findAll() {
		return cenovnikRepository.findAll();
	}

	@Override
	public Page<Cenovnik> findAll(Pageable pageable) {
		return cenovnikRepository.findAll(pageable);
	}

	@Override
	public Cenovnik findOne(long id) {
		return cenovnikRepository.findOne(id);
	}

	@Override
	public List<Cenovnik> findAllByPreduzeceId(long preduzeceId) {
		return cenovnikRepository.findAllByPreduzeceId(preduzeceId);
	}

	@Override
	public List<Cenovnik> findAllByDatumVazenjaBetween(String pocetniDatum, String kranjiDatum) {
		return cenovnikRepository.findAllByDatumVazenjaBetween(pocetniDatum, kranjiDatum);
	}

	@Override
	public Cenovnik save(Cenovnik cenovnik) {
		return cenovnikRepository.save(cenovnik);
	}

	@Override
	public void remove(long id) {
		cenovnikRepository.delete(id);
	}

}
