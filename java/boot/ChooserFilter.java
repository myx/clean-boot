/**
 * 
 */
package boot;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

final class ChooserFilter implements FileFilter {
	List<Long>	version	= null;
	
	boolean		zip		= false;
	
	File		file	= null;
	
	@Override
	public boolean accept(final File pathname) {
		try {
			final String name = pathname.getName();
			if (pathname.isDirectory()) {
				final List<Long> version = boot.Main.parseVersion( name );
				if (boot.Main.compareVersions( version, this.version ) > 0) {
					this.version = version;
					this.file = pathname;
					this.zip = false;
				}
				return true;
			}
			if (pathname.isFile() && (name.endsWith( ".jar" ) || name.endsWith( ".zip" ))) {
				final List<Long> version = boot.Main.parseVersion( name.substring( 0, name.length() - 4 ) );
				if (boot.Main.compareVersions( version, this.version ) > 0) {
					this.version = version;
					this.file = pathname;
					this.zip = true;
				}
				return true;
			}
			return false;
		} catch (final Throwable t) {
			return false;
		}
	}
	
	File getFile() {
		return this.file;
	}
	
	List<Long> getVersion() {
		return this.version;
	}
	
	boolean isZip() {
		return this.zip;
	}
}
