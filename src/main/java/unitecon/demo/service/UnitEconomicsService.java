package unitecon.demo.service;

import org.springframework.cache.annotation.Cacheable;
import unitecon.demo.dto.CalculationRequestDto;
import unitecon.demo.dto.CalculationResponseDto;

public interface UnitEconomicsService {

	@Cacheable(key = "#request.hashCode()")
	CalculationResponseDto calculate(CalculationRequestDto request);

}
