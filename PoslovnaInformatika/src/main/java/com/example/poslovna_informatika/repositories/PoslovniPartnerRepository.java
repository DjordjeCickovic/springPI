package com.example.poslovna_informatika.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.poslovna_informatika.model.PoslovniPartner;

public interface PoslovniPartnerRepository extends JpaRepository<PoslovniPartner, Long> {

	List<PoslovniPartner> findAll();

	Page<PoslovniPartner> findAll(Pageable pageable);

	PoslovniPartner findByNaziv(String naziv);

	List<PoslovniPartner> findAllByAdresa(String adresa);

	List<PoslovniPartner> findAllByVrsta(String vrsta);

	List<PoslovniPartner> findAllByMestoId(long mestoId);

	List<PoslovniPartner> findAllByPreduzeceId(long preduzeceId);

}