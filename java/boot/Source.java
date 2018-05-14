/**
 * Created on 01.11.2002
 * 
 * myx - barachta */
package boot;

/**
 * @author myx
 * 
 * myx - barachta 
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
