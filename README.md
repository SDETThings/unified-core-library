🚀 unified-core-library
A unified automation utility library to make every tester’s life easier

The Unified Core Library is a reusable automation foundation designed to simplify, standardize, and accelerate test development across UI Testing, API Testing, and Hybrid frameworks.

This library brings together essential utilities, helpers, and wrappers that test engineers use daily—cleanly packaged and versioned so teams can integrate it into any automation project without repetitive boilerplate.

💡 Why This Library Exists

Most automation teams waste time re-building the same core functions again and again:

WebDriver setup / teardown

Playwright browser management

API clients & authentication handling

Reporting utilities

Error handling & retry logic

Waits, element actions, scrolling, screenshots

Logging wrappers

Data generation & common validations

Unified Core Library eliminates that duplication.

It provides a production-ready, plug-and-play core so SDETs can focus on writing meaningful tests instead of reinventing infrastructure every time.

✨ Key Features
✔ Centralized Browser Automation Utilities

Selenium wrapper (actions, waits, JS interactions, window handling)

Playwright wrapper (locators, waits, reliability utilities)

Thread-safe driver storage (ThreadLocal)

✔ API Testing Helpers

REST Assured wrappers

Pre-configured request/response logging

Token/auth helpers

JSON schema validation support

✔ Cross-Cutting Utilities

Common error module

Automatic retries

Screenshot utilities

Extent & Allure reporting helpers

Logging utilities (Log4j wrapper)

File I/O utilities (JSON, YAML, Excel readers)

Random data generators

✔ Cloud Provider Helpers (Optional)

BrowserStack session management

Azure Key Vault secret retrieval

Azure Identity utilities

✔ Reusable across multiple frameworks

Use this library in:

TestNG-based frameworks

Cucumber (BDD) setups

Hybrid UI/API frameworks

GitHub Actions CI pipelines

Any microservice testing layout

📦 Installation (Maven)

Add the GitHub Packages repository:

<repositories>
    <repository>
        <id>github-sdetthings</id>
        <url>https://maven.pkg.github.com/SDETThings/unified-core-library</url>
    </repository>
</repositories>


Then add the dependency:

<dependency>
    <groupId>io.github.SDETThings</groupId>
    <artifactId>unified-core-library</artifactId>
    <version>1.0.0</version>
</dependency>


🔐 Authentication is required for GitHub Packages.
You’ll need a Personal Access Token with read:packages.
