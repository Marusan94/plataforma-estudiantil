"""
HUPA1: Calcular el promedio general por grupo (.mean(), ordenar de mayor a menor).
HUPA2: Identificar materias con mayor indice de reprobacion (nota < 3.0 por materia).
"""
import os
import pandas as pd

DATA_PATH = os.path.join(os.path.dirname(__file__), "..", "data", "calificaciones.csv")
REPORT_PATH = os.path.join(os.path.dirname(__file__), "..", "reports")

def run():
    print("=" * 60)
    print("ANÁLISIS ACADÉMICO (HUPA1 & HUPA2)")
    print("=" * 60)
    df = pd.read_csv(DATA_PATH)

    # HUPA1: Agrupar calificaciones por grupo y calcular media ordenada
    promedio_grupo = df.groupby('grupo_id')['nota'].mean().round(2).sort_values(ascending=False).reset_index()
    promedio_grupo.columns = ['grupo_id', 'promedio_nota']
    print("\n[HUPA1] Promedio general por grupo (Mayor a menor):")
    print(promedio_grupo.to_string(index=False))

    # HUPA2: Identificar materias con mayor indice de reprobacion (nota < 3.0)
    df['reprobada'] = df['nota'] < 3.0
    resumen_reprobacion = df.groupby('materia').agg(
        total_evaluaciones=('nota', 'count'),
        evaluaciones_reprobadas=('reprobada', 'sum'),
        porcentaje_reprobacion=('reprobada', lambda x: round(x.mean() * 100, 2)),
        promedio_materia=('nota', lambda x: round(x.mean(), 2))
    ).reset_index().sort_values(by='porcentaje_reprobacion', ascending=False)

    print("\n[HUPA2] Índice de reprobación por materia (Notas < 3.0):")
    print(resumen_reprobacion.to_string(index=False))

    os.makedirs(REPORT_PATH, exist_ok=True)
    out_csv = os.path.join(REPORT_PATH, "reprobacion_por_materia.csv")
    resumen_reprobacion.to_csv(out_csv, index=False, encoding='utf-8')
    print(f"\n-> Reporte guardado en: {out_csv}")

if __name__ == "__main__":
    run()