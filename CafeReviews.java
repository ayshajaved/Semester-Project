import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CafeReviews {

    private ObservableList<Review> reviews;

    public TableView<Review> showReviews() {
        TableView<Review> tableView = new TableView<>();

        TableColumn<Review, String> designationColumn = new TableColumn<>("Designation");
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("designation"));

        TableColumn<Review, String> commentsColumn = new TableColumn<>("Comments");
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));

        TableColumn<Review, Integer> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        tableView.getColumns().add(designationColumn);
        tableView.getColumns().add(commentsColumn);
        tableView.getColumns().add(ratingColumn);

        reviews = FXCollections.observableArrayList();

        File file = new File("CafeReviews.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 3);
                if (parts.length == 3) {
                    String designation = parts[0];
                    String comments = parts[1];
                    int rating = Integer.parseInt(parts[2].trim());
                    reviews.add(new Review(designation, comments, rating));
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
        private final String comments;
        private final int rating;

        public Review(String designation, String comments, int rating) {
            this.designation = designation;
            this.comments = comments;
            this.rating = rating;
        }

        public String getDesignation() {
            return designation;
        }

        public String getComments() {
            return comments;
        }

        public int getRating() {
            return rating;
        }
    }
}