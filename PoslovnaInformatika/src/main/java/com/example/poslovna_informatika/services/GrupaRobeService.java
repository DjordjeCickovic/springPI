package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.GrupaRobe;
import com.example.poslovna_informatika.repositories.GrupaRobeRepository;
import com.example.poslovna_informatika.serviceInterfaces.GrupaRobeServiceInterface;

@Service
public class GrupaRobeService implements GrupaRobeServiceInterface {

	@Autowired
	GrupaRobeRepository grupaRobeRepository;

	@Override
	public List<GrupaRobe> findAll() {
		return grupaRobeRepository.findAll();
	}

	@Override
	public Page<GrupaRobe> findAll(Pageable pageable) {
		return grupaRobeRepository.findAll(pageable);
	}

	@Override
	public GrupaRobe findOne(long id) {
		return grupaRobeRepository.findOne(id);
	}

	@Override
	public GrupaRobe findByNaziv(String naziv) {
		return grupaRobeRepository.findByNaziv(naziv);
	}

	@Override
	public List<GrupaRobe> findAllByPreduzeceId(long preduzeceId) {
		return grupaRobeRepository.findAllByPreduzeceId(preduzeceId);
	}

	@Override
	public List<GrupaRobe> findAllByPdvId(long pdvId) {
		return grupaRobeRepository.findAllByPdvId(pdvId);
	}

	@Override
	public GrupaRobe save(GrupaRobe grupaRobe) {
		return grupaRobeRepository.save(grupaRobe);
	}

	@Override
	public void remove(long id) {
		grupaRobeRepository.delete(id);
	}
}
