package efd.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModellingscenarioDTO {
    private String id;
    private String notes;
    private Integer version;
    private String author;
    private java.sql.Timestamp date;
    private String description;
    private String title;
    private String projectProjectId;
    private String studyId;
    private String modelType;
    private String livelihoodZoneLzid;
    private String foodsubstitutionId;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public java.sql.Timestamp getDate() {
        return this.date;
    }

    public void setDate(java.sql.Timestamp date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProjectProjectId() {
        return this.projectProjectId;
    }

    public void setProjectProjectId(String projectProjectId) {
        this.projectProjectId = projectProjectId;
    }

    public String getStudyId() {
        return this.studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getModelType() {
        return this.modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getLivelihoodZoneLzid() {
        return this.livelihoodZoneLzid;
    }

    public void setLivelihoodZoneLzid(String livelihoodZoneLzid) {
        this.livelihoodZoneLzid = livelihoodZoneLzid;
    }

    public String getFoodsubstitutionId() {
        return this.foodsubstitutionId;
    }

    public void setFoodsubstitutionId(String foodsubstitutionId) {
        this.foodsubstitutionId = foodsubstitutionId;
    }
}
