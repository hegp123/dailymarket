package ar.com.dailyMarket.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.beanutils.DynaBean;

public class MonthlySalesReportService extends BaseReportService{
	
	public byte[] runReport(DynaBean reportData, Collection col, String report, Map<String, String> filters)throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();		
        String imgs = "";
        
        Context initCtx;
		try {			
			initCtx = new InitialContext();
			Context envCtx = (Context)initCtx.lookup("java:comp/env");
	        imgs = (String) envCtx.lookup("reportsFolder");
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
        parameters.put("reportsFolder", imgs); 
		
        try {
		    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(MonthlySalesReportService.class.getResourceAsStream("/reports/" + report + ".jasper"));
			
		  //Para probar
		    col.add(new Mensual(new Integer(58), new Integer(764), "09/09"));
		    col.add(new Mensual(new Integer(58), new Integer(678), "10/09"));
		    col.add(new Mensual(new Integer(58), new Integer(813), "11/09"));
		    col.add(new Mensual(new Integer(58), new Integer(1351), "12/09"));
		    
		    return JasperRunManager.runReportToPdf(jasperReport, parameters, getDataSource(col, filters));			
        } catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	protected class Mensual {
		protected Integer producto;
		protected Integer ventas;
		protected Double prom;
		protected String month;
		
		public Mensual(Integer pr, Integer ve, String month) {
			producto = pr;
			ventas = ve;
			this.month = month; 
			prom =  Double.parseDouble(pr.toString()) / Double.parseDouble(ve.toString());
		}
		
		public Integer getProducto() {
			return producto;
		}
		public void setProducto(Integer producto) {
			this.producto = producto;
		}
		public Integer getVentas() {
			return ventas;
		}
		public void setVentas(Integer ventas) {
			this.ventas = ventas;
		}
		public Double getProm() {
			return prom;
		}
		public void setProm(Double prom) {
			this.prom = prom;
		}
		public String getMonth() {
			return month;
		}
		public void setMonth(String month) {
			this.month = month;
		}		
	}
	
    private JRDataSource getDataSource(Collection results, Map<String, String> filters) {
        return new CustomDS(results, filters);
    }
    
    protected class CustomDS implements JRDataSource  {		
        protected Iterator<Object> iterator; 
        protected Object currentValue; 
        protected Map<String, String> filters;
        
        public CustomDS(Collection c, Map<String, String> filters) {
        	this.iterator = c.iterator();
        	this.filters = filters;
        }
        
        public Object getFieldValue(JRField field) {
            Mensual pr = (Mensual) currentValue;
        	if ("producto".equals(field.getName())) {
        		return pr.getProducto();
        	} else if("ventas".equals(field.getName())) {
        		return pr.getVentas();
        	} else if("groupProduct".equals(field.getName())) {
        		return filters.get("groupProduct");
        	} else if("productFilter".equals(field.getName())) {
        		return filters.get("productFilter");
        	} else if("periodo".equals(field.getName())) {
        		return filters.get("periodo");
        	} else if("hourlyBand".equals(field.getName())) {
        		return filters.get("hourlyBand");
        	} else if("mes".equals(field.getName())) {
        		return pr.getMonth();
        	} else if("prom".equals(field.getName())) {
        		return pr.getProm().toString().substring(0, pr.getProm().toString().indexOf(".") + 3);
        	}
        	return null;
        }
        
        public boolean next() throws JRException { 
            currentValue = iterator.hasNext() ? iterator.next() : null; 
            return (currentValue != null);
        }
    }  
}
