/*
 * Created on 01.11.2005
 */
package boot;

final class SourceDirectImpl extends SourceDirectAbstract {
	
	SourceDirectImpl() {
		// empty
	}
	
	SourceDirectImpl(final String[] contents, final Source source) {
		for (final String current : contents) {
			final byte[] data = source.get( current );
			this.put( current, data );
		}
	}
}
