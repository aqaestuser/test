# NPGW-QA

## Languages and Frameworks

The project uses the following:

- **[Java 17](https://openjdk.org/projects/jdk/17/)** as the programming language.
- **[Maven](https://maven.apache.org)** as the builder and manager of project.
- **[Playwright](https://playwright.dev/)** as the framework for Web Testing and Automation.
- **[TestNG](https://testng.org/doc/)** as the Testing Framework.
- **[IntelliJ IDEA](https://www.jetbrains.com/idea/)** as the Integrated Development Environment.

## Installation Steps

Install the next packages on your local machine using the following links:
- *[Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)*
- *[Maven](https://maven.apache.org/download.cgi)*
- *[Allure Report](https://allurereport.org/docs/install/)*
- *[IntelliJ IDEA](https://www.jetbrains.com/idea/download)*

To use the framework:
- [Clone](https://www.jetbrains.com/help/clion/set-up-a-git-repository.html#clone-repo) the repository on your local machine:
```
git clone https://github.com/NPGW/npgw-ui-test.git
```

## Project Structure

The project is structured as follows:

```bash
📦:.
└───test
    ├───java
    │   ├───page
    │   ├───runner
    │   └───testdata
    └───resources
```

### Test Design
- Each test in the [tests package](./src/test/java) is independent and complete.
- [BaseTest](./src/test/java/runner/BaseTest.java) class uses TestNG annotations to configure the setup and teardown processes.
    - **@BeforeClass** : Responsible for initializing the Playwright instance and launching the browser.
      It also records the environment setup, including the browser type.
    - **@BeforeMethod** : For each test method, a new browser context is created, and Playwright tracing starts.
      It ensures the user is navigated to the login page before executing the test case.
    - **@AfterMethod** : After each test method execution, screenshots and video captures (for CI failures)
      are saved into target directory.
    - **@AfterClass** : At the end of the test class, the browser and Playwright instance are closed
      to free up resources and ensure a clean state for the next test.
- For each test method, a new Playwright server is started to ensure test isolation, preventing interference
  between test instances.

### Test Execution
- The tests can be executed using *TestNG* as the test runner.
- The tests can be executed from maven test command or individual TestNG test from local after cloning the repo.

### Maven commands to run tests:
 - To run all the unit tests

`mvn clean test`

 - To run a single test class

`mvn test -Dtest=LoginPageTest`

- To run a specific methods within test classes

`mvn test -Dtest=LoginPageTest#testLogin`

- To run in Debug Mode

`mvn -X test -Dtest=LoginPageTest#testLogin`
`mvn -X test -Dtest=LoginPageTest#testNavigateToLoginPage`
