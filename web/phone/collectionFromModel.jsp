<%@ include file="imports.jsp"%>

<%@ page import="org.openxava.controller.meta.MetaAction"%>
<%@ page import="org.openxava.controller.meta.MetaControllers"%>
<%@ page import="org.openxava.model.meta.MetaProperty"%>
<%@ page import="org.openxava.web.Actions"%>
<%@ page import="org.openxava.web.Ids"%>

<%@ page import="com.openxava.phone.util.CalculatedListElementIterator"%>
<%@ page import="com.openxava.phone.util.ListElement"%>

<%@ page import="java.util.Iterator"%>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>

<%
String propertyPrefix = collectionName + ".";
String listClass = idCollection == null ? style.getList() : "phone-frame";
String prefixIdRow = Ids.decorate(request, idCollection + ".");
String tabObject = request.getParameter("tabObject"); 
tabObject = (tabObject == null || tabObject.equals(""))?"xava_tab":tabObject;
String onSelectCollectionElementAction = subview.getOnSelectCollectionElementAction();
MetaAction onSelectCollectionElementMetaAction = Is.empty(onSelectCollectionElementAction)?null:MetaControllers.getMetaAction(onSelectCollectionElementAction);
String selectedRowStyle = style.getSelectedRowStyle(); 
String rowStyle = "";
%>	   	
<table id="<xava:id name='<%=idCollection%>'/>" class="<%=listClass%>" <%=style.getListCellSpacing()%> style="<%=style.getListStyle()%>">
<%
int row = 0;
for (Iterator<ListElement> it = new CalculatedListElementIterator(subview, request, errors); it.hasNext();) {	
	ListElement el = it.next();
	
	String idRow = prefixIdRow + row;
	String actionOnClick = Actions.getActionOnClick(
			request.getParameter("application"), request.getParameter("module"), 
			onSelectCollectionElementAction, row, viewName, idRow,
			selectedRowStyle, rowStyle, 
			onSelectCollectionElementMetaAction, tabObject);
%>  
<tr id="<%=idRow%>" >
<td class="phone-list-element-check"">
	<INPUT type="CHECKBOX" name="<xava:id name='xava_selected'/>" value="<%=propertyPrefix%>__SELECTED__:<%=row%>" <%=actionOnClick%>/>
</td>
<td class="phone-list-element">
	<xava:link action='<%=lineAction%>' argv='<%="row=" + (row++) + ",viewObject=" + viewName%>'>		
		<div class="phone-list-element-header"><%=el.getHeader()%></div>
		<div class="phone-list-element-subheader"><%=el.getSubheader()%></div>
		<div class="phone-list-element-content"><%=el.getContent()%></div>
	</xava:link>	
</td>
<td class="phone-list-element-pointer"><img src="<%=request.getContextPath()%>/<%=style.getImagesFolder()%>/chevron.png"/></td>
</tr>
<%
}
if (row == 0) {
%>
<tr><td colspan="2" class="phone-list-element">
	<div class="phone-list-element-message">
		<xava:message key="no_objects"/>
	</div>	
</td></tr>	
<% 
}
%>
</table>