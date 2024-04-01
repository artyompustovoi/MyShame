package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductType {
    private int id;
    private String name;
    private String bestBefore;

    public ProductType(int id, String name, String bestBefore) {
        this.id = id;
        this.name = name;
        this.bestBefore = bestBefore;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(String bestBefore) {
        this.bestBefore = bestBefore;
    }


    public static List<ProductType> getAllProductTypes() throws SQLException {
        List<ProductType> productTypes = new ArrayList<>();
        String sql = "SELECT id, name, bestBefore FROM productType";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1q2w3e4r");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String bestBefore = resultSet.getString("bestBefore");
                productTypes.add(new ProductType(id, name, bestBefore));
            }
        }
        return productTypes;
    }

    public static void insertProductType(ProductType productType) throws SQLException {
        String sql = "INSERT INTO productType (name, bestBefore) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/your_database", "your_username", "your_password");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productType.getName());
            statement.setString(2, productType.getBestBefore());
            statement.executeUpdate();
        }
    }

}
