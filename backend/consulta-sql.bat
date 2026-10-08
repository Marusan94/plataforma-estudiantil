@echo off
REM =====================================================================
REM  CONSULTA SQL DIRECTA - sin abrir la consola web
REM  EDU.CORE / Angeek Box
REM
REM  Uso:
REM    consulta-sql.bat "SELECT COUNT(*) FROM notas"
REM    consulta-sql.bat -f mis-consultas.sql
REM =====================================================================
setlocal enabledelayedexpansion

set "H2JAR=C:\Users\USUARIO\.m2\repository\com\h2database\h2\2.2.224\h2-2.2.224.jar"
set "JDBC=jdbc:h2:file:./data/edudb;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE"
set "JAVA=java"
set "CP=%H2JAR%"

if "%H2JAR%"=="" (
  echo [ERROR] No se encontro el driver H2 en:
  echo   %H2JAR%
  echo.
  echo Descargalo de: https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar
  echo y ponlo en esa ruta, o cambia la variable H2JAR en este script.
  pause
  exit /b 1
)

if "%~1"=="" (
  echo.
  echo === CONSULTA SQL - EDU.CORE ===
  echo.
  echo Uso:
  echo   consulta-sql.bat "SELECT COUNT(*) FROM notas"
  echo   consulta-sql.bat -f archivo.sql
  echo.
  echo Base de datos: %JDBC%
  echo.
  pause
  exit /b 0
)

if "%~1"=="-f" (
  if "%~2"=="" (
    echo [ERROR] Falta el archivo SQL.
    pause
    exit /b 1
  )
  echo.
  echo === Ejecutando %~2 ===
  echo.
  "%JAVA%" -cp "%CP%" org.h2.tools.Shell -url "%JDBC%" -user sa -sql "RUNSCRIPT FROM '%~2'"
  echo.
  pause
  exit /b 0
)

echo.
echo === Ejecutando consulta ===
echo.
"%JAVA%" -cp "%CP%" org.h2.tools.Shell -url "%JDBC%" -user sa -sql "%~1"
echo.
pause