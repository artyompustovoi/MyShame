package org.example;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Scanner;

public class PostgreSQLExample {

    public static void getFromPGSQL() {
        String jdbcUrl  = "jdbc:postgresql://localhost:5432/";
        String dbname   = "academy";
        String username = "student";
        String password = getPassword(dbname, username);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbcUrl + dbname, username, password);
        } catch (SQLException sqle) {
            System.out.println("Cannot connect to PostgreSQL");
        }
        if (connection != null) {
            String select = "SELECT name FROM student";
            System.out.println("Connected. Processing " + select);
            try(Statement st = connection.createStatement()) {
                ResultSet queryResult = st.executeQuery(select);
                String name = "";
                System.out.println(queryResult);
                while (queryResult.next()) {
                    name = queryResult.getString("name");
                    System.out.println(name);
                }
            } catch (SQLException ex) {
                System.out.println("Cannot execute query " + select);
            }

            String insert = "INSERT INTO student (name) VALUES (?)";
            try (PreparedStatement st = connection.prepareStatement(insert)) {
                //st.setInt(1, idCur);
                st.setString(1, "Asya");
                //st.setObject(4, date);
                st.executeUpdate();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Cannot execute query " + insert);
            }
            // Close the connection
            try {
                connection.close();
            } catch (SQLException sqle) {
                System.out.println("Cannot disconnect from PostgreSQL");
            }
        }
    }
    private static String getPassword(String dbname, String username) {
        String pss;
        try {
            pss = readPgpass(dbname, username);
        } catch (IOException ioe) {
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Cannot read pgpass file. Please provide password for db academy_top user tutor:");
            pss = keyboard.nextLine();
            keyboard.close();
        }
        return pss;
    }
    private static String readPgpass(String dbname, String username) throws IOException {
        // Join standard path for pgpass in our system
        Path filePath = Paths.get(
                System.getenv("APPDATA"),
                "postgresql",
                "pgpass.conf");
        // Open file
        String pss = "";
        String line = "";
        String[] parts;
        System.out.println(filePath.toString());
        File file = new File(filePath.toString());
        Scanner inputFile = new Scanner(file);
        // Read lines from the file until no more are left.
        while (inputFile.hasNext() && pss == "") {
            line = inputFile.nextLine();
            // hostname:port:dbname:username:password
            // 0        1    2      3        4
            System.out.println(line);
            parts = line.split(":");
            if (parts.length >= 5 && parts[2].equals(dbname) && parts[3].equals(username)) {
                pss = parts[4].trim();  // deleting end of line
                System.out.println(pss);
            }
        }
        // Close the file.
        inputFile.close();
        return pss;
    }
}