package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.PDV;
import com.example.poslovna_informatika.repositories.PdvRepository;
import com.example.poslovna_informatika.serviceInterfaces.PdvServiceInterface;

@Service
public class PdvService implements PdvServiceInterface {

	@Autowired
	private PdvRepository pdvRepository;

	@Override
	public List<PDV> findAll() {
		return pdvRepository.findAll();

	}
	
	@Override
	public Page<PDV> findAll(Pageable pageable) {
		return pdvRepository.findAll(pageable);

	}

	@Override
	public PDV findOne(long id) {
		return pdvRepository.findOne(id);
	}

	@Override
	public PDV findByNaziv(String naziv) {
		return pdvRepository.findByNaziv(naziv);
	}

	@Override
	public PDV save(PDV pdv) {
		return pdvRepository.save(pdv);
	}

	@Override
	public void remove(long id) {
		pdvRepository.delete(id);
	}

}
