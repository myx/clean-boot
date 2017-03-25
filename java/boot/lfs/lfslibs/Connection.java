/**
 * 
 */
package boot.lfs.lfslibs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import boot.Source;

final class Connection extends URLConnection {
	private final byte[]	data;
	
	Connection(final URL url) throws IOException {
		super( url );
		final String name = url.getPath().substring( 1 );
		byte[] data = Handler.SOURCE_GLOBAL.get( name );
		if (data == Source.FOLDER) {
			final String prefix = name.endsWith( "/" )
					? name
					: name + '/';
			final int prefixLength = prefix.length();
			final StringBuilder list = new StringBuilder();
			for (final String key : Handler.SOURCE_GLOBAL.list()) {
				if (key.length() <= prefixLength
						|| !key.regionMatches( 0, name, 0, prefixLength )
						|| key.indexOf( '/', prefixLength - 1 ) != prefixLength - 1) {
					continue;
				}
				list.append( key.substring( prefixLength ) );
				list.append( '\r' );
				list.append( '\n' );
			}
			data = list.toString().getBytes( "utf-8" );
		} else if (data == null) {
			throw new IOException( "No such data: " + url );
		}
		this.data = data;
	}
	
	@Override
	public void connect() {
		// empty
	}
	
	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream( this.data );
	}
}
