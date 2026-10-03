@echo off
title Plataforma Integral de Gestion y Acompanamiento Estudiantil
echo ======================================================================
echo   PLATAFORMA INTEGRAL DE GESTION Y ACOMPANAMIENTO ESTUDIANTIL
echo ======================================================================
echo.
echo [1/3] Iniciando Servidor Backend (Spring Boot)...
start "Backend Spring Boot" cmd /k "cd backend && ..\..\..\..\..\..\Documents\Java\apache-maven-3.9.11\bin\mvn.cmd spring-boot:run"

echo [2/3] Iniciando Servidor Frontend (React + Vite)...
start "Frontend React" cmd /k "cd frontend && npm run dev"

echo [3/3] Ejecutando Analisis de Datos en Python...
cd data-analysis
call venv\Scripts\activate.bat
python run_all_analysis.py

echo.
echo ======================================================================
echo  SISTEMA EN EJECUCION
echo  - Frontend: http://localhost:5173
echo  - Backend API: http://localhost:8080
echo  - Consola H2: http://localhost:8080/h2-console
echo  - Reportes de Analisis: data-analysis\reports\
echo ======================================================================
pause