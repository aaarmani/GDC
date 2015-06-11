package ProxyConnectionTest;

import br.com.actia.authenticator.SimpleAuthenticator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class ProxyConnectionTest {
    private void openConnectionTest() {
	// OPEN CONNECTION THRU PROXY WITH AUTHENTICATION
	String url = "http://globoesporte.globo.com/servico/semantica/editorias/plantao/futebol/futebol-internacional/feed.rss";
	// String proxy = "10.0.0.202";
	// String port = "3128";
	String username = System.getProperty("http.proxyUser");
	String password = System.getProperty("http.proxyPassword");
	
        Authenticator.setDefault(new SimpleAuthenticator(username, password));
	
	URL server = null;

	try {
            server = new URL(url);
	}
	catch (MalformedURLException e) {
            e.printStackTrace();
	}

	Properties systemProperties = System.getProperties();
	
	//systemProperties.setProperty("http.proxyHost", proxy);
	//systemProperties.setProperty("http.proxyPort", port);
	
	InputStream in = null;
	HttpURLConnection connection = null;
	
	try {
            connection = (HttpURLConnection) server.openConnection();
            connection.connect();
            in = connection.getInputStream();
	} catch (IOException e) {
            e.printStackTrace();
	}
	
	// System.out.println(readFromInputStream(in));
    }

    private static String readFromInputStream(InputStream in) {
        StringBuffer strBuf = new StringBuffer();
        char ac[];
        
        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
        
        try {
            while (buf.ready()) {
                ac = new char[10000];
                buf.read(ac);
                strBuf.append(ac);
            }
            
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return strBuf.toString().trim();
    }
}
