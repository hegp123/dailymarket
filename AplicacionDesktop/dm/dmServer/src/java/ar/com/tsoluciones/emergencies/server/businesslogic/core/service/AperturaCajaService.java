package ar.com.tsoluciones.emergencies.server.businesslogic.core.service;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.Preferences;

import ar.com.tsoluciones.arcom.logging.Log;
import ar.com.tsoluciones.arcom.security.User;
import ar.com.tsoluciones.arcom.security.services.implementation.UserService;
import ar.com.tsoluciones.arcom.security.services.proxyinterface.UserServiceInterface;
import ar.com.tsoluciones.emergencies.server.businesslogic.core.service.proxyinterface.AperturaCajaServiceInterface;


public class AperturaCajaService implements AperturaCajaServiceInterface {
	/**
	 * Logea un usuario en el sistema
	 *
	 */
	public boolean abrirCaja(String username, String montoApertura, String fecha, String huellaDigital) {
		UserServiceInterface userServiceInterface = new UserService();
		User userToAuthenticate = userServiceInterface.getUserByUserName(username);

		if (userToAuthenticate == null) {
			Log.getLogger(this.getClass()).debug("Usuario no encontrado");
			return false;
		}

		Log.getLogger(this.getClass()).debug("Autenticando: " + username);
		return userToAuthenticate.authenticate(huellaDigital);
	}

	
	public boolean altaHuellaDigital(String username, String password, String huella ){
		
		UserServiceInterface userServiceInterface = new UserService();
		User user = userServiceInterface.getUserByUserName(username);
		
		byte[] huellaByte= null;
		// Es primer logueo?
		if(user.getHuelladigital() == null || user.getHuelladigital().length == 0){
			huellaByte = MyBase64.decode(huella);
			user.setHuelladigital(huellaByte);
		//TODO updatear usuario
			return true;
		}
		return false;
	}

	public static class MyBase64 {
	     private static class MyPreferences extends AbstractPreferences {
	         private Map<String,String> map = new HashMap<String,String>();
	         MyPreferences() { super(null,""); }
	         protected void putSpi(String key,String value) { map.put(key,value); }
	         protected String getSpi(String key) { return map.get(key); }
	         protected void removeSpi(String key) { map.remove(key); }
	         protected void removeNodeSpi() { }
	         protected String[] keysSpi() { return null; }
	         protected String[] childrenNamesSpi() { return null; }
	         protected AbstractPreferences childSpi(String key) { return null; }
	         protected void syncSpi() {}
	         protected void flushSpi() {}
	     }
	     static String encode(byte[] ba) {
	         Preferences p = new MyPreferences();
	         p.putByteArray("",ba);
	         return p.get("",null);
	     }
	     static byte[] decode(String s) {
	         Preferences p = new MyPreferences();
	         p.put("",s);
	         return p.getByteArray("",null);
	     }
	
	 }

}
