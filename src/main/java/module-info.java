module com.example.smtpmailclient_datacommf23 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.smtpmailclient_datacommf23 to javafx.fxml;
    exports com.example.smtpmailclient_datacommf23;
}