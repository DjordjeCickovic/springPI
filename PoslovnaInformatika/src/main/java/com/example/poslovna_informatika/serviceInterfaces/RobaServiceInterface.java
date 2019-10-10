package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.Roba;

public interface RobaServiceInterface {

	List<Roba> findAll();

	Page<Roba> findAll(Pageable pageable);

	Roba findOne(long id);

	List<Roba> findAllByNaziv(String nazivRobe);

	List<Roba> findAllByGrupaRobeId(long grupaRobeId);

	List<Roba> findAllByJediniceMereId(long jedinicaMereId);

	Roba save(Roba roba);

	void remove(long id);

}
