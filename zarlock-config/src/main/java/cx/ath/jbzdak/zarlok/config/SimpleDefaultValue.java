package cx.ath.jbzdak.zarlok.config;

public class SimpleDefaultValue extends DefaultValue {

	private final String defaultValue; 
	
	public SimpleDefaultValue(String key, String defaultValue) {
		super(key);
		this.defaultValue = defaultValue;
	}

	public SimpleDefaultValue(String key, String defaultValue, boolean feedValueToProperties
	) {
		super(key, feedValueToProperties);
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	
	

}
