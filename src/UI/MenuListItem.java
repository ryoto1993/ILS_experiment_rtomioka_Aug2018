package UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class MenuListItem extends ListCell<MenuItemModel> {

    @FXML
    private ImageView icon_holder;
    @FXML
    private Label menu_text;
    @FXML
    private Label description_text;
    @FXML
    private HBox item_container;

    /**
     * コンストラクタ.
     * 初期化時に表示するNodeのインスタンスを生成しておく.
     */
    public MenuListItem() {
        initComponent();
    }

    private void initComponent() {
        FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource("menu_list_item.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(MenuItemModel menuItemModel, boolean empty) {
        super.updateItem(menuItemModel, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            menu_text.setText(menuItemModel.getTitleProperty().get());
            description_text.setText(menuItemModel.getDescriptionProperty().get());
            setGraphic(item_container);
        }
    }
}
