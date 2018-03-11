package efd.actions;

import static org.openxava.jpa.XPersistence.commit;
import static org.openxava.jpa.XPersistence.getManager;

import java.io.*;
import java.sql.*;

/* Read XLS Community Interview  spreadsheet */

import java.util.*;
import java.util.Collection;

import javax.persistence.*;
import org.apache.poi.poifs.filesystem.*;
import org.hsqldb.lib.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.view.*;
import org.openxava.web.editors.*;
import org.openxava.web.servlets.*;

import com.openxava.naviox.model.*;

import antlr.*;

import org.openxava.tab.*;

import efd.model.*;
import efd.model.WealthGroupInterview.*;
import efd.utils.*;
import sun.swing.*;

import org.apache.commons.lang.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.opc.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class ParseXLSFile extends CollectionBaseAction implements IForwardAction, JxlsConstants, IFilePersistor {

	private int row;
	private String forwardURI = null;
	Blob spreadsheet = null;
	private AttachedFile spreadsfile = null;
	private Integer rowNumber = 0;
	private Integer columnNumber = 0;
	private Integer sheet = 0;

	public void execute() throws Exception {

		System.out.println("in xls parse ");

		String wgiid = getView().getValueString("wgiid");

		WealthGroupInterview wgi = XPersistence.getManager().find(WealthGroupInterview.class, wgiid);

		System.out.println("in xls parse " + getView().getValueString("wgiid"));

		System.out.println("in xls 2        " + wgi.getSpreadsheet());

		String spreadsheetId = wgi.getSpreadsheet();

		Connection con = null;
		try {
			con = DataSourceConnectionProvider.getByComponent("WealthGroupInterview").getConnection();

			PreparedStatement ps = con
					.prepareStatement("select id,data from OXFILES where ID = '" + spreadsheetId + "'");
			System.out.println("prepped statment = " + ps.toString());
			System.out.println("prepped");
			ResultSet rs = ps.executeQuery();
			System.out.println("queried");

			System.out.println("In Blob " + rs.getMetaData());
			System.out.println("In Blob col = " + rs.findColumn("ID"));

			rs.next();
			InputStream input = rs.getBinaryStream("DATA"); /* get stream data as well */
			System.out.println("done inputstream ");

			; /* Move to first row */

			String spreadsheetPkey = rs.getString(1);

			System.out.println("done blob get");

			spreadsfile = find(spreadsheetPkey);
			System.out.println("done attache file get");

			Workbook wb = WorkbookFactory.create(input);

			System.out.println("tab = " + wb.getSheetName(1));

			System.out.println("done pkg setup ");

			for (sheet = 0; sheet < 5; sheet++) {
				for (rowNumber = 0; rowNumber < 6; rowNumber++) {
					for (columnNumber = 0; columnNumber < 8; columnNumber++) {

						System.out.println("sheet = " + rowNumber + " " + columnNumber + " " + columnNumber + " "
								+ wb.getSheetAt(sheet).getRow(rowNumber).getCell(columnNumber));
					}
				}
			} 
			
			
			System.out.println("done wb setup ");
			ps.close();

			return;
		} catch (

		Exception ex) {
			addError("Problem getting spreadsheet BLOB", ex);
			// You can throw any runtime exception here
			throw new SystemException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
			}
		}

		/* Now process the spreadsheet */

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

}