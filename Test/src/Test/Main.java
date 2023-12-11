package Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.*;


public class Main {
    public static void main(String[] args) {

        String dburl = "jdbc:sqlite:/Users/james/Desktop/SQLite/mydb.db";

        try (Connection connection = DriverManager.getConnection(dburl)) {
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();

                String query = "SELECT * FROM table_1";

                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {

                        int ID = resultSet.getInt("ID");

                        String firstname = resultSet.getString("FIRSTNAME");

                        String lastname = resultSet.getString("LASTNAME");

                        int age = resultSet.getInt("AGE");

                        System.out.println("|Person ID:| " + ID +
                                " |firstname:| " + firstname +
                                " |lastname:| " + lastname +
                                " |Age:| " + age);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        startHttpServer();

    }

        static class RootHandler implements HttpHandler {
            public void handle(HttpExchange exchange) throws IOException {
                // Handle HTTP requests here
                String response = "Hello, this is the root handler!";
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }

        private static void startHttpServer() {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
                server.createContext("/", new RootHandler());
                server.setExecutor(null);
                server.start();
                System.out.println("The server is listening on port 8080");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}



