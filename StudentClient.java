import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;

public class StudentClient extends Application {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private PrintWriter out;
    private TextArea billArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CUI Cafe Student Client");

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 10;");

        Label menuLabel = new Label("Menu:");
        TableView<MenuItem> menuTable = new TableView<>();
        setupMenuTable(menuTable);

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Button orderButton = new Button("Order");
        orderButton.setOnAction(e -> {
            MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
            String quantity = quantityField.getText();
            if (selectedItem != null && !quantity.isEmpty()) {
                sendMessage("Order: Student: " + selectedItem.getName() + ": " + quantity);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order Placed");
                alert.setHeaderText(null);
                alert.setContentText("Your order has been placed.");
                alert.showAndWait();
            }
        });

        Label billLabel = new Label("Bill:");
        billArea = new TextArea();
        billArea.setEditable(false);

        Label reviewLabel = new Label("Submit Review:");
        TextField reviewField = new TextField();
        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating (1-5)");
        Button submitReviewButton = new Button("Submit");
        submitReviewButton.setOnAction(e -> {
            String review = reviewField.getText();
            String rating = ratingField.getText();
            if (!review.isEmpty() && !rating.isEmpty()) {
                sendMessage("Review: Student: " + review + ": " + rating);
                reviewField.clear();
                ratingField.clear();
            }
        });

        root.getChildren().addAll(menuLabel, menuTable, quantityField, orderButton, billLabel, billArea, reviewLabel, reviewField, ratingField, submitReviewButton);

        Scene scene = new Scene(root,1200,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        //new thread to start the connection
        new Thread(this::connectToServer).start();
    }

    private void setupMenuTable(TableView<MenuItem> table) {
        TableColumn<MenuItem, String> nameColumn = new TableColumn<>("Item");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);

        TableColumn<MenuItem, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(100);

        table.getColumns().addAll(nameColumn, priceColumn);

        table.setItems(loadMenuItems());
    }

    private ObservableList<MenuItem> loadMenuItems() {
        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
        try {
            File file = new File("cafemenu.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 4);
                if (parts.length >= 4) {
                    String item = parts[1].trim();
                    int price = Integer.parseInt(parts[3].trim());
                    menuItems.add(new MenuItem(item, price));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                final String finalMessage = message;
                System.out.println("Received from server: " + finalMessage);
                if (finalMessage.startsWith("Bill: Student")) {
                    Platform.runLater(() -> showBillWindow(finalMessage));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showBillWindow(String bill) {
        Stage billStage = new Stage();
        billStage.setTitle("Student Bill");

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 10;");

        TextArea billArea = new TextArea(bill);
        billArea.setEditable(false);

        Label reviewLabel = new Label("Submit Review:");
        TextField reviewField = new TextField();
        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating (1-5)");
        Button submitReviewButton = new Button("Submit");
        submitReviewButton.setOnAction(e -> {
            String review = reviewField.getText();
            String rating = ratingField.getText();
            if (!review.isEmpty() && !rating.isEmpty()) {
                sendMessage("Review: Student: " + review + ": " + rating);
                billStage.close();
            }
        });

        root.getChildren().addAll(billArea, reviewLabel, reviewField, ratingField, submitReviewButton);

        Scene scene = new Scene(root, 300, 400);
        billStage.setScene(scene);
        billStage.show();
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class MenuItem {
        private final String name;
        private final int price;

        public MenuItem(String name, int price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }
    }
}