/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.plantilla;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Plantilla extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public Plantilla() {
        setTitle("Tabla de Tarjetas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Número");
        model.addColumn("Tipo");
        model.addColumn("CSV");
        model.addColumn("Estado");

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        fetchCardData();
    }

    private void fetchCardData() {
        try {
            URL url = new URL("http://localhost:9000/api/v1/plantilla/list");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder jsonResult = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResult.append(scanner.nextLine());
                }
                scanner.close();

                String jsonString = jsonResult.toString();
                // Parse JSON array
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id_card");
                    String name = jsonObject.getString("name");
                    String number = jsonObject.getString("number");
                    String type = jsonObject.getString("type");
                    int csv = jsonObject.getInt("csv");
                    int status = jsonObject.getInt("status");

                    model.addRow(new Object[]{id, name, number, type, csv, status});
                }
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Plantilla plantilla = new Plantilla();
            plantilla.setVisible(true);
        });
    }
}