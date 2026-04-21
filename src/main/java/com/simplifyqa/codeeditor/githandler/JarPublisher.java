package com.simplifyqa.codeeditor.githandler;

import com.simplifyqa.codeeditor.plugin.CodeEditorPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.*;

public class JarPublisher {

    private static final String ENDPOINT = "http://localhost:4040/publish/codeeditor";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final int SUCCESS = 0;
    private static final int ERROR = 1;

    private static final Logger log = Logger.getLogger(JarPublisher.class.getName());

    static {
        configureLogger();
    }

    private static void configureLogger() {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                String level = record.getLevel().getLocalizedName();
                String message = record.getMessage().replaceAll("\\r?\\n", " ");
                if (level.equalsIgnoreCase("SEVERE")) {
                    return "[" + ANSI_RED + level + ANSI_RESET + "] " + message + System.lineSeparator();
                }
                return "[" + ANSI_BLUE + level + ANSI_RESET + "] " + message + System.lineSeparator();
            }
        });

        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.INFO);
    }

    public static void main(String[] args) {
        try {
            String projectDir = System.getProperty("user.dir");
            String projectId = CodeEditorPlugin.projectId;

            // Validate JAR exists
            File targetDir = new File(projectDir, "target");
            File[] jarFiles = targetDir.listFiles((dir, name) -> name.endsWith("-jar-with-dependencies.jar"));
            if (jarFiles == null || jarFiles.length == 0) {
                log.log(Level.SEVERE, ANSI_RED + "No *-jar-with-dependencies.jar found in target/. Run 'mvn package' first." + ANSI_RESET);
                System.exit(ERROR);
            }

            printGap();
            log.info(ANSI_YELLOW + "=============================== STARTING PUBLISH PROCESS ===============================" + ANSI_RESET);
            printGap();

            String requestBody = "{\"projectId\":\"" + projectId + "\",\"projectDir\":\"" + escapeJson(projectDir) + "\"}";
            log.info("Publishing JAR for projectId: " + projectId);

            int result = sendPublishRequest(requestBody);
            printGap();
            System.exit(result);
        } catch (Exception e) {
            printGap();
            log.log(Level.SEVERE, ANSI_RED + "ERROR: " + ANSI_YELLOW + e.getMessage() + ANSI_RESET);
            System.exit(ERROR);
        }
    }

    private static int sendPublishRequest(String requestBody) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(ENDPOINT);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            String responseCodeColor = responseCode == 200 ? ANSI_GREEN : ANSI_RED;
            printGap();
            log.info("API RESPONSE CODE: " + responseCodeColor + responseCode + ANSI_RESET);

            StringBuilder response = new StringBuilder();
            InputStream stream = responseCode >= 400 ? connection.getErrorStream() : connection.getInputStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            String responseStr = response.toString();
            String status = extractField(responseStr, "status");
            String version = extractField(responseStr, "version");
            String error = extractField(responseStr, "error");

            if ("SUCCESS".equals(status) && version != null) {
                printGap();
                log.info(ANSI_GREEN + "=============================== PUBLISH SUCCESSFUL ===============================" + ANSI_RESET);
                log.info(ANSI_GREEN + "Published version: " + version + ANSI_RESET);
                return SUCCESS;
            } else {
                printGap();
                log.log(Level.SEVERE, ANSI_RED + "=============================== PUBLISH FAILED ===============================" + ANSI_RESET);
                if (error != null) {
                    log.log(Level.SEVERE, ANSI_RED + "Error: " + error + ANSI_RESET);
                }
                return ERROR;
            }

        } catch (Exception e) {
            printGap();
            log.log(Level.SEVERE, ANSI_RED + "ERROR: " + ANSI_YELLOW + e.getMessage() + ANSI_RESET);
            return ERROR;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String extractField(String json, String fieldName) {
        int startIndex = json.indexOf("\"" + fieldName + "\":");
        if (startIndex == -1) return null;

        startIndex = json.indexOf(":", startIndex) + 1;
        if (startIndex == 0) return null;

        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }

        if (startIndex >= json.length()) return null;

        if (json.charAt(startIndex) == '"') {
            int endIndex = json.indexOf("\"", startIndex + 1);
            while (endIndex != -1 && json.charAt(endIndex - 1) == '\\') {
                endIndex = json.indexOf("\"", endIndex + 1);
            }
            if (endIndex == -1) return null;
            return json.substring(startIndex + 1, endIndex);
        } else {
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
            if (endIndex == -1) return null;
            return json.substring(startIndex, endIndex).trim();
        }
    }

    private static String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static void printGap() {
        System.out.println();
    }
}
