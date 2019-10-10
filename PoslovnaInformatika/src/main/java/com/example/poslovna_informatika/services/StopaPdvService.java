package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.StopaPDV;
import com.example.poslovna_informatika.repositories.StopaPdvRepository;
import com.example.poslovna_informatika.serviceInterfaces.StopaPdvServiceInterface;

@Service
public class StopaPdvService implements StopaPdvServiceInterface {

	@Autowired
	private StopaPdvRepository stopaPdvRepository;

	@Override
	public List<StopaPDV> findAll() {
		return stopaPdvRepository.findAll();
	}

	@Override
	public Page<StopaPDV> findAll(Pageable pageable) {
		return stopaPdvRepository.findAll(pageable);
	}

	@Override
	public StopaPDV findOne(long id) {
		return stopaPdvRepository.findOne(id);
	}

	@Override
	public List<StopaPDV> findAllByPdvId(long pdvId) {
		return stopaPdvRepository.findAllByPdvId(pdvId);
	}

	@Override
	public List<StopaPDV> findAllByProcenat(double procenat) {
		return stopaPdvRepository.findAllByProcenat(procenat);
	}

	@Override
	public StopaPDV findByDatumVazenja(String datumVazenja) {
		return stopaPdvRepository.findByDatumVazenja(datumVazenja);
	}

	@Override
	public StopaPDV save(StopaPDV stopaPDV) {
		return stopaPdvRepository.save(stopaPDV);
	}

	@Override
	public void remove(long id) {
		stopaPdvRepository.delete(id);
	}

}
