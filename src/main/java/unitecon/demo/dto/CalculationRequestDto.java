package unitecon.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationRequestDto {

	@NotNull(message = "Marketing spend is required")
	@Positive(message = "Marketing spend must be positive")
	private Double marketingSpend;

	@NotNull(message = "Number of new customers is required")
	@Positive(message = "Number of new customers must be positive")
	private Integer newCustomers;

	@NotNull(message = "Average check is required")
	@Positive(message = "Average check must be positive")
	private Double averageCheck;

	@NotNull(message = "Purchases per year is required")
	@PositiveOrZero(message = "Purchases per year cannot be negative")
	private Integer purchasesPerYear;

	@NotNull(message = "Customer lifetime (months) is required")
	@Positive(message = "Customer lifetime (months) must be positive, >= 1")
	private Integer customerLifetimeMonths;

	@NotNull(message = "Revenue is required")
	@Positive(message = "Revenue must be positive")
	private Double revenue;

	@NotNull(message = "Cost of goods sold is required")
	@PositiveOrZero(message = "Cost of goods sold cannot be negative")
	private Double costOfGoodsSold;

	@NotNull(message = "Fixed costs is required")
	@PositiveOrZero(message = "Fixed costs cannot be negative")
	private Double fixedCosts;

	@NotNull(message = "Variable cost per unit is required")
	@PositiveOrZero(message = "Variable cost per unit cannot be negative")
	private Double variableCostPerUnit;
}