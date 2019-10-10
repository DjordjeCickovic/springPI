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

import com.example.poslovna_informatika.dto.StavkaCenovnikaDTO;
import com.example.poslovna_informatika.model.Cenovnik;
import com.example.poslovna_informatika.model.Roba;
import com.example.poslovna_informatika.model.StavkaCenovnika;
import com.example.poslovna_informatika.services.CenovnikService;
import com.example.poslovna_informatika.services.RobaService;
import com.example.poslovna_informatika.services.StavkaCenovnikaService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping(value = "api/stavka-cenovnika")
public class StavkaCenovnikaController {

	private StavkaCenovnikaService stavkaCenovnikaService;
	private CenovnikService cenovnikService;
	private RobaService robaService;

	@Autowired
	public StavkaCenovnikaController(StavkaCenovnikaService stavkaCenovnikaService, CenovnikService cenovnikService,
			RobaService robaService) {
		this.stavkaCenovnikaService = stavkaCenovnikaService;
		this.cenovnikService = cenovnikService;
		this.robaService = robaService;
	}

	@GetMapping(value = "/stavkacenovnikareport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/StavkaCenovnikaReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=stavkacenovnikareport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<StavkaCenovnikaDTO>> getItems() {
		List<StavkaCenovnika> stavkaCenovnikas = stavkaCenovnikaService.findAll();
		List<StavkaCenovnikaDTO> stavkaCenovnikaDTOS = new ArrayList<StavkaCenovnikaDTO>();
		for (StavkaCenovnika sc : stavkaCenovnikas) {
			stavkaCenovnikaDTOS.add(new StavkaCenovnikaDTO(sc));
		}
		return new ResponseEntity<List<StavkaCenovnikaDTO>>(stavkaCenovnikaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<StavkaCenovnikaDTO>> getItemsPageable(@RequestParam("page") int page) {
		Page<StavkaCenovnika> stavkaCenovnikas = stavkaCenovnikaService.findAll(new PageRequest(page, 3));
		List<StavkaCenovnika> stavkeCenovnikaList = stavkaCenovnikas.getContent();
		List<StavkaCenovnikaDTO> stavkaCenovnikaDTOS = new ArrayList<StavkaCenovnikaDTO>();
		for (StavkaCenovnika sc : stavkeCenovnikaList) {
			stavkaCenovnikaDTOS.add(new StavkaCenovnikaDTO(sc));
		}
		return new ResponseEntity<List<StavkaCenovnikaDTO>>(stavkaCenovnikaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<StavkaCenovnikaDTO> getStavkaCenovnika(@PathVariable("id") Long id) {
		StavkaCenovnika stavkaCenovnika = stavkaCenovnikaService.findOne(id);
		if (stavkaCenovnika == null) {
			return new ResponseEntity<StavkaCenovnikaDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<StavkaCenovnikaDTO>(new StavkaCenovnikaDTO(stavkaCenovnika), HttpStatus.OK);
	}

	@GetMapping(value = "cenovnik/{cenovnikID}")
	public ResponseEntity<List<StavkaCenovnikaDTO>> getStavkaCenovnikaByCenovnikID(
			@PathVariable("cenovnikID") Long cenovnikId) {
		List<StavkaCenovnika> stavkaCenovnikas = stavkaCenovnikaService.findAllByCenovnikId(cenovnikId);
		List<StavkaCenovnikaDTO> stavkaCenovnikaDTOS = new ArrayList<StavkaCenovnikaDTO>();
		for (StavkaCenovnika sc : stavkaCenovnikas) {
			stavkaCenovnikaDTOS.add(new StavkaCenovnikaDTO(sc));
		}
		return new ResponseEntity<List<StavkaCenovnikaDTO>>(stavkaCenovnikaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "roba/{robaID}")
	public ResponseEntity<StavkaCenovnikaDTO> getStavkaCenovnikaByRobaID(@PathVariable("robaID") Long robaId) {
		StavkaCenovnika stavkaCenovnika = stavkaCenovnikaService.findByRobaId(robaId);
		if (stavkaCenovnika == null) {
			return new ResponseEntity<StavkaCenovnikaDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<StavkaCenovnikaDTO>(new StavkaCenovnikaDTO(stavkaCenovnika), HttpStatus.OK);
	}

	@GetMapping(value = "cena/{pocetnaCena}/{krajnjaCena}")
	public ResponseEntity<List<StavkaCenovnikaDTO>> getStavkaCenovnikaBetweenCene(
			@PathVariable("pocetnaCena") Double pocetnaCena, @PathVariable("krajnjaCena") Double krajnjaCena) {
		List<StavkaCenovnika> stavkaCenovnikas = stavkaCenovnikaService.findAllByCenaBetween(pocetnaCena, krajnjaCena);
		List<StavkaCenovnikaDTO> stavkaCenovnikaDTOS = new ArrayList<StavkaCenovnikaDTO>();
		for (StavkaCenovnika sc : stavkaCenovnikas) {
			stavkaCenovnikaDTOS.add(new StavkaCenovnikaDTO(sc));
		}
		return new ResponseEntity<List<StavkaCenovnikaDTO>>(stavkaCenovnikaDTOS, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<StavkaCenovnikaDTO> saveItem(@RequestBody StavkaCenovnikaDTO stavkaCenovnikaDTO) {
		StavkaCenovnika sc = new StavkaCenovnika();
		sc.setCena(stavkaCenovnikaDTO.getCena());

		if (stavkaCenovnikaDTO.getCenovnik() != null && stavkaCenovnikaDTO.getCenovnik().getId() != 0) {
			Cenovnik cenovnik = cenovnikService.findOne(stavkaCenovnikaDTO.getCenovnik().getId());
			sc.setCenovnik(cenovnik);
		}
		if (stavkaCenovnikaDTO.getRoba() != null && stavkaCenovnikaDTO.getRoba().getId() != 0) {
			Roba roba = robaService.findOne(stavkaCenovnikaDTO.getRoba().getId());
			sc.setRoba(roba);
		}

		sc = stavkaCenovnikaService.save(sc);
		return new ResponseEntity<StavkaCenovnikaDTO>(new StavkaCenovnikaDTO(sc), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<StavkaCenovnikaDTO> updateItem(@RequestBody StavkaCenovnikaDTO stavkaCenovnikaDTO,
			@PathVariable("id") long id) {
		StavkaCenovnika sc = stavkaCenovnikaService.findOne(id);

		if (sc == null) {
			return new ResponseEntity<StavkaCenovnikaDTO>(HttpStatus.BAD_REQUEST);
		}

		sc.setCena(stavkaCenovnikaDTO.getCena());

		if (stavkaCenovnikaDTO.getCenovnik() != null && stavkaCenovnikaDTO.getCenovnik().getId() != 0) {
			Cenovnik cenovnik = cenovnikService.findOne(stavkaCenovnikaDTO.getCenovnik().getId());
			sc.setCenovnik(cenovnik);
		}
		if (stavkaCenovnikaDTO.getRoba() != null && stavkaCenovnikaDTO.getRoba().getId() != 0) {
			Roba roba = robaService.findOne(stavkaCenovnikaDTO.getRoba().getId());
			sc.setRoba(roba);
		}

		sc = stavkaCenovnikaService.save(sc);

		return new ResponseEntity<StavkaCenovnikaDTO>(new StavkaCenovnikaDTO(sc), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		StavkaCenovnika sc = stavkaCenovnikaService.findOne(id);
		if (sc != null) {
			stavkaCenovnikaService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}
