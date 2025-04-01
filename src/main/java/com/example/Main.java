package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.example.models.DeliverTo;
import com.example.models.LoginBody;
import com.example.models.Order;
import com.example.models.Person;
import com.example.models.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    // Storing token globally
    private static String authToken = null;

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws IOException, InterruptedException {
        boolean found = false;
        // Repeat the prompt until a valid file is found
        while (!found) {
            Scanner filenameInput = new Scanner(System.in);
            System.out.print("Enter Filename: ");
            String filename = filenameInput.nextLine();

            try (InputStream is = Main.class.getClassLoader().getResourceAsStream(filename)) {
                if (is == null) {
                    System.out.println("File not found: " + filename);
                } else {
                    System.out.println("File found!!: " + filename);
                    filenameInput.close();
                    found = true;

                    // Reading csv file and skipping header
                    CSVReader reader = new CSVReaderBuilder(new InputStreamReader(is))
                            .withSkipLines(1)
                            .build();

                    String[] nextLine;

                    // Basically a for each loop for each line in the CSV
                    while ((nextLine = reader.readNext()) != null) {
                        String name = nextLine[0];
                        String address1 = nextLine[1];
                        String address2 = nextLine[2];
                        String address3 = nextLine[3];
                        String address4 = nextLine[4];
                        String postalCode = nextLine[5];
                        String email = nextLine[6];
                        String phoneNumber = nextLine[7];

                        Person person = new Person(name, address1, address2, address3, address4, postalCode, email,
                                phoneNumber);

                        Order order = makeOrder(person);

                        if (!sendOrder(order)) {
                            break;
                        }

                    }
                    reader.close();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static String getToken() throws JsonProcessingException, IOException, InterruptedException {

        Dotenv dotenv = Dotenv.load();
        // Fetching email and password from .env file
        String email = dotenv.get("EMAIL");
        String password = dotenv.get("PASSWORD");

        // If token already exists, use that and don't fetch a new one
        if (authToken != null) {
            return authToken;
        }

        LoginBody loginBody = new LoginBody(email, password);
        ObjectMapper objectMapper = new ObjectMapper();

        String body = objectMapper.writeValueAsString(loginBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://test-api.novatech.co.uk/token/get"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());

        TokenResponse token = objectMapper.readValue(response.body(), TokenResponse.class);

        authToken = token.getToken();

        return authToken;

    }

    public static Order makeOrder(Person person) throws JsonProcessingException {

        // Create a list for address lines, but filter out null or empty lines
        List<String> addressLines = List.of(
                person.getAddress1(),
                person.getAddress2(),
                person.getAddress3(),
                person.getAddress4()).stream()
                .filter(line -> line != null && !line.isEmpty())
                .toList();

        if (addressLines.size() < 2) {
            // If there are less than 2 address lines, throw an exception
            throw new IllegalArgumentException("At least two address lines are required. Order details: " +
                    "Name: " + person.getName() + ", Postal Code: " + person.getPostalCode() + ", Address Lines: " + addressLines);
        }

        String namePart = person.getName().replace(" ", "");
        String postalCodePart = person.getPostalCode().replace(" ", "");
        String orderNumber = (namePart + postalCodePart).toUpperCase();

        if (orderNumber.length() > 14) {
            orderNumber = orderNumber.substring(0, 14);
        }

        DeliverTo deliverTo = new DeliverTo(
                addressLines,
                person.getEmail(),
                person.getName(),
                person.getPhoneNumber(),
                person.getPostalCode());

        return new Order(orderNumber, deliverTo, person);
    }

    public static boolean sendOrder(Order order) throws JsonProcessingException, IOException, InterruptedException {

        ObjectMapper objectMapper = new ObjectMapper();
        String orderBody = objectMapper.writeValueAsString(order);

        String token = getToken();

        // Setting retry limit of 2 to account for token stuff
        int attemptLimits = 2;
        int attempt = 0;

        HttpClient client = HttpClient.newHttpClient();

        while (attempt <= attemptLimits) {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://test-api.novatech.co.uk/order"))
                    .POST(HttpRequest.BodyPublishers.ofString(orderBody))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 401) {
                System.out.println("Token expired. Refetching...");
                token = getToken();
            } else {
                Map<String, Object> responseBody = objectMapper.readValue(response.body(), Map.class);

                // If order was successful, output details and return true
                if (response.statusCode() <= 299) {
                    System.out.println("Order name: " + order.getPerson().getName());
                    System.out.println("Order number: " + responseBody.get("order_number"));
                    return true;
                } else {
                    return false;
                }

            }

            attempt++;

        }

        System.out.println("Failed to send order after " + (attemptLimits + 1) + " attempts.");
        return false;

    }

}
