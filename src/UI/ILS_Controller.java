package UI;

import ILS.ILS;
import Manager.Manager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ILS_Controller implements Initializable {
    @FXML
    Button button_start;
    @FXML
    Button button_stop;
    @FXML
    Button button_resume;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ILS ils = Manager.getIls();
    }
}
