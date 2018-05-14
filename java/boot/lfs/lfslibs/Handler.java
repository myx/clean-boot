/**
 * Created on 26.11.2002
 * 
 * myx - barachta */
package boot.lfs.lfslibs;

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
	public static final String	URL_PREFFIX	= "lfslibs://";
	
	/**
	 * 
	 */
	static Source				SOURCE_GLOBAL;
	
	/**
	 * @param sourceGlobal
	 */
	public static final void setSource(final Source sourceGlobal) {
		Handler.SOURCE_GLOBAL = sourceGlobal;
	}
	
	@Override
	protected URLConnection openConnection(final URL url) throws IOException {
		return new Connection( url );
	}
}
