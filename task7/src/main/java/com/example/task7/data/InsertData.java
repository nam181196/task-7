package com.example.task7.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static org.hibernate.cache.spi.support.SimpleTimestamper.next;

public class InsertData {
    private static final String URL = "jdbc:mysql://localhost:3306/users";
    private static final String USER = "root" ;
    private static final String PASSWORD = "" ;
    private static final String SQL_INSERT = "INSERT INTO user (first_name, middle_name, last_name) VALUES (?, ?, ?)";
    private static final int BATCH_SIZE = 1000;

    private static final String[] FIRSTNAMES = {"Nguyen", "Tran", "Le", "Pham", "Hoang", "Vu", "Dang", "Bui", "Do", "Ho"};
    private static final String[] MIDDLENAMES = {"Van", "Thi", "Huu", "Minh", "Gia", "Anh", "Tuan", "Thanh", "Duc", "Quoc"};
    private static final String[] LASTNAMES = {"An", "Binh", "Cuong", "Duong", "Hanh", "Hoa", "Khanh", "Linh", "Nam", "Phong"};

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Random random = new Random();

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(SQL_INSERT);

            for (int i = 1; i <= 5000000; i++) {
                String firstName = FIRSTNAMES[random.nextInt(FIRSTNAMES.length)];
                String middleName = MIDDLENAMES[random.nextInt(MIDDLENAMES.length)];
                String lastName = LASTNAMES[random.nextInt(LASTNAMES.length)];

                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, middleName);
                preparedStatement.setString(3, lastName);
                preparedStatement.addBatch();

                if (i % BATCH_SIZE == 0) {
                    preparedStatement.executeBatch();
                    connection.commit();
                    System.out.println("Inserted " + i + " records");
                }
            }

            // Insert remaining records
            preparedStatement.executeBatch();
            connection.commit();
            System.out.println("Inserted remaining records");

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
