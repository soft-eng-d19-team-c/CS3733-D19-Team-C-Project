package base;

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
    REQUESTSERVICE("/views/requestServiceView.fxml"),
    VIEWSERVICES("/views/ServiceRequestTableNew.fxml"),
    BOOKROOM("/views/.../.fxml");
    //DEVMAP("/views/../");



    protected String path;

    EnumScreenType(String str) {
        this.path = str;
    }

    public String getPath() {
        return path;
    }
}
