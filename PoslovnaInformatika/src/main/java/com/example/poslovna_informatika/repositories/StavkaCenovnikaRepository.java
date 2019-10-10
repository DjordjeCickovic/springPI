package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.StavkaCenovnika;

public interface StavkaCenovnikaRepository extends JpaRepository<StavkaCenovnika, Long> {

	List<StavkaCenovnika> findAll();

	Page<StavkaCenovnika> findAll(Pageable pageable);

	List<StavkaCenovnika> findAllByCenovnikId(long cenovnikId);

	StavkaCenovnika findByRobaId(long robaId);

	List<StavkaCenovnika> findAllByCenaBetween(double pocetnaCena, double krajnjaCena);

}
