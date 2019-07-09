package efd.model;



import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@View(members = "topic,configQuestion ")


@Entity

public class ReusableConfigTopic extends EFDIdentifiable {

@Required
@Column(length = 45, unique = true)
private String topic;
/*************************************************************************************************/
//@EditAction("")
@CollectionView("FromTopic")
@OneToMany(mappedBy = "topic")

private Collection<ConfigQuestion> configQuestion;
/*************************************************************************************************/



public String getTopic() {
	return topic;
}

public void setTopic(String topic) {
	this.topic = topic;
}

public Collection<ConfigQuestion> getConfigQuestion() {
	return configQuestion;
}

public void setConfigQuestion(Collection<ConfigQuestion> configQuestion) {
	this.configQuestion = configQuestion;
}



}


	



