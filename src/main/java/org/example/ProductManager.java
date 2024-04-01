package org.example;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {


    public static List<String> getAllProductsInfo() {
        List<String> productInfoList = new ArrayList<>();
        String sql = "SELECT p.name AS product_name, p.price, pt.bestBefore, pt.name AS type_name " +
                "FROM product p " +
                "INNER JOIN productType pt ON p.type_id = pt.id";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1q2w3e4r");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {


            while (resultSet.next()) {
                String productName = resultSet.getString("product_name");
                String priceString = resultSet.getString("price").replaceAll("[^\\d.]", "");
                double price = Double.parseDouble(priceString);
                String bestBefore = resultSet.getString("bestBefore");
                String typeName = resultSet.getString("type_name");

                String productInfo = String.format("Name: %s, Price: %.2f, Best Before: %s, Type: %s",
                        productName, price, bestBefore, typeName);
                productInfoList.add(productInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return productInfoList;
    }

    public static void main(String[] args) {
        ProductManager productManager = new ProductManager();
        List<String> productInfoList = productManager.getAllProductsInfo();

        for (String productInfo : productInfoList) {
            System.out.println(productInfo);
        }
    }
    public static void writeProductsToFile(String filename, List<String> productInfoList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String productInfo : productInfoList) {
                writer.write(productInfo);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<String> readProductsFromFile(String filename) {
        List<String> productInfoList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String productName = parts[0].trim();
                    String priceString = parts[1].trim().replaceAll("[^\\d.]", "");
                    double price = Double.parseDouble(priceString);
                    String bestBefore = parts[2].trim();
                    String typeName = parts[3].trim();
                    String productInfo = String.format("Name: %s, Price: %.2f, Best Before: %s, Type: %s",
                            productName, price, bestBefore, typeName);
                    productInfoList.add(productInfo);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return productInfoList;
    }
}
