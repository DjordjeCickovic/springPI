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

import com.example.poslovna_informatika.dto.StopaPdvDTO;
import com.example.poslovna_informatika.model.PDV;
import com.example.poslovna_informatika.model.StopaPDV;
import com.example.poslovna_informatika.services.PdvService;
import com.example.poslovna_informatika.services.StopaPdvService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping(value = "api/stopa-pdv")
public class StopaPdvController {

	private StopaPdvService stopaPdvService;
	private PdvService pdvService;

	@Autowired
	public StopaPdvController(StopaPdvService stopaPdvService, PdvService pdvService) {
		this.stopaPdvService = stopaPdvService;
		this.pdvService = pdvService;

	}

	@GetMapping(value = "/stopapdvreport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/StopaPdvReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=stopapdvreport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<StopaPdvDTO>> getStopaPDVa() {
		List<StopaPDV> stopaPDVS = stopaPdvService.findAll();
		List<StopaPdvDTO> stopaPdvDTOS = new ArrayList<StopaPdvDTO>();
		for (StopaPDV st : stopaPDVS) {
			stopaPdvDTOS.add(new StopaPdvDTO(st));
		}
		return new ResponseEntity<List<StopaPdvDTO>>(stopaPdvDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<StopaPdvDTO>> getStopaPDVaPageable(@RequestParam("page") int page) {
		Page<StopaPDV> stopaPDVS = stopaPdvService.findAll(new PageRequest(page, 3));
		List<StopaPDV> stopaPDVList = stopaPDVS.getContent();
		List<StopaPdvDTO> stopaPdvDTOS = new ArrayList<StopaPdvDTO>();
		for (StopaPDV st : stopaPDVList) {
			stopaPdvDTOS.add(new StopaPdvDTO(st));
		}
		return new ResponseEntity<List<StopaPdvDTO>>(stopaPdvDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<StopaPdvDTO> getStopaPdv(@PathVariable("id") Long id) {
		StopaPDV stopaPdv = stopaPdvService.findOne(id);
		if (stopaPdv == null) {
			return new ResponseEntity<StopaPdvDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<StopaPdvDTO>(new StopaPdvDTO(stopaPdv), HttpStatus.OK);
	}

	@GetMapping(value = "/pdv/{pdvID}")
	public ResponseEntity<List<StopaPdvDTO>> getStopaPdvByPdvId(@PathVariable("pdvID") Long id) {
		List<StopaPDV> stopaPDVS = stopaPdvService.findAllByPdvId(id);
		List<StopaPdvDTO> stopaPdvDTOS = new ArrayList<StopaPdvDTO>();
		for (StopaPDV st : stopaPDVS) {
			stopaPdvDTOS.add(new StopaPdvDTO(st));
		}
		return new ResponseEntity<List<StopaPdvDTO>>(stopaPdvDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/procenat/{procenat}")
	public ResponseEntity<List<StopaPdvDTO>> getStopaPdvByProcenat(@PathVariable("procenat") Double procenat) {
		List<StopaPDV> stopaPDVS = stopaPdvService.findAllByProcenat(procenat);
		;
		List<StopaPdvDTO> stopaPdvDTOS = new ArrayList<StopaPdvDTO>();
		for (StopaPDV st : stopaPDVS) {
			stopaPdvDTOS.add(new StopaPdvDTO(st));
		}
		return new ResponseEntity<List<StopaPdvDTO>>(stopaPdvDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/datumVazenja/{datum}")
	public ResponseEntity<StopaPdvDTO> getStopaPdvByDatumVazenja(@PathVariable("datum") String datum) {
		StopaPDV stopaPDV = stopaPdvService.findByDatumVazenja(datum);
		if (stopaPDV == null) {
			return new ResponseEntity<StopaPdvDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<StopaPdvDTO>(new StopaPdvDTO(stopaPDV), HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<StopaPdvDTO> saveItem(@RequestBody StopaPdvDTO stopaPdvDTO) {
		StopaPDV sp = new StopaPDV();
		sp.setDatumVazenja(stopaPdvDTO.getDatumVazenja());
		sp.setProcenat(stopaPdvDTO.getProcenat());

		if (stopaPdvDTO.getPdv() != null && stopaPdvDTO.getPdv().getId() != 0) {
			PDV pdv = pdvService.findOne(stopaPdvDTO.getPdv().getId());
			sp.setPdv(pdv);
		}

		sp = stopaPdvService.save(sp);
		return new ResponseEntity<StopaPdvDTO>(new StopaPdvDTO(sp), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<StopaPdvDTO> updateItem(@RequestBody StopaPdvDTO stopaPdvDTO, @PathVariable("id") long id) {
		StopaPDV sp = stopaPdvService.findOne(id);

		if (sp == null) {
			return new ResponseEntity<StopaPdvDTO>(HttpStatus.BAD_REQUEST);
		}

		sp.setDatumVazenja(stopaPdvDTO.getDatumVazenja());
		sp.setProcenat(stopaPdvDTO.getProcenat());

		if (stopaPdvDTO.getPdv() != null && stopaPdvDTO.getPdv().getId() != 0) {
			PDV pdv = pdvService.findOne(stopaPdvDTO.getPdv().getId());
			sp.setPdv(pdv);
		}

		sp = stopaPdvService.save(sp);
		return new ResponseEntity<StopaPdvDTO>(new StopaPdvDTO(sp), HttpStatus.OK);

	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		StopaPDV stopa = stopaPdvService.findOne(id);
		if (stopa != null) {
			stopaPdvService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}
