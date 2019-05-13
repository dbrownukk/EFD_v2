/* Question LOV Type question lov answer type LOV to be used in generated spreadsheet */
/* DRB 21/2/19 */

package efd.model;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.model.*;



@Views({ @View(members = "lovType,questionLOV")})


@Entity

@Table(name = "questionlovtype")// ,uniqueConstraints = {@UniqueConstraint(name = "questionlov", columnNames = { "configQuestion_ID", "lovValue"}) })

public class QuestionLOVType extends Identifiable {

	@Required
	private String lovType;

	@OneToMany 
	//@NotEmpty   - does not work well with collections
	@Size(min=1)
	private Collection<QuestionLOV> questionLOV;

	public String getLovType() {
		return lovType;
	}

	public void setLovType(String lovType) {
		this.lovType = lovType;
	}

	public Collection<QuestionLOV> getQuestionLOV() {
		return questionLOV;
	}

	public void setQuestionLOV(Collection<QuestionLOV> questionLOV) {
		this.questionLOV = questionLOV;
	}




	
	
	
	
}
