package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.GrupaRobe;

public interface GrupaRobeServiceInterface {
	
	List<GrupaRobe> findAll();
	
	Page<GrupaRobe> findAll(Pageable pageable);

	GrupaRobe findOne(long id);

	GrupaRobe findByNaziv(String naziv);

	List<GrupaRobe> findAllByPreduzeceId(long preduzeceId);

	List<GrupaRobe> findAllByPdvId(long pdvId);

	GrupaRobe save(GrupaRobe grupaRobe);

	void remove(long id);

}
