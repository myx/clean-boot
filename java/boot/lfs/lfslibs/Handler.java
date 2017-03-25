/**
 * Created on 26.11.2002
 * 
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of file
 * comments go to Window>Preferences>Java>Code Generation.
 */
package boot.lfs.lfslibs;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import boot.Source;

/**
 * @author myx
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
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
