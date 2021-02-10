package efd.actions;

import java.time.*;

import org.apache.commons.fileupload.*;
import org.openxava.actions.*;

public class ShowFileAction extends ViewBaseAction {

	public void execute() throws Exception {
		FileItem file = (FileItem) getView().getValue("file");
		LocalDate date = (LocalDate) getView().getValue("date");
		addMessage(file.getName() + " shown on: " + date);
		addMessage(file.getName() + " content: " + file.getString());
	}

}