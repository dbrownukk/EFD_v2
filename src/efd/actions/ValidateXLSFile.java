package efd.actions;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.sql.Array;

/* Read XLS Community Interview  spreadsheet */

import java.util.Collection;
import java.text.SimpleDateFormat;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.web.editors.*;
import efd.model.*;
import efd.model.WealthGroupInterview.*;
import javassist.bytecode.Descriptor.*;

import org.apache.poi.ss.usermodel.*;
import org.hibernate.*;
import org.hibernate.mapping.*;

public class ValidateXLSFile extends CollectionBaseAction implements IForwardAction, JxlsConstants, IFilePersistor {



	public void execute() throws Exception {

		 int row = 0;
		 String forwardURI = null;
		
		 int i = 0;
		 Integer rowNumber = 0;
		 Integer cellNumber = 0;
		 Integer sheetNumber = 0;
		 
		 String[] cellArray = null;
		 int arrayCount = 0;
		 Cell cell;
		
		 String sdate;
		 String interviewers;
		 String typeOfYear;
		 String stest;
		 Double dtest;
		 Boolean messaging = false;
		
		
		
		// System.out.println("in xls parse ");

		String wgiid = getView().getValueString("wgiid");

		WealthGroupInterview wgi = XPersistence.getManager().find(WealthGroupInterview.class, wgiid);
		if (wgi.getSpreadsheet().isEmpty()) {
			addError("Upload completed Interview Spreadsheet before validating");
			return;
		}
		else if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Generated)) {
			addError("Cannot Validate Interview Spreadsheet - Upload and Parse Spreadsheet first ");
			return;
		}
		else if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Uploaded)) {
			addError("Cannot Validate Interview Spreadsheet - Parse Spreadsheet first");
			return;
		}
		else if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Validated)) {
			addError("Cannot Validate Interview Spreadsheet Data - Already Validated");
			return;
		}

		String var1 = null, var2=null, var3=null, var4=null, var5=null;
		Integer int1, int2, int3;
		Cell cell1, cell2, cell3;
		Double d1, d2, d3, d4;
		Boolean getNextRow = false;
		String market1, market2, market3;
		Double percent1, percent2, percent3;
			/*************************************************************************************************/
			/* VALIDATE EACH Resource Asset type
			 * Note that Tradeable has yet to be defined....
			 */
			

			/* Get Asset id for Livestock Asset */
		/*	
		Collection lsq = XPersistence.getManager().createQuery("from ResourceType where ResourceTypeName = 'Livestock'").getResultList();
			
			Query landq =XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Land'");
			Query fsq = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Food Stocks'");
			Query  cropq = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Crop'");
			Query transferq = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Transfers'");
			Query lspq = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Livestock Products'");
			Query wfq = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Wild foods'");
			Query empq = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Employment'");
*/		
		
			
		System.out.println("in afs 10 ");
			
			
		
		
			Collection<AssetFoodStock> afs = wgi.getAssetFoodStock();
			
			System.out.println(afs.toString());
			
			
			
			
			System.out.println("in afs 20 ");
						
			//XPersistence.getManager().persist(wgi);
			// System.out.println("Assets = 46 ");
			getView().refresh();
			addMessage("Successful Validation");
			return;
		}

	@Override
	public void save(AttachedFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLibrary(String libraryId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AttachedFile find(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<AttachedFile> findLibrary(String libraryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getForwardURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean inNewWindow() {
		// TODO Auto-generated method stub
		return false;
	}


}
