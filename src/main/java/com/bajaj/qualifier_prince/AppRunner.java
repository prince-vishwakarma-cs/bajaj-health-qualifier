package com.bajaj.qualifier_prince;

import com.bajaj.qualifier_prince.dto.FinalQuery;
import com.bajaj.qualifier_prince.dto.UserDetails;
import com.bajaj.qualifier_prince.dto.WebhookResponse;
import com.bajaj.qualifier_prince.services.ChallengeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);
    private final ChallengeService challengeService;

    public AppRunner(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("--- Start ---");

        UserDetails myDetails = new UserDetails(
                "Prince Vishwakarma",
                "0101CS221100",
                "pv913177@gmail.com"
        );

        WebhookResponse response = challengeService.generateWebhook(myDetails);

        if (response != null) {
            logger.info("Received Access Token: {}", response.accessToken());
            logger.info("Received Webhook URL: {}", response.webhook());

            String sqlQuery = """
                SELECT
                    e1.EMP_ID,
                    e1.FIRST_NAME,
                    e1.LAST_NAME,
                    d.DEPARTMENT_NAME,
                    COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
                FROM
                    EMPLOYEE e1
                JOIN
                    DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
                LEFT JOIN
                    EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e1.DOB < e2.DOB
                GROUP BY
                    e1.EMP_ID,
                    e1.FIRST_NAME,
                    e1.LAST_NAME,
                    d.DEPARTMENT_NAME
                ORDER BY
                    e1.EMP_ID DESC;""";

            FinalQuery solution = new FinalQuery(sqlQuery);

            challengeService.submitFinalQuery(response.webhook(), response.accessToken(), solution);
        } else {
            logger.error("Could not retrieve webhook. Halting process.");
        }

        logger.info("--- Done ---");
    }
}