/* Reference table for lookups */
/* DRB 11/8/18 */

package efd.model;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

@Entity

public class ReferenceCode extends Identifiable {

	@Column(name = "referenceType", nullable = false)
	@Required
	private String referenceType;

	@Column(name = "referenceName", nullable = false)
	@Required
	private String referenceName;

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

}
