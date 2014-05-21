package coolbuy360.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class httpURI {
	public static InputStream getStreamFromURL(String URL) {  
        InputStream in=null;  
        try {  
            URL url=new URL(URL);  
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();  
            in=connection.getInputStream();  
              
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return in;  
          
    }  
}

	  
    
