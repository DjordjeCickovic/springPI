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

import com.example.poslovna_informatika.dto.MestoDTO;
import com.example.poslovna_informatika.model.Mesto;
import com.example.poslovna_informatika.services.MestoService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping(value = "api/mesto")
public class MestoController {

	private MestoService mestoService;

	@Autowired
	public MestoController(MestoService mestoService) {
		this.mestoService = mestoService;
	}

	@GetMapping(value = "/mestoreport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/MestoReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=mestoreport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<MestoDTO>> getMestos() {
		List<Mesto> mestos = mestoService.findAll();
		List<MestoDTO> mestosDTO = new ArrayList<MestoDTO>();
		for (Mesto u : mestos) {
			mestosDTO.add(new MestoDTO(u));
		}
		return new ResponseEntity<List<MestoDTO>>(mestosDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/pageable")
	public ResponseEntity<List<MestoDTO>> getMestosPagable(@RequestParam("page") int page) {
		Page<Mesto> mestos = mestoService.findAll(new PageRequest(page, 3));
		List<MestoDTO> mestosDTO = new ArrayList<MestoDTO>();
		for (Mesto u : mestos) {
			mestosDTO.add(new MestoDTO(u));
		}
		return new ResponseEntity<List<MestoDTO>>(mestosDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<MestoDTO> getMesto(@PathVariable("id") Long id) {
		Mesto mesto = mestoService.findOne(id);
		if (mesto == null) {
			return new ResponseEntity<MestoDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<MestoDTO>(new MestoDTO(mesto), HttpStatus.OK);
	}

	@GetMapping(value = "grad/{grad}")
	public ResponseEntity<MestoDTO> getMesto(@PathVariable("grad") String grad) {
		Mesto mesto = mestoService.findByGrad(grad);
		if (mesto == null) {
			return new ResponseEntity<MestoDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<MestoDTO>(new MestoDTO(mesto), HttpStatus.OK);
	}

	@GetMapping(value = "/drzava/{drzava}")
	public ResponseEntity<List<MestoDTO>> getMestosDrzava(@PathVariable("drzava") String drzava) {
		List<Mesto> mestos = mestoService.findByDrzava(drzava);
		List<MestoDTO> mestosDTO = new ArrayList<MestoDTO>();
		for (Mesto u : mestos) {
			mestosDTO.add(new MestoDTO(u));
		}
		return new ResponseEntity<List<MestoDTO>>(mestosDTO, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<MestoDTO> saveMesto(@RequestBody MestoDTO mestoDTO) {
		Mesto mesto = new Mesto();
		mesto.setGrad(mestoDTO.getGrad());
		mesto.setDrzava(mestoDTO.getDrzava());

		mesto = mestoService.save(mesto);
		return new ResponseEntity<MestoDTO>(new MestoDTO(mesto), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<MestoDTO> updateMesto(@RequestBody MestoDTO mestoDTO, @PathVariable("id") Long id) {
		// a product must exist
		Mesto mesto = mestoService.findOne(id);

		if (mesto == null) {
			return new ResponseEntity<MestoDTO>(HttpStatus.BAD_REQUEST);
		}

		mesto.setGrad(mestoDTO.getGrad());
		mesto.setDrzava(mestoDTO.getDrzava());

		mesto = mestoService.save(mesto);

		return new ResponseEntity<MestoDTO>(new MestoDTO(mesto), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteMesto(@PathVariable("id") Long id) {
		Mesto mesto = mestoService.findOne(id);
		if (mesto != null) {
			mestoService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}