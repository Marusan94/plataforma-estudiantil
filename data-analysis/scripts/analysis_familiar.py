"""
HUPF1: Analizar cuantos familiares consultan el perfil del estudiante y detectar sin interaccion.
HUPF2: Conocer en que horarios acceden mas los familiares (histograma por hora).
"""
import os
import pandas as pd
import matplotlib.pyplot as plt

DATA_ACCESOS = os.path.join(os.path.dirname(__file__), "..", "data", "accesos_familiares.csv")
DATA_PERFILES = os.path.join(os.path.dirname(__file__), "..", "data", "perfiles.csv")
REPORT_PATH = os.path.join(os.path.dirname(__file__), "..", "reports")

def run():
    print("=" * 60)
    print("ANÁLISIS DE MÓDULO FAMILIAR (HUPF1 & HUPF2)")
    print("=" * 60)
    df_accesos = pd.read_csv(DATA_ACCESOS)
    df_perfiles = pd.read_csv(DATA_PERFILES)

    # Cast to int
    df_accesos['estudiante_id'] = pd.to_numeric(df_accesos['estudiante_id'], errors='coerce').fillna(0).astype(int)
    df_perfiles['estudiante_id'] = pd.to_numeric(df_perfiles['estudiante_id'], errors='coerce').fillna(0).astype(int)

    # HUPF1: Agrupar por ID de estudiante y contar accesos
    accesos_por_estudiante = df_accesos.groupby('estudiante_id').size().reset_index(name='total_accesos_familia')
    
    # Cruzar con perfiles para detectar estudiantes sin interaccion
    todos_estudiantes = df_perfiles[['estudiante_id', 'nombre_estudiante']].drop_duplicates()
    merge_interaccion = pd.merge(todos_estudiantes, accesos_por_estudiante, on='estudiante_id', how='left').fillna(0)
    merge_interaccion['total_accesos_familia'] = merge_interaccion['total_accesos_familia'].astype(int)

    print("\n[HUPF1] Interacción de familiares por estudiante:")
    print(merge_interaccion.to_string(index=False))

    sin_interaccion = merge_interaccion[merge_interaccion['total_accesos_familia'] == 0]
    print(f"\n-> Estudiantes sin seguimiento familiar registrado: {len(sin_interaccion)}")
    if not sin_interaccion.empty:
        print(sin_interaccion[['estudiante_id', 'nombre_estudiante']].to_string(index=False))

    # HUPF2: Extraer hora de timestamp y agrupar por franja horaria
    df_accesos['timestamp'] = pd.to_datetime(df_accesos['timestamp'])
    df_accesos['hora'] = df_accesos['timestamp'].dt.hour

    accesos_por_hora = df_accesos['hora'].value_counts().sort_index()
    print("\n[HUPF2] Distribución de accesos por hora del día:")
    for hora, cant in accesos_por_hora.items():
        print(f"   {hora:02d}:00 hrs: {cant} accesos")

    os.makedirs(REPORT_PATH, exist_ok=True)
    plt.figure(figsize=(8, 4.5))
    plt.hist(df_accesos['hora'], bins=range(0, 25), color='#0284c7', edgecolor='black', alpha=0.7)
    plt.title("Histograma de Horarios de Acceso de Familiares (HUPF2)", fontsize=13, pad=12)
    plt.xlabel("Hora del Día (0 - 23h)", fontsize=11)
    plt.ylabel("Frecuencia de Accesos", fontsize=11)
    plt.xticks(range(0, 24, 2))
    plt.grid(axis='y', linestyle='--', alpha=0.7)

    hist_file = os.path.join(REPORT_PATH, "histograma_accesos_familiares.png")
    plt.tight_layout()
    plt.savefig(hist_file, dpi=150)
    plt.close()
    print(f"\n-> Histograma guardado en: {hist_file}")

if __name__ == "__main__":
    run()