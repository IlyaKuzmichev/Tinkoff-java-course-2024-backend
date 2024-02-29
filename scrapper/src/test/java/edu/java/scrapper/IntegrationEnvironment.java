package edu.java.scrapper;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Testcontainers
public abstract class IntegrationEnvironment {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    private static void runMigrations(JdbcDatabaseContainer<?> c) {
        try {
            Path migrationsPath = Paths.get("../../../../../../../migrations");
            Path changelogPath = migrationsPath.resolve("master.xml");
            String containerChangelogPath = "/liquibase/changelog/master.xml";

            c.withCopyFileToContainer(MountableFile.forHostPath(changelogPath), containerChangelogPath);
            c.execInContainer("liquibase",
                "--changeLogFile=" + containerChangelogPath,
                "--url=" + c.getJdbcUrl(),
                "--username=" + c.getUsername(),
                "--password=" + c.getPassword(),
                "update"
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
