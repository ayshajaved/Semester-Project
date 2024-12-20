import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Color;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class AdminClient extends Application {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final String ADMIN_PASSWORD = "123";
    private PrintWriter out;
    private TableView<Order> studentOrdersTable;
    private TableView<Order> facultyOrdersTable;
    private Map<String, MenuItem> menuItems;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("CUI Cafe Admin Client");
        // Load menu items and their quantities
        loadMenuItems();
        //Password prompt
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Admin Login");
        passwordDialog.setHeaderText("Enter Admin Password");
        passwordDialog.setContentText("Password:");

        passwordDialog.showAndWait().ifPresent(password -> {
            if (ADMIN_PASSWORD.equals(password)) {
                showAdminInterface(primaryStage);
                new Thread(this::connectToServer).start();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Password");
                alert.setContentText("The password you entered is incorrect.");
                alert.showAndWait();
            }
        });
    }

    private void showAdminInterface(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 10;");
        HBox ordersBox = new HBox(10);
        VBox studentBox = new VBox(10);
        Label studentOrdersLabel = new Label("Student Orders");
        studentOrdersLabel.setStyle(
        "-fx-text-fill: black;" +
        "-fx-font-size: 18;" +
        "-fx-background-color: light grey;"
        );
    
        studentOrdersTable = new TableView<>();
        setupOrderTable(studentOrdersTable);
        studentBox.getChildren().addAll(studentOrdersLabel, studentOrdersTable);

        VBox facultyBox = new VBox(10);
        Label facultyOrdersLabel = new Label("Faculty Orders");
        facultyOrdersLabel.setStyle(
        "-fx-text-fill: black;" +
        "-fx-font-size: 18;" +
        "-fx-background-color: light grey;"
        );
        facultyOrdersTable = new TableView<>();
        setupOrderTable(facultyOrdersTable);
        facultyBox.getChildren().addAll(facultyOrdersLabel, facultyOrdersTable);

        ordersBox.getChildren().addAll(studentBox, facultyBox);

        Button generateBillButton = new Button("Generate Bill");
        generateBillButton.setOnAction(e -> generateBill());
        Image backgroundImage = new Image("yoo.png");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        root.setBackground(new Background(background));
        root.getChildren().addAll(ordersBox, generateBillButton);

        Scene scene = new Scene(root,1200,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupOrderTable(TableView<Order> table) {
        TableColumn<Order, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        itemColumn.setPrefWidth(200);

        TableColumn<Order, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setPrefWidth(100);

        table.getColumns().addAll(itemColumn, quantityColumn);
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from server: " + message);
                if (message.startsWith("Order: Student")) {
                    String[] parts = message.split(":");
                    String item = parts[2].trim();
                    int quantity = Integer.parseInt(parts[3].trim());
                    studentOrdersTable.getItems().add(new Order(item, quantity));
                } else if (message.startsWith("Order: Faculty")) {
                    String[] parts = message.split(":");
                    String item = parts[2].trim();
                    int quantity = Integer.parseInt(parts[3].trim());
                    facultyOrdersTable.getItems().add(new Order(item, quantity));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateBill() {
        ObservableList<Order> studentOrders = studentOrdersTable.getItems();
        ObservableList<Order> facultyOrders = facultyOrdersTable.getItems();
        String studentBill = calculateBill(studentOrders);
        String facultyBill = calculateBill(facultyOrders);
        // Send bills to clients
        sendMessage("Bill: Student: " + studentBill);
        sendMessage("Bill: Faculty: " + facultyBill);
        // Clear orders and bills
        studentOrdersTable.getItems().clear();
        facultyOrdersTable.getItems().clear();
    }
    private String calculateBill(ObservableList<Order> orders) {
        StringBuilder bill = new StringBuilder();
        int total = 0;
        for (Order order : orders) {
            String item = order.getItem();
            int quantity = order.getQuantity();
            if (menuItems.containsKey(item) && menuItems.get(item).getQuantity() >= quantity) {
                int price = menuItems.get(item).getPrice();
                total += quantity * price;
                bill.append(item).append(" x ").append(quantity).append(" = $").append(quantity * price).append("\n");
                menuItems.put(item, new MenuItem(item, menuItems.get(item).getQuantity() - quantity, price));
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Item Unavailable");
                alert.setContentText("The item " + item + " is unavailable or insufficient quantity.");
                alert.showAndWait();
            }
        }
        bill.append("Total: $").append(total);
        return bill.toString();
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    private void loadMenuItems() {
        menuItems = new HashMap<>();
        try {
            File file = new File("cafemenu.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 4);
                if (parts.length >= 4) {
                    String item = parts[1].trim();
                    int quantity = Integer.parseInt(parts[2].trim());
                    int price = Integer.parseInt(parts[3].trim());
                    menuItems.put(item, new MenuItem(item, quantity, price));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Order {
        private final String item;
        private final int quantity;

        public Order(String item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public String getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static class MenuItem {
        private final String name;
        private final int quantity;
        private final int price;

        public MenuItem(String name, int quantity, int price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getPrice() {
            return price;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}