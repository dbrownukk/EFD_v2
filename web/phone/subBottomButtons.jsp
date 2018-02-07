<%@ page import="org.openxava.controller.meta.MetaAction"%>
<%@ page import="org.openxava.util.Labels"%>
<%@ page import="org.openxava.web.Ids"%>

<%
String controllerName = subcontroller.getControllerName();
String subcontrollerId = Ids.decorate(request, "sc-" + controllerName + "_" + mode);
String buttonId = Ids.decorate(request, "sc-button-" + controllerName + "_" + mode);
String actionsId = Ids.decorate(request, "sc-ul-" + controllerName + "_" + mode);
%>

<div id=<%=subcontrollerId%> class="phone-subcontroller" style="width: 100%;">
	<input id="<%=buttonId%>" value="<%=Labels.get(controllerName)%>" type="button" 
	       onclick="phone.subcontroller('<%=subcontrollerId%>', '<%=buttonId%>', '<%=actionsId%>')">
	<ul id="<%=actionsId%>" class="phone-subcontroller-actions">
<%
it = subcontroller.getMetaActions().iterator();
while (it.hasNext()) {
	MetaAction action = it.next();
	if (!action.appliesToMode(mode)) continue;	
%>
	<li>
		<xava:link action="<%=action.getQualifiedName()%>">
			<%=action.getLabel()%>
		</xava:link>
	</li>
<%
}
%>
	</ul>
</div>