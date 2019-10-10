package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.Preduzece;

public interface PreduzeceRepository extends JpaRepository<Preduzece, Long> {

	List<Preduzece> findAll();

	Page<Preduzece> findAll(Pageable pageable);

	Preduzece findByNaziv(String naziv);

	Preduzece findByPib(int pib);

	List<Preduzece> findAllByAdresa(String adresa);

	List<Preduzece> findAllByTelefon(String telefon);

	List<Preduzece> findAllByEmail(String email);

	List<Preduzece> findAllByMestoId(long mestoId);

}