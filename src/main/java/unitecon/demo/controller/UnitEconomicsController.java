package unitecon.demo.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import unitecon.demo.dto.CalculationRequestDto;
import unitecon.demo.dto.CalculationResponseDto;
import unitecon.demo.service.UnitEconomicsService;


@RestController
@RequestMapping("/api/v1/unit-economics")
@Validated
@Slf4j


public class UnitEconomicsController {
	private final UnitEconomicsService service;

	public UnitEconomicsController(UnitEconomicsService service) {
		this.service = service;

	}


	@PostMapping("/calculate")
	public ResponseEntity<CalculationResponseDto> calculate(
			@Valid @RequestBody CalculationRequestDto request) {

		log.info("Calculating unit economics for request: {}", request);
		CalculationResponseDto response = service.calculate(request);
		return ResponseEntity.ok(response);
	}


	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("OK");
	}

}