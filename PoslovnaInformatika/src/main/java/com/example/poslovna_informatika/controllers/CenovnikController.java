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
import org.springframework.data.domain.Pageable;
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

import com.example.poslovna_informatika.dto.CenovnikDTO;
import com.example.poslovna_informatika.model.Cenovnik;
import com.example.poslovna_informatika.model.Preduzece;
import com.example.poslovna_informatika.services.CenovnikService;
import com.example.poslovna_informatika.services.PreduzeceService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import scala.Console;

@RestController
@RequestMapping(value = "api/cenovnik")
public class CenovnikController {

	private final CenovnikService cenovnikService;
	private final PreduzeceService preduzeceService;

	@Autowired
	public CenovnikController(CenovnikService cenovnikService, PreduzeceService preduzeceService) {
		this.cenovnikService = cenovnikService;
		this.preduzeceService = preduzeceService;
	}

	@GetMapping(value = "/cenovnikreport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/CenovnikReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=cenovnikreport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<CenovnikDTO>> getCenovnici() {
		List<Cenovnik> cenovnici = cenovnikService.findAll();
		List<CenovnikDTO> cenovnikDTOS = new ArrayList<CenovnikDTO>();
		for (Cenovnik c : cenovnici) {
			cenovnikDTOS.add(new CenovnikDTO(c));
		}
		return new ResponseEntity<List<CenovnikDTO>>(cenovnikDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<CenovnikDTO>> getCenovniciPageable(@RequestParam("page") int page) {
		Page<Cenovnik> cenovnici = cenovnikService.findAll(new PageRequest(page, 3));
		List<Cenovnik> cenovniciList = cenovnici.getContent();
		List<CenovnikDTO> cenovnikDTOS = new ArrayList<CenovnikDTO>();
		for (Cenovnik c : cenovniciList) {
			cenovnikDTOS.add(new CenovnikDTO(c));
		}
		return new ResponseEntity<List<CenovnikDTO>>(cenovnikDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CenovnikDTO> getCenovnik(@PathVariable("id") Long id) {
		Cenovnik cenovnik = cenovnikService.findOne(id);
		if (cenovnik == null) {
			return new ResponseEntity<CenovnikDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<CenovnikDTO>(new CenovnikDTO(cenovnik), HttpStatus.OK);
	}

	@GetMapping(value = "/preduzece/{preduzeceId}")
	public ResponseEntity<List<CenovnikDTO>> getCenovikByPreduzeceId(@PathVariable("preduzeceId") Long preduzeceId) {
		List<Cenovnik> cenovnici = cenovnikService.findAllByPreduzeceId(preduzeceId);
		List<CenovnikDTO> cenovnikDTOS = new ArrayList<CenovnikDTO>();
		for (Cenovnik c : cenovnici) {
			cenovnikDTOS.add(new CenovnikDTO(c));
		}
		return new ResponseEntity<List<CenovnikDTO>>(cenovnikDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/datumVazenja/{pocetniDatum}/{krajnjiDatum}")
	public ResponseEntity<List<CenovnikDTO>> getCenovnikBetweenDatumiVazenja(
			@PathVariable("pocetniDatum") String pocetniDatum, @PathVariable("krajnjiDatum") String krajnjiDatum) {
		List<Cenovnik> cenovnici = cenovnikService.findAllByDatumVazenjaBetween(pocetniDatum, krajnjiDatum);

		List<CenovnikDTO> cenovnikDTOS = new ArrayList<CenovnikDTO>();
		for (Cenovnik c : cenovnici) {
			cenovnikDTOS.add(new CenovnikDTO(c));
		}
		return new ResponseEntity<List<CenovnikDTO>>(cenovnikDTOS, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<CenovnikDTO> saveItem(@Validated @RequestBody CenovnikDTO cenovnikDTO, Errors errors) {
		if(errors.hasErrors()) {
			Console.println("SVE GRESKE U STRING" + errors.getAllErrors().toString());
	
			return new ResponseEntity(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Cenovnik c = new Cenovnik();
		c.setDatumVazenja(cenovnikDTO.getDatumVazenja());

		if (cenovnikDTO.getPreduzece() != null && cenovnikDTO.getPreduzece().getId() != 0) {
			Preduzece p = preduzeceService.findOne(cenovnikDTO.getPreduzece().getId());
			c.setPreduzece(p);

		}

		c = cenovnikService.save(c);
		return new ResponseEntity<CenovnikDTO>(new CenovnikDTO(c), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<CenovnikDTO> updateItem(@RequestBody CenovnikDTO cenovnikDTO, @PathVariable("id") long id) {
		Cenovnik c = cenovnikService.findOne(id);

		if (c == null) {
			return new ResponseEntity<CenovnikDTO>(HttpStatus.BAD_REQUEST);
		}

		c.setDatumVazenja(cenovnikDTO.getDatumVazenja());

		if (cenovnikDTO.getPreduzece() != null && cenovnikDTO.getPreduzece().getId() != 0) {
			Preduzece p = preduzeceService.findOne(cenovnikDTO.getPreduzece().getId());
			c.setPreduzece(p);
		}

		c = cenovnikService.save(c);

		return new ResponseEntity<CenovnikDTO>(new CenovnikDTO(c), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		Cenovnik c = cenovnikService.findOne(id);
		if (c != null) {
			cenovnikService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}
