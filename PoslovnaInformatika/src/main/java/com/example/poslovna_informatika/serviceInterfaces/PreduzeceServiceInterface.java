package com.example.poslovna_informatika.serviceInterfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.poslovna_informatika.model.Preduzece;

public interface PreduzeceServiceInterface {

	List<Preduzece> findAll();

	Page<Preduzece> findAll(Pageable pageable);

	Preduzece findOne(long id);

	Preduzece findByNaziv(String naziv);

	Preduzece findByPib(int pib);

	List<Preduzece> findAllByAdresa(String adresa);

	List<Preduzece> findAllByTelefon(String telefon);

	List<Preduzece> findAllByEmail(String email);

	List<Preduzece> findAllByMestoId(long mestoId);

	Preduzece save(Preduzece preduzece);

	void remove(long id);

	Preduzece findOne(Preduzece preduzece);

}