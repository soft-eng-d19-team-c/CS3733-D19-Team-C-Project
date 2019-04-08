package base;

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
    INTERPRETERSERVICE("/views/interpreterService.fxml"),
    INTERNALTRANSPORTATIONSERVICE("/views/InternalTransportationServiceRequest.fxml"),
    EXTERNALTRANSPORTATIONSERVICE("/views/externalTransportationService.fxml"),
    SECURITYSERVICE("/views/securityServiceRequest.fxml"),
    PRESCRIPTIONSERVICE("/views/prescriptionService.fxml"),
    PRESCRIPTIONSERVICETABLE("/views/prescriptionServiceTable.fxml"),
    ITSERVICE("/views/itService.fxml"),
    GIFTSTORESERVICE("/views/giftStoreService.fxml"),
    RELIGIOUSSERVICE("/views/religiousservices.fxml"),
    FLORISTSERVICE("/views/FloristServiceRequest.fxml");

    protected String path;

    EnumScreenType(String str) {
        this.path = str;
    }

    public String getPath() {
        return path;
    }
}
