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
public class GrokAiHelper {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    @Value("${grok.api.key}")
    private String GROQ_API_KEY; // Replace with actual key
    private static final String MODEL_NAME = "qwen/qwen3-32b";

    private final String singlePrompt = "give me the answer only in this format category: { predicted category} party: {predictedParty} Predict the category (e.g., food, petrol, travel) and transaction party who is involved in the transaction for the following transaction remark:";

    private final String batchPrompt = "give me the answer only in this format list of [category: { predicted category} party: {predictedParty}, category: { predicted category} party: {predictedParty}]  also number of remarks I am sending should match the number of results you are retuning and also should be in same order Predict the category (e.g., food, petrol, travel, Business, If human name involved add as Persons, if any snack name found add as food) and transaction party who is involved in the transaction if human name found set that as party for the following transaction remarks read the remarks carefully and predict the category and party for each remark in the list: ";

    public List<String> predictTransactionPartyAndCategory(String remark) {
        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + GROQ_API_KEY);

            // Prepare request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", MODEL_NAME);
            requestBody.put("reasoning_format","hidden");
            requestBody.put("messages", List.of(
                Map.of(
                    "role", "user",
                    "content", singlePrompt + remark
                )
            ));

            // Send request
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    GROQ_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            log.info("Grok API response: {}", response.getBody());
            // Parse response
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            String text = responseJson.path("choices").path(0).path("message").path("content").asText();
            
            Pattern pattern = Pattern.compile("category: \\{(.*?)\\} party: \\{(.*?)\\}");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String category = matcher.group(1).trim();
                String party = matcher.group(2).trim();
                return List.of(category, party);
            }

            log.warn("No matching pattern found in Grok response: {}", text);
            return List.of("UNKNOWN", "UNKNOWN");
            
        } catch (Exception e) {
            log.error("Error predicting category and transaction party with Grok API: {}", e.getMessage());
            return List.of("UNKNOWN", "UNKNOWN");
        }
    }

public List<List<String>> predictBatchTransactionPartyAndCategory(List<String> remarks) {
    try {
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + GROQ_API_KEY);

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL_NAME);
        requestBody.put("reasoning_format", "hidden");
        requestBody.put("messages", List.of(
            Map.of(
                "role", "user",
                "content", batchPrompt + String.join(", ", remarks)
            )
        ));

        // Send request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                GROQ_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );
        log.info("Grok API response: {}", response.getBody());

        // Parse response
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        String content = responseJson.path("choices").path(0).path("message").path("content").asText();
        
        List<List<String>> result = new ArrayList<>();
        
        // Updated pattern to match the new format with curly braces
        Pattern pattern = Pattern.compile("category:\\s*\\{([^}]+)\\}\\s*party:\\s*\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String category = matcher.group(1).trim();
            String party = matcher.group(2).trim();
            result.add(List.of(category, party));
        }

        if (result.isEmpty()) {
            log.warn("No predictions found in Grok response: {}", content);
            return List.of(List.of("UNKNOWN", "UNKNOWN"));
        }

        return result;

    } catch (Exception e) {
        log.error("Error predicting batch category and transaction party with Grok API: {}", e.getMessage());
        return List.of(List.of("UNKNOWN", "UNKNOWN"));
    }
}
}