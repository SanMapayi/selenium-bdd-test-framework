# Selenium BDD Test Framework

A Java test-automation project for SauceDemo using Selenium WebDriver, Cucumber, TestNG, Maven, and optional Docker/Selenium Grid execution.

The main scenario logs in, identifies the highest-priced product without relying on the sort control, adds it to the cart, and checks the cart contents.

## Design

- Page objects encapsulate locators and browser interactions.
- Cucumber step definitions describe journeys and perform assertions.
- Hooks manage browser setup, teardown, logging, and failure screenshots.
- WebDriver and waits use thread-local storage. This does not establish that every shared helper is ready for parallel execution.

## Run locally

Requires JDK 23 or newer, Maven, an installed supported browser, and network access to SauceDemo.

~~~sh
mvn -B test "-Dheadless=true" "-Dbrowser=chrome"
~~~

Override browser, headless, url, and other configuration values with JVM properties. Precedence is JVM property, environment variable, then config/config.properties. Camel-case keys map to uppercase environment variables with underscores, such as WINDOW_SIZE.

## Browser-free verification

~~~sh
mvn -B test "-Dcucumber.execution.dry-run=true"
~~~

This checks feature-to-step bindings and runs configuration unit tests without launching a browser. It does not verify selectors, website availability, or browser behaviour.

For configuration tests alone:

~~~sh
mvn -B test "-Dtest=ReadConfigTest"
~~~

## Docker and Selenium Grid

~~~sh
docker compose up --build --abort-on-container-exit --exit-code-from cucumber-tests
docker compose down
~~~

The compose file supplies a hub, Chrome/Firefox nodes, and a test container. Remote execution depends on hub/node readiness; startup order alone is not a readiness guarantee.

## Reports

Cucumber writes its HTML report to target/cucumber-report.html. Failed scenarios attach screenshots to the report. Generated logs, screenshots, and reports should stay out of source commits.

## Current limitations

Live tests depend on an external demo site and browser/driver compatibility. Parallel execution needs review of shared screenshot and scenario helpers. GitHub Actions runs browser-free checks; a green result is not a claim that a live browser run passed.