/**
 * Created on 31.10.2002
 * 
 * myx - barachta */
package boot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/** @author barachta
 * 
 *         myx - barachta */
public final class Main {
	
	/** @param v1
	 * @param v2
	 * @return */
	static final int compareVersions(final List<Long> v1, final List<Long> v2) {
		
		if (v2 == null) {
			return v1 == null
				? 0
				: 1;
		}
		assert v1 != null : "NULL version is unexpected";
		final int v1l = v1.size();
		final int v2l = v2.size();
		for (int i = 0;; ++i) {
			if (v1l <= i) {
				return v2l == v1l
					? 0
					: -1;
			}
			if (v2l <= i) {
				return 1;
			}
			final int compare = v1.get(i).compareTo(v2.get(i));
			if (compare == 0) {
				continue;
			}
			return compare;
		}
	}
	
	private static final void initProperty(final String key, final String valueDefault) {
		
		System.setProperty(key, System.getProperty(key, valueDefault));
	}
	
	/** @param args
	 * @throws Throwable */
	public static void main(final String[] args) throws Throwable {
		
		System.err.println("BOOT(0): AE(3) Launcher 5.0");
		
		final File pathCurrent = new File(System.getProperty("user.dir"));
		final File pathHome = new File(System.getProperty("user.home"));
		
		Main.initProperty(
				"ru.myx.ae3.properties.path.public", //
				pathCurrent.getAbsolutePath());
		Main.initProperty(
				"ru.myx.ae3.properties.path.protected", //
				new File(new File(pathHome, "acm.cm5"), "protected").getAbsolutePath());
		Main.initProperty(
				"ru.myx.ae3.properties.path.private", //
				new File(new File(pathHome, "acm.cm5"), "private").getAbsolutePath());
		Main.initProperty(
				"ru.myx.ae3.properties.path.shared", //
				new File(new File(pathHome, "acm.cm5"), "shared").getAbsolutePath());
		
		final File pathPublic = new File(System.getProperty("ru.myx.ae3.properties.path.public"));
		final File pathProtected = new File(System.getProperty("ru.myx.ae3.properties.path.protected"));
		final File pathPrivate = new File(System.getProperty("ru.myx.ae3.properties.path.private"));
		final File pathShared = new File(System.getProperty("ru.myx.ae3.properties.path.shared"));
		
		System.err.println("BOOT(0): path current:   " + pathCurrent.getAbsolutePath());
		System.err.println("BOOT(0): path user:      " + pathHome.getAbsolutePath());
		System.err.println("BOOT(0): path public:    " + pathPublic.getAbsolutePath());
		System.err.println("BOOT(0): path protected: " + pathProtected.getAbsolutePath());
		System.err.println("BOOT(0): path private:   " + pathPrivate.getAbsolutePath());
		System.err.println("BOOT(0): path shared:    " + pathShared.getAbsolutePath());
		
		final Thread thread = new BootRunner(args, pathProtected, pathPublic);
		thread.start();
	}
	
	/** IN MEMORY OF OLDER VERSIONS <code>
	static final long parseVersion(final String version) {
		int cByte = 0;
		long result = 0;
		final StringTokenizer st = new StringTokenizer( version, "." );
		while (st.hasMoreTokens()) {
			final String current = st.nextToken();
			final long value = Integer.parseInt( current );
			if (value > 127) {
				throw new IllegalArgumentException( "Version value contains numbers greater than 127, version="
						+ version
						+ ", value="
						+ current );
			}
			result |= value << ((7 - (cByte++)) * 8);
		}
		return result;
	}
	 * </code>
	 * 
	 * @param version
	 * @return */
	static final List<Long> parseVersion(final String version) {
		
		final List<Long> result = new ArrayList<>(4);
		final StringTokenizer st = new StringTokenizer(version, ".");
		while (st.hasMoreTokens()) {
			final String current = st.nextToken();
			final long value = Long.parseLong(current);
			result.add(Long.valueOf(value));
		}
		return result;
	}
}
