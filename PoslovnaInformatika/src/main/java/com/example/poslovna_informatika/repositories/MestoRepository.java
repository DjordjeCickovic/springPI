package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.Mesto;

public interface MestoRepository extends JpaRepository<Mesto, Long> {

	List<Mesto> findAll();

	Page<Mesto> findAll(Pageable pageable);

	List<Mesto> findByDrzava(String drzava);

	Mesto findByGrad(String grad);

}
