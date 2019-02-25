/* Reference table for question lov answer type to be used in generated spreadsheet */
/* DRB 21/2/19 */

package efd.model;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

@Entity

public class QuestionLOV extends Identifiable {

	@Required
	private String lovValue;

	@ManyToOne
	private ConfigQuestion configQuestion;

	public String getLovValue() {
		return lovValue;
	}

	public void setLovValue(String lovValue) {
		this.lovValue = lovValue;
	}

	public ConfigQuestion getConfigQuestion() {
		return configQuestion;
	}

	public void setConfigQuestion(ConfigQuestion configQuestion) {
		this.configQuestion = configQuestion;
	}



	
	
	
	
}
