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

import com.example.poslovna_informatika.dto.PoslovnaGodinaDTO;
import com.example.poslovna_informatika.model.PoslovnaGodina;
import com.example.poslovna_informatika.services.PoslovnaGodinaService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping(value = "api/poslovna-godina")
public class PoslovnaGodinaController {

	private PoslovnaGodinaService poslovnaGodinaService;

	@Autowired
	public PoslovnaGodinaController(PoslovnaGodinaService poslovnaGodinaService) {
		this.poslovnaGodinaService = poslovnaGodinaService;
	}

	@GetMapping(value = "/poslovnagodinareport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/PoslovnaGodinaReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=poslovnagodinareport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<PoslovnaGodinaDTO>> getPoslGodine() {
		List<PoslovnaGodina> poslovnaGodinas = poslovnaGodinaService.findAll();
		List<PoslovnaGodinaDTO> poslovnaGodinaDTOS = new ArrayList<PoslovnaGodinaDTO>();
		for (PoslovnaGodina pg : poslovnaGodinas) {
			poslovnaGodinaDTOS.add(new PoslovnaGodinaDTO(pg));
		}
		return new ResponseEntity<List<PoslovnaGodinaDTO>>(poslovnaGodinaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<PoslovnaGodinaDTO>> getPoslovneGodinePageable(@RequestParam("page") int page) {
		Page<PoslovnaGodina> poslovnaGodinas = poslovnaGodinaService.findAll(new PageRequest(page, 3));
		List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinas.getContent();
		List<PoslovnaGodinaDTO> poslovnaGodinaDTOS = new ArrayList<PoslovnaGodinaDTO>();
		for (PoslovnaGodina pg : poslovnaGodinaList) {
			poslovnaGodinaDTOS.add(new PoslovnaGodinaDTO(pg));
		}
		return new ResponseEntity<List<PoslovnaGodinaDTO>>(poslovnaGodinaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<PoslovnaGodinaDTO> getPoslovnaGodina(@PathVariable("id") Long id) {
		PoslovnaGodina poslovnaGodina = poslovnaGodinaService.findOne(id);
		if (poslovnaGodina == null) {
			return new ResponseEntity<PoslovnaGodinaDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<PoslovnaGodinaDTO>(new PoslovnaGodinaDTO(poslovnaGodina), HttpStatus.OK);
	}

	@GetMapping(value = "zakljucena/{zakljucena}")
	public ResponseEntity<List<PoslovnaGodinaDTO>> getPoslGodinebyZakljucena(
			@PathVariable("zakljucena") boolean zakljucena) {
		List<PoslovnaGodina> poslovnaGodinas = poslovnaGodinaService.findAllByZakljucena(zakljucena);

		List<PoslovnaGodinaDTO> poslovnaGodinaDTOS = new ArrayList<PoslovnaGodinaDTO>();
		for (PoslovnaGodina pg : poslovnaGodinas) {
			poslovnaGodinaDTOS.add(new PoslovnaGodinaDTO(pg));
		}
		return new ResponseEntity<List<PoslovnaGodinaDTO>>(poslovnaGodinaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "godina/{pocetnaGodina}/{krajnjaGodina}")
	public ResponseEntity<List<PoslovnaGodinaDTO>> getPoslGodinebetweenGodine(
			@PathVariable("pocetnaGodina") int pocetnaGodina, @PathVariable("krajnjaGodina") int krajnjaGodina) {
		List<PoslovnaGodina> poslovnaGodinas = poslovnaGodinaService.findAllByGodinaBetween(pocetnaGodina,
				krajnjaGodina);
		List<PoslovnaGodinaDTO> poslovnaGodinaDTOS = new ArrayList<PoslovnaGodinaDTO>();
		for (PoslovnaGodina pg : poslovnaGodinas) {
			poslovnaGodinaDTOS.add(new PoslovnaGodinaDTO(pg));
		}
		return new ResponseEntity<List<PoslovnaGodinaDTO>>(poslovnaGodinaDTOS, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<PoslovnaGodinaDTO> saveItem(@RequestBody PoslovnaGodinaDTO poslovnaGodinaDTO) {
		PoslovnaGodina pg = new PoslovnaGodina();
		pg.setGodina(poslovnaGodinaDTO.getGodina());
		pg.setZakljucena(poslovnaGodinaDTO.isZakljucena());

		pg = poslovnaGodinaService.save(pg);
		return new ResponseEntity<PoslovnaGodinaDTO>(new PoslovnaGodinaDTO(pg), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<PoslovnaGodinaDTO> updateItem(@RequestBody PoslovnaGodinaDTO poslovnaGodinaDTO,
			@PathVariable("id") long id) {
		PoslovnaGodina pg = poslovnaGodinaService.findOne(id);

		if (pg == null) {
			return new ResponseEntity<PoslovnaGodinaDTO>(HttpStatus.BAD_REQUEST);
		}
		pg.setGodina(poslovnaGodinaDTO.getGodina());
		pg.setZakljucena(poslovnaGodinaDTO.isZakljucena());

		pg = poslovnaGodinaService.save(pg);

		return new ResponseEntity<PoslovnaGodinaDTO>(new PoslovnaGodinaDTO(pg), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		PoslovnaGodina pg = poslovnaGodinaService.findOne(id);
		if (pg != null) {
			poslovnaGodinaService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}