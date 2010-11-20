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

public class SalesReportService extends BaseReportService{
	
	public byte[] runReport(DynaBean reportData, Collection col, String report, Map<String, String> filters,String tipo)throws Exception {
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
		    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(SalesReportService.class.getResourceAsStream("/reports/" + report + ".jasper"));
			
		  //Para probar
		    if (tipo.equals("Anual")) {
			    col.add(new Anual(new Integer(58), new Integer(764), "2007"));
			    col.add(new Anual(new Integer(58), new Integer(678), "2008"));
			    col.add(new Anual(new Integer(58), new Integer(813), "2009"));
			    col.add(new Anual(new Integer(58), new Integer(1351), "2010"));
		    } else {
		    	col.add(new Anual(new Integer(58), new Integer(764), "09/09"));
			    col.add(new Anual(new Integer(58), new Integer(678), "10/09"));
			    col.add(new Anual(new Integer(58), new Integer(813), "11/09"));
			    col.add(new Anual(new Integer(58), new Integer(1351), "12/09"));
		    }
		    
		    return JasperRunManager.runReportToPdf(jasperReport, parameters, getDataSource(col, filters, tipo));			
        } catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	protected class Anual {
		protected Integer producto;
		protected Integer ventas;
		protected Double prom;
		protected String anio;
		
		public Anual(Integer pr, Integer ve, String anio) {
			producto = pr;
			ventas = ve;
			this.anio = anio; 
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
		public String getAnio() {
			return anio;
		}
		public void setAnio(String anio) {
			this.anio = anio;
		}			
	}
	
    private JRDataSource getDataSource(Collection results, Map<String, String> filters, String tipo) {
        return new CustomDS(results, filters, tipo);
    }
    
    protected class CustomDS implements JRDataSource  {		
        protected Iterator<Object> iterator; 
        protected Object currentValue; 
        protected Map<String, String> filters;
        protected String tipo;
        
        public CustomDS(Collection c, Map<String, String> filters, String tipo) {
        	this.iterator = c.iterator();
        	this.filters = filters;
        	this.tipo = tipo;
        }
        
        public Object getFieldValue(JRField field) {
        	Anual pr = (Anual) currentValue;
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
        	} else if("date".equals(field.getName())) {
        		return pr.getAnio();
        	} else if("prom".equals(field.getName())) {
        		return pr.getProm().toString().substring(0, pr.getProm().toString().indexOf(".") + 3);
        	} else if("tipo".equals(field.getName())) {
        		return tipo;
        	} else if("tipoPeriodo".equals(field.getName())) {
        		return tipo.equals("Anual") ? "A�o" : "Mes";
        	}        	
        	return null;
        }
        
        public boolean next() throws JRException { 
            currentValue = iterator.hasNext() ? iterator.next() : null; 
            return (currentValue != null);
        }
    }          
}