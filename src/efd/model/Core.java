package efd.model;

import javax.persistence.*;

import org.openxava.model.*;

public class Core extends EFDIdentifiable{
	
	@Version
	private Integer version;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	

}
