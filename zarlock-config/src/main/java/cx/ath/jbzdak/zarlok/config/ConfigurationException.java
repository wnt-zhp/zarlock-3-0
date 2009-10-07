package cx.ath.jbzdak.zarlok.config;

import cx.ath.jbzdak.jpaGui.ExceptionForUser;

/**
 * Zwraca informacje o jakimś błędzie w konfiguracji. 
 *
 */
public class ConfigurationException extends Exception implements ExceptionForUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
