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

import com.example.poslovna_informatika.dto.RobaDTO;
import com.example.poslovna_informatika.model.GrupaRobe;
import com.example.poslovna_informatika.model.JedinicaMere;
import com.example.poslovna_informatika.model.Roba;
import com.example.poslovna_informatika.services.GrupaRobeService;
import com.example.poslovna_informatika.services.JedinicaMereService;
import com.example.poslovna_informatika.services.RobaService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import scala.Console;

@RestController
@RequestMapping(value = "api/roba")
public class RobaController {

	private RobaService robaService;
	private GrupaRobeService grupaRobeService;
	private JedinicaMereService jedinicaMereService;

	@Autowired
	public RobaController(RobaService robaService, GrupaRobeService grupaRobeService,
			JedinicaMereService jedinicaMereService) {
		this.robaService = robaService;
		this.grupaRobeService = grupaRobeService;
		this.jedinicaMereService = jedinicaMereService;

	}

	@GetMapping(value = "/robareport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(getClass().getResource("/reports/RobaReport.jasper").openStream(),
				null, DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=robareport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<RobaDTO>> getItems() {
		List<Roba> robas = robaService.findAll();
		List<RobaDTO> robaDTOS = new ArrayList<RobaDTO>();
		for (Roba r : robas) {
			robaDTOS.add(new RobaDTO(r));
		}
		return new ResponseEntity<List<RobaDTO>>(robaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<RobaDTO>> getItems(@RequestParam("page") int page) {
		Page<Roba> robas = robaService.findAll(new PageRequest(page, 3));
		List<Roba> robaLista = robas.getContent();
		List<RobaDTO> robaDTOS = new ArrayList<RobaDTO>();
		for (Roba r : robaLista) {
			robaDTOS.add(new RobaDTO(r));
		}
		return new ResponseEntity<List<RobaDTO>>(robaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<RobaDTO> getRoba(@PathVariable("id") Long id) {
		Roba roba = robaService.findOne(id);
		if (roba == null) {
			return new ResponseEntity<RobaDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<RobaDTO>(new RobaDTO(roba), HttpStatus.OK);
	}

	@GetMapping(value = "naziv/{nazivRobe}")
	public ResponseEntity<List<RobaDTO>> getRobaNaziv(@PathVariable("nazivRobe") String nazivRobe) {
		List<Roba> robas = robaService.findAllByNaziv(nazivRobe);
		List<RobaDTO> robaDTOS = new ArrayList<RobaDTO>();
		for (Roba r : robas) {
			robaDTOS.add(new RobaDTO(r));
		}
		return new ResponseEntity<List<RobaDTO>>(robaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "grupaRobe/{grupaRobeId}")
	public ResponseEntity<List<RobaDTO>> getRobaGrupaRobe(@PathVariable("grupaRobeId") long grupaRobeId) {
		List<Roba> robas = robaService.findAllByGrupaRobeId(grupaRobeId);
		List<RobaDTO> robaDTOS = new ArrayList<RobaDTO>();
		for (Roba r : robas) {
			robaDTOS.add(new RobaDTO(r));
		}
		return new ResponseEntity<List<RobaDTO>>(robaDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "jedinicaMere/{jedinicaMereId}")
	public ResponseEntity<List<RobaDTO>> getRobaJedinicaMere(@PathVariable("jedinicaMereId") long jedinicaMereId) {
		List<Roba> robas = robaService.findAllByJediniceMereId(jedinicaMereId);
		List<RobaDTO> robaDTOS = new ArrayList<RobaDTO>();
		for (Roba r : robas) {
			robaDTOS.add(new RobaDTO(r));
		}
		return new ResponseEntity<List<RobaDTO>>(robaDTOS, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<RobaDTO> saveItem(@Validated @RequestBody RobaDTO robaDTO, Errors errors) {
		if(errors.hasErrors()) {
			Console.println("SVE GRESKE U STRING" + errors.getAllErrors().toString());
	
			return new ResponseEntity(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Roba r = new Roba();
		r.setNaziv(robaDTO.getNaziv());

		if (robaDTO.getGrupaRobe() != null && robaDTO.getGrupaRobe().getId() != 0) {
			GrupaRobe gr = grupaRobeService.findOne(robaDTO.getGrupaRobe().getId());
			r.setGrupaRobe(gr);
		}
		if (robaDTO.getJedinicaMere() != null && robaDTO.getJedinicaMere().getId() != 0) {
			JedinicaMere jm = jedinicaMereService.findOne(robaDTO.getJedinicaMere().getId());
			r.setJediniceMere(jm);
		}

		r = robaService.save(r);
		return new ResponseEntity<RobaDTO>(new RobaDTO(r), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<RobaDTO> updateItem(@RequestBody RobaDTO robaDTO, @PathVariable("id") long id) {
		Roba r = robaService.findOne(id);

		if (r == null) {
			return new ResponseEntity<RobaDTO>(HttpStatus.BAD_REQUEST);
		}

		r.setNaziv(robaDTO.getNaziv());

		if (robaDTO.getGrupaRobe() != null && robaDTO.getGrupaRobe().getId() != 0) {
			GrupaRobe gr = grupaRobeService.findOne(robaDTO.getGrupaRobe().getId());
			r.setGrupaRobe(gr);
		}
		if (robaDTO.getJedinicaMere() != null && robaDTO.getJedinicaMere().getId() != 0) {
			JedinicaMere jm = jedinicaMereService.findOne(robaDTO.getJedinicaMere().getId());
			r.setJediniceMere(jm);
		}

		r = robaService.save(r);
		return new ResponseEntity<RobaDTO>(new RobaDTO(r), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		Roba r = robaService.findOne(id);
		if (r != null) {
			robaService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}
