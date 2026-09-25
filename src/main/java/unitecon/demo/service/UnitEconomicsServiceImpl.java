package unitecon.demo.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import unitecon.demo.dto.CalculationRequestDto;
import unitecon.demo.dto.CalculationResponseDto;


@Service
@Slf4j
@CacheConfig(cacheNames = {"unitEconomics"})


public class UnitEconomicsServiceImpl implements UnitEconomicsService {


	@Cacheable(key = "#request.hashCode()")
	@Override
	public CalculationResponseDto calculate(CalculationRequestDto request) {

		double cac = calculateCAC(request);
		double ltv = calculateLTV(request);
		double romi = calculateROMI(request);
		double margin = calculateMargin(request);
		int BEP = calculateBEPoint(request);
		String status = determineStatus(ltv, cac);

		return CalculationResponseDto.builder()
				.cac(round(cac))
				.ltv(round(ltv))
				.romi(round(romi))
				.marginPercent(round(margin))
				.BEPoint(BEP)
				.status(status)
				.build();
	}

	private double calculateCAC(CalculationRequestDto req) {
		if (req.getNewCustomers() == 0) return 0.0;
		return req.getMarketingSpend() / req.getNewCustomers();
	}

	private double calculateLTV(CalculationRequestDto req) {
		return req.getAverageCheck()
				* req.getPurchasesPerYear()
				* (req.getCustomerLifetimeMonths() / 12.0);
	}

	private double calculateROMI(CalculationRequestDto req) {
		double marketingReturn = req.getRevenue() - req.getMarketingSpend();
		return (marketingReturn / req.getMarketingSpend()) * 100.0;
	}

	private double calculateMargin(CalculationRequestDto req) {
		if (req.getRevenue() == 0) return 0.0;
		return ((req.getRevenue() - req.getCostOfGoodsSold()) / req.getRevenue()) * 100.0;
	}

	private int calculateBEPoint(CalculationRequestDto req) {
		double contributionMargin = req.getAverageCheck() - req.getVariableCostPerUnit();
		if (contributionMargin <= 0) return Integer.MAX_VALUE;
		return (int) Math.ceil(req.getFixedCosts() / contributionMargin);
	}

	private String determineStatus(double ltv, double cac) {
		if (ltv > cac * 3) return "profitable";  // if LTV is bigger than
		if (ltv > cac) return "warning";              // выше, но не в 3 раза
		return "unprofitable";                        // бизнес в минусе
	}


	private double round(double value) {
		return Math.round(value * 100.0) / 100.0;
	}


}