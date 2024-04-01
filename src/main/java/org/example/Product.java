package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private int typeId;
    private Timestamp produce;
    private double price;

    public Product(int id, String name, int typeId, Timestamp produce, double price) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.produce = produce;
        this.price = price;
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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Timestamp getProduce() {
        return produce;
    }

    public void setProduce(Timestamp produce) {
        this.produce = produce;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public static List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, type_id, produce, price FROM product";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1q2w3e4r");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int typeId = resultSet.getInt("type_id");
                Timestamp produce = resultSet.getTimestamp("produce");
                //double price = resultSet.getDouble("price"); //
                String priceString = resultSet.getString("price").replaceAll("[^\\d.]", "");
                double price = Double.parseDouble(priceString);

                products.add(new Product(id, name, typeId, produce, price));
            }
        }
        return products;
    }

    public static void insertProduct(Product product) throws SQLException {
        String sql = "INSERT INTO product (name, type_id, produce, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/your_database", "your_username", "your_password");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setInt(2, product.getTypeId());
            statement.setTimestamp(3, product.getProduce());
            statement.setDouble(4, product.getPrice());
            statement.executeUpdate();
        }
    }

}