import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuClient extends Application {
    @Override
    public void start(Stage primaryStage) {
        //setting title
        primaryStage.setTitle("Cafe Menu");
        VBox root = new VBox(10);
        
        root.setStyle("-fx-padding: 20;");
        root.setPrefSize(800, 600);
        Image backgroundImage = new Image("yoo.png");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        root.setBackground(new Background(background));

        Text title = new Text("Cafe Menu");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Times New Roman", 30));
        root.getChildren().add(title);

        TableView<MenuItem> tableView = new TableView<>();
        tableView.setPrefWidth(750);

        TableColumn<MenuItem, String> juicesColumn = new TableColumn<>("Juices");
        juicesColumn.setCellValueFactory(new PropertyValueFactory<>("juices"));
        juicesColumn.setPrefWidth(250);

        TableColumn<MenuItem, String> fastFoodColumn = new TableColumn<>("Fast Food");
        fastFoodColumn.setCellValueFactory(new PropertyValueFactory<>("fastFood"));
        fastFoodColumn.setPrefWidth(250);

        TableColumn<MenuItem, String> desiFoodColumn = new TableColumn<>("Desi Food");
        desiFoodColumn.setCellValueFactory(new PropertyValueFactory<>("desiFood"));
        desiFoodColumn.setPrefWidth(250);

        tableView.getColumns().addAll(juicesColumn, fastFoodColumn, desiFoodColumn);

        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
        List<String> juices = new ArrayList<>();
        List<String> fastFood = new ArrayList<>();
        List<String> desiFood = new ArrayList<>();

        try {
            File file = new File("cafemenu.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 4);
                if (parts.length >= 4) {
                    String category = parts[0];
                    String item = parts[1] + " - $" + parts[2];

                    switch (category) {
                        case "Juices":
                            juices.add(item);
                            break;
                        case "Fast Food":
                            fastFood.add(item);
                            break;
                        case "Desi Food":
                            desiFood.add(item);
                            break;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //adding items to the table of menu
        for (int i = 0; i < juices.size(); i++) {
            String juiceItem = juices.get(i);
            String fastFoodItem = fastFood.get(i);
            String desiFoodItem = desiFood.get(i);
            menuItems.add(new MenuItem(juiceItem, fastFoodItem, desiFoodItem));
        }
        tableView.setItems(menuItems);
        root.getChildren().add(tableView);

        //for scrooling if contents in table exceeds
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        
        //setting scene
        Scene scene = new Scene(scrollPane,1200,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static class MenuItem {
        private final String juices;
        private final String fastFood;
        private final String desiFood;

        public MenuItem(String juices, String fastFood, String desiFood) {
            this.juices = juices;
            this.fastFood = fastFood;
            this.desiFood = desiFood;
        }

        public String getJuices() {
            return juices;
        }

        public String getFastFood() {
            return fastFood;
        }

        public String getDesiFood() {
            return desiFood;
        }
    }
}
