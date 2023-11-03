module krwiles.productinventory {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    opens krwiles.productinventory to javafx.fxml, javafx.base;
    opens controller to javafx.fxml, javafx.base;
    opens model to javafx.base; //this line was added to fix a bug
    exports krwiles.productinventory;
    exports controller;
    exports model; //this line was added to export javadocs correctly
}