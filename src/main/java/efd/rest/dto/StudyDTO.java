package efd.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyDTO {
    private String id;
    private String notes;
    private Integer version;
    private String description;
    private java.sql.Timestamp endDate;
    private Integer referenceYear;
    private java.sql.Timestamp startDate;
    private String studyName;
    private String cProject;
    private String cLocation;

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.sql.Timestamp getEndDate() {
        return this.endDate;
    }

    public void setEndDate(java.sql.Timestamp endDate) {
        this.endDate = endDate;
    }

    public Integer getReferenceYear() {
        return this.referenceYear;
    }

    public void setReferenceYear(Integer referenceYear) {
        this.referenceYear = referenceYear;
    }

    public java.sql.Timestamp getStartDate() {
        return this.startDate;
    }

    public void setStartDate(java.sql.Timestamp startDate) {
        this.startDate = startDate;
    }

    public String getStudyName() {
        return this.studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getCProject() {
        return this.cProject;
    }

    public void setCProject(String cProject) {
        this.cProject = cProject;
    }

    public String getCLocation() {
        return this.cLocation;
    }

    public void setCLocation(String cLocation) {
        this.cLocation = cLocation;
    }
}
