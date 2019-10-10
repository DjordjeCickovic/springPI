package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.Mesto;

public interface MestoServiceInterface {

	List<Mesto> findAll();
	
	Page<Mesto> findAll(Pageable pageable);

	Mesto findOne(long mestoId);

	Mesto findByGrad(String grad);

	List<Mesto> findByDrzava(String drzava);

	Mesto save(Mesto cenovnik);

	void remove(long id);

}
