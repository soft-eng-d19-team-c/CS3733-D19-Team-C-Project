package base;

public enum EnumScreenType {
    NODETABLE("/views/nodeTable.fxml"),
    NODEEDIT("/views/nodeEdit.fxml"),
    EDITMAP("/views/mapeditpage.fxml"),
    DOWNLOADCSV("/views/download.fxml"),
    DASHBOARD("/views/universalDashboard.fxml"),
    LOGIN("/views/loginScreen.fxml"),
    PATHFINDING("/views/findpathscreen.fxml"),
    SEARCHLOCATION("/views/searchLocation.fxml"),
    BOOKLOCATIONSCALENDAR("/views/bookLocationCalendar.fxml"),
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
    BOOKLOCATIONMAP("/views/bookLocationMap.fxml"),
    // service request tables,
    SERVICEREQUESTTABLESDASHBOARD("/views/serviceRequestTablesDashboard.fxml"),
    SANITATIONSERVICETABLE("/views/sanitationServiceRequestTable.fxml"),
    INTERPRETERSERVICETABLE("/views/interpreterServiceRequestTable.fxml"),
    INTERNALTRANSPORTATIONSERVICETABLE("/views/internalTransportationServiceRequestTable.fxml"),
    EXTERNALTRANSPORTATIONSERVICETABLE("/views/externalTransportationServiceRequestTable.fxml"),
    SECURITYSERVICETABLE("/views/securityServiceRequestTable.fxml"),
    PRESCRIPTIONSERVICETABLE("/views/prescriptionServiceTable.fxml"),
    ITSERVICETABLE("/views/ITServiceRequestTable.fxml"),
    GIFTSTORESERVICETABLE("/views/giftStoreRequestTable.fxml"),
    RELIGIOUSSERVICETABLE("/views/religiousRequestTable.fxml"),
    FLORISTSERVICETABLE("/views/floristServiceRequestTable.fxml"),
    WELCOME("/views/screensaver.fxml"),
    ABOUT("/views/aboutPage.fxml"),
    ADMIN("/views/adminDashboard.fxml"),
    APIS("/views/apiDashboard.fxml"),
    PATHFINDINGROBOT("/views/ROBOT_findpathscreen.fxml");

    protected String path;

    EnumScreenType(String str) {
        this.path = str;
    }

    public String getPath() {
        return path;
    }
}
