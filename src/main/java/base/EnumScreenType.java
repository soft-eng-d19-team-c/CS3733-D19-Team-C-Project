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
    BOOKLOCATIONS("/views/bookLocation.fxml"),
    VIEWBOOKINGS("/views/bookedLocationsTable.fxml"),
    SERVICESDASHBOARD("/views/servicesDashboard.fxml"),
    VIEWSERVICEREQUESTS("/views/serviceRequestTable.fxml"),
    SANITATIONSERVICE("/views/sanitationService.fxml"),
    INTERPRETERSERVICE("/views/servicesDashboard.fxml"),
    INTERNALTRANSPORTATIONSERVICE("/views/servicesDashboard.fxml"),
    EXTERNALTRANSPORTATIONSERVICE("/views/externalTransportationService.fxml"),
    SECURITYSERVICE("/views/servicesDashboard.fxml"),
    PRESCRIPTIONSERVICE("/views/prescriptionService.fxml"),
    PRESCRIPTIONSERVICETABLE("/views/prescriptionServiceTable.fxml"),
    ITSERVICE("/views/servicesDashboard.fxml"),
    GIFTSTORESERVICE("/views/servicesDashboard.fxml"),
    RELIGIOUSSERVICE("/views/servicesDashboard.fxml"),
    FLORISTSERVICE("/views/servicesDashboard.fxml");


    protected String path;

    EnumScreenType(String str) {
        this.path = str;
    }

    public String getPath() {
        return path;
    }
}
