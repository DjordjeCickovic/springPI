package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.Cenovnik;

public interface CenovnikServiceInterface {

	List<Cenovnik> findAll();

	Page<Cenovnik> findAll(Pageable pageable);

	Cenovnik findOne(long id);

	List<Cenovnik> findAllByPreduzeceId(long preduzeceId);

	List<Cenovnik> findAllByDatumVazenjaBetween(String pocetniDatum, String kranjiDatum);

	Cenovnik save(Cenovnik cenovnik);

	void remove(long id);

}
