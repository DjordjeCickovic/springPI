package com.example.poslovna_informatika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.poslovna_informatika.model.PoslovniPartner;
import com.example.poslovna_informatika.repositories.PoslovniPartnerRepository;
import com.example.poslovna_informatika.serviceInterfaces.PoslovniPartnerServiceInterface;

@Service
public class PoslovniPartnerService implements PoslovniPartnerServiceInterface {
	@Autowired
	PoslovniPartnerRepository poslovniPartnerRepository;

	@Override
	public List<PoslovniPartner> findAll() {
		return poslovniPartnerRepository.findAll();

	}

	@Override
	public Page<PoslovniPartner> findAll(Pageable pageable) {
		return poslovniPartnerRepository.findAll(pageable);

	}

	@Override
	public PoslovniPartner findOne(long id) {
		return poslovniPartnerRepository.findOne(id);
	}

	@Override
	public PoslovniPartner findByNaziv(String naziv) {
		return poslovniPartnerRepository.findByNaziv(naziv);
	}

	@Override
	public List<PoslovniPartner> findAllByAdresa(String adresa) {
		return poslovniPartnerRepository.findAllByAdresa(adresa);
	}

	@Override
	public List<PoslovniPartner> findAllByVrsta(String vrsta) {
		return poslovniPartnerRepository.findAllByVrsta(vrsta);
	}

	@Override
	public List<PoslovniPartner> findAllByMestoId(long mestoId) {
		return poslovniPartnerRepository.findAllByMestoId(mestoId);
	}

	@Override
	public List<PoslovniPartner> findAllByPreduzeceId(long preduzeceId) {
		return poslovniPartnerRepository.findAllByPreduzeceId(preduzeceId);
	}

	@Override
	public PoslovniPartner save(PoslovniPartner poslovniPartner) {
		return poslovniPartnerRepository.save(poslovniPartner);
	}

	@Override
	public void remove(long id) {
		poslovniPartnerRepository.delete(id);
	}

}