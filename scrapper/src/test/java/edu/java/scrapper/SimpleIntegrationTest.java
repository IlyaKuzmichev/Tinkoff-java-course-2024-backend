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
    public void testCheckExistingTablesInsideContainer() {
        try (
            Connection connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
            Statement statement = connection.createStatement()) {

            ResultSet resultSetUsers = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'users')");
            resultSetUsers.next();
            assertTrue(resultSetUsers.getBoolean(1), "Table 'users' does not exist");

            ResultSet resultSetAllLinks = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'all_links')");
            resultSetAllLinks.next();
            assertTrue(resultSetAllLinks.getBoolean(1), "Table 'all_links' does not exist");

        } catch (SQLException e) {
            throw new RuntimeException("Error while checking tables existence", e);
        }
    }

    @Test
    public void testExistingDataInTableInsideContainer() {
        try (
            Connection connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
            Statement statement = connection.createStatement()) {

            ResultSet resultSetUsersQuantity = statement.executeQuery("SELECT COUNT(*) AS quantity FROM users");
            resultSetUsersQuantity.next();
            assertTrue(resultSetUsersQuantity.getInt("quantity") == 3, "Error occured");


        } catch (SQLException e) {
            throw new RuntimeException("Error while checking tables existence", e);
        }
    }
}
