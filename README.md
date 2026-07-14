# clean-boot

A minimal Java bootstrap/classloader: `Source`/`SourceZip`/`SourceDirectImpl`/`SourceFiles` load code from different origins (loose files vs. zips), `LdCore`/`LdLibs` discover and load libraries, and `ChooserFilter`/`LibraryChooserFilter` pick the right variant among candidates. `BootRunner`/`Main` are the entry points.

This variant-selection-by-filter shape is the same idea `myx.common` later formalized as filename-based OS dispatch (`foo.Darwin` vs `foo.Common`, see `magic-devops`) — worth treating as the conceptual ancestor when comparing the two, not a coincidence.
