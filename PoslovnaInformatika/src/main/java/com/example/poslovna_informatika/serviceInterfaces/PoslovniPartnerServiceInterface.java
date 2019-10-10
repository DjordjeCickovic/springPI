package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.PoslovniPartner;

public interface PoslovniPartnerServiceInterface {

	List<PoslovniPartner> findAll();

	Page<PoslovniPartner> findAll(Pageable pageable);

	PoslovniPartner findOne(long id);

	PoslovniPartner findByNaziv(String naziv);

	List<PoslovniPartner> findAllByAdresa(String adresa);

	List<PoslovniPartner> findAllByVrsta(String vrsta);

	List<PoslovniPartner> findAllByMestoId(long mestoId);

	List<PoslovniPartner> findAllByPreduzeceId(long preduzeceId);

	PoslovniPartner save(PoslovniPartner poslovniPartner);

	void remove(long id);

}