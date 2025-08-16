package in.house.financial.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiAiHelper {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    @Value("${gemini.api.key:}") // Use application properties to inject the API key
    private  String GEMINI_API_KEY;

    private final String singlePrompt = "give me the answer only in int his format category: { predicted category} party: {predictedParty} Predict the category (e.g., food, petrol, travel) and transaction party  who is involved in the transaction for the following transaction remark:";

    private final String batchPrompt = "give me the answer only in in this format list of [category: { predicted category} party: {predictedParty}, category: { predicted category} party: {predictedParty}] Predict the category (e.g., food, petrol, travel, Business, If human name involved add as Persons)  also number of remarks I am sending should match the number of results you are retuning and transaction party  who is involved in the transaction if human name found set that as party for the following transaction remarks read the remarks carefully and predict the category and party for each remark in the list: ";

    public List<String> predictTransactionPartyAndCategory(String remark) {
        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // Prepare request body
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            Map<String, String> parts = new HashMap<>();
            parts.put("text", singlePrompt + remark);
            content.put("parts", new Object[]{parts});
            requestBody.put("contents", new Object[]{content});

            // Send request
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    GEMINI_API_URL + "?key=" + GEMINI_API_KEY,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Parse response
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            String text = responseJson.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText();
            Pattern pattern = Pattern.compile("category: \\{(.*?)\\} party: \\{(.*?)\\}");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String category = matcher.group(1); // Extracts the first group (category)
                String party = matcher.group(2);    // Extracts the second group (party)

                System.out.println("Category: " + category);
                System.out.println("Party: " + party);
            } else {
                System.out.println("No match found!");
            }
            String aiCategory = responseJson.path("contents").path(0).path("parts").path(0).path("text").asText();
            String aiTransactionParty = responseJson.path("contents").path(0).path("parts").path(1).path("text").asText();

            // Return both category and transaction party
            return List.of(aiCategory, aiTransactionParty);
        } catch (Exception e) {
            log.error("Error predicting category and transaction party with Gemini API: {}", e.getMessage());
            return List.of("UNKNOWN", "UNKNOWN");
        }
    }

    public List<List<String>> predictBatchTransactionPartyAndCategory(List<String> remarks) {
        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // Prepare request body
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            Map<String, String> parts = new HashMap<>();
            parts.put("text", batchPrompt + String.join(", ", remarks));
            content.put("parts", new Object[]{parts});
            requestBody.put("contents", new Object[]{content});

            // Send request
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    GEMINI_API_URL + "?key=" + GEMINI_API_KEY,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Parse response
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            String textContent = responseJson.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText();

            // Extract the JSON string between "```json" and "```"
            Pattern jsonPattern = Pattern.compile("```json\\s*([\\s\\S]*?)\\s*```");
            Matcher jsonMatcher = jsonPattern.matcher(textContent);

            if (!jsonMatcher.find()) {
                log.error("No JSON block found in Gemini response: {}", textContent);
                return List.of(List.of("UNKNOWN", "UNKNOWN"));
            }

            String jsonString = jsonMatcher.group(1);

            // Parse the extracted JSON string
            JsonNode jsonArrayNode = objectMapper.readTree(jsonString);

            List<List<String>> predictions = new ArrayList<>();

            if (jsonArrayNode.isArray()) {
                for (JsonNode item : jsonArrayNode) {
                    if (item.has("category") && item.has("party")) {
                        String category = item.path("category").asText();
                        String party = item.path("party").asText();
                        predictions.add(List.of(category, party));
                    } else {
                        log.warn("JSON item missing 'category' or 'party' field: {}", item.toPrettyString());
                        // Optionally add UNKNOWN or skip
                        predictions.add(List.of("UNKNOWN", "UNKNOWN"));
                    }
                }
            } else {
                log.error("Expected JSON array but got: {}", jsonString);
                return List.of(List.of("UNKNOWN", "UNKNOWN"));
            }
            return predictions;
        } catch (Exception e) {
            log.error("Error predicting batch category and transaction party with Gemini API: {}", e.getMessage());
            return List.of(List.of("UNKNOWN", "UNKNOWN"));
        }
    }
}