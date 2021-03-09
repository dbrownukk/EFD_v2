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
    private final ModellingScenarioRepository modellingScenarioRepository;
    JxlsWorkbook report;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();




    public byte[] runModReports(final String title, boolean copingStrategy) throws Exception {

        Project project = new Project();
        Study study = new Study();

        ModellingScenario ms = modellingScenarioRepository.findByTitle(title);

        ModellingReports modellingReports = new ModellingReports();

        modellingReports.setModellingScenarioId(ms.getId());

        System.out.println("modelling type = " + ms.getModelType());

        modellingReports.setModelType(CustomReportSpecListModelling.ModelType.ChangeScenario); //  ChangeScenario, CopingStrategy

        if(copingStrategy){
            modellingReports.setModelType(CustomReportSpecListModelling.ModelType.CopingStrategy);
        }
        else
        {
            modellingReports.setModelType(CustomReportSpecListModelling.ModelType.ChangeScenario);
        }

        modellingReports.setModellingScenario(ms);


        //  project = XPersistence.getManager().find(Project.class, projectId);
        System.out.println("done find ");
        System.out.println("project = " + ms.getProject().getProjecttitle());
        //modellingReports.setProject(project);

        HttpSession getHttpSession = null;


        modellingReports.setSession(getHttpSession);
        modellingReports.execute();

        //Object attribute = modellingReports.getSession().getAttribute(ReportXLSServlet.SESSION_XLS_REPORT);

        report = modellingReports.getReport();


        System.out.println("report = " + report);

        try {
            report.write(bos);
        } finally {
            bos.close();
        }
        byte[] bytes = bos.toByteArray();


        return bytes;


    }



}
