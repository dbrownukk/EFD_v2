/* Reference table for question lov answer type to be used in generated spreadsheet */
/* DRB 21/2/19 */

package efd.model;

import javax.persistence.*;



import org.openxava.annotations.*;
import org.openxava.model.*;


@Views({ @View(members = "lovValue")})



@Entity

@Table(name = "questionlov")//,      uniqueConstraints = @UniqueConstraint(name = "questionlov",    columnNames = { "configQuestion_ID", "lovValue"}))




public class QuestionLOV extends Identifiable {

	@Required
	private String lovValue;


	
	@ManyToOne
	private QuestionLOVType questionLOVType;
	
	
	
	

	public QuestionLOVType getQuestionLOVType() {
		return questionLOVType;
	}

	public void setQuestionLOVType(QuestionLOVType questionLOVType) {
		this.questionLOVType = questionLOVType;
	}

	public String getLovValue() {
		return lovValue;
	}

	public void setLovValue(String lovValue) {
		this.lovValue = lovValue;
	}





	
	
	
	
}
