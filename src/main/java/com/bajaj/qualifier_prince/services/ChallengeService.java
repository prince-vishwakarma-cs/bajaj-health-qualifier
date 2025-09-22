package com.bajaj.qualifier_prince.services;

import com.bajaj.qualifier_prince.dto.FinalQuery;
import com.bajaj.qualifier_prince.dto.UserDetails;
import com.bajaj.qualifier_prince.dto.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChallengeService {

    private static final Logger logger = LoggerFactory.getLogger(ChallengeService.class);
    private final RestTemplate restTemplate;

    public ChallengeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WebhookResponse generateWebhook(UserDetails userDetails) {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        logger.info("Requesting webhook from URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDetails> requestEntity = new HttpEntity<>(userDetails, headers);

        try {
            ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(url, requestEntity, WebhookResponse.class);
            logger.info("Webhook generated successfully!");
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error generating webhook: ", e);
            return null;
        }
    }

    public void submitFinalQuery(String webhookUrl, String accessToken, FinalQuery finalQuery) {
        logger.info("Submitting final query to: {}", webhookUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        logger.info("Submitting Query: {}", finalQuery.finalQuery());
        logger.info("Using Token: {}", accessToken);

        headers.set("Authorization", accessToken);

        HttpEntity<FinalQuery> requestEntity = new HttpEntity<>(finalQuery, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(webhookUrl, HttpMethod.POST, requestEntity, String.class);
            logger.info("Submission successful! Status: {}, Response: {}", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            logger.error("Error submitting final query: ", e);
        }
    }
}

