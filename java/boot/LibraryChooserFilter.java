/**
 * 
 */
package boot;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

final class LibraryChooserFilter implements FileFilter {
	@Override
	public boolean accept(final File pathname) {
		try {
			final String fullname = pathname.getName();
			final String least;
			final String name;
			final Library entry;
			{
				final int posV = fullname.lastIndexOf( '-' );
				if (posV == -1) {
					System.out.println( "AE2-CORE: skipping: " + fullname );
					return false;
				}
				least = fullname.substring( posV + 1 );
				name = fullname.substring( 0, posV );
				final Library library = LdLibs.LIBRARIES.get( name );
				if (library == null) {
					entry = new Library( name );
					LdLibs.LIBRARIES.put( name, entry );
				} else {
					entry = library;
				}
			}
			if (pathname.isDirectory()) {
				final List<Long> version = boot.Main.parseVersion( least );
				if (boot.Main.compareVersions( version, entry.version ) > 0) {
					entry.version = version;
					entry.file = pathname;
					entry.zip = false;
				}
				return true;
			}
			if (pathname.isFile() && (least.endsWith( ".jar" ) || least.endsWith( ".zip" ))) {
				final List<Long> version = boot.Main.parseVersion( least.substring( 0, least.length() - 4 ) );
				if (boot.Main.compareVersions( version, entry.version ) > 0) {
					entry.version = version;
					entry.file = pathname;
					entry.zip = true;
				}
				return true;
			}
			return false;
		} catch (final Throwable t) {
			return false;
		}
	}
}
