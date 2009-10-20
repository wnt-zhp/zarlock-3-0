package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.zarlok.entities.listeners.BlockConfigEntryUpdate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import javax.persistence.*;

/**
 * Encja zawierająca jeden klucz konfiguracji.
 */
@Entity
@Table(name = "CONFIG_ENTRY")
@NamedQuery(name = "getConfigEntryByName", query = "SELECT ce FROM ConfigEntry ce WHERE ce.name = :name")
@EntityListeners(value = { BlockConfigEntryUpdate.class })
@SequenceGenerator(name = "ZARLOCK_SEQUENCE_GENERATOR", sequenceName = "ZARLOCK_SEQUENCE", allocationSize = 32, initialValue = 0)
public class ConfigEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZARLOCK_SEQUENCE_GENERATOR")
	@Column(name = "ID")
	Long id;

	@NotEmpty
	@Length(min = 1, max = 100)
	@Column(name = "NAME")
	String name;

	@Lob
	@Column(name = "VALUE")
	String value;

	/**
	 * Czy jest widoczna dla użyszkodnika.
	 */
	@Column(name = "VISIBLE")
	Boolean visible = Boolean.FALSE;

	/**
	 * Czy jest edytowalna (czyli updejtowalna) i czy da się ją usunąć
	 */
	@Column(name = "EDITABLE")
	Boolean editable = Boolean.FALSE;

	@Length(min = 0, max = 2000)
	@Column(name = "DESCRIPTION")
	String desctiption;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public String getDesctiption() {
		return desctiption;
	}

	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}
}
