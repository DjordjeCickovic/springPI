package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.StavkaCenovnika;
import com.example.poslovna_informatika.repositories.StavkaCenovnikaRepository;
import com.example.poslovna_informatika.serviceInterfaces.StavkaCenovnikaServiceInterface;

@Service
public class StavkaCenovnikaService implements StavkaCenovnikaServiceInterface {

	@Autowired
	StavkaCenovnikaRepository stavkaCenovnikaRepository;

	@Override
	public List<StavkaCenovnika> findAll() {
		return stavkaCenovnikaRepository.findAll();

	}

	@Override
	public Page<StavkaCenovnika> findAll(Pageable pageable) {
		return stavkaCenovnikaRepository.findAll(pageable);

	}

	@Override
	public StavkaCenovnika findOne(long id) {
		return stavkaCenovnikaRepository.findOne(id);
	}

	@Override
	public List<StavkaCenovnika> findAllByCenovnikId(long cenovnikId) {
		return stavkaCenovnikaRepository.findAllByCenovnikId(cenovnikId);
	}

	@Override
	public StavkaCenovnika findByRobaId(long robaId) {
		return stavkaCenovnikaRepository.findByRobaId(robaId);
	}

	@Override
	public List<StavkaCenovnika> findAllByCenaBetween(double pocetnaCena, double krajnjaCena) {
		return stavkaCenovnikaRepository.findAllByCenaBetween(pocetnaCena, krajnjaCena);
	}

	@Override
	public StavkaCenovnika save(StavkaCenovnika stavkaCenovnika) {
		return stavkaCenovnikaRepository.save(stavkaCenovnika);
	}

	@Override
	public void remove(long id) {
		stavkaCenovnikaRepository.delete(id);
	}

}
