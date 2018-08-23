package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    ListView<MenuItemModel> menu_list;
    @FXML
    ScrollPane main_pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize menu list
        menu_list.setCellFactory(listView -> new MenuListItem());
        ObservableList<MenuItemModel> bookmarkModels = createMenuItemModels();
        menu_list.setItems(bookmarkModels);

        // Menu list listener
        menu_list.getSelectionModel().selectedIndexProperty().addListener(
                ( ov , old , current ) ->
                {
                    FXMLLoader loader = null;
                    switch (current.intValue()) {
                        case 0:
                            loader = new FXMLLoader(getClass().getResource("ils_control_pane.fxml"));
                            break;
                        case 1:
                            loader = new FXMLLoader(getClass().getResource("ils_control_pane.fxml"));
                            break;
                    }

                    try {
                        Pane newWindow = loader.load();
                        main_pane.setContent(newWindow);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        );
    }

    private ObservableList<MenuItemModel> createMenuItemModels() {
        ObservableList<MenuItemModel> menuItemModels = FXCollections.observableArrayList();


        MenuItemModel model = new MenuItemModel(
                "ILS Control" , "知的照明システムの各種操作を行います");
        menuItemModels.add(model);

        model = new MenuItemModel(
                "Light Viewer", "照明の調光状況を表示します"
        );
        menuItemModels.add(model);

        return menuItemModels;
    }

}
