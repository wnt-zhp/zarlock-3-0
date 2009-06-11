package cx.ath.jbzdak.zarlok.config;

import javax.annotation.Nonnull;

/**
 * Domyślna wartość dla {@link Preferences}.
 * @author jb
 *
 */
public abstract class DefaultValue {
	
	private final String key;
	
	private final boolean feedValueToProperties;

	public DefaultValue(String key) {
		super();
		this.key = key;
		this.feedValueToProperties = true;
	}
	
	public DefaultValue(String key, boolean feedValueToProperties) {
		super();
		this.key = key;
		this.feedValueToProperties = feedValueToProperties;
	}

	/**
	 * Klucz. Klucz będzie prependowany suffixem programu.
	 * @return
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Określa czy wpisywać wartość do propertiesów. 
	 * Domyślnie tak, ale nie zawsze (na przykład przy ustawianiu
	 * L&F który możę być konfigurowany poza programem na 
	 * fąfnaście sposobów) tego chcemy. 
	 */
	public boolean isFeedValueToProperties() {
		return feedValueToProperties;
	}
	
	/**
	 * Wylicza domyślną wartość.
	 */
	@Nonnull
	public abstract String getDefaultValue();

}
