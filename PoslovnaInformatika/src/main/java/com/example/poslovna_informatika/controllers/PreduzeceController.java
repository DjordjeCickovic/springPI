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

import com.example.poslovna_informatika.dto.PreduzeceDTO;
import com.example.poslovna_informatika.model.Mesto;
import com.example.poslovna_informatika.model.Preduzece;
import com.example.poslovna_informatika.services.MestoService;
import com.example.poslovna_informatika.services.PreduzeceService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping(value = "api/preduzece")
public class PreduzeceController {

	private PreduzeceService preduzeceService;
	private MestoService mestoService;

	@Autowired
	public PreduzeceController(PreduzeceService preduzeceService, MestoService mestoService) {
		this.preduzeceService = preduzeceService;
		this.mestoService = mestoService;

	}

	@GetMapping(value = "/preduzecereport")
	@ResponseBody
	public ResponseEntity getReport() throws JRException, IOException, SQLException {
		JasperPrint jp = JasperFillManager.fillReport(
				getClass().getResource("/reports/PreduzeceReport.jasper").openStream(), null,
				DataSrc.getMysqlDataSource().getConnection());
		ByteArrayInputStream bis = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jp));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=preduzecereport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping
	public ResponseEntity<List<PreduzeceDTO>> getItems() {
		List<Preduzece> preduzeces = preduzeceService.findAll();
		List<PreduzeceDTO> preduzeceDTOS = new ArrayList<PreduzeceDTO>();
		for (Preduzece p : preduzeces) {
			preduzeceDTOS.add(new PreduzeceDTO(p));
		}
		return new ResponseEntity<List<PreduzeceDTO>>(preduzeceDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/pageable")
	public ResponseEntity<List<PreduzeceDTO>> getItems(@RequestParam("page") int page) {
		Page<Preduzece> preduzeces = preduzeceService.findAll(new PageRequest(page, 3));
		List<Preduzece> preduzeceLista = preduzeces.getContent();
		List<PreduzeceDTO> preduzeceDTOS = new ArrayList<PreduzeceDTO>();
		for (Preduzece p : preduzeceLista) {
			preduzeceDTOS.add(new PreduzeceDTO(p));
		}
		return new ResponseEntity<List<PreduzeceDTO>>(preduzeceDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<PreduzeceDTO> getPreduzce(@PathVariable("id") Long id) {
		Preduzece preduzece = preduzeceService.findOne(id);
		if (preduzece == null) {
			return new ResponseEntity<PreduzeceDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<PreduzeceDTO>(new PreduzeceDTO(preduzece), HttpStatus.OK);
	}

	@GetMapping(value = "naziv/{naziv}")
	public ResponseEntity<PreduzeceDTO> getPreduzcebyNaziv(@PathVariable("naziv") String naziv) {
		Preduzece preduzece = preduzeceService.findByNaziv(naziv);
		if (preduzece == null) {
			return new ResponseEntity<PreduzeceDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<PreduzeceDTO>(new PreduzeceDTO(preduzece), HttpStatus.OK);
	}

	@GetMapping(value = "pib/{pib}")
	public ResponseEntity<PreduzeceDTO> getPreduzcebyPib(@PathVariable("pib") int pib) {
		Preduzece preduzece = preduzeceService.findByPib(pib);
		if (preduzece == null) {
			return new ResponseEntity<PreduzeceDTO>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<PreduzeceDTO>(new PreduzeceDTO(preduzece), HttpStatus.OK);
	}

	@GetMapping(value = "adresa/{adresa}")
	public ResponseEntity<List<PreduzeceDTO>> getPreduzecaByAdresa(@PathVariable("adresa") String adresa) {
		List<Preduzece> preduzeces = preduzeceService.findAllByAdresa(adresa);
		List<PreduzeceDTO> preduzeceDTOS = new ArrayList<PreduzeceDTO>();
		for (Preduzece p : preduzeces) {
			preduzeceDTOS.add(new PreduzeceDTO(p));
		}
		return new ResponseEntity<List<PreduzeceDTO>>(preduzeceDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "email/{email}")
	public ResponseEntity<List<PreduzeceDTO>> getPreduzecaByEmail(@PathVariable("email") String email) {
		List<Preduzece> preduzeces = preduzeceService.findAllByEmail(email);
		List<PreduzeceDTO> preduzeceDTOS = new ArrayList<PreduzeceDTO>();
		for (Preduzece p : preduzeces) {
			preduzeceDTOS.add(new PreduzeceDTO(p));
		}
		return new ResponseEntity<List<PreduzeceDTO>>(preduzeceDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "mesto/{mestoId}")
	public ResponseEntity<List<PreduzeceDTO>> getPreduzecaByMesto(@PathVariable("mestoId") Long mestoId) {
		List<Preduzece> preduzeces = preduzeceService.findAllByMestoId(mestoId);
		List<PreduzeceDTO> preduzeceDTOS = new ArrayList<PreduzeceDTO>();
		for (Preduzece p : preduzeces) {
			preduzeceDTOS.add(new PreduzeceDTO(p));
		}
		return new ResponseEntity<List<PreduzeceDTO>>(preduzeceDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "telefon/{telefon}")
	public ResponseEntity<List<PreduzeceDTO>> getPreduzecaByTelefon(@PathVariable("telefon") String telefon) {
		List<Preduzece> preduzeces = preduzeceService.findAllByTelefon(telefon);
		List<PreduzeceDTO> preduzeceDTOS = new ArrayList<PreduzeceDTO>();
		for (Preduzece p : preduzeces) {
			preduzeceDTOS.add(new PreduzeceDTO(p));
		}
		return new ResponseEntity<List<PreduzeceDTO>>(preduzeceDTOS, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<PreduzeceDTO> saveItem(@RequestBody PreduzeceDTO preduzeceDTO) {
		Preduzece p = new Preduzece();
		p.setAdresa(preduzeceDTO.getAdresa());
		p.setEmail(preduzeceDTO.getEmail());
		p.setLogoPath(preduzeceDTO.getLogoPath());
		p.setNaziv(preduzeceDTO.getNaziv());
		p.setPib(preduzeceDTO.getPib());
		p.setTelefon(preduzeceDTO.getTelefon());

		if (preduzeceDTO.getMesto() != null && preduzeceDTO.getMesto().getId() != 0) {
			Mesto m = mestoService.findOne(preduzeceDTO.getMesto().getId());
			p.setMesto(m);
		}

		p = preduzeceService.save(p);
		return new ResponseEntity<PreduzeceDTO>(new PreduzeceDTO(p), HttpStatus.CREATED);

	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<PreduzeceDTO> updateItem(@RequestBody PreduzeceDTO preduzeceDTO,
			@PathVariable("id") long id) {
		Preduzece p = preduzeceService.findOne(id);

		if (p == null) {
			return new ResponseEntity<PreduzeceDTO>(HttpStatus.BAD_REQUEST);
		}

		p.setAdresa(preduzeceDTO.getAdresa());
		p.setEmail(preduzeceDTO.getEmail());
		p.setLogoPath(preduzeceDTO.getLogoPath());
		p.setNaziv(preduzeceDTO.getNaziv());
		p.setPib(preduzeceDTO.getPib());
		p.setTelefon(preduzeceDTO.getTelefon());

		if (preduzeceDTO.getMesto() != null && preduzeceDTO.getMesto().getId() != 0) {
			Mesto m = mestoService.findOne(preduzeceDTO.getMesto().getId());
			p.setMesto(m);
		}

		p = preduzeceService.save(p);
		return new ResponseEntity<PreduzeceDTO>(new PreduzeceDTO(p), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		Preduzece p = preduzeceService.findOne(id);
		if (p != null) {
			preduzeceService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
}