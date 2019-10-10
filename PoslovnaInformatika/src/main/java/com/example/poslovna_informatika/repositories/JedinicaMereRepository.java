package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.JedinicaMere;


public interface JedinicaMereRepository extends JpaRepository<JedinicaMere, Long> {

	List<JedinicaMere> findAll();
	
	Page<JedinicaMere> findAll(Pageable pageable);
	
	JedinicaMere findByNaziv(String naziv);

}
