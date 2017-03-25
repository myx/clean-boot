/**
 * Created on 01.11.2002
 * 
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of file
 * comments go to Window>Preferences>Java>Code Generation.
 */
package boot;

/**
 * @author myx
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public interface Source {
	/**
	 * Used by the URLStreamHandlers to distinguish a folder with no cost.
	 */
	public static final byte[]	FOLDER	= new byte[0];
	
	/**
	 * @param name
	 * @return load data
	 */
	byte[] get(final String name);
	
	/**
	 * @param name
	 * @return load class data
	 */
	byte[] getClass(final String name);
	
	/**
	 * @return boolean
	 */
	boolean isDirect();
	
	/**
	 * @return string array
	 */
	String[] list();
}
