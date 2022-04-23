package client;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


public class MainController {


    static class XCell extends ListCell<String> {
        Image img = new Image("https://img.icons8.com/pastel-glyph/2x/upload.png", 25, 25, false, false);
        ImageView view = new ImageView(img);
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("", view);
        String lastItem;


        /**
         * Method used to open window with Confirmation Info.
         */
        @FXML
        void fileInfoAction() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmationPage.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Confirmation");
                stage.setScene(new Scene(root, 400, 200));
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(this.getScene().getWindow());
                stage.show();
            } catch (Exception ignored) {
            }
        }

        /**
         * Method used to change image for button, when the file was been loaded.
         * @param button button, for which we need to change img.
         */
        static void changeImg(Button button) {
            Image img = new Image("https://img.icons8.com/cute-clipart/2x/cloud-checked.png");
            ImageView view = new ImageView(img);
            view.setFitHeight(25);
            view.setFitWidth(25);
            button.setText("");
            button.setGraphic(view);
        }

        public XCell() {
            super();
            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Functions.button = button;
                    Functions.fileName = lastItem.split(":size:")[0];
                    Functions.fileSize = Long.parseLong(lastItem.split(":size:")[1]);
                    Functions.sendNameOfFile(Functions.fileName);
                    fileInfoAction();
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(item != null ? item.split(":size:")[0] : "<null>");
                setGraphic(hbox);
            }
        }
    }

    static ObservableList<String> list = FXCollections.observableArrayList();
    static ObservableList<String> names = FXCollections.observableArrayList();

    @FXML
    private TextField serverInput;

    @FXML
    private TextField portInput;

    @FXML
    private Button connectButton;

    @FXML
    private StackPane paneId;

    @FXML
    private ListView<String> lv;


    /**
     * Method used to connect to server and to fill our list of files.
     */
    @FXML
    void connectAction() {
        Functions.getConnection(serverInput.getText(), portInput.getText());

        list.addAll(Functions.files);
        for (var f : Functions.files)
            names.add(f.split(":size:")[0]);

        lv = new ListView<>(list);
        lv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new XCell();
            }
        });
        paneId.getChildren().add(lv);
    }

    @FXML
    void initialize() { }
}
