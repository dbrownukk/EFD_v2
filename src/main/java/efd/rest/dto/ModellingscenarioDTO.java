package efd.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModellingscenarioDTO {
    @Null
    private String id;
    private String notes;
    private Integer version;
    private String author;
    private java.sql.Timestamp date;
    private String description;
    @NotBlank
    private String title;
    private String projectProjectId;
    private String studyId;
    private String modelType;
    private String livelihoodZoneLzid;
    private String foodsubstitutionId;


}
