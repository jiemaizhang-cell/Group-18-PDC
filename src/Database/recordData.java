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
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class recordData {

    public static void insertRecord(String playerName, int round, int playerScore, int bankerScore, String winner) {
        String sql = """
            INSERT INTO GAME_RECORD 
            (PLAY_TIME, PLAYER_NAME, PLAY_ROUNDS, PLAYER_SCORE, BANKER_SCORE, WINNER)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, playerName);
            ps.setInt(3, round);
            ps.setInt(4, playerScore);
            ps.setInt(5, bankerScore);
            ps.setString(6, winner);
            ps.executeUpdate();

            System.out.println("Game record saved to embedded DB!");
        } catch (Exception e) {
            System.err.println("Failed to insert record: " + e.getMessage());
        }
    }
}
