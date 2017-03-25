/**
 * 
 */
package boot.lfs.lfsboot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import boot.Source;

final class Connection extends URLConnection {
	private final byte[]	data;
	
	Connection(final URL url, final Source source) throws IOException {
		super( url );
		final String name = url.toString().substring( Handler.URL_PREFFIX_LENGTH );
		byte[] data = source.get( name );
		if (data == Source.FOLDER) {
			final String prefix = name.endsWith( "/" )
					? name
					: name + '/';
			final int prefixLength = prefix.length();
			final StringBuilder list = new StringBuilder();
			for (final String key : source.list()) {
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
