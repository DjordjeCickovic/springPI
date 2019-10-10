package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.PDV;

public interface PdvServiceInterface {

	List<PDV> findAll();

	Page<PDV> findAll(Pageable pageable);
	
	PDV findOne(long id);

	PDV findByNaziv(String naziv);

	PDV save(PDV pdv);

	void remove(long id);

}
