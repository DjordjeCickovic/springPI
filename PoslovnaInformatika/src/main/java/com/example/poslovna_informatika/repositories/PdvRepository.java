package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.PDV;

public interface PdvRepository extends JpaRepository<PDV, Long> {

	List<PDV> findAll();
	
	Page<PDV> findAll(Pageable pageable);
	
	PDV findByNaziv(String naziv);

}
