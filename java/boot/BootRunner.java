/**
 * 
 */
package boot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

final class BootRunner extends Thread {
	public static String getMainClass(final Source source) throws Throwable {
		final byte[] data = source.get( "META-INF/MANIFEST.MF" );
		if (data == null) {
			return BootRunner.getProperties( source, new Properties() ).getProperty( "main.class", "Main" );
		}
		final String tmp = new String( data ).replace( '\r', '\n' );
		final int p1 = tmp.toLowerCase().indexOf( "\nmain-class:" );
		if (p1 == -1) {
			throw new IllegalArgumentException( "No main class specified in manifest!" );
		}
		final int p2 = tmp.indexOf( '\n', p1 + 12 );
		final String result = p2 == -1
				? tmp.substring( p1 + 12 )
				: tmp.substring( p1 + 12, p2 );
		return result.trim();
	}
	
	private static final Method getMainMethod(final Source source, final ClassLoader bootClassLoader) throws Throwable {
		final String mainClassName = BootRunner.getMainClass( source );
		System.out.println( "AE Launcher: Main class: " + mainClassName );
		final Properties properties = BootRunner.getProperties( source, System.getProperties() );
		System.setProperties( properties );
		System.out.println( "AE Launcher: intializing kernel." );
		System.out.flush();
		final Class<?> mainClass = Class.forName( mainClassName, true, bootClassLoader );
		return mainClass.getMethod( "main", String[].class );
	}
	
	public static Properties getProperties(final Source source, final Properties parent) throws IOException {
		final Properties result = new Properties( parent );
		if (parent == System.getProperties()) {
			result.putAll( parent );
		}
		{
			final byte[] data = source.get( "properties" );
			if (data != null) {
				final Properties local = new Properties();
				local.load( new ByteArrayInputStream( data ) );
				for (final Enumeration<?> names = local.propertyNames(); names.hasMoreElements();) {
					final String current = names.nextElement().toString();
					if (result.getProperty( current, "" ).trim().length() == 0) {
						result.setProperty( current, local.getProperty( current ) );
					}
				}
			}
		}
		{
			final byte[] data = source.get( ".properties" );
			if (data != null) {
				final Properties local = new Properties();
				local.load( new ByteArrayInputStream( data ) );
				for (final Enumeration<?> names = local.propertyNames(); names.hasMoreElements();) {
					final String current = names.nextElement().toString();
					if (result.getProperty( current, "" ).trim().length() == 0) {
						result.setProperty( current, local.getProperty( current ) );
					}
				}
			}
		}
		return result;
	}
	
	private final String[]		args;
	
	private final ClassLoader	bootClassLoader;
	
	private final File			rootPublic;
	
	private final File			rootProtected;
	
	private final File			current;
	
	private final Source		source;
	
	BootRunner(final String[] args, final File rootProtected, final File rootPublic) throws Exception {
		super( (ThreadGroup) null, "Core loader thread" );
		this.args = args;
		{
			final String property = System.getProperty( "java.protocol.handler.pkgs", "" );
			if (property.indexOf( "boot.lfs" ) == -1) {
				if (property.length() == 0) {
					System.setProperty( "java.protocol.handler.pkgs", "boot.lfs" );
				} else {
					System.setProperty( "java.protocol.handler.pkgs", property + "|boot.lfs" );
				}
			}
			assert new URL( boot.lfs.lfsboot.Handler.URL_PREFFIX + "dummy" ).getProtocol() != null : "URL handler is not properly set, cannot access class resources!";
		}
		{
			assert rootProtected.exists() || rootProtected.mkdirs() : "Cannot create directory: path="
					+ rootProtected.getAbsolutePath();
			assert rootPublic.exists() || rootPublic.mkdirs() : "Cannot create directory: path="
					+ rootPublic.getAbsolutePath();
			assert rootProtected.isDirectory() : "Root is not a directory: path=" + rootProtected.getAbsolutePath();
			assert rootPublic.isDirectory() : "Root is not a directory: path=" + rootPublic.getAbsolutePath();
			
			final File boot = new File( rootPublic, "boot" );
			assert boot.exists() || boot.mkdirs() : "Cannot create directory: path=" + boot.getAbsolutePath();
			assert boot.isDirectory() : "Boot is not a directory: path=" + boot.getAbsolutePath();
			
			this.rootProtected = rootProtected;
			this.rootPublic = rootPublic;
			final ChooserFilter chooser = new ChooserFilter();
			boot.listFiles( chooser );
			this.current = chooser.getFile();
			final boolean zip = chooser.isZip();
			if (this.current == null) {
				throw new RuntimeException( "Cannot find any boot image!" );
			}
			this.source = zip
					? new SourceZip( this.current )
					: new SourceFiles( this.current );
			// : new SourceDirect( new SourceFiles( current ) );
			/*
			 * if (zip) { this.source = new SourceZip( this.current ); } else {
			 * final SourceFiles files = new SourceFiles( this.current );
			 * this.source = new SourceDirectImpl( files.list(), files ); }
			 */
		}
		{
			final ClassLoader current = BootRunner.class.getClassLoader();
			this.bootClassLoader = new LdCore( new LdLibs( new File( this.rootProtected, "axiom" ),
					new File( this.rootPublic, "axiom" ),
					current ), this.source );
			this.setContextClassLoader( this.bootClassLoader );
		}
	}
	
	@Override
	public void run() {
		System.err.println( "AE Launcher: boot thread started" );
		try {
			if (this.bootClassLoader == null) {
				throw new RuntimeException( "Cannot obtain class loader!" );
			}
			System.out.println( "AE Launcher: Boot from: " + this.bootClassLoader );
			final Method mtd = BootRunner.getMainMethod( this.source, this.bootClassLoader );
			if (mtd != null) {
				System.out.println( "AE Launcher: invoking kernel." );
				System.out.flush();
				try {
					mtd.invoke( null, new Object[] { this.args } );
					{
						if (Thread.currentThread().isDaemon()) {
							System.out.println( "AE Launcher: Daemon thread - exiting after initialization." );
							System.out.flush();
						} else {
							System.out.println( "AE Launcher: Non-daemon thread - entering idle loop." );
							System.out.flush();
							for (; !Thread.interrupted();) {
								try {
									Thread.sleep( 15000L );
								} catch (final InterruptedException e) {
									break;
								}
							}
							System.out.println( "AE Launcher: Non-daemon thread - finished idle loop." );
							System.out.flush();
						}
					}
				} finally {
					System.out.println( "AE Launcher: finished." );
					System.out.flush();
				}
			}
		} catch (final Throwable t) {
			System.out.println( "AE Launcher: FATAL INITIALIZATION ERROR." );
			t.printStackTrace( System.out );
			System.out.flush();
			try {
				Thread.sleep( 5000L );
			} catch (final InterruptedException e) {
				// ignore
			}
		}
	}
}
