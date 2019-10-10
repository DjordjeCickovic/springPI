package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.PoslovnaGodina;

public interface PoslovnaGodinaRepository extends JpaRepository<PoslovnaGodina, Long> {

	List<PoslovnaGodina> findAll();

	Page<PoslovnaGodina> findAll(Pageable pageable);

	List<PoslovnaGodina> findAllByGodinaBetween(int pocetnaGodina, int krajnjaGodina);

	List<PoslovnaGodina> findAllByZakljucena(boolean zakljucena);

}