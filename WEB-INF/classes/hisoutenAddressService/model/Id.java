package hisoutenAddressService.model;

/**
 * ID
 * 
 * @author bngper
 */
public class Id {

	private String _value;

	@SuppressWarnings("unused")
	@Deprecated
	private Id() {
		this(null);
	}

	public Id(String value) {
		_value = value;
	}

	public Id copy() {
		return new Id(new String(_value));
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Id)) {
			return false;
		}
		if (other == this) {
			return true;
		}

		return _value.equals(((Id) other)._value);
	}

	@Override
	public int hashCode() {
		return _value.hashCode();
	}

	public String getValue() {
		return _value;
	}

	@Deprecated
	public void setValue(String value) {
		_value = value;
	}
}
