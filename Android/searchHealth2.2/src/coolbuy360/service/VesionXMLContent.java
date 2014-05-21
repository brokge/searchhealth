package coolbuy360.service;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class VesionXMLContent extends DefaultHandler {
		private String Version; // 获取版本号
		private String preTag;// 前一个元素名称
		
		public String getVersion() {
			return Version;
		}
		@Override
		public void startDocument() throws SAXException {
			Version =new String();
		}
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			
				String data = new String(ch, start, length);
				if ("string".equals(preTag)) {
					Version = data;		
			}
		}
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			preTag = localName;
			
		}
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			preTag = null;
			
		}		
	}