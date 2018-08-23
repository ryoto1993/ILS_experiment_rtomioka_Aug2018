package UI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MenuItemModel {
    private StringProperty title = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();


    public MenuItemModel(String menu_title, String menu_description) {
        this.title.set(menu_title);
        this.description.set(menu_description);
    }

    public StringProperty getTitleProperty() {
        return title;
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }
}
