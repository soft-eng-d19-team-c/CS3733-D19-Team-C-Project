package controller;

public enum EnumScreenType {
    NODETABLE("/views/prototypeNodeTable.fxml"),
    MAP("/views/prototypeMap.fxml"),
    NODEEDIT("/views/prototypeNodeEdit.fxml"),
    DOWNLOADCSV("/views/downloadscreen.fxml");

    protected String path;

    EnumScreenType(String str) {
        this.path = str;
    }

    public String getPath() {
        return path;
    }
}
