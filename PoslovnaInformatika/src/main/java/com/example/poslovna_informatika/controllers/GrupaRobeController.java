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

import com.example.poslovna_informatika.dto.GrupaRobeDTO;
import com.example.poslovna_informatika.model.Cenovnik;
import com.example.poslovna_informatika.model.GrupaRobe;
import com.example.poslovna_informatika.model.PDV;
import com.example.poslovna_informatika.model.Preduzece;
import com.example.poslovna_informatika.services.GrupaRobeService;
import com.example.poslovna_informatika.services.PdvService;
import com.example.poslovna_informatika.services.PreduzeceService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping(value = "api/grupa-robe")
public class GrupaRobeController {

	private GrupaRobeService grupaRobeService;
	private PdvService pdvService;
	private PreduzeceService preduzeceService;

	@Autowired
	public GrupaRobeController(GrupaRobeService grupaRobeService, PdvService pdvService,
			PreduzeceService preduzeceService) {
		this.grupaRobeService = grupaRobeService;
		this.pdvService = pdvService;
		this.preduzeceService = preduzeceService;
	}

	@GetMapping(value = "/gruparobereport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/GrupaRobeReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=gruparobereport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<GrupaRobeDTO>> getGrupaRobe() {
		List<GrupaRobe> grupaRobe = grupaRobeService.findAll();
		List<GrupaRobeDTO> grupaRobeDTOS = new ArrayList<GrupaRobeDTO>();
		for (GrupaRobe gr : grupaRobe) {
			grupaRobeDTOS.add(new GrupaRobeDTO(gr));
		}
		return new ResponseEntity<List<GrupaRobeDTO>>(grupaRobeDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<GrupaRobeDTO>> getGrupaRobe(@RequestParam("page") int page) {
		Page<GrupaRobe> grupaRobe = grupaRobeService.findAll(new PageRequest(page, 3));
		List<GrupaRobe> grupaRobeList = grupaRobe.getContent();
		List<GrupaRobeDTO> grupaRobeDTOS = new ArrayList<GrupaRobeDTO>();
		for (GrupaRobe gr : grupaRobeList) {
			grupaRobeDTOS.add(new GrupaRobeDTO(gr));
		}
		return new ResponseEntity<List<GrupaRobeDTO>>(grupaRobeDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<GrupaRobeDTO> getGrupaRobe(@PathVariable("id") Long id) {
		GrupaRobe grupaRobe = grupaRobeService.findOne(id);
		if (grupaRobe == null) {
			return new ResponseEntity<GrupaRobeDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<GrupaRobeDTO>(new GrupaRobeDTO(grupaRobe), HttpStatus.OK);
	}

	@GetMapping(value = "naziv/{naziv}")
	public ResponseEntity<GrupaRobeDTO> getGrupaRobeNaziv(@PathVariable("naziv") String naziv) {
		GrupaRobe grupaRobe = grupaRobeService.findByNaziv(naziv);
		if (grupaRobe == null) {
			return new ResponseEntity<GrupaRobeDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<GrupaRobeDTO>(new GrupaRobeDTO(grupaRobe), HttpStatus.OK);
	}

	@GetMapping(value = "preduzece/{preduzeceId}")
	public ResponseEntity<List<GrupaRobeDTO>> getGrupaRobePreduzece(@PathVariable("preduzeceId") long preduzeceId) {
		List<GrupaRobe> grupaRobe = grupaRobeService.findAllByPreduzeceId(preduzeceId);
		List<GrupaRobeDTO> grupaRobeDTOS = new ArrayList<GrupaRobeDTO>();
		for (GrupaRobe gr : grupaRobe) {
			grupaRobeDTOS.add(new GrupaRobeDTO(gr));
		}
		return new ResponseEntity<List<GrupaRobeDTO>>(grupaRobeDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "pdv/{pdvId}")
	public ResponseEntity<List<GrupaRobeDTO>> getGrupaRobePdv(@PathVariable("pdvId") long pdvId) {
		List<GrupaRobe> grupaRobe = grupaRobeService.findAllByPdvId(pdvId);
		List<GrupaRobeDTO> grupaRobeDTOS = new ArrayList<GrupaRobeDTO>();
		for (GrupaRobe gr : grupaRobe) {
			grupaRobeDTOS.add(new GrupaRobeDTO(gr));
		}
		return new ResponseEntity<List<GrupaRobeDTO>>(grupaRobeDTOS, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<GrupaRobeDTO> saveItem(@RequestBody GrupaRobeDTO grupaRobeDTO) {
		GrupaRobe gr = new GrupaRobe();
		gr.setNaziv(grupaRobeDTO.getNaziv());

		if (grupaRobeDTO.getPdv() != null && grupaRobeDTO.getPdv().getId() != 0) {
			PDV pdv = pdvService.findOne(grupaRobeDTO.getPdv().getId());
			gr.setPdv(pdv);
		}
		if (grupaRobeDTO.getPreduzece() != null && grupaRobeDTO.getPreduzece().getId() != 0) {
			Preduzece preduzece = preduzeceService.findOne(grupaRobeDTO.getPreduzece().getId());
			gr.setPreduzece(preduzece);
		}

		gr = grupaRobeService.save(gr);
		return new ResponseEntity<GrupaRobeDTO>(new GrupaRobeDTO(gr), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<GrupaRobeDTO> updateItem(@RequestBody GrupaRobeDTO grupaRobeDTO,
			@PathVariable("id") long id) {
		GrupaRobe gr = grupaRobeService.findOne(id);

		if (gr == null) {
			return new ResponseEntity<GrupaRobeDTO>(HttpStatus.BAD_REQUEST);
		}

		gr.setNaziv(grupaRobeDTO.getNaziv());

		if (grupaRobeDTO.getPdv() != null && grupaRobeDTO.getPdv().getId() != 0) {
			PDV pdv = pdvService.findOne(grupaRobeDTO.getPdv().getId());
			gr.setPdv(pdv);
		}
		if (grupaRobeDTO.getPreduzece() != null && grupaRobeDTO.getPreduzece().getId() != 0) {
			Preduzece preduzece = preduzeceService.findOne(grupaRobeDTO.getPreduzece().getId());
			gr.setPreduzece(preduzece);
		}

		gr = grupaRobeService.save(gr);
		return new ResponseEntity<GrupaRobeDTO>(new GrupaRobeDTO(gr), HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		GrupaRobe gr = grupaRobeService.findOne(id);
		if (gr != null) {
			grupaRobeService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}
