"""
Orquestador Principal de Analítica de Datos
Ejecuta todas las historias de usuario de analítica (HUP1 - HUPE2)
"""
import os
import sys

# Agregar scripts al path
sys.path.append(os.path.join(os.path.dirname(__file__), "scripts"))

import analysis_registro
import analysis_hoja_vida
import analysis_familiar
import analysis_academico
import analysis_asistencia
import analysis_bienestar
import analysis_estadistico

def main():
    print("\n" + "#" * 70)
    print("  EJECUCIÓN DEL MÓDULO DE ANÁLISIS DE DATOS Y ESTADÍSTICAS")
    print("#" * 70 + "\n")

    analysis_registro.run()
    analysis_hoja_vida.run()
    analysis_familiar.run()
    analysis_academico.run()
    analysis_asistencia.run()
    analysis_bienestar.run()
    analysis_estadistico.run()

    print("\n" + "#" * 70)
    print("  ¡TODOS LOS ANÁLISIS, REPORTES CSV Y GRÁFICOS FUERON GENERADOS CON ÉXITO!")
    print("#" * 70 + "\n")

if __name__ == "__main__":
    main()