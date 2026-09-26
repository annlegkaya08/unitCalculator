package unitecon.demo.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import unitecon.demo.dto.CalculationRequestDto;
import unitecon.demo.service.UnitEconomicsService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableCaching
@DisplayName("UnitEconomics caching")
class UnitEconomicsCacheTest {

	@Autowired
	private UnitEconomicsService service;

	@Autowired
	private CacheManager cacheManager;

	private CalculationRequestDto request() {
		return CalculationRequestDto.builder()
				.marketingSpend(1500.0)
				.newCustomers(10)
				.averageCheck(800.0)
				.purchasesPerYear(1)
				.customerLifetimeMonths(3)
				.revenue(2400.0)
				.costOfGoodsSold(600.0)
				.fixedCosts(5000.0)
				.variableCostPerUnit(150.0)
				.build();
	}

	@Test
	@DisplayName("second call with same request is served from cache")
	void shouldCacheResults() {
		CalculationRequestDto req = request();

		cacheManager.getCache("unitEconomics").clear();
		service.calculate(req);
		service.calculate(req);

		Object cachedKey = req.hashCode();
		assertThat(cacheManager.getCache("unitEconomics").get(cachedKey)).isNotNull();
	}

	@Test
	@DisplayName("different requests produce different cache entries")
	void differentRequestsUseDifferentKeys() {
		CalculationRequestDto req1 = request();
		CalculationRequestDto req2 = request();
		req2.setMarketingSpend(2000.0);

		cacheManager.getCache("unitEconomics").clear();
		service.calculate(req1);
		service.calculate(req2);

		assertThat(cacheManager.getCache("unitEconomics").get(req1.hashCode())).isNotNull();
		assertThat(cacheManager.getCache("unitEconomics").get(req2.hashCode())).isNotNull();
	}
}