cd "%~dp0"
java.exe -cp hackerdb\webapp-root\WEB-INF\lib\hsqldb.jar org.hsqldb.Server -database.0 file:"%~dp0\testdbase\hackers" -dbname.0 hackers -address localhost
