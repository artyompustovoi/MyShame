package org.example;

import java.sql.SQLException;
import java.util.List;


public class Main {
    public static void main(String[] args) throws SQLException {
        List<String> productInfoList = ProductManager.getAllProductsInfo(); //читаем из базы
        ProductManager.writeProductsToFile("test", productInfoList); //записываем
        productInfoList = ProductManager.readProductsFromFile("test");//читаем из файла
        for (String productInfo : productInfoList) { //выводим в консоль
            System.out.println(productInfo);
        }
    }
}
