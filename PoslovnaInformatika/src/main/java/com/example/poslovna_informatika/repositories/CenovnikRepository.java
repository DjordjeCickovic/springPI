package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.Cenovnik;

public interface CenovnikRepository extends JpaRepository<Cenovnik, Long> {

	List<Cenovnik> findAll();

	Page<Cenovnik> findAll(Pageable pageable);

	List<Cenovnik> findAllByPreduzeceId(Long preduzeceId);

	List<Cenovnik> findAllByDatumVazenjaBetween(String pocetniDatum, String kranjiDatum);

}
