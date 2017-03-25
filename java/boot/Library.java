/**
 * 
 */
package boot;

import java.util.List;

final class Library {
	final String	libraryName;
	
	List<Long>		version	= null;
	
	boolean			zip		= false;
	
	java.io.File	file	= null;
	
	Library(final String featureName) {
		this.libraryName = featureName;
	}
	
	java.io.File getFile() {
		return this.file;
	}
	
	/**
	 * never used! example
	 * 
	 * @return
	 */
	List<Long> getVersion() {
		return this.version;
	}
	
	boolean isZip() {
		return this.zip;
	}
}
