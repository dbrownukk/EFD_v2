package efd.rest.domain.model;


import javax.persistence.*;

import org.openxava.model.*;

@Entity

public class CurrentVersion extends Identifiable{

	
	private int currentVersion;

	public int getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(int currentVersion) {
		this.currentVersion = currentVersion;
	}


	
	
	
	
}
