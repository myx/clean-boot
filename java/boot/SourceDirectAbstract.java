/*
 * Created on 01.11.2005
 */
package boot;

import java.util.HashMap;
import java.util.Map;

/**
 * @author myx
 * 
 */
abstract class SourceDirectAbstract implements Source {
	
	private final Map<String, byte[]>	data		= new HashMap<>( 512, 0.5f );
	
	private int							codeBytes	= 0;
	
	private int							dataBytes	= 0;
	
	@Override
	public final byte[] get(final String name) {
		final String key;
		if (name.endsWith( ".class" )) {
			key = name.replace( '/', '.' ).substring( 0, name.length() - 6 );
		} else {
			key = name;
		}
		return this.data.get( key );
	}
	
	@Override
	public final byte[] getClass(final String key) {
		return this.data.get( key );
	}
	
	@Override
	public final boolean isDirect() {
		return true;
	}
	
	@Override
	public String[] list() {
		return this.data.keySet().toArray( new String[this.data.size()] );
	}
	
	protected void put(final String name, final byte[] bytes) {
		this.data.put( name, bytes );
		if (name.endsWith( ".class" )) {
			this.codeBytes += bytes.length;
			this.data.put( name.substring( 0, name.length() - 6 ).replace( '/', '.' ), bytes );
		} else {
			this.dataBytes += bytes.length;
		}
		final int pos = name.lastIndexOf( '/' );
		if (pos != -1) {
			final String folder = name.substring( 0, pos );
			if (!this.data.containsKey( folder )) {
				this.data.put( folder, Source.FOLDER );
			}
		}
	}
	
	@Override
	public final String toString() {
		return "DIRECT: entries=" + this.data.size() + ", code=" + this.codeBytes + ", data=" + this.dataBytes;
	}
}
