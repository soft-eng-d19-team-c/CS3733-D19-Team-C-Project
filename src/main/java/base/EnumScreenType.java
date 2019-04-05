package base;

/**
 * This Enum is to store all of the screens that our app can switch to in the Facade design pattern.
 * @author Ryan LaMarche
 */
public enum EnumScreenType {
    NODETABLE("/views/prototypeNodeTable.fxml"),
    MAP("/views/prototypeMap.fxml"),
    NODEEDIT("/views/prototypeNodeEdit.fxml"),
    DOWNLOADCSV("/views/downloadscreen.fxml"),
    DASHBOARD("/views/assistScreen v3.fxml"),
    SEARCH("/views/autocompleteSearchBar.fxml"),
    LOGIN("/views/login screen example.fxml"),
    FINDPATH("/views/findpath.fxml"),
    SELECTLOCATION("/views/selectlocation.fxml"),
    REQUESTSERVICE("/views/requestServiceViewNew.fxml"),
    VIEWSERVICES("/views/ServiceRequestTableNew.fxml"),
    BOOKROOM("/views/bookLocationScreen.fxml"),
    BOOKEDLOCATIONS("/views/bookedLocationsTable.fxml");
    //DEVMAP("/views/../");


    protected String path;

    EnumScreenType(String str) {
        this.path = str;
    }

    public String getPath() {
        return path;
    }
}
