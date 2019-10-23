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
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
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

import com.example.poslovna_informatika.dto.FakturaDTO;
import com.example.poslovna_informatika.model.Faktura;
import com.example.poslovna_informatika.model.PoslovnaGodina;
import com.example.poslovna_informatika.model.PoslovniPartner;
import com.example.poslovna_informatika.model.Preduzece;
import com.example.poslovna_informatika.services.FakturaService;
import com.example.poslovna_informatika.services.PoslovnaGodinaService;
import com.example.poslovna_informatika.services.PoslovniPartnerService;
import com.example.poslovna_informatika.services.PreduzeceService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import scala.Console;

@RestController
@RequestMapping(value = "api/faktura")
public class FakturaController {

	private FakturaService fakturaService;
	private PreduzeceService preduzeceService;
	private PoslovnaGodinaService poslovnaGodinaService;
	private PoslovniPartnerService poslovniPartnerService;

	@Autowired
	public FakturaController(FakturaService fakturaService, PreduzeceService preduzeceService,
			PoslovnaGodinaService poslovnaGodinaService, PoslovniPartnerService poslovniPartnerService) {
		this.fakturaService = fakturaService;
		this.preduzeceService = preduzeceService;
		this.poslovnaGodinaService = poslovnaGodinaService;
		this.poslovniPartnerService = poslovniPartnerService;
	}

	@GetMapping(value = "/fakturareport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/FakturaReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=fakturareport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<FakturaDTO>> getFaktura() {
		List<Faktura> jediniceMera = fakturaService.findAll();
		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : jediniceMera) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<FakturaDTO>> getFakturaPageable(@RequestParam("page") int page) {
		Page<Faktura> fakture = fakturaService.findAll(new PageRequest(page, 3));
		List<Faktura> faktureList = fakture.getContent();
		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : faktureList) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<FakturaDTO> getFaktura(@PathVariable("id") Long id) {
		Faktura faktura = fakturaService.findOne(id);
		if (faktura == null) {
			return new ResponseEntity<FakturaDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<FakturaDTO>(new FakturaDTO(faktura), HttpStatus.OK);
	}

	@GetMapping(value = "/brojFakture/{brojFakture}")
	public ResponseEntity<FakturaDTO> getFakturabyBrojFakture(@PathVariable("brojFakture") long id) {
		Faktura faktura = fakturaService.findByBrFakture(id);
		if (faktura == null) {
			return new ResponseEntity<FakturaDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<FakturaDTO>(new FakturaDTO(faktura), HttpStatus.OK);
	}

	@GetMapping(value = "/preduzece/{preduzeceId}")
	public ResponseEntity<List<FakturaDTO>> getFakturebyPreduzeceId(@PathVariable("preduzeceId") long preduzeceId) {
		List<Faktura> fakture = fakturaService.findAllByPreduzeceId(preduzeceId);
		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : fakture) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/poslovniPartner/{poslovniPartnerId}")
	public ResponseEntity<List<FakturaDTO>> getFakturebyPoslovniPartnerId(
			@PathVariable("poslovniPartnerId") long poslovniPartnerId) {
		List<Faktura> fakture = fakturaService.findAllByPoslovniPartnerId(poslovniPartnerId);
		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : fakture) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/poslovnaGodina/{poslovnaGodinaId}")
	public ResponseEntity<List<FakturaDTO>> getFakturebyPoslovnaGodinaId(
			@PathVariable("poslovnaGodinaId") long poslovnaGodinaId) {
		List<Faktura> fakture = fakturaService.findAllByPoslovnaGodinaId(poslovnaGodinaId);
		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : fakture) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/datumFakture/{pocetniDatum}/{krajnjiDatum}")
	public ResponseEntity<List<FakturaDTO>> getFaktureBetweenDatumiFakture(
			@PathVariable("pocetniDatum") String pocetniDatum, @PathVariable("krajnjiDatum") String krajnjiDatum) {
		List<Faktura> fakture = fakturaService.findAllByDatumFaktureBetween(pocetniDatum, krajnjiDatum);
		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : fakture) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/datumValute/{pocetniDatum}/{krajnjiDatum}")
	public ResponseEntity<List<FakturaDTO>> getFaktureBetweenDatumiValute(
			@PathVariable("pocetniDatum") String pocetniDatum, @PathVariable("krajnjiDatum") String krajnjiDatum) {
		List<Faktura> fakture = fakturaService.findAllByDatumValuteBetween(pocetniDatum, krajnjiDatum);
		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : fakture) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/osnovica/{pocetnaOsnovica}/{krajnjaOsnovica}")
	public ResponseEntity<List<FakturaDTO>> getFaktureBetweenOsnovice(
			@PathVariable("pocetnaOsnovica") double pocetnaOsnovica,
			@PathVariable("krajnjaOsnovica") double krajnjaOsnovica) {
		List<Faktura> fakture = fakturaService.findAllByOsnovicaBetween(pocetnaOsnovica, krajnjaOsnovica);
		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : fakture) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/ukupanPdv/{pocetanUkupanPdv}/{krajnjiUkupanPdv}")
	public ResponseEntity<List<FakturaDTO>> getFaktureBetweenUkupanPdvs(
			@PathVariable("pocetanUkupanPdv") double pocetanUkupanPdv,
			@PathVariable("krajnjiUkupanPdv") double krajnjiUkupanPdv) {
		List<Faktura> fakture = fakturaService.findAllByUkupanPDVBetween(pocetanUkupanPdv, krajnjiUkupanPdv);

		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : fakture) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/iznosZaPlacanje/{pocetniIznos}/{krajnjiIznos}")
	public ResponseEntity<List<FakturaDTO>> getFaktureBetweenIznos(@PathVariable("pocetniIznos") double pocetniIznos,
			@PathVariable("krajnjiIznos") double krajnjiIznos) {
		List<Faktura> fakture = fakturaService.findAllByIznosZaPlacanjeBetween(pocetniIznos, krajnjiIznos);

		List<FakturaDTO> fakturaDTOS = new ArrayList<FakturaDTO>();
		for (Faktura c : fakture) {
			fakturaDTOS.add(new FakturaDTO(c));
		}
		return new ResponseEntity<List<FakturaDTO>>(fakturaDTOS, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<FakturaDTO> saveItem(@Validated @RequestBody FakturaDTO fakturaDTO, Errors errors) {
		if(errors.hasErrors()) {
			Console.println("SVE GRESKE U STRING" + errors.getAllErrors().toString());
	
			return new ResponseEntity(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Faktura f = new Faktura();
		f.setDatumFakture(fakturaDTO.getDatumFakture());
		f.setDatumValute(fakturaDTO.getDatumValute());
		f.setIznosZaPlacanje(fakturaDTO.getIznosZaPlacanje());
		f.setOsnovica(fakturaDTO.getOsnovica());
		f.setUkupanPDV(fakturaDTO.getUkupanPDV());

		if (fakturaDTO.getPoslovnaGodina().getId() != 0) {
			PoslovnaGodina pg = poslovnaGodinaService.findOne(fakturaDTO.getPoslovnaGodina().getId());
			f.setPoslovnaGodina(pg);

		}
		if (fakturaDTO.getPoslovniPartner().getId() != 0) {
			PoslovniPartner pp = poslovniPartnerService.findOne(fakturaDTO.getPoslovniPartner().getId());
			f.setPoslovniPartner(pp);

		}
		if (fakturaDTO.getPreduzece().getId() != 0) {
			Preduzece p = preduzeceService.findOne(fakturaDTO.getPreduzece().getId());
			f.setPreduzece(p);

		}

		f = fakturaService.save(f);
		return new ResponseEntity<FakturaDTO>(new FakturaDTO(f), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<FakturaDTO> updateItem(@RequestBody FakturaDTO fakturaDTO, @PathVariable("id") long id) {
		Faktura f = fakturaService.findOne(id);

		if (f == null) {
			return new ResponseEntity<FakturaDTO>(HttpStatus.BAD_REQUEST);
		}

		f.setDatumFakture(fakturaDTO.getDatumFakture());
		f.setDatumValute(fakturaDTO.getDatumValute());

		if (fakturaDTO.getPoslovnaGodina() != null && fakturaDTO.getPoslovnaGodina().getId() != 0) {
			PoslovnaGodina pg = poslovnaGodinaService.findOne(fakturaDTO.getPoslovnaGodina().getId());
			f.setPoslovnaGodina(pg);
			;
		}
		if (fakturaDTO.getPoslovniPartner() != null && fakturaDTO.getPoslovniPartner().getId() != 0) {
			PoslovniPartner pp = poslovniPartnerService.findOne(fakturaDTO.getPoslovniPartner().getId());
			f.setPoslovniPartner(pp);
			;
		}
		if (fakturaDTO.getPreduzece() != null && fakturaDTO.getPreduzece().getId() != 0) {
			Preduzece p = preduzeceService.findOne(fakturaDTO.getPreduzece().getId());
			f.setPreduzece(p);
			;
		}

		f = fakturaService.save(f);

		return new ResponseEntity<FakturaDTO>(new FakturaDTO(f), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		Faktura f = fakturaService.findOne(id);
		if (f != null) {
			fakturaService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}
