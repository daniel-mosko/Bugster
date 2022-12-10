package sk.upjs.entity;

public class Severity {
    private int id;
    private String name;
    private int severityLevel;

    public Severity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(int severityLevel) {
        this.severityLevel = severityLevel;
    }

    @Override
    public String toString() {
        return name;
    }
}
