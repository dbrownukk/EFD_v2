package efd.actions;

import java.util.*;

import javax.persistence.*;

import efd.model.Project;
import org.apache.commons.beanutils.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;


import efd.model.*;

public class MergeProject extends ViewBaseAction {

	/* Merge from remote Project into core EFD project in efd schema */

	public void execute() throws Exception {

		try {
			Project stageProject;
			//LivelihoodZone[] stageLivelihoodZone = new LivelihoodZone[50];
			//Site[] stageSite = new Site[50];
			
			Collection<LivelihoodZone> stageLivelihoodZone = new ArrayList<LivelihoodZone>();
			Collection<Site> stageSite = new ArrayList<Site>();
			
			String defaultSchema = XPersistence.getDefaultSchema();
			System.out.println("in merge ");

			//String currentOrg = Organizations.getCurrent(getRequest());

			System.out.println("Create new proj");

			String projid = (String) getView().getValue("projectid");
			if (projid == null)
				addError("Merge failed, No Project");
			
			
			stageProject = (Project) XPersistence.getManager().find(Project.class, projid);
			System.out.println("From proj title  " + stageProject.getProjecttitle());
			System.out.println("From proj altExhnageRate  " + stageProject.getAltExchangeRate());
			
			List<LivelihoodZone> lzList = (List<LivelihoodZone>) stageProject.getLivelihoodZone();
			
			System.out.println("number of lzs = " + lzList.size());
			Collection<Site> siteList = null;
			System.out.println("set site");
			List<String> locationids = new ArrayList<String>();
			System.out.println("set string");
			Query querysite;
			for (int i = 0; i< lzList.size();i++)
			{
				LivelihoodZone qLZ = new LivelihoodZone();
				qLZ = XPersistence.getManager().find(LivelihoodZone.class, lzList.get(i).getLzid());
				
				if(!qLZ.equals(null))
				{
					//stageLivelihoodZone[i] = new LivelihoodZone();
					stageLivelihoodZone.add(qLZ);
					
				}
				
				Iterator<LivelihoodZone> lzit = stageLivelihoodZone.iterator();
				while(lzit.hasNext())
					System.out.println("found lz = "+lzit.next().getLzname());
				
				System.out.println("q1 lzid = "+lzList.get(i).getLzid().toString());
				querysite = (Query) XPersistence.getManager().createQuery("select locationid from Site where lz = '" + lzList.get(i).getLzid().toString() + "'");
				System.out.println("q2");
				locationids = querysite.getResultList();
				System.out.println("q3");
				for(int j = 0; j < locationids.size(); j++)
				{
					System.out.println("q33 locid"+locationids.get(j));
					
					/* need to check if nothing returned */ 
					
					
					Site qsite = XPersistence.getManager().find(Site.class, locationids.get(j));
					if (!qsite.equals(null))
							{
							stageSite.add(qsite);
							}
					System.out.println("q4");
					System.out.println("Site = "+stageSite);
					System.out.println("q5");
				}
					
			}
				
				

			/*********************************************************************************************************/		
			/* Now change to EFD schema and do insert */
			/*********************************************************************************************************/

			XPersistence.commit(); // frees the current XPersistence session

			XPersistence.setDefaultSchema("efd");

			/* Project */
			System.out.println("set efd schema ");
			Project newProject = new Project();
			System.out.println("set new project ");
			BeanUtils.copyProperties(newProject, stageProject);
			System.out.println("copied - new project title =  " + newProject.getProjecttitle());

			newProject.setProjectid(null);
			newProject.setLivelihoodZone(null);
			
			XPersistence.getManager().persist(newProject);
		    
			
			System.out.println("Create new proj 2 proj = " + stageProject.getProjecttitle());
			System.out.println("New Proj ID  = " + newProject.getProjectid());

			/* LZ */
			// get current LZ from lzList
			LivelihoodZone lz = new LivelihoodZone();
			
			Iterator<LivelihoodZone> lzitins = stageLivelihoodZone.iterator();
			while (lzitins.hasNext())
			{
				System.out.println("l1");
				LivelihoodZone newLZ = new LivelihoodZone();
				System.out.println("l2");
				BeanUtils.copyProperties(newLZ, lzitins.next());
				System.out.println("l3");
				newLZ.setLzid(null);
				System.out.println("l4");
				
				
				//newLZ.setProject((Collection<Project>) newProject);
				System.out.println("l5 == "+newProject.getProjecttitle());
				//newProject.setLivelihoodZone(stageLivelihoodZone);
				
				System.out.println("l6");
				XPersistence.getManager().persist(newLZ);
				
				System.out.println("l7");
			}
			System.out.println("l8");
			
			
			XPersistence.commit();	
			
			System.out.println("l9 newproj"+newProject.getProjecttitle());
			
			
			
			//XPersistence.setDefaultSchema(defaultSchema);
			// LivelihoodZone stageLZ =
			// XPersistence.getManager().find(LivelihoodZone.class,stageProject.getLivelihoodZone());

		} catch (

		Exception ex) {
			addError("Merge Failed " + ex);
		}

	}
}
