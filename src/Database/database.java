/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author Lenovo
 */
//Built by yaya, to set up the database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class database {

    private static final String DB_URL = "jdbc:derby:BlackjackDB;create=true";
    private static Connection conn = null;
    private static boolean isInitialized = false;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL);
                if (!isInitialized) {
                    System.out.println("Connected to embedded Derby database");
                    createTable(conn);
                    isInitialized = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return conn;
    }

    //create table in database
    private static void createTable(Connection conn) {
        String createTableSQL = """
            CREATE TABLE GAME_RECORD (
                ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                PLAY_TIME TIMESTAMP,
                PLAYER_NAME VARCHAR(50),
                PLAY_ROUNDS INT,
                PLAYER_SCORE INT,
                BANKER_SCORE INT,
                WINNER VARCHAR(50)
            )
            """;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("GAME_RECORD table created.");
        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                // X0Y32: Table already exists, asked from gpt
                System.out.println("Table GAME_RECORD already exists, skipping creation.");
            } else {
                System.err.println("Table creation failed: " + e.getMessage());
            }
        }
    }

    //close the database
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed.");
            }
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
                System.out.println("Derby DB shut down (embedded mode)");
            } catch (SQLException e) {
                if ("XJ015".equals(e.getSQLState())) {
                    // XJ015: Database has beed shut down.
                    System.out.println("Derby DB shut down successfully.");
                } else {
                    System.err.println("Error shutting down DB: " + e.getMessage());
                }
            }
        } catch (SQLException ignored) {
        }
    }
}
