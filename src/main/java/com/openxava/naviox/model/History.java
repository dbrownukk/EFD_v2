package com.openxava.naviox.model;

import org.apache.commons.lang.StringUtils;
import org.openxava.annotations.Stereotype;
import org.openxava.annotations.Tab;
import org.openxava.jpa.XPersistence;
import org.openxava.model.Identifiable;
import org.openxava.util.Users;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@Table(name="OXHISTORY")
@Tab(defaultOrder="${dateTime} desc")
public class History extends Identifiable {
	
	private final static int ENTITY_LENGTH = 100;
	private final static int KEY_LENGTH = 100;
	
	@Stereotype("DATETIME")
	private Date dateTime;
	
	@Column(name="userName", length=30)
	private String user;
	
	@Column(length=15)
	private String ip;

	public enum Action { CONSULTED, CREATED, MODIFIED, REMOVED }; 
	@Enumerated(EnumType.STRING)
	private Action action;
	
	@Column(length=ENTITY_LENGTH)
	private String entity;
	
	@Column(name="entityKey", length=KEY_LENGTH)
	private String key;
	
	@Column(length=255)
	@Stereotype("MEMO")
	private String changes;
	
	public static void consulted(String entity, Map key) {
		History h = create(Action.CONSULTED, entity, key);
		XPersistence.getManager().persist(h);
	}


	public static void created(String entity, Map key) {
		History h = create(Action.CREATED, entity, key);
		XPersistence.getManager().persist(h);
	}

	public static void modified(String entity, Map key, String changes) {
		if (changes.length() > 255) { 
			modified(entity, key, "..." + changes.substring(252));
			try { Thread.sleep(1); } catch (Exception ex) {}
			changes = changes.substring(0, 252) + "...";
		}
		History h = create(Action.MODIFIED, entity, key);
		h.changes = changes;
		XPersistence.getManager().persist(h);
	}

	public static void removed(String entity, Map key) {
		History h = create(Action.REMOVED, entity, key);
		XPersistence.getManager().persist(h);
	}
	
	private static History create(Action action, String entity, Map key) {
		History h = new History();
		h.dateTime = new Date();
		h.user = Users.getCurrent();
		h.ip = Users.getCurrentIP();
		h.action = action;
		h.entity = StringUtils.abbreviate(entity, ENTITY_LENGTH);
		h.key = key.toString().replace("{", "").replace("}", "");
		h.key = StringUtils.abbreviate(h.key, KEY_LENGTH);
		return h;
	}
	
	public String getIp() {
		return ip;
	}

	public Action getAction() {
		return action;
	}

	public String getEntity() {
		return entity;
	}

	public String getKey() {
		return key;
	}

	public String getChanges() {
		return changes;
	}

	public Date getDateTime() {
		return dateTime;
	}


	public String getUser() {
		return user;
	}

}
