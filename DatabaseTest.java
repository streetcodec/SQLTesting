package com.example.tests;

import com.example.utils.DatabaseUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    private static Connection connection;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DatabaseUtils.getConnection();
        connection.createStatement().execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(50), email VARCHAR(50), age INT)");
        connection.createStatement().execute("INSERT INTO users (id, name, email, age) VALUES (1, 'Alice', 'alice@example.com', 25)");
        connection.createStatement().execute("INSERT INTO users (id, name, email, age) VALUES (2, 'Bob', 'bob@example.com', 30)");
        connection.createStatement().execute("INSERT INTO users (id, name, email, age) VALUES (3, 'Charlie', 'charlie@example.com', 28)");
    }

    @AfterAll
    public static void teardownDatabase() throws SQLException {
        if (connection != null) {
            connection.createStatement().execute("DROP TABLE users");
            connection.close();
        }
    }

    @Test
    public void testUserCount() throws SQLException {
        ResultSet rs = DatabaseUtils.executeQuery("SELECT COUNT(*) AS totalUsers FROM users");
        rs.next();
        int count = rs.getInt("totalUsers");
        assertEquals(3, count, "User count should be 3.");
    }

    @Test
    public void testUserDataIntegrity() throws SQLException {
        ResultSet rs = DatabaseUtils.executeQuery("SELECT * FROM users WHERE id = 1");
        rs.next();
        assertEquals("Alice", rs.getString("name"), "User name should be 'Alice'");
        assertEquals("alice@example.com", rs.getString("email"), "Email should match");
        assertEquals(25, rs.getInt("age"), "Age should be 25");
    }

    @Test
    public void testDuplicateEmails() throws SQLException {
        ResultSet rs = DatabaseUtils.executeQuery("SELECT email, COUNT(*) FROM users GROUP BY email HAVING COUNT(*) > 1");
        assertFalse(rs.next(), "There should be no duplicate emails.");
    }

    @Test
    public void testAgeRange() throws SQLException {
        ResultSet rs = DatabaseUtils.executeQuery("SELECT * FROM users WHERE age < 18 OR age > 60");
        assertFalse(rs.next(), "No users should be younger than 18 or older than 60.");
    }

    @Test
    public void testPerformance() throws SQLException {
        long startTime = System.currentTimeMillis();
        ResultSet rs = DatabaseUtils.executeQuery("SELECT * FROM users");
        while (rs.next()) {
            rs.getInt("id");  // Simulate data processing
        }
        long endTime = System.currentTimeMillis();
        assertTrue((endTime - startTime) < 1000, "Query should execute within 1000 milliseconds.");
    }
}
