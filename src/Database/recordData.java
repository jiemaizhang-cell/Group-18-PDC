/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author Lenovo
 */
//Built by yaya, to read and write datas.
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class recordData {

    //write the player's name into database
    public static void insertLoginRecord(String playerName) {
        try (Connection conn = database.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS LOGIN_RECORD ("
                    + "LOGIN_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "PLAYER_NAME VARCHAR(30))");
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO LOGIN_RECORD (PLAYER_NAME) VALUES (?)");
            ps.setString(1, playerName);
            ps.executeUpdate();
            System.out.println("Login record saved for " + playerName);
        } catch (SQLException e) {
            System.out.println("Error in insertLoginRecord: " + e.getMessage());
        }
    }

    //write the every round record into database
    public static void insertRoundRecord(String name, int roundNo, int playerScore, int bankerScore, String result) {
        try (Connection conn = database.getConnection()) {

            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, "ROUND_RECORD", null);
            if (!rs.next()) {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE ROUND_RECORD ("
                        + "PLAY_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                        + "PLAYER_NAME VARCHAR(30), ROUND_NO INT, "
                        + "PLAYER_SCORE INT, BANKER_SCORE INT, RESULT VARCHAR(10))");
                System.out.println("ROUND_RECORD table created.");
            }

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO ROUND_RECORD (PLAYER_NAME, ROUND_NO, PLAYER_SCORE, BANKER_SCORE, RESULT) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setInt(2, roundNo);
            ps.setInt(3, playerScore);
            ps.setInt(4, bankerScore);
            ps.setString(5, result);
            ps.executeUpdate();
            System.out.println("Round " + roundNo + " record saved.");

        } catch (SQLException e) {
            System.out.println("Error in insertRoundRecord: " + e.getMessage());
        }
    }

    //write the final result into database
    public static void insertRecord(String name, int rounds, int playerWins, int bankerWins, String winner) {
        try (Connection conn = database.getConnection()) {

            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, "GAME_RECORD", null);
            if (!rs.next()) {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE GAME_RECORD ("
                        + "PLAY_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                        + "PLAYER_NAME VARCHAR(30), "
                        + "PLAY_ROUNDS INT, "
                        + "PLAYER_WINS INT, "
                        + "BANKER_WINS INT, "
                        + "WINNER VARCHAR(30))");
                System.out.println("GAME_RECORD table created.");
            }

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO GAME_RECORD (PLAY_TIME, PLAYER_NAME, PLAY_ROUNDS, PLAYER_WINS, BANKER_WINS, WINNER) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, name);
            ps.setInt(3, rounds);
            ps.setInt(4, playerWins);
            ps.setInt(5, bankerWins);
            ps.setString(6, winner);
            ps.executeUpdate();
            System.out.println("Game record saved to DB!");

        } catch (SQLException e) {
            System.out.println("Error in insertRecord: " + e.getMessage());
        }
    }

    //read all the records from database
    public static void showAllRecords() {
        try (Connection conn = database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM GAME_RECORD ORDER BY PLAY_TIME DESC")) {
            System.out.println("\n--- All Game Records ---");
            while (rs.next()) {
                Timestamp t = rs.getTimestamp("PLAY_TIME");
                String time = t.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                System.out.printf("%s | %s | %d rounds | %d:%d | Winner: %s%n",
                        time, rs.getString("PLAYER_NAME"),
                        rs.getInt("PLAY_ROUNDS"),
                        rs.getInt("PLAYER_WINS"),
                        rs.getInt("BANKER_WINS"),
                        rs.getString("WINNER"));
            }
        } catch (SQLException e) {
            System.out.println("Error in showAllRecords: " + e.getMessage());
        }
    }

    //read all the player's record from the database
    public static void showPlayerHistory(String name) {
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM GAME_RECORD WHERE PLAYER_NAME = ? ORDER BY PLAY_TIME DESC")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- " + name + "'s Game History ---");
            while (rs.next()) {
                Timestamp t = rs.getTimestamp("PLAY_TIME");
                String time = t.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                System.out.printf("%s | %d:%d | Winner: %s%n",
                        time, rs.getInt("PLAYER_WINS"), rs.getInt("BANKER_WINS"), rs.getString("WINNER"));
            }
        } catch (SQLException e) {
            System.out.println("Error in showPlayerHistory: " + e.getMessage());
        }
    }

    //read the rank of the players who play the most
    public static void showTopPlayers() {
        try (Connection conn = database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(
                "SELECT PLAYER_NAME, COUNT(*) AS GAMES FROM GAME_RECORD GROUP BY PLAYER_NAME ORDER BY GAMES DESC FETCH FIRST 5 ROWS ONLY")) {
            System.out.println("\n--- Top 5 Players ---");
            int rank = 1;
            while (rs.next()) {
                System.out.printf("%d. %s - %d games%n", rank++, rs.getString("PLAYER_NAME"), rs.getInt("GAMES"));
            }
        } catch (SQLException e) {
            System.out.println("Error in showTopPlayers: " + e.getMessage());
        }
    }
}
