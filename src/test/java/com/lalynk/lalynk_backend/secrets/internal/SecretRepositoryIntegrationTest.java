
package com.lalynk.lalynk_backend.secrets.internal;

import com.lalynk.lalynk_backend.secrets.internal.Secret;
import com.lalynk.lalynk_backend.secrets.internal.SecretRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class SecretRepositoryIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("lalynk_test").withUsername("test").withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private SecretRepository secretRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void secretCanOnlyBeConsumedOnce() {
        UUID userId = UUID.randomUUID();
        String publicToken = UUID.randomUUID().toString();

        Secret secret = new Secret(
                userId,
                Instant.now().plus(7, ChronoUnit.DAYS),
                "encrypted-test-content",
                publicToken
        );

        secretRepository.saveAndFlush(secret);

        int firstAttempt =
                secretRepository.consumeIfAvailable(publicToken);

        int secondAttempt =
                secretRepository.consumeIfAvailable(publicToken);

        assertEquals(
                1,
                firstAttempt,
                "The first attempt should consume the secret"
        );

        assertEquals(
                0,
                secondAttempt,
                "The second attempt must not consume it again"
        );
    }


    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void concurrentAttemptsCanOnlyConsumeSecretOnce() throws Exception {
        UUID userId = UUID.randomUUID();
        String publicToken = UUID.randomUUID().toString();

        Secret secret = new Secret(
                userId,
                Instant.now().plus(7, ChronoUnit.DAYS),
                "encrypted-test-content",
                publicToken
        );

        TransactionTemplate transactionTemplate =
                new TransactionTemplate(transactionManager);

        // Save the secret before starting the concurrent attempts.
        transactionTemplate.executeWithoutResult(status ->
                secretRepository.saveAndFlush(secret)
        );

        int numberOfThreads = 10;

        ExecutorService executor =
                Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch ready = new CountDownLatch(numberOfThreads);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Callable<Integer> attempt = () -> {
                // Signal that this thread is ready.
                ready.countDown();

                // Wait until all threads are ready to start.
                start.await();

                // Try to consume the secret in its own transaction.
                return transactionTemplate.execute(status ->
                        secretRepository.consumeIfAvailable(publicToken)
                );
            };

            List<Future<Integer>> futures = new ArrayList<>();

            // Submit all attempts to the thread pool.
            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executor.submit(attempt));
            }

            // Wait until all threads have reached the start gate.
            ready.await();

            // Release all threads to attempt consumption.
            start.countDown();

            int successfulAttempts = 0;

            // Collect the results from all threads.
            for (Future<Integer> future : futures) {
                successfulAttempts += future.get();
            }

            assertEquals(
                    1,
                    successfulAttempts,
                    "Exactly one of the concurrent attempts should consume the secret"
            );
        } finally {
            // Clean up the thread pool.
            executor.shutdownNow();
        }
    }


    @Test
    void revokedSecretCannotBeConsumed() {
        UUID userId = UUID.randomUUID();
        String publicToken = UUID.randomUUID().toString();

        Secret secret = new Secret(
                userId,
                Instant.now().plus(7, ChronoUnit.DAYS),
                "encrypted-test-content",
                publicToken
        );

        secretRepository.saveAndFlush(secret);

        int revokeResult = secretRepository.revokeIfAvailable(
                secret.getId(),
                userId
        );

        int consumeResult =
                secretRepository.consumeIfAvailable(publicToken);

        assertEquals(
                1,
                revokeResult,
                "The secret should be revoked successfully"
        );

        assertEquals(
                0,
                consumeResult,
                "A revoked secret must not be consumed"
        );
    }

    @Test
    void expiredSecretCannotBeConsumed() {
        UUID userId = UUID.randomUUID();
        String publicToken = UUID.randomUUID().toString();

        Secret secret = new Secret(
                userId,
                Instant.now().minus(1, ChronoUnit.DAYS),
                "encrypted-test-content",
                publicToken
        );

        secretRepository.saveAndFlush(secret);

        int result = secretRepository.consumeIfAvailable(publicToken);

        assertEquals(
                0,
                result,
                "An expired secret must not be consumed"
        );
    }

    @Test
    void userCannotRevokeAnotherUsersSecret() {
        UUID ownerId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();
        String publicToken = UUID.randomUUID().toString();

        Secret secret = new Secret(
                ownerId,
                Instant.now().plus(7, ChronoUnit.DAYS),
                "encrypted-test-content",
                publicToken
        );

        secretRepository.saveAndFlush(secret);

        int revokeResult = secretRepository.revokeIfAvailable(
                secret.getId(),
                anotherUserId
        );

        int consumeResult =
                secretRepository.consumeIfAvailable(publicToken);

        assertEquals(
                0,
                revokeResult,
                "A user must not revoke another user's secret"
        );

        assertEquals(
                1,
                consumeResult,
                "The secret should remain consumable by its valid token"
        );
    }

}
