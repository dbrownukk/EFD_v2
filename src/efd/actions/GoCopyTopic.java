
/* In Project module override standard add LZ to remove LZs already associated with Project */

package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class GoCopyTopic extends ViewBaseAction {

	public void execute() throws Exception {

		// get Topic List
		List<ReusableConfigTopic> topic = XPersistence.getManager().createQuery("from ReusableConfigTopic")
				.getResultList();

		TopicList topicList = new TopicList();

		System.out.println("in dialog call");
		showDialog();
		getView().setTitle("Enter Topic to copy from");

		getView().setModelName("TopicList");

		setControllers("CopyinTopicQuestions", "Dialog");

	}

}
