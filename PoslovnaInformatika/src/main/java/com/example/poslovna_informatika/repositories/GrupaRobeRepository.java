package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.GrupaRobe;

public interface GrupaRobeRepository extends JpaRepository<GrupaRobe, Long> {

	List<GrupaRobe> findAll();

	Page<GrupaRobe> findAll(Pageable pageable);

	GrupaRobe findByNaziv(String naziv);

	List<GrupaRobe> findAllByPreduzeceId(long preduzeceId);

	List<GrupaRobe> findAllByPdvId(long pdvId);

}
