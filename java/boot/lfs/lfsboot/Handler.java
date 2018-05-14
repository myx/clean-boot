/**
 * Created on 25.11.2002
 * 
 * myx - barachta */
package boot.lfs.lfsboot;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import boot.Source;

/**
 * @author myx
 * 
 * myx - barachta 
 */
public class Handler extends URLStreamHandler {
	/**
     * 
     */
	public static final String	URL_PREFFIX			= "lfsboot://";
	
	static final int			URL_PREFFIX_LENGTH	= Handler.URL_PREFFIX.length();
	
	/**
     * 
     */
	public static Source		source;
	
	@Override
	protected URLConnection openConnection(final URL url) throws IOException {
		return new Connection( url, Handler.source );
	}
}
