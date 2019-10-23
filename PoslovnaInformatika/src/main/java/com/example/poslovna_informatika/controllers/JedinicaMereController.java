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

import com.example.poslovna_informatika.dto.JedinicaMereDTO;
import com.example.poslovna_informatika.model.JedinicaMere;
import com.example.poslovna_informatika.services.JedinicaMereService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import scala.Console;

@RestController
@RequestMapping(value = "api/jedinica-mere")
public class JedinicaMereController {

	private JedinicaMereService jedinicaMereService;

	@Autowired
	public JedinicaMereController(JedinicaMereService jedinicaMereService) {
		this.jedinicaMereService = jedinicaMereService;
	}

	@GetMapping(value = "/jedinicamerereport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/JedinicaMereReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=jedinicamerereport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<JedinicaMereDTO>> getJediniceMere() {
		List<JedinicaMere> jediniceMera = jedinicaMereService.findAll();
		List<JedinicaMereDTO> jedinicaMereDTOS = new ArrayList<JedinicaMereDTO>();
		for (JedinicaMere jm : jediniceMera) {
			jedinicaMereDTOS.add(new JedinicaMereDTO(jm));
		}
		return new ResponseEntity<List<JedinicaMereDTO>>(jedinicaMereDTOS, HttpStatus.OK);
	}
	
	@GetMapping(value = "/pageable")
	public ResponseEntity<List<JedinicaMereDTO>> getJediniceMerePageable(@RequestParam("page") int page) {
		Page<JedinicaMere> jediniceMera = jedinicaMereService.findAll(new PageRequest(page, 3));
		List<JedinicaMere> jedinicaMeraList = jediniceMera.getContent();
		List<JedinicaMereDTO> jedinicaMereDTOS = new ArrayList<JedinicaMereDTO>();
		for (JedinicaMere jm : jedinicaMeraList) {
			jedinicaMereDTOS.add(new JedinicaMereDTO(jm));
		}
		return new ResponseEntity<List<JedinicaMereDTO>>(jedinicaMereDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<JedinicaMereDTO> getJedinicaMere(@PathVariable("id") Long id) {
		JedinicaMere jedinicaMere = jedinicaMereService.findOne(id);
		if (jedinicaMere == null) {
			return new ResponseEntity<JedinicaMereDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<JedinicaMereDTO>(new JedinicaMereDTO(jedinicaMere), HttpStatus.OK);
	}

	@GetMapping(value = "naziv/{naziv}")
	public ResponseEntity<JedinicaMereDTO> getJedinicaMere(@PathVariable("naziv") String naziv) {
		JedinicaMere jedinicaMere = jedinicaMereService.findByNaziv(naziv);
		if (jedinicaMere == null) {
			return new ResponseEntity<JedinicaMereDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<JedinicaMereDTO>(new JedinicaMereDTO(jedinicaMere), HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<JedinicaMereDTO> saveItem(@Validated @RequestBody JedinicaMereDTO jedinicaMereDTO, Errors errors) {
		if(errors.hasErrors()) {
			Console.println("SVE GRESKE U STRING" + errors.getAllErrors().toString());
	
			return new ResponseEntity(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		JedinicaMere jm = new JedinicaMere();
		jm.setNaziv(jedinicaMereDTO.getNaziv());
		jedinicaMereService.save(jm);
		return new ResponseEntity<JedinicaMereDTO>(new JedinicaMereDTO(jm), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<JedinicaMereDTO> updateItem(@RequestBody JedinicaMereDTO jedinicaMereDTO,
			@PathVariable("id") long id) {
		JedinicaMere jm = jedinicaMereService.findOne(id);

		if (jm == null) {
			return new ResponseEntity<JedinicaMereDTO>(HttpStatus.BAD_REQUEST);
		}

		jm.setNaziv(jedinicaMereDTO.getNaziv());

		jm = jedinicaMereService.save(jm);

		return new ResponseEntity<JedinicaMereDTO>(new JedinicaMereDTO(jm), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
		JedinicaMere jm = jedinicaMereService.findOne(id);
		if (jm != null) {
			jedinicaMereService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}
