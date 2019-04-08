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
    // services
    SERVICESDASHBOARD("/views/servicesDashboard.fxml"),
    SANITATIONSERVICE("/views/sanitationService.fxml"),
    INTERPRETERSERVICE("/views/interpreterService.fxml"),
    INTERNALTRANSPORTATIONSERVICE("/views/InternalTransportationServiceRequest.fxml"),
    EXTERNALTRANSPORTATIONSERVICE("/views/externalTransportationService.fxml"),
    SECURITYSERVICE("/views/securityServiceRequest.fxml"),
    PRESCRIPTIONSERVICE("/views/prescriptionService.fxml"),
    ITSERVICE("/views/itService.fxml"),
    GIFTSTORESERVICE("/views/giftStoreService.fxml"),
    RELIGIOUSSERVICE("/views/religiousservices.fxml"),
    FLORISTSERVICE("/views/FloristServiceRequest.fxml"),
    // service request tables,
    SERVICEREQUESTTABLESDASHBOARD("/views/serviceRequestTablesDashboard.fxml"),
    SANITATIONSERVICETABLE("/views/sanitationServiceRequestTable.fxml"),
    INTERPRETERSERVICETABLE("/views/interpreterService.fxml"),
    INTERNALTRANSPORTATIONSERVICETABLE("/views/InternalTransportationServiceRequest.fxml"),
    EXTERNALTRANSPORTATIONSERVICETABLE("/views/externalTransportationService.fxml"),
    SECURITYSERVICETABLE("/views/securityServiceRequest.fxml"),
    PRESCRIPTIONSERVICETABLE("/views/prescriptionServiceTable.fxml"),
    ITSERVICETABLE("/views/itService.fxml"),
    GIFTSTORESERVICETABLE("/views/giftStoreService.fxml"),
    RELIGIOUSSERVICETABLE("/views/religiousservices.fxml"),
    FLORISTSERVICETABLE("/views/FloristServiceRequest.fxml");

    protected String path;

    EnumScreenType(String str) {
        this.path = str;
    }

    public String getPath() {
        return path;
    }
}
