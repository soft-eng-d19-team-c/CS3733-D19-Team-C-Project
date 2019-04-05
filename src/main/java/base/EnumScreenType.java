package base;

/**
 * This Enum is to store all of the screens that our app can switch to in the Facade design pattern.
 * @author Ryan LaMarche
 */
public enum EnumScreenType {
    NODETABLE("/views/nodeTable.fxml"),
    NODEEDIT("/views/nodeEdit.fxml"),
    EDITMAP("/views/editMap.fxml"),
    DOWNLOADCSV("/views/download.fxml"),
    DASHBOARD("/views/universalDashboard.fxml"),
    LOGIN("/views/loginScreen.fxml"),
    PATHFINDING("/views/pathfinding.fxml"),
    SEARCHLOCATION("/views/searchLocation.fxml"),
    REQUESTSERVICE("/views/requestService.fxml"),
    VIEWSERVICEREQUESTS("/views/serviceRequestTable.fxml"),
    BOOKLOCATIONS("/views/bookLocation.fxml"),
    VIEWBOOKINGS("/views/bookedLocationsTable.fxml");


    protected String path;

    EnumScreenType(String str) {
        this.path = str;
    }

    public String getPath() {
        return path;
    }
}
