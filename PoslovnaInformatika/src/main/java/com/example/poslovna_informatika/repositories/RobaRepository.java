package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.Roba;

public interface RobaRepository extends JpaRepository<Roba, Long> {

	List<Roba> findAll();

	Page<Roba> findAll(Pageable pageable);

	List<Roba> findAllByNaziv(String nazivRobe);

	List<Roba> findAllByGrupaRobeId(long grupaRobeId);

	List<Roba> findAllByJediniceMereId(long jedinicaMereId);

}
