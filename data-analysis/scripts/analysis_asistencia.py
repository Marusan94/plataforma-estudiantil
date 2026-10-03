"""
HUPAS1: Calcular la asistencia promedio por estudiante (exportar resultados).
HUPAS2: Detectar estudiantes con ausencias recurrentes (> 3 ausencias).
"""
import os
import pandas as pd

DATA_PATH = os.path.join(os.path.dirname(__file__), "..", "data", "asistencias.csv")
REPORT_PATH = os.path.join(os.path.dirname(__file__), "..", "reports")

def run():
    print("=" * 60)
    print("ANÁLISIS DE ASISTENCIA (HUPAS1 & HUPAS2)")
    print("=" * 60)
    df = pd.read_csv(DATA_PATH)

    # HUPAS1: Calcular asistencia promedio por estudiante
    df['es_presente'] = df['estado'].str.lower() == 'presente'
    resumen_asist = df.groupby(['estudiante_id', 'nombre_estudiante']).agg(
        total_sesiones=('estado', 'count'),
        sesiones_presente=('es_presente', 'sum'),
        porcentaje_asistencia=('es_presente', lambda x: round(x.mean() * 100, 2))
    ).reset_index().sort_values(by='porcentaje_asistencia', ascending=False)

    print("\n[HUPAS1] Asistencia promedio por estudiante:")
    print(resumen_asist.to_string(index=False))

    os.makedirs(REPORT_PATH, exist_ok=True)
    out_csv = os.path.join(REPORT_PATH, "asistencia_por_estudiante.csv")
    resumen_asist.to_csv(out_csv, index=False, encoding='utf-8')
    print(f"\n-> Resultados exportados a: {out_csv}")

    # HUPAS2: Detectar ausencias recurrentes (> 3)
    df['es_ausente'] = df['estado'].str.lower() == 'ausente'
    ausencias_por_estudiante = df.groupby(['estudiante_id', 'nombre_estudiante'])['es_ausente'].sum().reset_index(name='total_ausencias')
    estudiantes_en_riesgo = ausencias_por_estudiante[ausencias_por_estudiante['total_ausencias'] > 3]

    print("\n[HUPAS2] Estudiantes con ausencias recurrentes (> 3 faltas):")
    if not estudiantes_en_riesgo.empty:
        print(estudiantes_en_riesgo.to_string(index=False))
    else:
        print("   No se detectaron estudiantes con mas de 3 ausencias.")

if __name__ == "__main__":
    run()