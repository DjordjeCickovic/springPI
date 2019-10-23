package com.example.poslovna_informatika.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.thymeleaf.spring4.expression.Fields;

import com.example.poslovna_informatika.dto.StavkaFaktureDTO;
import com.example.poslovna_informatika.model.Faktura;
import com.example.poslovna_informatika.model.Roba;
import com.example.poslovna_informatika.model.StavkaCenovnika;
import com.example.poslovna_informatika.model.StavkaFakture;
import com.example.poslovna_informatika.model.StopaPDV;
import com.example.poslovna_informatika.services.FakturaService;
import com.example.poslovna_informatika.services.RobaService;
import com.example.poslovna_informatika.services.StavkaCenovnikaService;
import com.example.poslovna_informatika.services.StavkaFaktureService;
import com.example.poslovna_informatika.services.StopaPdvService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import scala.Console;

@RestController
@RequestMapping(value = "api/stavka-fakture")
public class StavkaFaktureController {

	private StavkaFaktureService stavkaFaktureService;
	private FakturaService fakturaService;
	private RobaService robaService;
	private StavkaCenovnikaService stavkaCenovnikaService;
	private StopaPdvService stopaPdvService;

	@Autowired
	public StavkaFaktureController(StavkaFaktureService stavkaFaktureService,
			StavkaCenovnikaService stavkaCenovnikaService, FakturaService fakturaService, RobaService robaService,
			StopaPdvService stopaPdvService) {
		this.stavkaFaktureService = stavkaFaktureService;
		this.stavkaCenovnikaService = stavkaCenovnikaService;
		this.fakturaService = fakturaService;
		this.robaService = robaService;
		this.stopaPdvService = stopaPdvService;
	}

	@GetMapping(value = "/stavkafakturereport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/StavkaFaktureReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=stavkafakturereport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<StavkaFaktureDTO>> getItems() {
		List<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAll();
		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkaFaktures) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<StavkaFaktureDTO>> getItems(@RequestParam("page") int page) {
		Page<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAll(new PageRequest(page, 3));
		List<StavkaFakture> stavkeFaktureList = stavkaFaktures.getContent();
		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkeFaktureList) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<StavkaFaktureDTO> getStavkaFakture(@PathVariable("id") Long id) {
		StavkaFakture stavkaFakture = stavkaFaktureService.findOne(id);
		if (stavkaFakture == null) {
			return new ResponseEntity<StavkaFaktureDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<StavkaFaktureDTO>(new StavkaFaktureDTO(stavkaFakture), HttpStatus.OK);
	}
	
	@GetMapping(value = "/fakturaId/{fakturaId}")
	public ResponseEntity<List<StavkaFaktureDTO>> getStavkaFaktureByFaktura(@PathVariable("fakturaId") int fakturaId) {
		List<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAllByFakturaId(fakturaId);
		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkaFaktures) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pdv/{pocetniPdv}/{krajnjiPdv}")
	public ResponseEntity<List<StavkaFaktureDTO>> getStavkeFaktureBetweenPdvs(
			@PathVariable("pocetniPdv") Double pocetniPdv, @PathVariable("krajnjiPdv") Double krajnjiPdv) {
		List<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAllByIznosPDVBetween(pocetniPdv, krajnjiPdv);
		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkaFaktures) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/cena/{pocetnaCena}/{krajnjaCena}")
	public ResponseEntity<List<StavkaFaktureDTO>> getStavkeFaktureBetweenCene(
			@PathVariable("pocetnaCena") Double pocetnaCena, @PathVariable("krajnjaCena") Double krajnjaCena) {
		List<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAllByJedinicnaCenaBetween(pocetnaCena,
				krajnjaCena);
		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkaFaktures) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/kolicina/{pocetnaKolicina}/{krajnjaKolicina}")
	public ResponseEntity<List<StavkaFaktureDTO>> getStavkeFaktureBetweenKolicine(
			@PathVariable("pocetnaKolicina") int pocetnaKolicina,
			@PathVariable("krajnjaKolicina") int krajnjaKolicina) {
		List<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAllByKolicinaBetween(pocetnaKolicina,
				krajnjaKolicina);

		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkaFaktures) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/osnovica/{pocetnaOsnovica}/{krajnjaOsnovica}")
	public ResponseEntity<List<StavkaFaktureDTO>> getStavkeFaktureBetweenOsnovice(
			@PathVariable("pocetnaOsnovica") Double pocetnaOsnovica,
			@PathVariable("krajnjaOsnovica") Double krajnjaOsnovica) {
		List<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAllByOsnovicaZaPDVBetween(pocetnaOsnovica,
				krajnjaOsnovica);

		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkaFaktures) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pdvProcenat/{pocetniProcenatPdv}/{krajnjiProcenatPdv}")
	public ResponseEntity<List<StavkaFaktureDTO>> getStavkeFaktureBetweenPdvsProcenti(
			@PathVariable("pocetniProcenatPdv") Double pocetniProcenatPdv,
			@PathVariable("krajnjiProcenatPdv") Double krajnjiProcenatPdv) {
		List<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAllByProcenatPDVBetween(pocetniProcenatPdv,
				krajnjiProcenatPdv);
		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkaFaktures) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/rabat/{pocetniRabat}/{krajnjiRabat}")
	public ResponseEntity<List<StavkaFaktureDTO>> getStavkeFaktureBetweenRabats(
			@PathVariable("pocetniRabat") Double pocetniRabat, @PathVariable("krajnjiRabat") Double krajnjiRabat) {
		List<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAllByRabatBetween(pocetniRabat, krajnjiRabat);
		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkaFaktures) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/iznosStavke/{pocetniIznosStavke}/{krajnjiIznosStavke}")
	public ResponseEntity<List<StavkaFaktureDTO>> getStavkeFaktureBetweenIznosi(
			@PathVariable("pocetniIznosStavke") Double pocetniIznosStavke,
			@PathVariable("krajnjiIznosStavke") Double krajnjiIznosStavke) {
		List<StavkaFakture> stavkaFaktures = stavkaFaktureService.findAllByIznosStavkeBetween(pocetniIznosStavke,
				krajnjiIznosStavke);

		List<StavkaFaktureDTO> stavkaFaktureDTOS = new ArrayList<StavkaFaktureDTO>();
		for (StavkaFakture sf : stavkaFaktures) {
			stavkaFaktureDTOS.add(new StavkaFaktureDTO(sf));
		}
		return new ResponseEntity<List<StavkaFaktureDTO>>(stavkaFaktureDTOS, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<?> saveItem(@Validated @RequestBody StavkaFaktureDTO stavkaFaktureDTO, Errors errors) {
		if(errors.hasErrors()) {
			Console.println("SVE GRESKE U STRING" + errors.getAllErrors().toString());
	
			return new ResponseEntity(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		
		StavkaFakture sf = new StavkaFakture();
		sf.setKolicina(stavkaFaktureDTO.getKolicina());

		float rabatPromenljiva = 0;
		if (stavkaFaktureDTO.getKolicina() >= 50) {
			rabatPromenljiva = 10;
		} else {
			rabatPromenljiva = 0;
		}

		sf.setRabat(rabatPromenljiva);
		double cenaBezPDVa = 0;
		double cenaSaPdvom = 0;
		double iznosPdva = 0;
		if (stavkaFaktureDTO.getRoba() != null && stavkaFaktureDTO.getRoba().getId() != 0) {
			Roba roba1 = robaService.findOne(stavkaFaktureDTO.getRoba().getId());
			sf.setRoba(roba1);
			StavkaCenovnika sc = stavkaCenovnikaService.findByRobaId(roba1.getId());
			double jedinicnaCena = sc.getCena();
			sf.setJedinicnaCena(jedinicnaCena);

			List<StopaPDV> stopePdva = stopaPdvService.findAll();
			ArrayList<Integer> listaBrojeva = new ArrayList<>();
			for (StopaPDV stopaPdv : stopePdva) {
				Console.println("datum vazenja"+stopaPdv.getDatumVazenja());
				Console.println("substring godine"+stopaPdv.getDatumVazenja().substring(6, 10));
				
				String godina = stopaPdv.getDatumVazenja().substring(6, 10);
				String mjesec = stopaPdv.getDatumVazenja().substring(3, 5);
				String dan = stopaPdv.getDatumVazenja().substring(0, 2);
				String potrebandatum = godina + mjesec + dan;
				
				Console.println("potreban datum"+potrebandatum);

				int potrebandatuminteger = Integer.parseInt(potrebandatum);
				
				listaBrojeva.add(potrebandatuminteger);
				
				Console.println("lista brojeva"+listaBrojeva);

				// 01-10-2018
			}
			int najveciBroj = Collections.max(listaBrojeva);
			String najveciBrojString = String.valueOf(najveciBroj);
			// 20181001
			String godina1 = najveciBrojString.substring(0, 4);
			String mjesec1 = najveciBrojString.substring(4, 6);
			String dan1 = najveciBrojString.substring(6, 8);
			String potrebandatum1 = dan1 + "-" + mjesec1 + "-" + godina1;
			// 01-10-2018
			StopaPDV stopaPdv = stopaPdvService.findByDatumVazenja(potrebandatum1);

			sf.setProcenatPDV(stopaPdv.getProcenat());

			if (stavkaFaktureDTO.getKolicina() >= 50) {

				cenaBezPDVa = 0;
				cenaSaPdvom = 0;
				iznosPdva = 0;

				cenaBezPDVa = stavkaFaktureDTO.getKolicina() * jedinicnaCena
						- stavkaFaktureDTO.getKolicina() * jedinicnaCena * rabatPromenljiva / 100;
				cenaSaPdvom = cenaBezPDVa + cenaBezPDVa * stopaPdv.getProcenat() / 100;
				iznosPdva = cenaSaPdvom - cenaBezPDVa;

				sf.setOsnovicaZaPDV(cenaBezPDVa);
				sf.setIznosPDV(iznosPdva);
				sf.setIznosStavke(cenaSaPdvom);
				sf.setProcenatPDV(stavkaFaktureDTO.getProcenatPDV());
			} else {

				cenaBezPDVa = 0;
				cenaSaPdvom = 0;
				iznosPdva = 0;

				cenaBezPDVa = stavkaFaktureDTO.getKolicina() * jedinicnaCena;
				cenaSaPdvom = cenaBezPDVa + cenaBezPDVa * stopaPdv.getProcenat() / 100;
				iznosPdva = cenaSaPdvom - cenaBezPDVa;

				sf.setOsnovicaZaPDV(cenaBezPDVa);
				sf.setIznosPDV(iznosPdva);
				sf.setIznosStavke(cenaSaPdvom);
				sf.setProcenatPDV(stavkaFaktureDTO.getProcenatPDV());
			}

		}

		if (stavkaFaktureDTO.getFaktura() != null && stavkaFaktureDTO.getFaktura().getId() != 0) {
			Faktura faktura = fakturaService.findOne(stavkaFaktureDTO.getFaktura().getId());
			sf.setFaktura(faktura);
			double staraosnovica = faktura.getOsnovica();
			double stariiznospdva = faktura.getUkupanPDV();
			double stariiznoszaplacanje = faktura.getIznosZaPlacanje();
			faktura.setOsnovica(staraosnovica + cenaBezPDVa);
			faktura.setUkupanPDV(stariiznospdva + iznosPdva);
			faktura.setIznosZaPlacanje(stariiznoszaplacanje + cenaSaPdvom);
			fakturaService.save(faktura);
		}
		List<StopaPDV> stopePdva = stopaPdvService.findAll();
		ArrayList<Integer> listaBrojeva = new ArrayList<>();
		for (StopaPDV stopaPdv : stopePdva) {

			String godina = stopaPdv.getDatumVazenja().substring(6, 10);
			String mjesec = stopaPdv.getDatumVazenja().substring(3, 5);
			String dan = stopaPdv.getDatumVazenja().substring(0, 2);
			String potrebandatum = godina + mjesec + dan;
			int potrebandatuminteger = Integer.parseInt(potrebandatum);
			listaBrojeva.add(potrebandatuminteger);
			// 01-10-2018
		}
		int najveciBroj = Collections.max(listaBrojeva);
		String najveciBrojString = String.valueOf(najveciBroj);
		// 20181001
		String godina1 = najveciBrojString.substring(0, 4);
		String mjesec1 = najveciBrojString.substring(4, 6);
		String dan1 = najveciBrojString.substring(6, 8);
		String potrebandatum1 = dan1 + "-" + mjesec1 + "-" + godina1;
		// 01-10-2018
		StopaPDV stopaPdv = stopaPdvService.findByDatumVazenja(potrebandatum1);

		sf.setProcenatPDV(stopaPdv.getProcenat());

		sf = stavkaFaktureService.save(sf);
		return new ResponseEntity<StavkaFaktureDTO>(new StavkaFaktureDTO(sf), HttpStatus.CREATED);
		
	}
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<StavkaFaktureDTO> updateItem(@Validated @RequestBody StavkaFaktureDTO stavkaFaktureDTO,
			@PathVariable("id") long id) {
		StavkaFakture sf = stavkaFaktureService.findOne(id);

		Faktura faktura1 = sf.getFaktura();

		double staraosnovica = faktura1.getOsnovica();
		double stariiznospdva = faktura1.getUkupanPDV();
		double stariiznoszaplacanje = faktura1.getIznosZaPlacanje();
		double osnovicastavke = sf.getOsnovicaZaPDV();
		double iznospdva = sf.getIznosPDV();
		double iznoszaplacanje = sf.getIznosStavke();

		faktura1.setOsnovica(staraosnovica - osnovicastavke);
		faktura1.setUkupanPDV(stariiznospdva - iznospdva);
		faktura1.setIznosZaPlacanje(stariiznoszaplacanje - iznoszaplacanje);

		fakturaService.save(faktura1);

		;
		float rabatPromenljiva = 0;
		if (stavkaFaktureDTO.getKolicina() >= 50) {
			rabatPromenljiva = 10;
		} else {
			rabatPromenljiva = 0;
		}

		sf.setRabat(rabatPromenljiva);
		double cenaBezPDVa = 0;
		double cenaSaPdvom = 0;
		double iznosPdva = 0;
		if (stavkaFaktureDTO.getRoba() != null && stavkaFaktureDTO.getRoba().getId() != 0) {
			Roba roba1 = robaService.findOne(stavkaFaktureDTO.getRoba().getId());
			sf.setRoba(roba1);
			StavkaCenovnika sc = stavkaCenovnikaService.findByRobaId(roba1.getId());
			double jedinicnaCena = sc.getCena();
			sf.setJedinicnaCena(jedinicnaCena);

			List<StopaPDV> stopePdva = stopaPdvService.findAll();
			ArrayList<Integer> listaBrojeva = new ArrayList<>();
			for (StopaPDV stopaPdv : stopePdva) {

				String godina = stopaPdv.getDatumVazenja().substring(6, 10);
				String mjesec = stopaPdv.getDatumVazenja().substring(3, 5);
				String dan = stopaPdv.getDatumVazenja().substring(0, 2);
				String potrebandatum = godina + mjesec + dan;
				int potrebandatuminteger = Integer.parseInt(potrebandatum);
				listaBrojeva.add(potrebandatuminteger);
				// 01-10-2018
			}
			int najveciBroj = Collections.max(listaBrojeva);
			String najveciBrojString = String.valueOf(najveciBroj);
			// 20181001
			String godina1 = najveciBrojString.substring(0, 4);
			String mjesec1 = najveciBrojString.substring(4, 6);
			String dan1 = najveciBrojString.substring(6, 8);
			String potrebandatum1 = dan1 + "-" + mjesec1 + "-" + godina1;
			// 01-10-2018
			StopaPDV stopaPdv = stopaPdvService.findByDatumVazenja(potrebandatum1);

			sf.setProcenatPDV(stopaPdv.getProcenat());

			if (stavkaFaktureDTO.getKolicina() >= 50) {

				cenaBezPDVa = 0;
				cenaSaPdvom = 0;
				iznosPdva = 0;

				cenaBezPDVa = stavkaFaktureDTO.getKolicina() * jedinicnaCena
						- stavkaFaktureDTO.getKolicina() * jedinicnaCena * rabatPromenljiva / 100;
				cenaSaPdvom = cenaBezPDVa + cenaBezPDVa * stopaPdv.getProcenat() / 100;
				iznosPdva = cenaSaPdvom - cenaBezPDVa;

				sf.setOsnovicaZaPDV(cenaBezPDVa);
				sf.setIznosPDV(iznosPdva);
				sf.setIznosStavke(cenaSaPdvom);
				sf.setProcenatPDV(stavkaFaktureDTO.getProcenatPDV());
			} else {

				cenaBezPDVa = 0;
				cenaSaPdvom = 0;
				iznosPdva = 0;

				cenaBezPDVa = stavkaFaktureDTO.getKolicina() * jedinicnaCena;
				cenaSaPdvom = cenaBezPDVa + cenaBezPDVa * stopaPdv.getProcenat() / 100;
				iznosPdva = cenaSaPdvom - cenaBezPDVa;

				sf.setOsnovicaZaPDV(cenaBezPDVa);
				sf.setIznosPDV(iznosPdva);
				sf.setIznosStavke(cenaSaPdvom);
				sf.setProcenatPDV(stavkaFaktureDTO.getProcenatPDV());
			}

		}

		if (stavkaFaktureDTO.getFaktura() != null && stavkaFaktureDTO.getFaktura().getId() != 0) {
			Faktura faktura2 = fakturaService.findOne(stavkaFaktureDTO.getFaktura().getId());
			sf.setFaktura(faktura2);
			double staraosnovica2 = faktura2.getOsnovica();
			double stariiznospdva2 = faktura2.getUkupanPDV();
			double stariiznoszaplacanje2 = faktura2.getIznosZaPlacanje();
			faktura2.setOsnovica(staraosnovica2 + cenaBezPDVa);
			faktura2.setUkupanPDV(stariiznospdva2 + iznosPdva);
			faktura2.setIznosZaPlacanje(stariiznoszaplacanje2 + cenaSaPdvom);
			fakturaService.save(faktura2);
		}
		List<StopaPDV> stopePdva = stopaPdvService.findAll();
		ArrayList<Integer> listaBrojeva = new ArrayList<>();
		for (StopaPDV stopaPdv : stopePdva) {

			String godina = stopaPdv.getDatumVazenja().substring(6, 10);
			String mjesec = stopaPdv.getDatumVazenja().substring(3, 5);
			String dan = stopaPdv.getDatumVazenja().substring(0, 2);
			String potrebandatum = godina + mjesec + dan;
			int potrebandatuminteger = Integer.parseInt(potrebandatum);
			listaBrojeva.add(potrebandatuminteger);
			// 01-10-2018
		}
		int najveciBroj = Collections.max(listaBrojeva);
		String najveciBrojString = String.valueOf(najveciBroj);
		// 20181001
		String godina1 = najveciBrojString.substring(0, 4);
		String mjesec1 = najveciBrojString.substring(4, 6);
		String dan1 = najveciBrojString.substring(6, 8);
		String potrebandatum1 = dan1 + "-" + mjesec1 + "-" + godina1;
		// 01-10-2018
		StopaPDV stopaPdv = stopaPdvService.findByDatumVazenja(potrebandatum1);

		sf.setProcenatPDV(stopaPdv.getProcenat());

		sf = stavkaFaktureService.save(sf);
		;

		sf.setKolicina(stavkaFaktureDTO.getKolicina());

		if (stavkaFaktureDTO.getRoba() != null && stavkaFaktureDTO.getRoba().getId() != 0) {
			Roba roba = robaService.findOne(stavkaFaktureDTO.getRoba().getId());
			sf.setRoba(roba);
		}

		if (stavkaFaktureDTO.getFaktura() != null && stavkaFaktureDTO.getFaktura().getId() != 0) {
			Faktura faktura = fakturaService.findOne(stavkaFaktureDTO.getFaktura().getId());
			sf.setFaktura(faktura);
		}

		sf = stavkaFaktureService.save(sf);

		return new ResponseEntity<StavkaFaktureDTO>(new StavkaFaktureDTO(sf), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		StavkaFakture sf = stavkaFaktureService.findOne(id);
		Faktura faktura = sf.getFaktura();
		double staraosnovica = faktura.getOsnovica();
		double stariiznospdva = faktura.getUkupanPDV();
		double stariiznoszaplacanje = faktura.getIznosZaPlacanje();
		double osnovicastavke = sf.getOsnovicaZaPDV();
		double iznospdva = sf.getIznosPDV();
		double iznoszaplacanje = sf.getIznosStavke();
		faktura.setOsnovica(staraosnovica - osnovicastavke);
		faktura.setUkupanPDV(stariiznospdva - iznospdva);
		faktura.setIznosZaPlacanje(stariiznoszaplacanje - iznoszaplacanje);
		fakturaService.save(faktura);

		if (sf != null) {
			stavkaFaktureService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}
