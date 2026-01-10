package org.example.tools;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class AllureReportScreenshot {
    public static void main(String[] args) throws Exception {
        String reportIndexPath = args.length >= 1 ? args[0] : "target/site/allure-maven-plugin/index.html";
        String outputPath = args.length >= 2 ? args[1] : "allure-report-screenshot.png";

        File reportFile = new File(reportIndexPath);
        if (!reportFile.exists()) {
            throw new IllegalStateException("Allure report not found at: " + reportFile.getAbsolutePath()
                    + ". Run: mvn allure:report");
        }

        File reportDir = reportFile.getParentFile();
        HttpServer server = startStaticServer(reportDir);
        int port = server.getAddress().getPort();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1440,900");

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("http://127.0.0.1:" + port + "/index.html");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(d -> {
                Object ready = ((JavascriptExecutor) d).executeScript("return document.readyState");
                return "complete".equals(String.valueOf(ready));
            });
            // Wait for Allure to finish bootstrapping (spinner removed)
            wait.until(d -> Boolean.TRUE.equals(((JavascriptExecutor) d)
                    .executeScript("return document.querySelector('.spinner') === null")));
            // Wait until widgets have loaded (no more 'Loading...' placeholders)
            wait.until(d -> Boolean.TRUE.equals(((JavascriptExecutor) d).executeScript("""
                    return !document.body || !document.body.innerText || !document.body.innerText.includes('Loading...');
                    """)));

            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path out = Path.of(outputPath);
            if (out.getParent() != null) {
                Files.createDirectories(out.getParent());
            }
            Files.write(out, png);
            System.out.println("Saved Allure report screenshot to: " + out.toAbsolutePath());
        } finally {
            driver.quit();
            server.stop(0);
        }
    }

    private static HttpServer startStaticServer(File rootDir) throws IOException {
        if (rootDir == null || !rootDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid report directory: " + rootDir);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> handle(exchange, rootDir));
        server.start();
        return server;
    }

    private static void handle(HttpExchange exchange, File rootDir) throws IOException {
        try {
            URI uri = exchange.getRequestURI();
            String rawPath = uri.getPath() == null ? "/" : uri.getPath();
            if (rawPath.equals("/")) {
                rawPath = "/index.html";
            }

            // Basic path traversal defense
            String rel = rawPath.replace('\\', '/');
            while (rel.startsWith("/")) {
                rel = rel.substring(1);
            }
            if (rel.contains("..")) {
                write(exchange, 400, "text/plain", "Bad path");
                return;
            }

            File target = new File(rootDir, rel);
            if (!target.exists() || target.isDirectory()) {
                write(exchange, 404, "text/plain", "Not found");
                return;
            }

            byte[] bytes = Files.readAllBytes(target.toPath());
            String contentType = contentType(target.getName());
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } finally {
            exchange.close();
        }
    }

    private static String contentType(String name) {
        String lower = name.toLowerCase();
        Map<String, String> map = new HashMap<>();
        map.put(".html", "text/html; charset=utf-8");
        map.put(".js", "application/javascript; charset=utf-8");
        map.put(".css", "text/css; charset=utf-8");
        map.put(".json", "application/json; charset=utf-8");
        map.put(".csv", "text/csv; charset=utf-8");
        map.put(".png", "image/png");
        map.put(".ico", "image/x-icon");
        map.put(".txt", "text/plain; charset=utf-8");
        for (Map.Entry<String, String> e : map.entrySet()) {
            if (lower.endsWith(e.getKey())) {
                return e.getValue();
            }
        }
        return "application/octet-stream";
    }

    private static void write(HttpExchange exchange, int code, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", contentType);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}


