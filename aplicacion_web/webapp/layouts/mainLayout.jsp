<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ResourceBundle" %>
<tiles:useAttribute name="title" classname="java.lang.String"/>

<%-- Layout component 
  parameters : title, header, menu, body, footer 
--%>
<html:html locale="true">

<HEAD>
<%
	ResourceBundle resources = ResourceBundle.getBundle("ApplicationResources", (Locale)request.getSession().getAttribute("org.apache.struts.action.LOCALE"));	
	String retVal = null;
        	try {
	        	String s = title;				  		
				retVal = resources.getString(s);	
			} catch (Exception e) {
				retVal = title;
			}
%>
    <TITLE>.:: DAILYMARKET :: <%=retVal%> ::.</TITLE>
    <META HTTP-EQUIV="Content-Type" content="text/html; charset=iso-8859-1"/>
	<META HTTP-EQUIV="Expires" CONTENT="-1"/>
    <META HTTP-EQUIV="Cache-Control" CONTENT="no-store, no-cache, must-revalidate, post-check=0, pre-check=0"/>
    <META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>  
	
	<script type="text/javascript" src="functions.js"></script>
	<script type="text/javascript" src="ieupdateV2.js"></script>
    <LINK href="site.css" type="text/css" rel="stylesheet">

	<link rel="stylesheet" type="text/css" href="yui/fonts/fonts-min.css" /> 
	<link rel="stylesheet" type="text/css" href="yui/autocomplete/assets/skins/sam/autocomplete.css"/> 
	
	<link rel="stylesheet" type="text/css" href="yui/button/assets/skins/sam/button.css" />
	<link rel="stylesheet" type="text/css" href="yui/container/assets/skins/sam/container.css" />
	<link rel="stylesheet" type="text/css" href="yui/calendar/assets/skins/sam/calendar.css" />
	<LINK href="dcc_calendar.css" type="text/css" rel="stylesheet">
	<LINK href="dcc_autocomplete.css" type="text/css" rel="stylesheet">
	
	<script type="text/javascript" src="dcc_autocomplete.js"></script>
	
	<script type="text/javascript" src="yui/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script type="text/javascript" src="yui/connection/connection-min.js"></script> 
	<script type="text/javascript" src="yui/animation/animation-min.js"></script>
	<script type="text/javascript" src="yui/datasource/datasource-min.js"></script>
	<script type="text/javascript" src="yui/autocomplete/autocomplete-min.js"></script>
	<script type="text/javascript" src="assistant.js"></script>

	<script type="text/javascript" src="yui/dragdrop/dragdrop-min.js"></script>
	<script type="text/javascript" src="yui/element/element-min.js"></script>
	<script type="text/javascript" src="yui/button/button-min.js"></script>
	<script type="text/javascript" src="yui/container/container-min.js"></script>
	<script type="text/javascript" src="yui/calendar/calendar-min.js"></script>
	<script type="text/javascript" src="dcc_calendar.js"></script>

	
	
</HEAD>

<BODY class="yui-skin-sam">
<table class="body" align="center" cellSpacing="0" cellPadding="0" border="0">
	
		
		<!-- Header -->
		<tr>
			<td align="center">
				<tiles:insert attribute="header"/>
			</td>			
		</tr>
		<tr>			       	
			<td align="center">	
				<!-- Menu y body -->
				<TABLE  cellSpacing="0" width="800" align="center" cellPadding="0"  border="0">
				    <TBODY>
				        <!-- Body -->
						<tiles:insert attribute="body"/>										
					</TBODY>
				</TABLE>
			</td>
		</tr>
		<tr>
			<td>		
				<!-- Footer -->		
				<tiles:insert attribute="footer"/>
			</td>
		</tr>
</table>
</BODY>
</html:html>
