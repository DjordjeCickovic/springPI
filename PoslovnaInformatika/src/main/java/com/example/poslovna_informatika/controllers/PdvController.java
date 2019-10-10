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

import com.example.poslovna_informatika.dto.PdvDTO;
import com.example.poslovna_informatika.model.PDV;
import com.example.poslovna_informatika.services.PdvService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping(value = "api/pdv")
public class PdvController {

	private PdvService pdvService;

	@Autowired
	public PdvController(PdvService pdvService) {
		this.pdvService = pdvService;
	}

	@GetMapping(value = "/pdvreport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(getClass().getResource("/reports/PDVReport.jasper").openStream(),
				null, DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=pdvreport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<PdvDTO>> getPdvs() {
		List<PDV> pdvs = pdvService.findAll();
		List<PdvDTO> pdvDTOS = new ArrayList<PdvDTO>();
		for (PDV pdv : pdvs) {
			pdvDTOS.add(new PdvDTO(pdv));
		}
		return new ResponseEntity<List<PdvDTO>>(pdvDTOS, HttpStatus.OK);
	}
	
	@GetMapping(value = "/pageable")
	public ResponseEntity<List<PdvDTO>> getPdvs(@RequestParam("page") int page) {
		Page<PDV> pdvs = pdvService.findAll(new PageRequest(page, 3));
		List<PDV> pdvList = pdvs.getContent();
		List<PdvDTO> pdvDTOS = new ArrayList<PdvDTO>();
		for (PDV pdv : pdvList) {
			pdvDTOS.add(new PdvDTO(pdv));
		}
		return new ResponseEntity<List<PdvDTO>>(pdvDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<PdvDTO> getStopaPdv(@PathVariable("id") Long id) {
		PDV pdv = pdvService.findOne(id);
		if (pdv == null) {
			return new ResponseEntity<PdvDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<PdvDTO>(new PdvDTO(pdv), HttpStatus.OK);
	}

	@GetMapping(value = "/naziv/{naziv}")
	public ResponseEntity<PdvDTO> getStopaPdvByNaziv(@PathVariable("naziv") String naziv) {
		PDV pdv = pdvService.findByNaziv(naziv);
		if (pdv == null) {
			return new ResponseEntity<PdvDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<PdvDTO>(new PdvDTO(pdv), HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<PdvDTO> saveItem(@RequestBody PdvDTO pdvDTO) {
		PDV pdv = new PDV();
		pdv.setNaziv(pdvDTO.getNaziv());

		pdv = pdvService.save(pdv);

		return new ResponseEntity<PdvDTO>(new PdvDTO(pdv), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<PdvDTO> updateItem(@RequestBody PdvDTO pdvDTO, @PathVariable("id") long id) {
		PDV pdv = pdvService.findOne(id);

		if (pdv == null) {
			return new ResponseEntity<PdvDTO>(HttpStatus.BAD_REQUEST);
		}
		pdv.setNaziv(pdvDTO.getNaziv());

		pdv = pdvService.save(pdv);

		return new ResponseEntity<PdvDTO>(new PdvDTO(pdv), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		PDV pdv = pdvService.findOne(id);
		if (pdv != null) {
			pdvService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}
