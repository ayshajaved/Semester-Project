import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ComsatsCafe extends Application {

    public void start(Stage primaryStage) {
        Pane pane=new Pane();
        Image backgroundImage = new Image("yoo.png");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(background));


     
        Text text=new Text("\n    CUI Cafe Management System");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Roboto", FontWeight.BOLD,40));
        text.setLayoutX(250);
        text.setLayoutY(250);
        pane.getChildren().add(text);

        Button studentCorner=new Button("Student Corner");
        studentCorner.setTextFill(Color.WHITE);
        studentCorner.setFont(Font.font("Roboto", FontWeight.BOLD,12));
        studentCorner.setPrefSize(120,40);
        studentCorner.setBackground(Background.EMPTY);
        pane.getChildren().add(studentCorner);
        studentCorner.setLayoutX(600);
        studentCorner.setLayoutY(500);
        studentCorner.setOnMouseEntered(e->{
            studentCorner.setTextFill(Color.ORANGE);
            studentCorner.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,null)));
        });
        studentCorner.setOnMouseExited(e->{studentCorner.setTextFill(Color.WHITE);
        studentCorner.setBackground(Background.EMPTY);
        });
        studentCorner.setOnAction(e -> {
            try{
                new StudentClient().start(new Stage());
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        });

        

        Button facultyCorner=new Button("Faculty Corner");
        facultyCorner.setTextFill(Color.WHITE);
        facultyCorner.setFont(Font.font("Roboto", FontWeight.BOLD,12));
        facultyCorner.setPrefSize(110,40);
        facultyCorner.setBackground(Background.EMPTY);
        pane.getChildren().add(facultyCorner);
        facultyCorner.setLayoutX(720);
        facultyCorner.setLayoutY(500);
        facultyCorner.setOnMouseEntered(e->{
            facultyCorner.setTextFill(Color.ORANGE);
            facultyCorner.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,null)));
        });
        facultyCorner.setOnMouseExited(e->{facultyCorner.setTextFill(Color.WHITE);
            facultyCorner.setBackground(Background.EMPTY);

        });
        facultyCorner.setOnAction(e -> {
            try {
                new FacultyClient().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button staff=new Button("Staff");
        staff.setTextFill(Color.WHITE);
        staff.setFont(Font.font("Roboto", FontWeight.BOLD,12));
        staff.setPrefSize(60,40);
        staff.setBackground(Background.EMPTY);
        pane.getChildren().add(staff);
        staff.setLayoutX(840);
        staff.setLayoutY(500);
        staff.setOnMouseEntered(e->{
            staff.setTextFill(Color.ORANGE);
            staff.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,null)));

        });
        staff.setOnMouseExited(e->{staff.setTextFill(Color.WHITE);
        staff.setBackground(Background.EMPTY);
        });
        staff.setOnAction(e -> {
            try {
                new AdminClient().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button cafeReviews=new Button("CafeReviews");
        cafeReviews.setTextFill(Color.WHITE);
        cafeReviews.setFont(Font.font("Roboto", FontWeight.BOLD,12));
        cafeReviews.setPrefSize(110,40);
        cafeReviews.setBackground(Background.EMPTY);
        pane.getChildren().add(cafeReviews);
        cafeReviews.setLayoutX(900);
        cafeReviews.setLayoutY(500);
        cafeReviews.setOnMouseEntered(e->{
            cafeReviews.setTextFill(Color.ORANGE);
            cafeReviews.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,null)));
        });
        cafeReviews.setOnMouseExited(e->{cafeReviews.setTextFill(Color.WHITE);
        cafeReviews.setBackground(Background.EMPTY);
        });
           cafeReviews.setOnAction(e -> {
            CafeReviews reviews = new CafeReviews();
            TableView<CafeReviews.Review> tableView = reviews.showReviews();
            Stage reviewStage = new Stage();
            reviewStage.setTitle("Cafe Reviews");
            Pane reviewPane = new Pane();

            Image bg = new Image("yoo.png");
        BackgroundSize backgroundS = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        BackgroundImage backg = new BackgroundImage(bg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundS);
        reviewPane.setBackground(new Background(backg));
            

        

            tableView.setPrefSize(750, 370);
            reviewPane.getChildren().add(tableView);

            Text selectingDesignation = new Text();
            selectingDesignation.setText("Select Designation:");
            selectingDesignation.setLayoutX(20);
            selectingDesignation.setLayoutY(400);
            selectingDesignation.setFill(Color.BLACK);
            selectingDesignation.setFont(Font.font("Times New Roman", 16));

            ComboBox<String> designationComboBox = new ComboBox<>();
            designationComboBox.getItems().addAll("Student", "Faculty");
            designationComboBox.setLayoutX(20);
            designationComboBox.setLayoutY(420);
            designationComboBox.setPrefSize(150, 30);

            Button filterByDesignation = new Button("Filter by Designation");
            filterByDesignation.setLayoutX(180);
            filterByDesignation.setLayoutY(420);
            filterByDesignation.setPrefSize(150, 30);
            filterByDesignation.setOnAction(event -> {
                String selectedDesignation = designationComboBox.getValue();
                if (selectedDesignation != null) {
                    ObservableList<CafeReviews.Review> filteredReviews = reviews.getReviews().stream()
                            .filter(review -> review.getDesignation().equalsIgnoreCase(selectedDesignation))
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
                    tableView.setItems(filteredReviews);
                }
            });

            Button sortByRating = new Button("Sort by Rating");
            sortByRating.setLayoutX(400);
            sortByRating.setLayoutY(420);
            sortByRating.setPrefSize(150, 30);
            sortByRating.setOnAction(event -> {
                ObservableList<CafeReviews.Review> sortedReviews = reviews.getReviews().stream()
                        .sorted(Comparator.comparingInt(CafeReviews.Review::getRating).reversed())
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                tableView.setItems(sortedReviews);
            });

            Button backButton = new Button("Back");
            backButton.setLayoutX(560);
            backButton.setLayoutY(420);
            backButton.setPrefSize(150, 30);
            backButton.setOnAction(event -> reviewStage.close());

            reviewPane.getChildren().addAll(selectingDesignation, designationComboBox, filterByDesignation, sortByRating, backButton);

            Scene reviewScene = new Scene(reviewPane, 1200,600);
            reviewStage.setScene(reviewScene);
            reviewStage.setResizable(false);
            reviewStage.show();
        });

        Button menu=new Button("Menu");
        menu.setTextFill(Color.WHITE);
        menu.setFont(Font.font("Roboto", FontWeight.BOLD,12));
        menu.setPrefSize(80,40);
        menu.setBackground(Background.EMPTY);
        pane.getChildren().add(menu);
        menu.setLayoutX(1020);
        menu.setLayoutY(500);
        menu.setOnMouseEntered(e->{
            menu.setTextFill(Color.ORANGE);
            menu.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,null)));
        });
        menu.setOnMouseExited(e->{menu.setTextFill(Color.WHITE);
        menu.setBackground(Background.EMPTY);
        });

        menu.setOnAction(e->{
            MenuClient menuClient = new MenuClient();
            try {
                new MenuClient().start(new Stage());  // Starting MenuClient with a new stage
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        Scene scene = new Scene(pane,1200,600);
        primaryStage.setTitle("CUI Menu");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
    

    public static void main(String[] args) {launch(args);}
}
class CafeReviews {
    private ObservableList<Review> reviews;

    public TableView<Review> showReviews() {
        TableView<Review> tableView = new TableView<>();

        TableColumn<Review, String> designationColumn = new TableColumn<>("Designation");
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("designation"));

        TableColumn<Review, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Review, String> commentsColumn = new TableColumn<>("Comments");
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));

        TableColumn<Review, Integer> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        tableView.getColumns().add(designationColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(commentsColumn);
        tableView.getColumns().add(ratingColumn);

        reviews = FXCollections.observableArrayList();

        File file = new File("CafeReviews.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 4);
                if (parts.length >= 4) {
                    String designation = parts[0];
                    String name = parts[1];
                    String comments = parts[2];
                    int rating = Integer.parseInt(parts[3]);
                    reviews.add(new Review(designation, name, comments, rating));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableView.setItems(reviews);
        return tableView;
    }

    public ObservableList<Review> getReviews() {
        return reviews;
    }

    public static class Review {
        private final String designation;
        private final String name;
        private final String comments;
        private final int rating;

        public Review(String designation, String name, String comments, int rating) {
            this.designation = designation;
            this.name = name;
            this.comments = comments;
            this.rating = rating;
        }

        public String getDesignation() {
            return designation;
        }

        public String getName() {
            return name;
        }

        public String getComments() {
            return comments;
        }

        public int getRating() {
            return rating;
        }
    }
}