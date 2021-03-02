package efd.rest.dto;

import efd.actions.ModellingReports;
import efd.model.CustomReportSpecListModelling;
import efd.model.ModellingScenario;
import efd.model.Project;
import efd.model.Study;
import efd.rest.repositories.ModellingScenarioRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/*
    @Author david
    @Create 01/03/2021 09:25
*/
@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ModellingReportsDto {
    public static final String KATAKWI_OHEA = "Katakwi - OHEA";
    private String modellingScenarioId;

    @Autowired
    ModellingScenarioRepository modellingScenarioRepository;


    public String runModReports() throws Exception {

        //String modellingScenarioId = "8a808484771f33410177207cbeba0015";
        Project project = new Project();
        Study study = new Study();

        ModellingScenario ms = modellingScenarioRepository.findByTitle(KATAKWI_OHEA);



        System.out.println("ms = "+ms.getTitle());

        //System.out.println("MSByTitle = "+byTitle.getTitle());

        //ModellingscenarioDTO modellingscenarioDTO = modellingScenarioMapper.modellingScenarioToModellingScenarionDto(
          //      modellingScenarioRepository.findByTitle("Katakwi - OHEA"));


        ModellingReports modellingReports = new ModellingReports();

        modellingReports.setModellingScenarioId(ms.getId());
        modellingReports.setModelType(CustomReportSpecListModelling.ModelType.ChangeScenario); //  ChangeScenario, CopingStrategy

        modellingReports.setModellingScenario(ms);


      //  project = XPersistence.getManager().find(Project.class, projectId);
        System.out.println("done find ");
        System.out.println("project = "+ms.getProject().getProjecttitle());
        //modellingReports.setProject(project);

        modellingReports.execute();

        return ("ran runModReports");

    }

}
