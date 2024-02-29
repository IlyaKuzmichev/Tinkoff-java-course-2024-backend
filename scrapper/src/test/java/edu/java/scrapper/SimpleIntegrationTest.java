package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@Testcontainers
public class SimpleIntegrationTest extends  IntegrationEnvironment {

    @Test
    public void testContainerIsRunning() {
        System.out.println("Container is Running");
    }

    @Test
    public void testExistingTablesInsideContainer() {
        try (
            Connection connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
            Statement statement = connection.createStatement()) {

            ResultSet resultSetUsers = statement.executeQuery("SELECT to_regclass('public.users')");
            assertTrue(resultSetUsers.next(), "Table 'users' does not exist");

            ResultSet resultSetAllLinks = statement.executeQuery("SELECT to_regclass('public.all_links')");
            assertTrue(resultSetAllLinks.next(), "Table 'all_links' does not exist");
        } catch (SQLException throwables) {
            throw new RuntimeException("Error while checking tables existence", throwables);
        }
    }
}
