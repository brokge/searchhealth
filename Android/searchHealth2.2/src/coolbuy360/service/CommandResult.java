/**
 * 
 */
package coolbuy360.service;

import java.util.HashMap;
import java.util.Map;

/**
 * ִ���������� * CommandResult���ز��������getResult()�õ�trueΪ�ɹ���getMessage()�õ�˵����Ϣ��
 * 
 * @author yangxc
 */
public class CommandResult {

	private Boolean _result;
	private String _message;
	private Map<String,String> _originalResult;

	public CommandResult(String result, String message) {
		this._message = message;
		if (result.equals("true")) {
			this._result = true;
		} else {
			this._result = false;
		}
	}
	
	public CommandResult(Boolean result, String message) {
		this._message = message;
		this._result = result;
	}

	/**
	 * @return �������˵����Ϣ
	 */
	public String getMessage() {
		return _message;
	}
	
	public void setMessage(String message) {
		_message = message;
	}

	/**
	 * @return ���������true��ʾ�ɹ�
	 */
	public Boolean getResult() {
		return _result;
	}
	
	public void setResult(Boolean result) {
		_result = result;
	}
	
	public Map<String,String> getOriginalResult() {
		return _originalResult;
	}
	
	public void setOriginalResult(Map<String,String> originalresult) {
		_originalResult = originalresult;
	}
	
	public String getValue(String key) {
		if (_originalResult != null) {
			return _originalResult.get(key);
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return _result.toString() + " : " + _message;
	}
	
	public enum Result {
		True,
		False,
		Net_Error,
		Time_Error,
		Location_Error,
		UnKnow_Error,
		No_Data,
		Have_Data		
	}
}
