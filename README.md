==Unit Economics API==
REST API built with Spring Boot for calculating unit economics metrics (CAC, LTV, ROMI, margin, break-even point). All endpoints are documented and testable via Swagger UI (OpenAPI 3).


===OVERVIEW===
Unit Economics API provides a single calculation endpoint that takes a set of business input parameters (marketing spend, new customers, average check, etc.) and returns key unit economics metrics such as:
CAC — Customer Acquisition Cost
LTV — Customer Lifetime Value
ROMI — Return on Marketing Investment
Margin % — Gross margin percentage
BEP — Break-Even Point (in units)
Status — Business health indicator (profitable / warning / unprofitable)
The service is stateless, cache-enabled, and validated at the DTO level.


===FEARURES===
✅ Unit economics calculation (CAC, LTV, ROMI, Margin, BEP)
✅ Automatic business status determination
✅ Jakarta Bean Validation for input DTOs
✅ Global exception handling with structured error responses
✅ In-memory caching of calculation results (@Cacheable)
✅ Interactive Swagger UI for API exploration
✅ Lombok for boilerplate reduction
✅ Health check endpoint


===STACK===
Technology	Version
Java	21
Spring Boot	4.0.6
Spring Web MVC	7.x
Spring Validation	3.x
Spring Cache	3.x
SpringDoc OpenAPI	3.0.3
Lombok	latest
Maven	3.9+

===PROJECT STRUCTURE===
src/
├── main/
│   ├── java/unitecon/demo/
│   │   ├── DemoApplication.java
│   │   ├── controller/
│   │   │   └── UnitEconomicsController.java
│   │   ├── dto/
│   │   │   ├── CalculationRequestDto.java
│   │   │   └── CalculationResponseDto.java
│   │   ├── service/
│   │   │   ├── UnitEconomicsService.java
│   │   │   └── UnitEconomicsServiceImpl.java
│   │   └── validation/
│   │       └── GlobalException.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/unitecon/demo/
        └── (tests to be added)

        
===GETTING STARTED===
Prerequisites
JDK 21 or higher
Maven 3.9+

===INSTALLATION===
Clone the repository and build the project:  (bash)
git clone https://github.com/your-username/unit-economics-api.git
cd unit-economics-api
mvn clean install

Running the Application (bash)
mvn spring-boot:run

Or build a JAR and run it:  (bash)
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar

The application will start on http://localhost:8080.


SpringDoc OpenAPI is included out of the box (springdoc-openapi-starter-webmvc-ui:3.0.3).

Resource	URL
Swagger UI	http://localhost:8080/swagger-ui.html
OpenAPI JSON	http://localhost:8080/v3/api-docs
OpenAPI YAML	http://localhost:8080/v3/api-docs.yaml
You can use Swagger UI to:

Explore the /api/v1/unit-economics/calculate endpoint
Send test requests with sample payloads
View response schemas and validation rules


===API ENDPIONTS===
Method	Endpoint	Description
POST	/api/v1/unit-economics/calculate	Calculate unit economics metrics
GET	/api/v1/unit-economics/health	Health check (returns OK)
Request & Response Examples
Request — POST /api/v1/unit-economics/calculate

{
  "marketingSpend": 1500,
  "newCustomers": 10,
  "averageCheck": 800,
  "purchasesPerYear": 1,
  "customerLifetimeMonths": 3,
  "revenue": 2400,
  "costOfGoodsSold": 600,
  "fixedCosts": 5000,
  "variableCostPerUnit": 150
}

Response — 200 OK

{
  "cac": 150.0,
  "ltv": 200.0,
  "romi": 60.0,
  "marginPercent": 75.0,
  "BEPoint": 8,
  "status": "warning"
}
cURL Example
curl -X POST http://localhost:8080/api/v1/unit-economics/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "marketingSpend": 1500,
    "newCustomers": 10,
    "averageCheck": 800,
    "purchasesPerYear": 1,
    "customerLifetimeMonths": 3,
    "revenue": 2400,
    "costOfGoodsSold": 600,
    "fixedCosts": 5000,
    "variableCostPerUnit": 150
  }'
  
===Health Check===
curl http://localhost:8080/api/v1/unit-economics/health
#OK


===BUSINESS LOGIC===
The service calculates the following metrics:

CAC	marketingSpend / newCustomers (0 if newCustomers == 0)
LTV	averageCheck × purchasesPerYear × (customerLifetimeMonths / 12)
ROMI	((revenue − marketingSpend) / marketingSpend) × 100%
Margin %	((revenue − costOfGoodsSold) / revenue) × 100% (0 if revenue == 0)
BEP	ceil(fixedCosts / (averageCheck − variableCostPerUnit))
Status	Based on LTV/CAC ratio (see below)
Status Determination
Condition	Status
LTV > CAC × 3	profitable
LTV > CAC	warning
otherwise	unprofitable
All numeric outputs are rounded to 2 decimal places.

If contributionMargin ≤ 0, BEP returns Integer.MAX_VALUE (unreachable break-even).



===Planned coverage===
Unit tests for UnitEconomicsServiceImpl (all formulas + edge cases)
Web layer tests for UnitEconomicsController using MockMvc
Validation tests via spring-boot-starter-validation-test
Cache behavior tests via spring-boot-starter-cache-test

===For questions or support, please open an issue in the repository.===

===Notes / TODO===
□ Add unit and integration tests
□ Add a LICENSE file
□ Add Dockerfile / docker-compose for containerized deployment
□ Add CI pipeline (GitHub Actions / GitLab CI)
□ Consider replacing cache key hashCode() with a proper composite key
□ Add actuator for production-grade health/metrics endpoints
□ Configure OpenAPI metadata (title, version, contact, license) via OpenAPI bean
