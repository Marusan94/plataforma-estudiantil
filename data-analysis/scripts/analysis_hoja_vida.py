"""
HUPH1: Saber cuantos estudiantes registran hoja de vida completa (columna booleana completado).
HUPH2: Encontrar las habilidades mas frecuentes en los perfiles (.str.split() y .explode(), exportar top 10 a CSV).
"""
import os
import pandas as pd

DATA_PATH = os.path.join(os.path.dirname(__file__), "..", "data", "perfiles.csv")
REPORT_PATH = os.path.join(os.path.dirname(__file__), "..", "reports")

def run():
    print("=" * 60)
    print("ANÁLISIS DE HOJA DE VIDA ESTUDIANTIL (HUPH1 & HUPH2)")
    print("=" * 60)
    df = pd.read_csv(DATA_PATH)

    # HUPH1: Detectar nulos o campos vacios y crear columna 'completado'
    campos_clave = ['resumen', 'intereses', 'experiencia', 'habilidades']
    # Completado si todos los campos clave no son nulos y no estan vacios
    df['completado'] = df[campos_clave].notna().all(axis=1) & (df[campos_clave] != '').all(axis=1)
    
    proporciones = df['completado'].value_counts(normalize=True) * 100
    conteo_completado = df['completado'].value_counts()
    resumen_hv = pd.DataFrame({'Total': conteo_completado, 'Porcentaje (%)': proporciones.round(2)})
    print("\n[HUPH1] Estado de completitud de Hoja de Vida:")
    print(resumen_hv.to_string())

    # HUPH2: Extraer y contar habilidades con .str.split() y .explode()
    habilidades_series = df['habilidades'].dropna().astype(str).str.split(r'\s*,\s*').explode()
    conteo_habilidades = habilidades_series.str.strip().value_counts().reset_index()
    conteo_habilidades.columns = ['habilidad', 'frecuencia']
    top_10_habilidades = conteo_habilidades.head(10)

    print("\n[HUPH2] Top 10 Habilidades más frecuentes (usando .str.split().explode()):")
    print(top_10_habilidades.to_string(index=False))

    os.makedirs(REPORT_PATH, exist_ok=True)
    top_10_csv = os.path.join(REPORT_PATH, "top_10_habilidades.csv")
    top_10_habilidades.to_csv(top_10_csv, index=False, encoding='utf-8')
    print(f"\n-> Top 10 exportado a CSV: {top_10_csv}")

if __name__ == "__main__":
    run()