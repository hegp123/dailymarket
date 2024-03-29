package ar.com.tsoluciones.arcom.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.type.Type;

import ar.com.tsoluciones.arcom.cor.InternalErrorException;
import ar.com.tsoluciones.arcom.hibernate.HibernateService;
import ar.com.tsoluciones.arcom.hibernate.Transactional;
import ar.com.tsoluciones.arcom.security.LoginHistory;
import ar.com.tsoluciones.arcom.security.Product;
import ar.com.tsoluciones.arcom.security.ProductoVenta;
import ar.com.tsoluciones.arcom.security.SesionVenta;
import ar.com.tsoluciones.arcom.security.Sucursal;
import ar.com.tsoluciones.arcom.security.User;
import ar.com.tsoluciones.arcom.security.services.factory.UserServiceFactory;
import ar.com.tsoluciones.arcom.security.services.proxyinterface.CajeroVentaServiceInterface;
import ar.com.tsoluciones.arcom.security.services.proxyinterface.UserServiceInterface;
import ar.com.tsoluciones.util.Cast;

public class CajeroVentaService implements CajeroVentaServiceInterface {

	public Product getProductByCode(String code) {
		List<Product> l = Cast.castList(Product.class, HibernateService.findByFilter(Product.class, new String[] { "code"}, new Object[] { code}, new Type[] {
			Hibernate.STRING}));

		if (l == null || l.size() == 0)
		return null;

		Product listedProduct = l.get(0);
		return listedProduct;
	}

	@Transactional
	public Long guardarSesionVenta(String idCaja, String cajero,
			String productos, String totalVenta) {
		
		UserServiceInterface userInterface = (UserServiceInterface) new UserServiceFactory().newInstance();
		User user = userInterface.getUserByUserName(cajero);
		String [] codigos = productos.split("-");
		
		SesionVenta sesionVenta = new SesionVenta();
		sesionVenta.setCajero(user);
		sesionVenta.setTotalVenta(Double.valueOf(totalVenta));
		Set<ProductoVenta> productosVenta = new HashSet<ProductoVenta>();
		sesionVenta.setIdCaja(Long.valueOf(idCaja));
		
//		sesionVenta.setProductos(productosVenta);
		
		HibernateService.updateObject(sesionVenta);
		
		for (int i = 0; i < codigos.length; i++) {
			String codeProd = (String) codigos[i];
			Product producto = getProductByCode(codeProd);
			
			//Se actualiza el stock del producto
			int stockActualizado = producto.getActualStock()-1;
			
			if(stockActualizado<= producto.getRepositionStock() && producto.getState().equalsIgnoreCase(Product.PRODUCT_STATE_STOCK)){
				producto.setState(Product.PRODUCT_STATE_PENDING);
			}
			
			producto.setActualStock(stockActualizado);
			HibernateService.updateObject(producto);
			
			ProductoVenta productoVenta = new ProductoVenta();
			productoVenta.setProducto(producto);
			productoVenta.setSesionVenta(sesionVenta);
			productosVenta.add(productoVenta);
			HibernateService.updateObject(productoVenta);
		}
		
		return sesionVenta.getId();
		
	}
	
	@Transactional
	public void actualizarProducto(Product product){
		HibernateService.updateObject(product);
	}
	
	
	public SesionVenta obtenerSesionVenta(Long id) {
		

		SesionVenta sesionVenta = HibernateService.getObject(SesionVenta.class, id, LockMode.NONE);
		return sesionVenta;
		
	}
	
	public Sucursal obtenerSucursal(Long id) {

		Sucursal sucursal = HibernateService.getObject(Sucursal.class, id,
				LockMode.NONE);
		return sucursal;

	}

	public void saveLoginHistory(LoginHistory loginHistory) {
		try {
			HibernateService.saveObject(loginHistory);
		} catch (HibernateException e) {
			throw new InternalErrorException("Error al intentar persistir el objeto LoginHistory", e);
		}
		
	}

	public void updateLoginHistory(LoginHistory loginHistory) {
		try {
			HibernateService.updateObject(loginHistory);
		} catch (HibernateException e) {
			throw new InternalErrorException("Error al intentar actualizar el objeto LoginHistory", e);
		}
		
	}

	public LoginHistory getLoginHistory(User user) {
		

		List<LoginHistory> l = Cast.castList(LoginHistory.class, HibernateService.findByTwoFilter(LoginHistory.class, "cajero.id", user.getId(), "fechaCierre"));


		if (l == null || l.size() == 0)
			return null;

		LoginHistory listedLoginHistory = l.get(0);
		return listedLoginHistory;
	}

}
