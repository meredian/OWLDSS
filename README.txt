 PROJECT INSTALLATION
======================================

1) Run Eclipse
2) Create new Java project
3) Uncheck "Use default location" and set directory to repository


Components additional notes:

 SEMP INSTALLATION
======================================
 Semp is one of implemented solvers for now. Sources are proprietary,
 but if you have one - it can help. Ignore otherwise.
 Linux installation:
 1) cp path/to/semp .wine/drive_c
 2) wine regsvr32 .wine/drive_c/semp/SempEngineAPI.dll
 3) run wine configuration -> libs, add Msvcp60d.dll, Msvcrtd.dll, Msvcirtd.dll as exported, build-in libs
 4) cp Msvc* ../windows/system32/
 5) wine regedit
 6) add "C:\semp" to HKLM\System\CurrentCotrolSet\Control\SessionManager\Environment\Path

 JRUBY INSTALLATION
======================================
 Ruby used for managing solver repository using java interface

 1) sudo apt-get install jruby
 2) jruby -S gem install commander ffi-ncurses
 3) ./repo.rb --help

 SQLITE INSTALLATION
======================================
 Sqlite used as sample storage for some data, to be used by mporter concept.
 Any installation in not necessary, but to run init script (if something goes wrong)
 sqlite3 required. Then simply run sql/example.sql