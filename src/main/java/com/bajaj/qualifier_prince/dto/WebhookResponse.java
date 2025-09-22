package com.bajaj.qualifier_prince.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WebhookResponse(String webhook, String accessToken) {
}