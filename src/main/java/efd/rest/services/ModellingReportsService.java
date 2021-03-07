package efd.rest.services;

import efd.actions.ModellingReports;
import efd.model.CustomReportSpecListModelling;
import efd.model.ModellingScenario;
import efd.model.Project;
import efd.model.Study;
import efd.rest.repositories.ModellingScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.openxava.util.jxls.JxlsWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;


/*
    @Author david
    @Create 01/03/2021 09:25
*/

@RequiredArgsConstructor
@Service
public class ModellingReportsService {
    JxlsWorkbook report;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private final ModellingScenarioRepository modellingScenarioRepository;

    public byte[] runModReports(final String title) throws Exception {

        Project project = new Project();
        Study study = new Study();

        ModellingScenario ms = modellingScenarioRepository.findByTitle(title);

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

        HttpSession getHttpSession = null;


        modellingReports.setSession(getHttpSession);
        modellingReports.execute();

        //Object attribute = modellingReports.getSession().getAttribute(ReportXLSServlet.SESSION_XLS_REPORT);

        report = modellingReports.getReport();

        System.out.println("reportback in runrep  = "+report);



        try {
            report.write(bos);
        } finally {
            bos.close();
        }
        byte[] bytes = bos.toByteArray();


       return bytes;

        //return report;


       // return ("ran runModReports");

    }

}
