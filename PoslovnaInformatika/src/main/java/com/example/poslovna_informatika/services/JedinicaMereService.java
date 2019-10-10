package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.JedinicaMere;
import com.example.poslovna_informatika.repositories.JedinicaMereRepository;
import com.example.poslovna_informatika.serviceInterfaces.JedinicaMereServiceInterface;

@Service
public class JedinicaMereService implements JedinicaMereServiceInterface {

	@Autowired
	JedinicaMereRepository jedinicaMereRepository;

	@Override
	public List<JedinicaMere> findAll() {
		return jedinicaMereRepository.findAll();

	}
	
	@Override
	public Page<JedinicaMere> findAll(Pageable pageable) {
		return jedinicaMereRepository.findAll(pageable);

	}

	@Override
	public JedinicaMere findOne(long id) {
		return jedinicaMereRepository.findOne(id);
	}

	@Override
	public JedinicaMere findByNaziv(String naziv) {
		return jedinicaMereRepository.findByNaziv(naziv);
	}

	@Override
	public JedinicaMere save(JedinicaMere jedinicaMere) {
		return jedinicaMereRepository.save(jedinicaMere);
	}

	@Override
	public void remove(long id) {
		jedinicaMereRepository.delete(id);
	}

}
