/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author Lenovo
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class database {

    private static final String DB_URL = "jdbc:derby:BlackjackDB;create=true";
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Connected to embedded Derby database");
                createTable(conn);
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
            }
        }
        return conn;
    }

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
            if (e.getSQLState().equals("X0Y32")) {
                //X0Y32=table already exists, asked from gpt
                System.out.println("Table GAME_RECORD already exists, skipping creation.");
            } else {
                System.err.println("Table creation failed: " + e.getMessage());
            }
        }
    }

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
                if (e.getSQLState().equals("XJ015")) {
                    //XJ015=table has been closed
                    System.out.println("Derby DB shut down successfully.");
                } else {
                    System.err.println("Error shutting down DB: " + e.getMessage());
                }
            }
        } catch (SQLException ignored) {
        }
    }
}
