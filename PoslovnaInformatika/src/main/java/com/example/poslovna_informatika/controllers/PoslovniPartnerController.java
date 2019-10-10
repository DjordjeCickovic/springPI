package com.example.poslovna_informatika.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.poslovna_informatika.dto.PoslovniPartnerDTO;
import com.example.poslovna_informatika.model.Mesto;
import com.example.poslovna_informatika.model.PoslovniPartner;
import com.example.poslovna_informatika.model.Preduzece;
import com.example.poslovna_informatika.services.MestoService;
import com.example.poslovna_informatika.services.PoslovniPartnerService;
import com.example.poslovna_informatika.services.PreduzeceService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping(value = "api/poslovni-partner")
public class PoslovniPartnerController {

	private PoslovniPartnerService poslovniPartnerService;
	private MestoService mestoService;
	private PreduzeceService preduzeceService;

	@Autowired
	public PoslovniPartnerController(PoslovniPartnerService poslovniPartnerService, MestoService mestoService,
			PreduzeceService preduzeceService) {
		this.poslovniPartnerService = poslovniPartnerService;
		this.mestoService = mestoService;
		this.preduzeceService = preduzeceService;

	}

	@GetMapping(value = "/poslovnipartnerreport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/PoslovniPartnerReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=poslovnipartnerreport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<PoslovniPartnerDTO>> getPoslovniPartneri() {
		List<PoslovniPartner> poslovniPartners = poslovniPartnerService.findAll();
		List<PoslovniPartnerDTO> poslovniPartnerDTOS = new ArrayList<PoslovniPartnerDTO>();
		for (PoslovniPartner pg : poslovniPartners) {
			poslovniPartnerDTOS.add(new PoslovniPartnerDTO(pg));
		}
		return new ResponseEntity<List<PoslovniPartnerDTO>>(poslovniPartnerDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<PoslovniPartnerDTO>> getPoslovniPartneriPagable(@RequestParam("page") int page) {
		Page<PoslovniPartner> poslovniPartners = poslovniPartnerService.findAll(new PageRequest(page, 3));
		List<PoslovniPartner> poslovniPartneriList = poslovniPartners.getContent();
		List<PoslovniPartnerDTO> poslovniPartnerDTOS = new ArrayList<PoslovniPartnerDTO>();
		for (PoslovniPartner pg : poslovniPartneriList) {
			poslovniPartnerDTOS.add(new PoslovniPartnerDTO(pg));
		}
		return new ResponseEntity<List<PoslovniPartnerDTO>>(poslovniPartnerDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<PoslovniPartnerDTO> getPoslovniPartner(@PathVariable("id") Long id) {
		PoslovniPartner poslovniPartner = poslovniPartnerService.findOne(id);
		if (poslovniPartner == null) {
			return new ResponseEntity<PoslovniPartnerDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<PoslovniPartnerDTO>(new PoslovniPartnerDTO(poslovniPartner), HttpStatus.OK);
	}

	@GetMapping(value = "naziv/{naziv}")
	public ResponseEntity<PoslovniPartnerDTO> getPoslovniPartnerbyNaziv(@PathVariable("naziv") String naziv) {
		PoslovniPartner poslovniPartner = poslovniPartnerService.findByNaziv(naziv);
		if (poslovniPartner == null) {
			return new ResponseEntity<PoslovniPartnerDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<PoslovniPartnerDTO>(new PoslovniPartnerDTO(poslovniPartner), HttpStatus.OK);
	}

	@GetMapping(value = "adresa/{adresa}")
	public ResponseEntity<List<PoslovniPartnerDTO>> getPoslovniPartneriByAdresa(@PathVariable("adresa") String adresa) {
		List<PoslovniPartner> poslovniPartners = poslovniPartnerService.findAllByAdresa(adresa);
		List<PoslovniPartnerDTO> poslovniPartnerDTOS = new ArrayList<PoslovniPartnerDTO>();
		for (PoslovniPartner pg : poslovniPartners) {
			poslovniPartnerDTOS.add(new PoslovniPartnerDTO(pg));
		}
		return new ResponseEntity<List<PoslovniPartnerDTO>>(poslovniPartnerDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "mesto/{mestoId}")
	public ResponseEntity<List<PoslovniPartnerDTO>> getPoslovniPartneriByMesto(@PathVariable("mestoId") Long mestoId) {
		List<PoslovniPartner> poslovniPartners = poslovniPartnerService.findAllByMestoId(mestoId);
		List<PoslovniPartnerDTO> poslovniPartnerDTOS = new ArrayList<PoslovniPartnerDTO>();
		for (PoslovniPartner pg : poslovniPartners) {
			poslovniPartnerDTOS.add(new PoslovniPartnerDTO(pg));
		}
		return new ResponseEntity<List<PoslovniPartnerDTO>>(poslovniPartnerDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "preduzece/{preduzeceId}")
	public ResponseEntity<List<PoslovniPartnerDTO>> getPoslovniPartneriByPreduzece(
			@PathVariable("preduzeceId") Long preduzeceId) {
		List<PoslovniPartner> poslovniPartners = poslovniPartnerService.findAllByPreduzeceId(preduzeceId);
		List<PoslovniPartnerDTO> poslovniPartnerDTOS = new ArrayList<PoslovniPartnerDTO>();
		for (PoslovniPartner pg : poslovniPartners) {
			poslovniPartnerDTOS.add(new PoslovniPartnerDTO(pg));
		}
		return new ResponseEntity<List<PoslovniPartnerDTO>>(poslovniPartnerDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "vrsta/{vrsta}")
	public ResponseEntity<List<PoslovniPartnerDTO>> getPoslovniPartneriByVrsta(@PathVariable("vrsta") String vrsta) {
		List<PoslovniPartner> poslovniPartners = poslovniPartnerService.findAllByVrsta(vrsta);
		List<PoslovniPartnerDTO> poslovniPartnerDTOS = new ArrayList<PoslovniPartnerDTO>();
		for (PoslovniPartner pg : poslovniPartners) {
			poslovniPartnerDTOS.add(new PoslovniPartnerDTO(pg));
		}
		return new ResponseEntity<List<PoslovniPartnerDTO>>(poslovniPartnerDTOS, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<PoslovniPartnerDTO> saveItem(@RequestBody PoslovniPartnerDTO poslovniPartnerDTO) {
		PoslovniPartner pp = new PoslovniPartner();
		pp.setAdresa(poslovniPartnerDTO.getAdresa());
		pp.setNaziv(poslovniPartnerDTO.getNaziv());
		pp.setVrsta(poslovniPartnerDTO.getVrsta());

		if (poslovniPartnerDTO.getMesto() != null && poslovniPartnerDTO.getMesto().getId() != 0) {
			Mesto m = mestoService.findOne(poslovniPartnerDTO.getMesto().getId());
			pp.setMesto(m);
		}

		if (poslovniPartnerDTO.getPreduzece() != null && poslovniPartnerDTO.getPreduzece().getId() != 0) {
			Preduzece p = preduzeceService.findOne(poslovniPartnerDTO.getPreduzece().getId());
			pp.setPreduzece(p);
		}

		pp = poslovniPartnerService.save(pp);
		return new ResponseEntity<PoslovniPartnerDTO>(new PoslovniPartnerDTO(pp), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<PoslovniPartnerDTO> updateItem(@RequestBody PoslovniPartnerDTO poslovniPartnerDTO,
			@PathVariable("id") long id) {
		PoslovniPartner pp = poslovniPartnerService.findOne(id);

		if (pp == null) {
			return new ResponseEntity<PoslovniPartnerDTO>(HttpStatus.BAD_REQUEST);
		}

		pp.setAdresa(poslovniPartnerDTO.getAdresa());
		pp.setNaziv(poslovniPartnerDTO.getNaziv());
		pp.setVrsta(poslovniPartnerDTO.getVrsta());

		if (poslovniPartnerDTO.getMesto() != null && poslovniPartnerDTO.getMesto().getId() != 0) {
			Mesto m = mestoService.findOne(poslovniPartnerDTO.getMesto().getId());
			pp.setMesto(m);
		}

		if (poslovniPartnerDTO.getPreduzece() != null && poslovniPartnerDTO.getPreduzece().getId() != 0) {
			Preduzece p = preduzeceService.findOne(poslovniPartnerDTO.getPreduzece().getId());
			pp.setPreduzece(p);
		}

		pp = poslovniPartnerService.save(pp);
		return new ResponseEntity<PoslovniPartnerDTO>(new PoslovniPartnerDTO(pp), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		PoslovniPartner pp = poslovniPartnerService.findOne(id);
		if (pp != null) {
			poslovniPartnerService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}