"""
HUPE1: Generar resumen estadistico por grupo (.describe() sobre nota, asistencia y guardar en archivo).
HUPE2: Identificar correlaciones entre nota y asistencia (.corr() y heatmap de seaborn).
"""
import os
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

CALIF_PATH = os.path.join(os.path.dirname(__file__), "..", "data", "calificaciones.csv")
ASIST_PATH = os.path.join(os.path.dirname(__file__), "..", "data", "asistencias.csv")
REPORT_PATH = os.path.join(os.path.dirname(__file__), "..", "reports")

def run():
    print("=" * 60)
    print("ANÁLISIS ESTADÍSTICO Y CORRELACIÓN (HUPE1 & HUPE2)")
    print("=" * 60)
    df_calif = pd.read_csv(CALIF_PATH)
    df_asist = pd.read_csv(ASIST_PATH)

    # HUPE1: Resumen estadistico con describe()
    desc_notas_grupo = df_calif.groupby('grupo_id')['nota'].describe()
    print("\n[HUPE1] Resumen estadístico de notas por grupo (.describe()):")
    print(desc_notas_grupo.to_string())

    os.makedirs(REPORT_PATH, exist_ok=True)
    resumen_file = os.path.join(REPORT_PATH, "resumen_estadistico_grupos.txt")
    with open(resumen_file, 'w', encoding='utf-8') as f:
        f.write("RESUMEN ESTADÍSTICO DE NOTAS POR GRUPO\n")
        f.write("=" * 50 + "\n")
        f.write(desc_notas_grupo.to_string())
    print(f"\n-> Resumen estadístico guardado en: {resumen_file}")

    # HUPE2: Correlacion entre nota y asistencia
    # 1. Agrupar notas promedio por estudiante
    est_notas = df_calif.groupby('estudiante_id')['nota'].mean().reset_index(name='promedio_calificaciones')

    # 2. Agrupar asistencia por estudiante
    df_asist['es_presente'] = df_asist['estado'].str.lower() == 'presente'
    est_asist = df_asist.groupby('estudiante_id')['es_presente'].mean().reset_index(name='tasa_asistencia')

    # 3. Unir ambos datasets
    df_correlacion = pd.merge(est_notas, est_asist, on='estudiante_id')
    
    # 4. Calcular matriz de correlacion .corr()
    matriz_corr = df_correlacion[['promedio_calificaciones', 'tasa_asistencia']].corr()
    print("\n[HUPE2] Matriz de correlación (.corr()):")
    print(matriz_corr.to_string())

    # 5. Generar Heatmap con Seaborn
    plt.figure(figsize=(6, 5))
    sns.heatmap(matriz_corr, annot=True, cmap="Blues", fmt=".3f", linewidths=1.5, cbar_kws={'label': 'Coeficiente de Correlación (r)'})
    plt.title("Heatmap: Correlación entre Calificaciones y Asistencia (HUPE2)", fontsize=12, pad=12)

    heatmap_file = os.path.join(REPORT_PATH, "heatmap_correlacion.png")
    plt.tight_layout()
    plt.savefig(heatmap_file, dpi=150)
    plt.close()
    print(f"\n-> Heatmap guardado en: {heatmap_file}")

if __name__ == "__main__":
    run()