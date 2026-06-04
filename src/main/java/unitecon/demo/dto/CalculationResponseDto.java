package unitecon.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor


public class CalculationResponseDto {

    private Double cac;
    private Double ltv;
    private Double romi;
    private Double marginPercent;
    private Integer BEPoint;
    private String status;

}