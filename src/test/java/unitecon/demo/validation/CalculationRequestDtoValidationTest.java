package unitecon.demo.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import unitecon.demo.dto.CalculationRequestDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CalculationRequestDto validation")
class CalculationRequestDtoValidationTest {

	private static ValidatorFactory factory;
	private static Validator validator;

	@BeforeAll
	static void init() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@AfterAll
	static void tearDown() {
		factory.close();
	}

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
	@DisplayName("valid request → no violations")
	void validRequestHasNoViolations() {
		Set<ConstraintViolation<CalculationRequestDto>> violations =
				validator.validate(validRequest());
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("negative marketingSpend → violation")
	void negativeMarketingSpend() {
		CalculationRequestDto req = validRequest();
		req.setMarketingSpend(-1.0);
		assertThat(validator.validate(req))
				.anyMatch(v -> v.getPropertyPath().toString().equals("marketingSpend"));
	}

	@Test
	@DisplayName("zero newCustomers → violation")
	void zeroNewCustomers() {
		CalculationRequestDto req = validRequest();
		req.setNewCustomers(0);
		assertThat(validator.validate(req))
				.anyMatch(v -> v.getPropertyPath().toString().equals("newCustomers"));
	}

	@Test
	@DisplayName("null customerLifetimeMonths → violation")
	void nullLifetimeMonths() {
		CalculationRequestDto req = validRequest();
		req.setCustomerLifetimeMonths(null);
		assertThat(validator.validate(req))
				.anyMatch(v -> v.getPropertyPath().toString().equals("customerLifetimeMonths"));
	}

	@Test
	@DisplayName("negative fixedCosts → violation")
	void negativeFixedCosts() {
		CalculationRequestDto req = validRequest();
		req.setFixedCosts(-100.0);
		assertThat(validator.validate(req))
				.anyMatch(v -> v.getPropertyPath().toString().equals("fixedCosts"));
	}
}