API Automation Framework – RestAssured + TestNG

This is a scalable API Automation Framework built using Java, TestNG, RestAssured, Maven, and Extent Reports.
It supports modular automation design, clean reporting, reusable utilities, and easy CI/CD integration.

🚀 Tech Stack
Java 8+
TestNG
RestAssured
Maven
Extent Reports
Log4j2
Jackson / Gson

📁 Project Structure
API-Automation-Framework
│
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.qa.api.base
│   │   │   ├── com.qa.api.client
│   │   │   ├── com.qa.api.utils
│   │   │   ├── com.qa.api.manager
│   ├── test
│       ├── java
│           ├── com.qa.api.tests
│       
│
├── testng.xml
├── pom.xml
├── README.md
└── .gitignore

🧪 Framework Features

Modular & reusable API test design
CRUD API Testing (GET, POST, PUT, DELETE)
Request & response logging
Base test class for setup/teardown
Config handling for environment switching
Automatic Extent HTML reports
Centralized utilities & constants
Supports parallel execution

Maven & CI/CD friendly
▶️ How to Run Tests
Run the full suite:
mvn clean test

Run specific TestNG XML:
mvn clean test -DsuiteXmlFile=testng.xml

📊 Reports
Extent HTML Report
Generated at: target/reports/ExtentReport.html

Surefire Reports
target/surefire-reports/

⚙️ Configuration File (config.properties)

Example:
baseURL=https://api.example.com
timeout=5000
token=yourAuthToken

🌐 Sample API Call (RestAssured)
Response response = given()
        .contentType("application/json")
        .body(payload)
        .when()
        .post("/contacts");
response.then().statusCode(200);

📦 How to Clone This Project
git clone https://github.com/SatishTanpure/RestAssured_API_AutomationFramework_TestNG.git

Import the project → Open using IntelliJ or Eclipse → Maven will auto-download dependencies.

👨‍💻 Author

Satish Tanpure
📧 satish.tanpure@yahoo.com

🔗 LinkedIn: https://www.linkedin.com/in/satish-tanpure-48679437/
