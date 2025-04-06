module com.example.codeblooded {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.codeblooded to javafx.fxml;
    exports com.example.codeblooded;
}