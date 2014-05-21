/**
 * 
 */
package coolbuy360.logic;

/**
 * 定义QL更新字段 
 * @author yangxc
 *
 */
public class QLUpdateField {

	private String _field;
	private String _value;
	private String _type = "string";
	private Boolean _needUpdate = true;
	
	/**
	 * 定义QL更新字段
	 * @param _field 字段名称
	 * @param value 字段值
	 * @param type 字段类型，如：int,datetime,string
	 * @param needUpdate 是否需要更新，默认为true
	 */
	public QLUpdateField(String field, String value, String type, Boolean needUpdate) {
		super();
		this._field = field;
		this._value = value;
		this._type = type;
		this._needUpdate = needUpdate;
	}
	
	/**
	 * 定义QL更新字段
	 * @param _field 字段名称
	 * @param value 字段值
	 * @param needUpdate 是否需要更新，默认为true
	 */
	public QLUpdateField(String field, String value, Boolean needUpdate) {
		super();
		this._field = field;
		this._value = value;
		this._needUpdate = needUpdate;
	}
	
	/**
	 * 定义QL更新字段
	 * @param _field 字段名称
	 * @param value 字段值
	 */
	public QLUpdateField(String field, String value) {
		super();
		this._field = field;
		this._value = value;
	}

	/**
	 * 字段名称
	 * @return the _field
	 */
	public String getField() {
		return _field;
	}
	
	/**
	 * @param _field the _field to set
	 */
	public void setField(String field) {
		this._field = field;
	}
	
	/**
	 * 字段值
	 * @return the value
	 */
	public String getValue() {
		return _value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this._value = value;
	}
	
	/**
	 * 字段值
	 * @return the type
	 */
	public String getType() {
		return _type;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setType(String type) {
		this._type = type;
	}
	
	/**
	 * 是否需要更新
	 * @return the needUpdate
	 */
	public Boolean getNeedUpdate() {
		return _needUpdate;
	}
	
	/**
	 * @param needUpdate the needUpdate to set
	 */
	public void setNeedUpdate(Boolean needUpdate) {
		this._needUpdate = needUpdate;
	}	
}
