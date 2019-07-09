
/* In Project module override standard add LZ to remove LZs already associated with Project */

package efd.actions;

import java.util.*;
import java.util.stream.*;

import javax.persistence.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.function.Predicate;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;

public class CustRepSpecAddAnswer extends GoAddElementsToCollectionAction {

	public void execute() throws Exception {

		super.execute();


	//getTab().setBaseCondition( 
	  //      "select distinct" +
	    //            " configQuestionUse.configQuestion.prompt, answer" +
	      //          "from " +
	        //        "ConfigAnswer");
	
	List<ConfigAnswer> answer = XPersistence.getManager().
		createQuery("from ConfigAnswer" ).getResultList();
	
	

	List<ConfigAnswer> uniqueAnswers = answer.stream()
			.filter(distinctByKeys(ConfigAnswer::getConfigQuestionUse,ConfigAnswer::getAnswer))
			.filter(p -> p.getAnswer() != null)
			.collect(Collectors.toList());
	
	
	
		
	System.out.println("in add action ");	
	String inlist = "";
	for (ConfigAnswer ca : uniqueAnswers) {
		System.out.println("anwer = "+ca.getConfigQuestionUse().getConfigQuestion().getPrompt()
				+" "+ca.getAnswer());
		inlist += "'"+ca.getId()+"'"+",";
		
	}
	
	inlist = StringUtils.chop(inlist);
	
	getTab().setBaseCondition("${id} in (" + inlist + ")");
	
	//mustRefreshCollection();
	//getParentView().refresh();
	
	
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors)
	{
	  final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
	   
	  return t ->
	  {
	    final List<?> keys = Arrays.stream(keyExtractors)
	                .map(ke -> ke.apply(t))
	                .collect(Collectors.toList());
	     
	    return seen.putIfAbsent(keys, Boolean.TRUE) == null;
	  };
	}
	

}
