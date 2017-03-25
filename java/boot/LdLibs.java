/**
 * Created on 26.11.2002
 * 
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of file
 * comments go to Window>Preferences>Java>Code Generation.
 */
package boot;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import boot.lfs.lfslibs.Handler;

/**
 * @author myx
 */
final class LdLibs extends ClassLoader {
	static final Map<String, Library>	LIBRARIES	= new TreeMap<>();
	
	private final ClassLoader			parent;
	
	private final Source[]				sources;
	
	private final Source				sourceGlobal;
	
	LdLibs(final File folderProtected, final File folderPublic, final ClassLoader parent) throws Exception {
		super( parent );
		this.parent = parent;
		final LibraryChooserFilter lcf = new LibraryChooserFilter();
		folderProtected.mkdirs();
		folderProtected.listFiles( lcf );
		if (!folderProtected.equals( folderPublic )) {
			folderPublic.mkdirs();
			folderPublic.listFiles( lcf );
		}
		{
			final List<Source> temp = new ArrayList<>();
			final List<String> tempNames = new ArrayList<>();
			final Map<String, Source> sources = new HashMap<>();
			final SourceDirectImpl sourceGlobal = new SourceDirectImpl();
			for (final Map.Entry<String, Library> current : LdLibs.LIBRARIES.entrySet()) {
				final Library library = current.getValue();
				final File currentFile = library.getFile();
				if (currentFile == null) {
					continue;
				}
				final boolean zip = library.isZip();
				final Source source;
				if (zip) {
					if (currentFile.length() == 0) {
						System.out.println( "AE Launcher: Skip library: " + library.libraryName + ", zero size" );
						continue;
					}
					source = new SourceZip( currentFile );
				} else {
					source = new SourceFiles( currentFile );
					/*
					 * final SourceFiles files = new SourceFiles( currentFile );
					 * source = new SourceDirectImpl( files.list(), files );
					 */
				}
				for (final String name : source.list()) {
					sourceGlobal.put( name, source.get( name ) );
				}
				System.out.println( "AE Launcher: Next library: " + library.libraryName + ", source: " + source );
				sources.put( library.libraryName, source );
				temp.add( source );
				tempNames.add( library.libraryName );
			}
			Handler.setSource( sourceGlobal );
			this.sources = temp.toArray( new Source[temp.size()] );
			this.sourceGlobal = sourceGlobal;
		}
	}
	
	@Override
	protected final Class<?> findClass(final String name) throws ClassNotFoundException {
		final byte[] data = this.sourceGlobal.getClass( name );
		if (data == null) {
			throw new ClassNotFoundException( "Not found: " + name );
		}
		return this.defineClass( name, data, 0, data.length );
	}
	
	@Override
	protected final URL findResource(final String name) {
		final Source source = this.sourceGlobal;
		final byte[] data = source.get( name );
		if (data == null) {
			return null;
		}
		final String url = Handler.URL_PREFFIX + "localhost/" + name;
		try {
			return new URL( url );
		} catch (final MalformedURLException e) {
			System.err.println( url );
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
		return "LB_LOADER, lib_count: " + this.sources.length;
	}
}
