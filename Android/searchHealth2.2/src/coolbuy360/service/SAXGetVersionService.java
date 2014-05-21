package coolbuy360.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SAXGetVersionService {
	public static String readRssXml(InputSource inStream)throws Exception {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser = spf.newSAXParser(); // ´´½¨½âÎöÆ÷
		VesionXMLContent handler = new VesionXMLContent();
		saxParser.parse(inStream, handler);
		return handler.getVersion();
	}
	
	
	
	
}
