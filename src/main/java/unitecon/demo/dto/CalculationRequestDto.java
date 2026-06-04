package unitecon.demo.dto;


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

    @Positive(message = "Marketing spend must be positive")
    private Double marketingSpend;

    @Positive(message = "Number of new customers must be positive")
    private Integer newCustomers;

    @Positive(message = "Average check must be positive")
    private Double averageCheck;

    @PositiveOrZero(message = "Purchases per year cannot be negative")
    private Integer purchasesPerYear;

    @Positive(message = "Customer lifetime (months) must be positive, >= 1")
    private Integer customerLifetimeMonths;

    @Positive(message = "Revenue must be positive")
    private Double revenue;

    @PositiveOrZero(message = "Cost of goods sold cannot be negative")
    private Double costOfGoodsSold;

    @PositiveOrZero(message = "Fixed costs cannot be negative")
    private Double fixedCosts;

    @PositiveOrZero(message = "Variable cost per unit cannot be negative")
    private Double variableCostPerUnit;
}