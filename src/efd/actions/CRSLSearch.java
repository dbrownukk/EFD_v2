package efd.actions;

import java.util.*;

import javax.inject.*;
import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import efd.model.*;

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
