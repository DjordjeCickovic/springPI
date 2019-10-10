package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.JedinicaMere;

public interface JedinicaMereServiceInterface {

	List<JedinicaMere> findAll();
	
	Page<JedinicaMere> findAll(Pageable pageable);

	JedinicaMere findOne(long id);

	JedinicaMere findByNaziv(String naziv);

	JedinicaMere save(JedinicaMere jedinicaMere);

	void remove(long id);

}
