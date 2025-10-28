/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

/**
 *
 * @author 14432
 */
//class6-Record: Built by Yaya. to record all the scores and show the records to the corresponding players.

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record {

    private static final String fileName = "Record.txt";

    //to save the records.
    public static void saveRecord(String name, int HumanScore, int BankerScore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));//Actually I don't know how to record the scores with the local time so I ask chatGPT.
            writer.write(time + " " + name + " " + HumanScore + ":" + BankerScore);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing record: " + e.getMessage());
        }
    }

    //to increase interations, it can show the records to the corresponding players.
    public static void readRecords(String name) {
        System.out.println("***Past Record Scores***");
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(" " + name + " ")) {
                    System.out.println(line);
                    found = true;
                }
            }
        } catch (IOException e) {
            System.out.println("No previous records found.");
        }
        if (!found) {
            System.out.println("No record found for " + name);
        }
    }
}
