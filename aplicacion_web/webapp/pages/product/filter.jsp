<%@ page language="java" %>

<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>

<TABLE class="searchform" cellSpacing="0" cellPadding="6" border="0">
	<TR> 
		<TD colspan="3"> 
			<html:errors/>
		</TD>
	</TR> 
	<TR> 
	   <td>
	   	<TABLE class="searchform" cellSpacing="0" cellPadding="6" border="0">
	    	<tr>
	    		<TH style="width:100px;padding-left:40px;"><bean:message key="ProductForm.code"/></TH>
	            <TD width="100px;"><html:text property="code"/></TD>	            	       
	    		<TH style="width:100px;padding-left:40px;"><bean:message key="ProductForm.name"/></TH>
	            <TD><html:text property="name"/></TD>
	        </tr>
	        <tr>
	    		<TH style="width:100px;padding-left:40px;"><bean:message key="ProductForm.description"/></TH>
	            <TD width="100px;"><html:text property="description"/></TD>
				<TH style="width:100px;padding-left:40px;"><bean:message key="ProductForm.groupProduct"/></TH>
				<TD>
					<html:select property="groupProductId">						
						<html:option value="-1">Seleccione</html:option>
						<html:options collection="groupsProduct" property="id" labelProperty="name" />
					</html:select>
				</TD>
			</TR>
		</table>
		</td>
	</TR>
</TABLE>
