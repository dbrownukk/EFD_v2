package efd.actions;

import java.util.*;

import org.openxava.actions.*;

public class CRSLSearch extends ReferenceSearchAction {



	public void execute() throws Exception {

			Map allValues = getPreviousView().getAllValues();
			System.out.println("allprevvals = "+allValues);
			
			allValues = getView().getAllValues();
			System.out.println("allvals = "+allValues);
		
			String studyid = getPreviousView().getValueString("id");
			
			super.execute();
			//getTab().setBaseCondition("${study.id} == '"+studyid+"'");

		

	}

}
