package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.StopaPDV;

public interface StopaPdvServiceInterface {

	List<StopaPDV> findAll();

	Page<StopaPDV> findAll(Pageable pageable);

	StopaPDV findOne(long id);

	List<StopaPDV> findAllByPdvId(long pdvId);

	List<StopaPDV> findAllByProcenat(double procenat);

	StopaPDV findByDatumVazenja(String datumVazenja);

	StopaPDV save(StopaPDV stopaPDV);
	
	void remove(long id);

}
