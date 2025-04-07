module com.example.codeblooded {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.codeblooded to javafx.fxml;
    exports com.example.codeblooded;
}