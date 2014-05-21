/**
 * 
 */
package coolbuy360.service;

import android.text.method.DigitsKeyListener;

/**
 * ÃÜÂë¿òµÄ×Ö·ûÏÞÖÆ
 * @author yangxc 
 */
public class PasswordDigitsKey extends DigitsKeyListener {

	@Override
	protected char[] getAcceptedChars() {
		// TODO Auto-generated method stub
		char[] pwdchar = { '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'k', 'l',
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z', ',', '.', '/', '~', '!', '@', '#', '*' };
		return pwdchar;
	}
}