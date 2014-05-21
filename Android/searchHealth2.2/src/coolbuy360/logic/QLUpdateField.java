/**
 * 
 */
package coolbuy360.logic;

/**
 * ����QL�����ֶ� 
 * @author yangxc
 *
 */
public class QLUpdateField {

	private String _field;
	private String _value;
	private String _type = "string";
	private Boolean _needUpdate = true;
	
	/**
	 * ����QL�����ֶ�
	 * @param _field �ֶ�����
	 * @param value �ֶ�ֵ
	 * @param type �ֶ����ͣ��磺int,datetime,string
	 * @param needUpdate �Ƿ���Ҫ���£�Ĭ��Ϊtrue
	 */
	public QLUpdateField(String field, String value, String type, Boolean needUpdate) {
		super();
		this._field = field;
		this._value = value;
		this._type = type;
		this._needUpdate = needUpdate;
	}
	
	/**
	 * ����QL�����ֶ�
	 * @param _field �ֶ�����
	 * @param value �ֶ�ֵ
	 * @param needUpdate �Ƿ���Ҫ���£�Ĭ��Ϊtrue
	 */
	public QLUpdateField(String field, String value, Boolean needUpdate) {
		super();
		this._field = field;
		this._value = value;
		this._needUpdate = needUpdate;
	}
	
	/**
	 * ����QL�����ֶ�
	 * @param _field �ֶ�����
	 * @param value �ֶ�ֵ
	 */
	public QLUpdateField(String field, String value) {
		super();
		this._field = field;
		this._value = value;
	}

	/**
	 * �ֶ�����
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
	 * �ֶ�ֵ
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
	 * �ֶ�ֵ
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
	 * �Ƿ���Ҫ����
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
