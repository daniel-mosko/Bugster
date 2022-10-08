module sk.upjs.bugster {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens sk.upjs.bugster to javafx.fxml;
    exports sk.upjs.bugster;
    exports sk.upjs.bugster.Controllers;
    opens sk.upjs.bugster.Controllers to javafx.fxml;
}