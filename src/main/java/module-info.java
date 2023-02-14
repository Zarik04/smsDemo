module com.example.smsdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;


    opens com.example.smsdemo to javafx.fxml;
    opens com.example.smsdemo.models to javafx.base;
    exports com.example.smsdemo;

    exports com.example.smsdemo.controllers.student;
    opens com.example.smsdemo.controllers.student to javafx.fxml;

    exports com.example.smsdemo.controllers.teacher;
    opens com.example.smsdemo.controllers.teacher to javafx.fxml;

    exports com.example.smsdemo.controllers.admin;
    opens com.example.smsdemo.controllers.admin to javafx.fxml;
}