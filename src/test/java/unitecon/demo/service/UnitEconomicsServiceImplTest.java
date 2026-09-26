package unitecon.demo.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import unitecon.demo.dto.CalculationRequestDto;
import unitecon.demo.dto.CalculationResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UnitEconomicsServiceImpl")
class UnitEconomicsServiceImplTest {

	private UnitEconomicsServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new UnitEconomicsServiceImpl();
	}

	private CalculationRequestDto.CalculationRequestDtoBuilder baseRequest() {
		return CalculationRequestDto.builder()
				.marketingSpend(1500.0)
				.newCustomers(10)
				.averageCheck(800.0)
				.purchasesPerYear(1)
				.customerLifetimeMonths(3)
				.revenue(2400.0)
				.costOfGoodsSold(600.0)
				.fixedCosts(5000.0)
				.variableCostPerUnit(150.0);
	}

	@Nested
	@DisplayName("CAC calculation")
	class CacTests {

		@Test
		@DisplayName("should calculate CAC = marketingSpend / newCustomers")
		void shouldCalculateCac() {
			CalculationResponseDto result = service.calculate(baseRequest().build());
			assertThat(result.getCac()).isEqualTo(150.0);
		}

		@Test
		@DisplayName("should return 0 when newCustomers == 0")
		void shouldReturnZeroCacWhenNoCustomers() {
			CalculationResponseDto result = service.calculate(
					baseRequest().newCustomers(0).build());
			assertThat(result.getCac()).isEqualTo(0.0);
		}
	}

	@Nested
	@DisplayName("LTV calculation")
	class LtvTests {

		@Test
		@DisplayName("should calculate LTV = averageCheck × purchasesPerYear × (months / 12)")
		void shouldCalculateLtv() {
			CalculationResponseDto result = service.calculate(baseRequest().build());
			assertThat(result.getLtv()).isEqualTo(200.0);
		}

		@Test
		@DisplayName("should return 0 when lifetime months is 0")
		void shouldReturnZeroLtvWhenNoLifetime() {
			CalculationResponseDto result = service.calculate(
					baseRequest().customerLifetimeMonths(0).build());
			assertThat(result.getLtv()).isEqualTo(0.0);
		}
	}

	@Nested
	@DisplayName("ROMI calculation")
	class RomiTests {

		@Test
		@DisplayName("should calculate ROMI = ((revenue − marketingSpend) / marketingSpend) × 100")
		void shouldCalculateRomi() {
			CalculationResponseDto result = service.calculate(baseRequest().build());
			assertThat(result.getRomi()).isEqualTo(60.0);
		}

		@Test
		@DisplayName("should return negative ROMI when marketing spend exceeds revenue")
		void shouldReturnNegativeRomi() {
			CalculationResponseDto result = service.calculate(
					baseRequest().marketingSpend(3000.0).build());
			assertThat(result.getRomi()).isEqualTo(-20.0);
		}
	}

	@Nested
	@DisplayName("Margin calculation")
	class MarginTests {

		@Test
		@DisplayName("should calculate margin = ((revenue − COGS) / revenue) × 100")
		void shouldCalculateMargin() {
			CalculationResponseDto result = service.calculate(baseRequest().build());
			assertThat(result.getMarginPercent()).isEqualTo(75.0);
		}

		@Test
		@DisplayName("should return 0 when revenue == 0")
		void shouldReturnZeroMarginWhenNoRevenue() {
			CalculationResponseDto result = service.calculate(
					baseRequest().revenue(0.0).build());
			assertThat(result.getMarginPercent()).isEqualTo(0.0);
		}
	}

	@Nested
	@DisplayName("Break-Even Point calculation")
	class BepTests {

		@Test
		@DisplayName("should calculate BEP = ceil(fixedCosts / contributionMargin)")
		void shouldCalculateBep() {
			CalculationResponseDto result = service.calculate(baseRequest().build());
			assertThat(result.getBEPoint()).isEqualTo(8);
		}

		@Test
		@DisplayName("should return Integer.MAX_VALUE when contribution margin ≤ 0")
		void shouldReturnMaxValueWhenNoContribution() {
			CalculationResponseDto result = service.calculate(
					baseRequest().averageCheck(100.0).variableCostPerUnit(150.0).build());
			assertThat(result.getBEPoint()).isEqualTo(Integer.MAX_VALUE);
		}
	}

	@Nested
	@DisplayName("Status determination")
	class StatusTests {

		@Test
		@DisplayName("should return 'profitable' when LTV > CAC × 3")
		void shouldReturnProfitable() {
			CalculationResponseDto result = service.calculate(
					baseRequest()
							.marketingSpend(1500.0)
							.newCustomers(10)
							.averageCheck(800.0)
							.purchasesPerYear(2)
							.customerLifetimeMonths(12)
							.build());
			assertThat(result.getStatus()).isEqualTo("profitable");
		}

		@Test
		@DisplayName("should return 'warning' when LTV > CAC but < CAC × 3")
		void shouldReturnWarning() {
			CalculationResponseDto result = service.calculate(baseRequest().build());
			assertThat(result.getStatus()).isEqualTo("warning");
		}

		@Test
		@DisplayName("should return 'unprofitable' when LTV ≤ CAC")
		void shouldReturnUnprofitable() {
			//unprofitable
			CalculationResponseDto result = service.calculate(
					baseRequest().marketingSpend(15000.0).build());
			assertThat(result.getStatus()).isEqualTo("unprofitable");
		}
	}

	@Test
	@DisplayName("should round all numeric outputs to 2 decimal places")
	void shouldRoundResults() {
		CalculationResponseDto result = service.calculate(
				baseRequest()
						.marketingSpend(1000.0)
						.newCustomers(3)
						.build());
		//2 sighs
		assertThat(result.getCac()).isEqualTo(333.33);
	}
}