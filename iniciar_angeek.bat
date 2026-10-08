@echo off
title Plataforma Angeek - Modo Offline / Online
echo ======================================================================
echo   PLATAFORMA EDUCATIVA ANGEEK (offline-first, un solo puerto)
echo ======================================================================
echo.
echo  Abre en tu navegador (este PC u otros en la misma red):
echo    - Este equipo : http://localhost:8080
echo    - Otros equipos: http://IP-DE-ESTE-PC:8080  (sin internet, misma WiFi/red)
echo.
echo  Datos guardados en: backend\data\  Archivos locales en: backend\media\
echo  Para detener: cierra esta ventana o Ctrl+C
echo ======================================================================
echo.
cd /d "%~dp0backend"
java -jar target\plataforma-estudiantil-1.0.0.jar
pause
