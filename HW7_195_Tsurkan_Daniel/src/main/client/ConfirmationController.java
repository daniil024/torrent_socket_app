package client;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ConfirmationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label fileName;

    @FXML
    private Label fileSize;

    @FXML
    private Button cancelButton;

    @FXML
    private Button uploadButton;


    /**
     * Method used to close confirmation page.
     */
    @FXML
    void cancelAction() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }


    /**
     * Method used to upload file.
     */
    @FXML
    void uploadAction() {
        DirectoryChooser dc = new DirectoryChooser();
        File file = dc.showDialog(cancelButton.getScene().getWindow());

        String inputPath = "";
        if (file != null && !file.isDirectory()) {
            Functions.showAlert("Вам необходимо выбрать папку для загрузки!");
            return;
        }
        if (file != null) {
            inputPath = file.getPath();
            Functions.receiveFile(inputPath);
            cancelAction();
            MainController.XCell.changeImg(Functions.button);
        }
    }

    @FXML
    void initialize() {
        fileName.setText(Functions.fileName);
        fileSize.setText(fileSize.getText() + Functions.fileSize.toString()+" bytes.");
    }
}
