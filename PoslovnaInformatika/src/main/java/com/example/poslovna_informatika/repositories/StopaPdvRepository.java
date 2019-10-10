package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.StopaPDV;

public interface StopaPdvRepository extends JpaRepository<StopaPDV, Long> {

	List<StopaPDV> findAll();

	Page<StopaPDV> findAll(Pageable pageable);

	List<StopaPDV> findAllByPdvId(long pdvId);

	List<StopaPDV> findAllByProcenat(double procenat);

	StopaPDV findByDatumVazenja(String datumVazenja);


}
