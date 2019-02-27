package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

public class TopicList {

	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "topic")

	private ReusableConfigTopic reusableConfigTopic;

	public ReusableConfigTopic getReusableConfigTopic() {
		return reusableConfigTopic;
	}

	public void setReusableConfigTopic(ReusableConfigTopic reusableConfigTopic) {
		this.reusableConfigTopic = reusableConfigTopic;
	}

}
