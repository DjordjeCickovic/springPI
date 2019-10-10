package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.Mesto;
import com.example.poslovna_informatika.repositories.MestoRepository;
import com.example.poslovna_informatika.serviceInterfaces.MestoServiceInterface;

@Service
public class MestoService implements MestoServiceInterface {

	@Autowired
	MestoRepository mestoRepository;

	@Override
	public List<Mesto> findAll() {
		return mestoRepository.findAll();

	}
	
	@Override
	public Page<Mesto> findAll(Pageable pageable) {
		return mestoRepository.findAll(pageable);

	}

	@Override
	public Mesto findOne(long mestoId) {
		return mestoRepository.findOne(mestoId);
	}

	@Override
	public Mesto findByGrad(String grad) {
		return mestoRepository.findByGrad(grad);
	}

	@Override
	public List<Mesto> findByDrzava(String drzava) {
		return mestoRepository.findByDrzava(drzava);
	}

	@Override
	public Mesto save(Mesto mesto) {
		return mestoRepository.save(mesto);
	}

	@Override
	public void remove(long id) {
		mestoRepository.delete(id);
	}

}
