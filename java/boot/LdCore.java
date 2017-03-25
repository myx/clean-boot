/**
 * Created on 01.11.2002
 * 
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of file
 * comments go to Window>Preferences>Java>Code Generation.
 */
package boot;

import java.net.MalformedURLException;
import java.net.URL;

import boot.lfs.lfsboot.Handler;

/**
 * @author myx
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
final class LdCore extends ClassLoader {
	private final ClassLoader	parent;
	
	private final Source		source;
	
	LdCore(final ClassLoader parent, final Source source) {
		super( parent );
		this.parent = parent;
		this.source = Handler.source = source;
	}
	
	@Override
	protected final Class<?> findClass(final String name) throws ClassNotFoundException {
		final byte[] data = this.source.getClass( name );
		if (data == null) {
			throw new ClassNotFoundException( "Not found: " + name );
		}
		return this.defineClass( name, data, 0, data.length );
	}
	
	@Override
	protected final URL findResource(final String name) {
		final byte[] data = this.source.get( name );
		if (data == null) {
			return null;
		}
		try {
			return new URL( Handler.URL_PREFFIX + name );
		} catch (final MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected final Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
		// First, check if the class has already been loaded
		Class<?> c = this.findLoadedClass( name );
		if (c == null) {
			try {
				c = this.parent.loadClass( name );
			} catch (final ClassNotFoundException e) {
				// If still not found, then invoke findClass<?> in order
				// to find the class.
				c = this.findClass( name );
			}
		}
		if (resolve) {
			this.resolveClass( c );
		}
		return c;
	}
	
	@Override
	public final String toString() {
		return "SR_LOADER, source: " + this.source;
	}
}
