package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.Roba;
import com.example.poslovna_informatika.repositories.RobaRepository;
import com.example.poslovna_informatika.serviceInterfaces.RobaServiceInterface;

@Service
public class RobaService implements RobaServiceInterface {
	@Autowired
	RobaRepository robaRepository;

	@Override
	public List<Roba> findAll() {
		return robaRepository.findAll();

	}

	@Override
	public Page<Roba> findAll(Pageable pageable) {
		return robaRepository.findAll(pageable);

	}

	@Override
	public Roba findOne(long id) {
		return robaRepository.findOne(id);
	}

	@Override
	public List<Roba> findAllByNaziv(String nazivRobe) {
		return robaRepository.findAllByNaziv(nazivRobe);
	}

	@Override
	public List<Roba> findAllByGrupaRobeId(long grupaRobeId) {
		return robaRepository.findAllByGrupaRobeId(grupaRobeId);
	}

	@Override
	public List<Roba> findAllByJediniceMereId(long jedinicaMereId) {
		return robaRepository.findAllByJediniceMereId(jedinicaMereId);
	}

	@Override
	public Roba save(Roba roba) {
		return robaRepository.save(roba);
	}

	@Override
	public void remove(long id) {
		robaRepository.delete(id);
	}

}
