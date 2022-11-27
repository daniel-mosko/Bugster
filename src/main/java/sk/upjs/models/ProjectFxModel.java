package sk.upjs.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sk.upjs.entity.Project;

public class ProjectFxModel {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private Long id;

    public ProjectFxModel(Project project) {
        this.id = project.getId();
        name.set(project.getName());
        description.set(project.getName());
    }

    public ProjectFxModel() {
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public Project getProject() {
        return new Project(id, getName(), getDescription());
    }
}
