package com.simplifyqa.codeeditor;

import com.simplifyqa.pluginbase.codeeditor.annotations.AutoInjectCurrentObject;
import java.util.ArrayList;
import com.simplifyqa.andoidabstract.driver.Element.IQaAndroidElement;
import com.simplifyqa.pluginbase.common.models.SqaObject;
import com.simplifyqa.pluginbase.plugin.execution.SubStep;
import com.simplifyqa.andoidabstract.driver.IQAAndroidDriver;
import com.simplifyqa.pluginbase.codeeditor.annotations.AutoInjectAndroidDriver;
import java.util.List;
import java.util.Map;
import com.simplifyqa.apiabstract.models.HttpRequestData;
import com.simplifyqa.pluginbase.api.http.models.HttpResponseData;
import com.simplifyqa.pluginbase.api.http.enums.APIStyle;
import com.simplifyqa.apiabstract.IQAApiDriver;
import com.simplifyqa.pluginbase.codeeditor.annotations.AutoInjectApiDriver;
import java.util.logging.Logger;
import com.simplifyqa.abstraction.desktop.driver.IQADesktopDriver;
import com.simplifyqa.abstraction.desktop.element.IQADesktopElement;
import com.simplifyqa.pluginbase.codeeditor.annotations.AutoInjectDesktopDriver;
import com.simplifyqa.pluginbase.codeeditor.annotations.AutoInjectExecutionLogger;
import com.simplifyqa.pluginbase.plugin.execution.IExecutionLogReporter;
import com.simplifyqa.pluginbase.codeeditor.annotations.FormulaBuilder;
import com.simplifyqa.pluginbase.codeeditor.annotations.Description;
import com.simplifyqa.pluginbase.codeeditor.annotations.Parameter;
import com.simplifyqa.pluginbase.codeeditor.annotations.enums.ParameterDataType;
import com.simplifyqa.pluginbase.codeeditor.annotations.SyncAction;
import com.simplifyqa.pluginbase.common.enums.TechnologyType;
import com.simplifyqa.pluginbase.plugin.annotations.ObjectTemplate;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import com.simplifyqa.pluginbase.plugin.execution.models.Status;
import java.time.LocalDateTime;
import java.util.HashMap;
import com.simplifyqa.iosabstract.driver.element.IQAiOSElement;
import com.simplifyqa.iosabstract.driver.IQAiOSDriver;
import com.simplifyqa.pluginbase.codeeditor.annotations.AutoInjectIOSDriver;
import com.simplifyqa.abstraction.element.IQAWebElement;
import com.simplifyqa.web.base.search.FindBy;
import com.simplifyqa.pluginbase.codeeditor.annotations.AutoInjectWebDriver;
import com.simplifyqa.abstraction.driver.IQAWebDriver;
import java.io.File;

/**
* Hello there!!, please keep the following things in mind while creating custom class.
* Your class should have a public default constructor.
* @SyncAction methods should be public and return a boolean value.
* uniqueId field in @SyncAction annotation should be unique throughout the project.
*/

public class SampleClass {

	@AutoInjectAndroidDriver
    private IQAAndroidDriver androidDriver;

	@AutoInjectApiDriver
    private IQAApiDriver apiDriver;

	@AutoInjectDesktopDriver
    private IQADesktopDriver desktopDriver;

	@AutoInjectIOSDriver
    private IQAiOSDriver iOsDriver;

	@AutoInjectWebDriver
    private IQAWebDriver webDriver;


    private static final Logger log = Logger.getLogger(SampleClass.class.getName());

    @AutoInjectCurrentObject
    private SqaObject currentStepObject;

    @AutoInjectExecutionLogger
    private IExecutionLogReporter logReporter;

    public SampleClass() {}

	@SyncAction(uniqueId = "sample-edit-profile-immediate-reflection", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.ANDROID, description = "This action edits the profile on Android and validates immediate reflection"), objectRequired = true)
    public boolean editProfileAndCheckImmediateReflection(String profileName, boolean profileStatus) {
        SqaObject profileNameObject = extractSqaObjectWithName("Profile Name");
        SqaObject profileStatusObject = extractSqaObjectWithName("Profile Status");
        List<SubStep> substeps = new ArrayList<>();
        // Interact with the profile name field
        IQaAndroidElement profileNameElement = androidDriver.findElement(profileNameObject);
        profileNameElement.click();
        profileNameElement.enterText(profileName);
        Map<String, String> profileNameTestData = formTestData(List.of("profileName"), List.of(profileName));
        SubStep substep1 = createSubstep("Updated profile name to: " + profileName, true, profileNameTestData);
        substeps.add(substep1);
        // Interact with the profile status toggle
        IQaAndroidElement profileStatusElement = androidDriver.findElement(profileStatusObject);
        if (profileStatus) {
            profileStatusElement.click(); // Toggle ON
        } else {
            profileStatusElement.click(); // Toggle OFF
        }
        SubStep substep2 = createSubstep("Updated profile status to: " + (profileStatus ? "Active" : "Inactive"), true, null);
        substeps.add(substep2);
        // Validate changes are reflected
        androidDriver.scrollToBottom();
        SubStep substep3 = createSubstep("Scrolled to the bottom to check changes reflection", true, null);
        substeps.add(substep3);
        // Final validation step (example placeholder)
        boolean changesReflected = androidDriver.findElement(profileNameObject).getText().equals(profileName)
            && androidDriver.findElement(profileStatusObject).isDisplayed();
        SubStep substep4 = createSubstep("Validated changes are reflected immediately", changesReflected, null);
        substeps.add(substep4);
        androidDriver.getExecutionLogReporter().reportSubSteps(substeps);
        return changesReflected;
    }


	@SyncAction(uniqueId = "sample-sign-out-validate-login", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.ANDROID, description = "Sign out and validate redirect to login page"), objectRequired = false)
    public boolean signOutAndValidateRedirectToLoginPage(String signOutButtonId, String loginPageElementId) {
        List<SubStep> substeps = new ArrayList<>();
        // Click on sign out button
        IQaAndroidElement signOutButton = androidDriver
            .findElement(com.simplifyqa.android.driver.element.search.FindBy.id(signOutButtonId));
        signOutButton.click();
        SubStep substep1 = createSubstep("Clicked on sign out button", true, null);
        substeps.add(substep1);
        // Scroll to the bottom to ensure transition
        androidDriver.scrollToBottom();
        SubStep substep2 = createSubstep("Scrolled to the bottom post sign-out", true, null);
        substeps.add(substep2);
        // Validate that the login page is displayed
        boolean loginPageDisplayed = androidDriver
            .findElement(com.simplifyqa.android.driver.element.search.FindBy.id(loginPageElementId)).isDisplayed();
        SubStep substep3 = createSubstep("Validated login page is displayed", loginPageDisplayed, null);
        substeps.add(substep3);
        androidDriver.getExecutionLogReporter().reportSubSteps(substeps);
        return loginPageDisplayed;
    }


	@SyncAction(uniqueId = "sample-send-email", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.API, description = "This action filters a table and verifies live results"), objectRequired = false)
    public boolean sendEmailTriggerAndVerifyInInbox() {
        try {
            // Create a dummy request
            HttpRequestData httpRequestData = HttpRequestData.builder()
                .url("https://example.com/sendEmailTrigger")
                .httpMethod(APIStyle.POST)
                .httpHeaders(new org.springframework.http.HttpHeaders())
                .body("{\"email\": \"test@example.com\", \"subject\": \"Test Email\", \"message\": \"This is a test email.\"}")
                .responseType(String.class)
                .build();
            
            HttpResponseData responseData = apiDriver.executeHttpRequest(httpRequestData);
            log.info("HTTP Status Code: " + responseData.getStatusCode());
            log.info("Response Body: " + responseData.getResponseBody());
            return responseData.getStatusCode() == 200;
        } catch (Exception e) {
            log.severe("Exception while sending email trigger: " + e.getMessage());
            return false;
        }
    }


	@SyncAction(uniqueId = "sample-notify-update-with-message", groupName = "API Actions", objectTemplate = @ObjectTemplate(name = TechnologyType.API, description = "Notifies an update using an API call with a custom message"), objectRequired = false)
    public boolean notifyUpdateWithMessage(String message) {
        List<SubStep> substeps = new ArrayList<>();
        try {
            // Create a POST request to notify an update
            HttpRequestData httpRequestData = HttpRequestData.builder()
                .url("https://example.com/notifyUpdate")
                .httpMethod(APIStyle.POST)
                .httpHeaders(new org.springframework.http.HttpHeaders())
                .body("{\"message\": \"" + message + "\"}")
                .responseType(String.class)
                .build();
    
            // Execute the API request
            HttpResponseData responseData = apiDriver.executeHttpRequest(httpRequestData);
            log.info("Response Status Code: " + responseData.getStatusCode());
            log.info("Response Body: " + responseData.getResponseBody());
    
            // Report and validate the response
            boolean notificationSuccess = responseData.getStatusCode() == 200;
            Map<String, String> notifyTestData = formTestData(
                List.of("message", "url"),
                List.of(message, "https://example.com/notifyUpdate")
            );
            SubStep substep = createSubstep("Sent notification update with message: " + message, notificationSuccess, notifyTestData);
            substeps.add(substep);
    
            apiDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return notificationSuccess;
        } catch (Exception e) {
            log.severe("Exception occurred while notifying update: " + e.getMessage());
            SubStep errorSubstep = createSubstep("Error during notification: " + e.getMessage(), false, null);
            substeps.add(errorSubstep);
            apiDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return false;
        }
    }


	@SyncAction(uniqueId = "sample-get-iss-coordinates", groupName = "API Actions", objectTemplate = @ObjectTemplate(name = TechnologyType.API, description = "Fetches the current coordinates of the International Space Station"), objectRequired = false)
    public boolean getISSCurrentCoordinates() {
        try {
            HttpRequestData httpRequestData = HttpRequestData.builder()
                .url("http://api.open-notify.org/iss-now.json")
                .httpMethod(APIStyle.GET)
                .responseType(String.class)
                .build();
    
            HttpResponseData responseData = apiDriver.executeHttpRequest(httpRequestData);
            log.info("Response Status Code: " + responseData.getStatusCode());
            log.info("Response Body: " + responseData.getResponseBody());
    
            if (responseData.getStatusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                @SuppressWarnings("unchecked")
                Map<String, Object> responseMap = objectMapper.readValue(responseData.getResponseBody(), Map.class);
                @SuppressWarnings("unchecked")
                Map<String, String> issPosition = (Map<String, String>) responseMap.get("iss_position");
                String latitude = issPosition.get("latitude");
                String longitude = issPosition.get("longitude");
                log.info("ISS Current Coordinates - Latitude: " + latitude + ", Longitude: " + longitude);
                apiDriver.getExecutionLogReporter().info("getISSCurrentCoordinates executed successfully with Latitude: " + latitude + " and Longitude: " + longitude);
                return true;
            } else {
                log.severe("Failed to fetch ISS coordinates. Status Code: " + responseData.getStatusCode());
                apiDriver.getExecutionLogReporter().info("getISSCurrentCoordinates execution failed.");
                return false;
            }
        } catch (Exception e) {
            log.severe("Exception occurred while fetching ISS coordinates: " + e.getMessage());
            apiDriver.getExecutionLogReporter().info("getISSCurrentCoordinates execution failed.");
            return false;
        }
    }


	@SyncAction(uniqueId = "sample-open-group-chat-attach-file-send", groupName = "Desktop", objectTemplate = @ObjectTemplate(name = TechnologyType.DESKTOP, description = "Open group chat, attach a file, and send it"))
    public boolean openGroupChatAndAttachAFileAndSend(String chatWindowId, String attachButtonId, String filePath, String sendButtonId) {
        List<SubStep> substeps = new ArrayList<>();
        try {
            // Locate and interact with the chat window
            boolean switchedToChatWindow = desktopDriver.switchToWindowByHandleId(chatWindowId);
            SubStep substep1 = createSubstep("Switched to chat window", switchedToChatWindow, null);
            substeps.add(substep1);
            // Locate and click the attach button
            IQADesktopElement attachButtonElement = desktopDriver
                .findElement(com.simplifyqa.desktop.base.search.FindBy.automationId(attachButtonId));
            boolean clickedAttachButton = attachButtonElement.click();
            SubStep substep2 = createSubstep("Clicked on attach button", clickedAttachButton, null);
            substeps.add(substep2);
            // Attach the file using keyboard actions
            boolean fileAttached = desktopDriver.keyBoardActions(filePath);
            Map<String, String> fileTestData = formTestData(List.of("filePath"), List.of(filePath));
            SubStep substep3 = createSubstep("Attached file: " + filePath, fileAttached, fileTestData);
            substeps.add(substep3);
            // Locate and click the send button
            IQADesktopElement sendButtonElement = desktopDriver
                .findElement(com.simplifyqa.desktop.base.search.FindBy.automationId(sendButtonId));
            boolean clickedSendButton = sendButtonElement.click();
            SubStep substep4 = createSubstep("Clicked on send button", clickedSendButton, null);
            substeps.add(substep4);
            desktopDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return clickedSendButton;
        } catch (Exception e) {
            log.severe("Error occurred in openGroupChatAndAttachAFileAndSend: " + e.getMessage());
            SubStep errorSubstep = createSubstep("Exception occurred during execution: " + e.getMessage(), false, null);
            substeps.add(errorSubstep);
            desktopDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return false;
        }
    }


	@SyncAction(uniqueId = "sample-desktop-check-status-and-take-action", groupName = "Desktop", objectTemplate = @ObjectTemplate(name = TechnologyType.DESKTOP, description = "Check the status in a desktop application and perform corresponding actions"))
    public boolean checkStatusAndTakeAction(String statusObjectName, String actionObjectName, String expectedStatus) {
        List<SubStep> substeps = new ArrayList<>();
        try {
            // Retrieve and validate the status object
            SqaObject statusObject = extractSqaObjectWithName(statusObjectName);
            IQADesktopElement statusElement = desktopDriver.findElement(statusObject);
            String actualStatus = statusElement.getText();
            boolean statusMatches = actualStatus.equals(expectedStatus);
            Map<String, String> statusTestData = formTestData(List.of("expectedStatus", "actualStatus"),
                List.of(expectedStatus, actualStatus));
            SubStep substep1 = createSubstep(
                "Validated status: Expected -> " + expectedStatus + ", Found -> " + actualStatus, statusMatches,
                statusTestData);
            substeps.add(substep1);
            if (!statusMatches) {
                desktopDriver.getExecutionLogReporter().reportSubSteps(substeps);
                return false;
            }
            // Retrieve and perform action on the object
            SqaObject actionObject = extractSqaObjectWithName(actionObjectName);
            IQADesktopElement actionElement = desktopDriver.findElement(actionObject);
            boolean movedToElement = actionElement.mouseHover();
            SubStep substep2 = createSubstep("Moved to action object: " + actionObjectName, movedToElement, null);
            substeps.add(substep2);
            boolean triggeredAction = actionElement.doubleClick();
            SubStep substep3 = createSubstep("Performed action with a double-click on object: " + actionObjectName,
                triggeredAction, null);
            substeps.add(substep3);
            if (!triggeredAction) {
                desktopDriver.getExecutionLogReporter().reportSubSteps(substeps);
                return false;
            }
            // Validate the outcome
            boolean actionVerified = statusElement.waitTillElementIsPresent();
            SubStep substep4 = createSubstep("Validated outcome based on action performed", actionVerified, null);
            substeps.add(substep4);
            desktopDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return actionVerified;
        } catch (Exception e) {
            log.severe("Error occurred in checkStatusAndTakeAction: " + e.getMessage());
            SubStep errorSubstep = createSubstep("Exception occurred during execution: " + e.getMessage(), false, null);
            substeps.add(errorSubstep);
            desktopDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return false;
        }
    }


	//CUSTOM FORMULA EXAMPLE
    @FormulaBuilder(name = "EXAMPLE_ONE", uniqueId = "unique1", description = @Description(details = "This formula does some operation", example = "=EXAMPLE_ONE(\"assw\",\"wewe\")", parameters = {@Parameter(keyName = "firstKey", value = "abcd", dataType = ParameterDataType.STRING), @Parameter(keyName = "secondKey", value = "lmno", dataType = ParameterDataType.STRING)}))
    public String exampleOne(String value1, String value2) {
        System.out.println("1st Formula param 1 is: " + value1);
        System.out.println("1st Formula param 2 is: " + value2);
        return value1 + "----" + value2;
    }


	//CUSTOM FORMULA EXAMPLE
    @FormulaBuilder(name = "EXAMPLE_TWO", uniqueId = "unique2", description = @Description(details = "This formula does some other operation", example = "=EXAMPLE_TWO(\"assw\",\"wewe\")", parameters = @Parameter(keyName = "firstKey", value = "abcd", dataType = ParameterDataType.STRING)))
    public int exampleTwo(int value1) {
        System.out.println("2nd Formula param is: " + value1);
        return ++value1;
    }


	@SyncAction(uniqueId = "sample-reverse-string", groupName = "Generic", objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action reverses a given string"), objectRequired = false)
    public boolean reverseString(String value) {
        if (value == null) {
            log.info("The provided string is null.");
            return false;
        }
        String reversed = new StringBuilder(value).reverse().toString();
        log.info("Original String: " + value + ", Reversed String: " + reversed);
        webDriver.getExecutionLogReporter().info("reverseString executed with result: " + reversed);
        return true;
    }


	@SyncAction(uniqueId = "sample-capitalize-first-letter", groupName = "Generic", objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action capitalizes the first letter of a given string"), objectRequired = false)
    public boolean capitalizeFirstLetter(String value) {
        if (value == null || value.isEmpty()) {
            log.info("The provided string is null or empty.");
            return false;
        }
        String capitalized = value.substring(0, 1).toUpperCase() + value.substring(1);
        log.info("Original String: " + value + ", Capitalized String: " + capitalized);
        webDriver.getExecutionLogReporter().info("capitalizeFirstLetter executed with result: " + capitalized);
        return true;
    }


	@SyncAction(uniqueId = "sample-generate-random-string", groupName = "Generic", objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action generates a random alphanumeric string of a given length"), objectRequired = false)
    public boolean generateRandomString(int length) {
        if (length <= 0) {
            log.info("Invalid length for random string generation: " + length);
            return false;
        }
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char randomChar = alphanumeric.charAt(random.nextInt(alphanumeric.length()));
            randomString.append(randomChar);
        }
        log.info("Generated Random String: " + randomString.toString());
        webDriver.getExecutionLogReporter().info("generateRandomString executed with result: " + randomString.toString());
        return true;
    }


	@SyncAction(uniqueId = "sample-execute-shell-command", groupName = "Generic", objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action executes shell commands based on OS"), objectRequired = false)
    public boolean executeShellCommand(String... commands) {
        if (commands == null || commands.length == 0) {
            log.info("No commands provided.");
            return false;
        }
        try {
            for (String command : commands) {
                ProcessBuilder processBuilder;
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                } else {
                    processBuilder = new ProcessBuilder("sh", "-c", command);
                }
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        log.info("Shell Output: " + line);
                        webDriver.getExecutionLogReporter().info("Shell Output: " + line);
                    }
                }
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    log.info("Command `" + command + "` failed with exit code: " + exitCode);
                    return false;
                }
                log.info("Command `" + command + "` executed successfully.");
            }
            webDriver.getExecutionLogReporter().info("executeShellCommand executed successfully.");
            return true;
        } catch (Exception e) {
            log.severe("Error executing shell command: " + e.getMessage());
            return false;
        }
    }


	@SyncAction(uniqueId = "sample-parse-json-string", groupName = "Generic", objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action parses a JSON string into a Map"))
    public boolean parseJsonString(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            log.info("Provided JSON string is null or empty.");
            return false;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> parsedMap = objectMapper.readValue(jsonString, Map.class);
            log.info("Parsed Map: " + parsedMap);
            webDriver.getExecutionLogReporter().info("parseJsonString executed successfully with result: " + parsedMap);
            return true;
        } catch (Exception e) {
            log.severe("Error parsing JSON string: " + e.getMessage());
            webDriver.getExecutionLogReporter().info("parseJsonString execution failed.");
            return false;
        }
    }


	@SyncAction(uniqueId = "sample-convert-to-json", groupName = "Generic", objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action converts an object to a JSON string using Jackson"))
    public boolean convertToJson(Object object) {
        if (object == null) {
            log.info("The provided object is null.");
            return false;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            log.info("Converted JSON: " + jsonString);
            webDriver.getExecutionLogReporter().info("convertToJson executed successfully with result: " + jsonString);
            return true;
        } catch (Exception e) {
            log.severe("Error converting object to JSON string: " + e.getMessage());
            webDriver.getExecutionLogReporter().info("convertToJson execution failed.");
            return false;
        }
    }


	private SqaObject extractSqaObjectWithName(String name) {
        Optional<SqaObject> sqaObjectFromRepo = webDriver.getSqaObjectFromRepo(name);
        log.info("Retrieved the SQA object with name: " + name);
        log.info("Retrieved the SQA object: " + sqaObjectFromRepo.get());
        return sqaObjectFromRepo.orElseThrow(RuntimeException::new);
    }


	private SubStep createSubstep(String description, boolean status, Map<String, String> testData) {
        SubStep subStep = new SubStep();
        subStep.setDescription("Clicked on profile icon");
        subStep.setStatus(status ? Status.PASSED : Status.FAILED);
        subStep.setLocalDateTime(LocalDateTime.now().toString());
        subStep.setTestData(testData);
        webDriver.getExecutionLogReporter().info(description);
        return subStep;
    }


	private Map<String, String> formTestData(List<String> keys, List<String> values) {
        if (keys == null || values == null || keys.size() != values.size()) {
            throw new IllegalArgumentException("Keys and values must be non-null and have the same number of elements.");
        }
        Map<String, String> testData = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            testData.put(keys.get(i), values.get(i));
        }
        return testData;
    }


	@SyncAction(uniqueId = "sample-post-comment-verify-sync", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.IOS, description = "This action posts a comment and verifies real-time sync"), objectRequired = true)
    public boolean postCommentAndVerifyRealTimeSync(String commentText, String verificationText) {
        List<SubStep> substeps = new ArrayList<>();
        try {
            // Locate the comment input box
            SqaObject commentInputBox = extractSqaObjectWithName("Comment Input Box");
            IQAiOSElement commentInputElement = iOsDriver.findElement(commentInputBox);
            commentInputElement.click();
            SubStep substep1 = createSubstep("Clicked on comment input box", true, null);
            substeps.add(substep1);
            // Enter the comment text
            commentInputElement.enterText(commentText);
            Map<String, String> commentTestData = formTestData(List.of("commentText"), List.of(commentText));
            SubStep substep2 = createSubstep("Entered comment text: " + commentText, true, commentTestData);
            substeps.add(substep2);
            // Locate and click the Post button
            SqaObject postButton = extractSqaObjectWithName("Post Button");
            boolean clickedPostButton = iOsDriver.ClickBytext(postButton);
            SubStep substep3 = createSubstep("Clicked on Post button", clickedPostButton, null);
            substeps.add(substep3);
            // Verify real-time sync of posted comment
            SqaObject postedCommentText = extractSqaObjectWithName("Posted Comment Verification Text");
            boolean isCommentVerified = iOsDriver.scrollTillTextIsPresent(postedCommentText);
            SubStep substep4 = createSubstep("Verified real-time sync with posted comment: " + verificationText, isCommentVerified, null);
            substeps.add(substep4);
            iOsDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return isCommentVerified;
        } catch (Exception e) {
            log.severe("Error occurred in postCommentAndVerifyRealTimeSync: " + e.getMessage());
            SubStep errorSubstep = createSubstep("Exception occurred during execution: " + e.getMessage(), false, null);
            substeps.add(errorSubstep);
            iOsDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return false;
        }
    }


	@SyncAction(uniqueId = "sample-filter-table-live-results", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.IOS, description = "This action filters a table and verifies live results"), objectRequired = true)
    public boolean filterTableAndVerifyLiveResults(String filterCriteria, String expectedText) {
        List<SubStep> substeps = new ArrayList<>();
        try {
            // Locate filter input box
            SqaObject filterInputBox = extractSqaObjectWithName("Filter Input Box");
            IQAiOSElement filterInputElement = iOsDriver.findElement(filterInputBox);
            filterInputElement.click();
            SubStep substep1 = createSubstep("Clicked on filter input box", true, null);
            substeps.add(substep1);
            filterInputElement.enterText(filterCriteria);
            Map<String, String> filterTestData = formTestData(List.of("filterCriteria"), List.of(filterCriteria));
            SubStep substep2 = createSubstep("Entered filter criteria: " + filterCriteria, true, filterTestData);
            substeps.add(substep2);
            // Locate and click Apply Filter button
            SqaObject applyFilterButton = extractSqaObjectWithName("Apply Filter Button");
            boolean clickedApplyFilterButton = iOsDriver.findElement(applyFilterButton).click();
            SubStep substep3 = createSubstep("Clicked on Apply Filter button", clickedApplyFilterButton, null);
            substeps.add(substep3);
            // Scroll to the relevant filtered result
            SqaObject filteredResultText = extractSqaObjectWithName("Filtered Result Verification Text");
            boolean isResultVerified = iOsDriver.scrollTillTextIsPresent(filteredResultText);
            SubStep substep4 = createSubstep("Scroll and verify filtered result: " + expectedText, isResultVerified, null);
            substeps.add(substep4);
            // Validate the filtered result matches the expected text
            boolean resultMatchesExpected = iOsDriver.findElement(filteredResultText).validateText(expectedText);
            SubStep substep5 = createSubstep("Validated filtered result matches expected text: " + expectedText, resultMatchesExpected, null);
            substeps.add(substep5);
            iOsDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return resultMatchesExpected;
        } catch (Exception e) {
            log.severe("Error occurred in filterTableAndVerifyLiveResults: " + e.getMessage());
            SubStep errorSubstep = createSubstep("Exception occurred during execution: " + e.getMessage(), false, null);
            substeps.add(errorSubstep);
            iOsDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return false;
        }
    }


	@SyncAction(uniqueId = "sample-open-notification-panel", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.WEB, description = "This action opens the notification panel and confirms the latest update"), objectRequired = false)
    public boolean openNotificationPanelAndConfirmLatestUpdate(String updateText) {
        SqaObject notificationPanelButton = extractSqaObjectWithName("Notification Panel button");
        SqaObject latestUpdateMessage = extractSqaObjectWithName("Latest Update message");
        List<SubStep> substeps = new ArrayList<>();
        boolean clickedOnNotificationPanel = webDriver.findElement(notificationPanelButton).click();
        log.info("Clicked on notification panel button");
        SubStep substep1 = createSubstep("Clicked on notification panel button", clickedOnNotificationPanel, null);
        substeps.add(substep1);
        boolean messageDisplayed = webDriver.findElement(latestUpdateMessage).elementIsDisplayed();
        log.info("Checked if latest update message is displayed");
        SubStep substep2 = createSubstep("Confirmed latest update message is displayed", messageDisplayed, null);
        substeps.add(substep2);
        if (messageDisplayed) {
            String messageText = webDriver.findElement(latestUpdateMessage).getText();
            log.info("Latest update text: " + messageText);
            boolean updateMatches = messageText.equals(updateText);
            log.info(updateMatches ? "Update text matches: " + updateText
                    : "Update text does not match. Expected: " + updateText + ", Found: " + messageText);
            SubStep substep3 = createSubstep("Validated latest update matches the expected text", updateMatches, null);
            substeps.add(substep3);
            webDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return updateMatches;
        } else {
            webDriver.getExecutionLogReporter().reportSubSteps(substeps);
            return false;
        }
    }


	@SyncAction(uniqueId = "sample-Reset Password", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.WEB, description = "This action edits the profile and checks if changes are reflected immediately"), objectRequired = false)
    public boolean resetPasswordAndValidateLoginWithNewCredentials(String email, String currentPassword, String newPassword, String confirmPassword, String urlToValidate) {
        SqaObject profileIcon = extractSqaObjectWithName("User link text");
        SqaObject resetPasswordLink = extractSqaObjectWithName("Reset Password link text");
        SqaObject currentPasswordBox = extractSqaObjectWithName("Current password text box");
        SqaObject newPasswordBox = extractSqaObjectWithName("New password text box");
        SqaObject reTypeNewPasswordBox = extractSqaObjectWithName("Re-type new password   text box");
        SqaObject reTypeNewPassword_1Box = extractSqaObjectWithName("Re-type new password  _1 text box");
        SqaObject submit = extractSqaObjectWithName("Submit button");
        SqaObject emailTextBox = extractSqaObjectWithName("Email text box");
        SqaObject passwordTextBox = extractSqaObjectWithName("Password text box");
        SqaObject login = extractSqaObjectWithName("Login button");
        List<SubStep> substeps = new ArrayList<>();
        boolean clickedOnProfile = webDriver.findElement(profileIcon).click();
        log.info("Cicked on profile icon");
        SubStep substep1 = createSubstep("Clicked on profile icon", clickedOnProfile, null);
        substeps.add(substep1);
        webDriver.findElement(resetPasswordLink).click();
        SubStep substep2 = createSubstep("Clicked on profile icon", clickedOnProfile, null);
        substeps.add(substep2);
        IQAWebElement element = webDriver.findElement(currentPasswordBox);
        element.click();
        element.enterText(currentPassword);
        Map<String, String> currPassTestData = formTestData(List.of("currentPassword"), List.of(currentPassword));
        SubStep substep3 = createSubstep("Clicked on profile icon", clickedOnProfile, currPassTestData);
        substeps.add(substep3);
        webDriver.findElement(newPasswordBox).click();
        SubStep substep4 = createSubstep("Entered new password", true, null);
        substeps.add(substep4);
        webDriver.findElement(newPasswordBox).enterText(newPassword);
        Map<String, String> newPasswordTestData = formTestData(List.of("newPassword"), List.of(newPassword));
        SubStep substep5 = createSubstep("Entered new password", true, newPasswordTestData);
        substeps.add(substep5);
        webDriver.findElement(reTypeNewPasswordBox).click();
        SubStep substep6 = createSubstep("Clicked on re-type new password box", true, null);
        substeps.add(substep6);
        webDriver.findElement(reTypeNewPassword_1Box).enterText(confirmPassword);
        Map<String, String> confirmPasswordTestData = formTestData(List.of("confirmPassword"),
        List.of(confirmPassword));
        SubStep substep7 = createSubstep("Entered re-typed new password", true, confirmPasswordTestData);
        substeps.add(substep7);
        webDriver.scrollToBottom();
        SubStep substep8 = createSubstep("Scrolled to bottom before submitting", true, null);
        substeps.add(substep8);
        webDriver.findElement(submit).click();
        SubStep substep9 = createSubstep("Clicked on submit button", true, null);
        substeps.add(substep9);
        boolean needToClick = true;
        int MAX_TIME_OUT = webDriver.getConfiguration().MAX_TIME_OUT();
        long startTime = System.currentTimeMillis();
        while (needToClick && (System.currentTimeMillis() - startTime) < MAX_TIME_OUT) {
            if (webDriver.findElement(submit).isEnabled()) {
                webDriver.findElement(submit).click();
                SubStep substep10 = createSubstep("Attempted clicking on submit button again", true, null);
                substeps.add(substep10);
                log.info("Clicked on submit button");
                needToClick = false;
            }
        }
        webDriver.findElement(emailTextBox).click();
        SubStep substep11 = createSubstep("Clicked on email text box", true, null);
        substeps.add(substep11);
        webDriver.findElement(emailTextBox).enterText(email);
        Map<String, String> emailTestData = formTestData(List.of("email"), List.of(email));
        SubStep substep12 = createSubstep("Entered email into email text box", true, emailTestData);
        substeps.add(substep12);
        webDriver.findElement(passwordTextBox).click();
        SubStep substep13 = createSubstep("Entered new password into password textbox", true, newPasswordTestData);
        substeps.add(substep13);
        webDriver.findElement(passwordTextBox).enterText(newPassword);
        SubStep substep14 = createSubstep("Entered password into password textbox", true, newPasswordTestData);
        substeps.add(substep14);
        webDriver.findElement(login).click();
        SubStep substep15 = createSubstep("Clicked on login button", true, null);
        substeps.add(substep15);
        if (!webDriver.findElement(profileIcon).elementIsDisplayed()) {
            SubStep substep16 = createSubstep("Failed to validate reset password as profile icon is not displayed",
            false, null);
            substeps.add(substep16);
            return false;
        }
        SubStep substep16 = createSubstep("Validated reset password with successful login", true, null);
        substeps.add(substep16);
        webDriver.getExecutionLogReporter().reportSubSteps(substeps);
        log.info("Validated reset password with change in url");
        return true;
    }


	@SyncAction(uniqueId = "sample-switch-theme-ui-transition", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.WEB, description = "This action switches the UI theme and verifies the transition"), objectRequired = false)
    public boolean switchThemeAndVerifyUITransition() {
        String themeToggleXPath = "//div[@class='theme-toggle-button']";
        String darkModeClassXPath = "//body[contains(@class, 'dark-mode')]";
        webDriver.findElement(FindBy.xpath(themeToggleXPath)).click();
        log.info("Clicked on theme toggle button");
        boolean isDarkModeApplied = webDriver.findElement(FindBy.xpath(darkModeClassXPath)).elementIsDisplayed();
        log.info(isDarkModeApplied ? "Dark mode successfully applied." : "Failed to apply dark mode.");
        return isDarkModeApplied;
    }


	@SyncAction(uniqueId = "sample-create-user-and-check-presence", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.WEB, description = "This action creates a user and checks their presence in the user list"), objectRequired = false)
    public boolean createUserAndCheckPresenceInUserList(String usernameInputId, String createButtonId, String userListXPath, String newUserName) {
        List<SubStep> substeps = new ArrayList<>();
        
        // Locate and interact with the username input field
        FindBy usernameInput = FindBy.id(usernameInputId);
        boolean movedToInput = webDriver.actions().moveToElement(usernameInput);
        SubStep substep1 = createSubstep("Moved to username input field", movedToInput, null);
        substeps.add(substep1);
        
        IQAWebElement inputElement = webDriver.findElement(usernameInput);
        inputElement.click();
        inputElement.enterText(newUserName);
        Map<String, String> testData = formTestData(List.of("newUserName"), List.of(newUserName));
        SubStep substep2 = createSubstep("Entered username: " + newUserName, true, testData);
        substeps.add(substep2);
    
        // Locate and interact with the create button
        FindBy createButton = FindBy.id(createButtonId);
        boolean movedToButton = webDriver.actions().moveToElement(createButton);
        SubStep substep3 = createSubstep("Moved to create button", movedToButton, null);
        substeps.add(substep3);
        
        boolean clickedCreateButton = webDriver.actions().leftClick(createButton);
        SubStep substep4 = createSubstep("Clicked on create button", clickedCreateButton, null);
        substeps.add(substep4);
    
        // Validate presence of the new user in the user list
        FindBy userList = FindBy.xpath(userListXPath);
        boolean userPresent = webDriver.findElement(userList).getText().contains(newUserName);
        SubStep substep5 = createSubstep("Checked for presence of user in user list: " + newUserName, userPresent, testData);
        substeps.add(substep5);
    
        webDriver.getExecutionLogReporter().reportSubSteps(substeps);
        return userPresent;
    }


	@SyncAction(uniqueId = "sample-trigger-export-verify-download", groupName = "Assertions", objectTemplate = @ObjectTemplate(name = TechnologyType.WEB, description = "This action triggers an export and verifies if a file is downloaded successfully"), objectRequired = false)
    public boolean triggerExportAndVerifyDownloadCompletion(String exportButtonId, String expectedFileName) {
        List<SubStep> substeps = new ArrayList<>();
    
        // Locate and click the export button
        FindBy exportButton = FindBy.id(exportButtonId);
        boolean clickedExportButton = webDriver.actions().leftClick(exportButton);
        SubStep substep1 = createSubstep("Clicked on export button", clickedExportButton, null);
        substeps.add(substep1);
    
        // Determine the user's download directory based on the OS
        String downloadDir = System.getProperty("os.name").toLowerCase().contains("win")
            ? System.getProperty("user.home") + "\\Downloads\\"
            : System.getProperty("user.home") + "/Downloads/";
    
        File expectedFile = new File(downloadDir + expectedFileName);
        long startTime = System.currentTimeMillis();
        boolean fileFound = false;
    
        // Wait for the file to appear in the downloads directory
        while ((System.currentTimeMillis() - startTime) < webDriver.getConfiguration().MAX_TIME_OUT()) {
            if (expectedFile.exists()) {
                fileFound = true;
                break;
            }
            try {
                Thread.sleep(1000); // Wait for 1 second before checking again
            } catch (InterruptedException e) {
                log.severe("Error during sleep while waiting for file download: " + e.getMessage());
            }
        }
    
        SubStep substep2 = createSubstep("Verified file download completion: " + expectedFileName, fileFound, null);
        substeps.add(substep2);
    
        webDriver.getExecutionLogReporter().reportSubSteps(substeps);
        return fileFound;
    
    }
    @SyncAction(uniqueId = "sample-uppercase-string", groupName = "Generic", 
    objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action converts a string to uppercase"), 
    objectRequired = false)
public boolean localcodeeditor(String value) {
    if (value == null) {
        log.info("The provided string is null.");
        return false;
    }
    String result = value.toUpperCase();
    log.info("Original String: " + value + ", Uppercase String: " + result);
    webDriver.getExecutionLogReporter().info("convertToUppercase executed with result: " + result);
    return true;
}
@SyncAction(uniqueId = "localcodjar", groupName = "Generic", 
    objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action converts a string to uppercase"), 
    objectRequired = false)
public boolean localcodeeditorjar(String value) {
    if (value == null) {
        log.info("The provided string is null.");
        return false;
    }
    String result = value.toUpperCase();
    log.info("Original String: " + value + ", Uppercase String: " + result);
    webDriver.getExecutionLogReporter().info("convertToUppercase executed with result: " + result);
    return true;
}
@SyncAction(uniqueId = "globalprojectlevel", groupName = "Generic", 
    objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action converts a string to uppercase"), 
    objectRequired = false)
public boolean globaljarprojectlevel(String value) {
    if (value == null) {
        log.info("The provided string is null.");
        return false;
    }
    String result = value.toUpperCase();
    log.info("Original String: " + value + ", Uppercase String: " + result);
    webDriver.getExecutionLogReporter().info("convertToUppercase executed with result: " + result);
    return true;
}
@SyncAction(uniqueId = "globalcheckDebugdkjhkwe", groupName = "Generic", 
    objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action global check at project level"), 
    objectRequired = false)
public boolean globaldebugnaemofmethod(String value) {
    if (value == null) {
        log.info("The provided string is null.");
        return false;
    }
    String result = value.toUpperCase();
    log.info("Original String: " + value + ", Uppercase String: " + result);
    webDriver.getExecutionLogReporter().info("convertToUppercase executed with result: " + result);
    return true;
}
@SyncAction(uniqueId = "uat-01", groupName = "Generic", 
    objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action converts a string to uppercase"), 
    objectRequired = false)
public boolean UATproject(String value) {
    if (value == null) {
        log.info("The provided string is null.");
        return false;
    }
    String result = value.toUpperCase();
    log.info("Original String: " + value + ", Uppercase String: " + result);
    webDriver.getExecutionLogReporter().info("convertToUppercase executed with result: " + result);
    return true;
}
@SyncAction(uniqueId = "uat-02", groupName = "Generic", 
    objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action converts a string to uppercase"), 
    objectRequired = false)
public boolean UATprojectlocal(String value) {
    if (value == null) {
        log.info("The provided string is null.");
        return false;
    }
    String result = value.toUpperCase();
    log.info("Original String: " + value + ", Uppercase String: " + result);
    webDriver.getExecutionLogReporter().info("convertToUppercase executed with result: " + result);
    return true;
}
@SyncAction(uniqueId = "uat-03", groupName = "Generic", 
    objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action converts a string to uppercase"), 
    objectRequired = false)
public boolean UATprojectlocallocal(String value) {
    if (value == null) {
        log.info("The provided string is null.");
        return false;
    }
    String result = value.toUpperCase();
    log.info("Original String: " + value + ", Uppercase String: " + result);
    webDriver.getExecutionLogReporter().info("convertToUppercase executed with result: " + result);
    return true;
}
@SyncAction(uniqueId = "uat-04", groupName = "Generic", 
    objectTemplate = @ObjectTemplate(name = TechnologyType.GENERIC, description = "This action converts a string to uppercase"), 
    objectRequired = false)
public boolean UATprojectlocallocalUAT(String value) {
    if (value == null) {
        log.info("The provided string is null.");
        return false;
    }
    String result = value.toUpperCase();
    log.info("Original String: " + value + ", Uppercase String: " + result);
    webDriver.getExecutionLogReporter().info("convertToUppercase executed with result: " + result);
    return true;
}

}
