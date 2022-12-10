package sk.upjs.entity;

public class Bug {
    private Long id;
    private String description;
    private String createdAt;
    private String updatedAt;
    private long projectId;
    private long assignerId;
    private long assigneeId;
    private long statusId;
    private long severityId;

    public Bug() {
    }

    public Bug(Long id, String description, String createdAt, String updatedAt, long projectId, long assignerId, long assigneeId, long statusId, long severityId) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projectId = projectId;
        this.assignerId = assignerId;
        this.assigneeId = assigneeId;
        this.statusId = statusId;
        this.severityId = severityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(long assignerId) {
        this.assignerId = assignerId;
    }

    public long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public long getSeverityId() {
        return severityId;
    }

    public void setSeverityId(long severityId) {
        this.severityId = severityId;
    }

    @Override
    public String toString() {
        return "Bug{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", projectId=" + projectId +
                ", assignerId=" + assignerId +
                ", assigneeId=" + assigneeId +
                ", statusId=" + statusId +
                ", severityId=" + severityId +
                '}';
    }

}
