package unitecon.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import unitecon.demo.dto.CalculationRequestDto;
import unitecon.demo.dto.CalculationResponseDto;
import unitecon.demo.service.UnitEconomicsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnitEconomicsController.class)
@Import(JacksonAutoConfiguration.class)
@DisplayName("UnitEconomicsController")
class UnitEconomicsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@MockitoBean
	private UnitEconomicsService service;

	@MockitoBean
	private org.springframework.cache.CacheManager cacheManager;


	private CalculationRequestDto validRequest() {
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
	@DisplayName("POST /calculate → 200 OK with metrics")
	void shouldReturnMetrics() throws Exception {
		CalculationResponseDto response = CalculationResponseDto.builder()
				.cac(150.0)
				.ltv(200.0)
				.romi(60.0)
				.marginPercent(75.0)
				.BEPoint(8)
				.status("warning")
				.build();

		when(service.calculate(any())).thenReturn(response);

		mockMvc.perform(post("/api/v1/unit-economics/calculate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(validRequest())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cac").value(150.0))
				.andExpect(jsonPath("$.ltv").value(200.0))
				.andExpect(jsonPath("$.romi").value(60.0))
				.andExpect(jsonPath("$.marginPercent").value(75.0))
				.andExpect(jsonPath("$.BEPoint").value(8))
				.andExpect(jsonPath("$.status").value("warning"));
	}

	@Test
	@DisplayName("POST /calculate with invalid body → 400 Bad Request")
	void shouldReturnBadRequestForInvalidBody() throws Exception {
		CalculationRequestDto invalid = validRequest();
		invalid.setMarketingSpend(-100.0);
		invalid.setNewCustomers(0);

		mockMvc.perform(post("/api/v1/unit-economics/calculate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalid)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.marketingSpend").exists())
				.andExpect(jsonPath("$.newCustomers").exists());
	}

	@Test
	@DisplayName("GET /health → 200 OK 'OK'")
	void shouldReturnHealthOk() throws Exception {
		mockMvc.perform(get("/api/v1/unit-economics/health"))
				.andExpect(status().isOk())
				.andExpect(content().string("OK"));
	}
}