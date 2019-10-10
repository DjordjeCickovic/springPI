package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.StavkaCenovnika;

public interface StavkaCenovnikaServiceInterface {

	List<StavkaCenovnika> findAll();

	Page<StavkaCenovnika> findAll(Pageable pageable);

	StavkaCenovnika findOne(long id);

	List<StavkaCenovnika> findAllByCenovnikId(long cenovnikId);

	StavkaCenovnika findByRobaId(long robaId);

	List<StavkaCenovnika> findAllByCenaBetween(double pocetnaCena, double krajnjaCena);

	StavkaCenovnika save(StavkaCenovnika stavkaCenovnika);

	void remove(long id);

}
