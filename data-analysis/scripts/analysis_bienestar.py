"""
HUPAW1: Conocer que tipos de apoyo se solicitan mas (top 3, grafico de pastel).
HUPAW2: Conocer la frecuencia de solicitudes por mes (extraer mes del timestamp/fecha).
"""
import os
import pandas as pd
import matplotlib.pyplot as plt

DATA_PATH = os.path.join(os.path.dirname(__file__), "..", "data", "solicitudes_bienestar.csv")
REPORT_PATH = os.path.join(os.path.dirname(__file__), "..", "reports")

def run():
    print("=" * 60)
    print("ANÁLISIS DE APOYO Y BIENESTAR (HUPAW1 & HUPAW2)")
    print("=" * 60)
    df = pd.read_csv(DATA_PATH)

    # HUPAW1: Agrupar por tipo de apoyo y top 3
    solicitudes_tipo = df['tipo_apoyo'].value_counts()
    print("\n[HUPAW1] Distribución de solicitudes por tipo de apoyo:")
    print(solicitudes_tipo.to_string())

    top_3 = solicitudes_tipo.head(3)
    print("\n-> Top 3 apoyos más comunes:")
    for i, (tipo, cant) in enumerate(top_3.items(), 1):
        print(f"   {i}. {tipo}: {cant} solicitudes")

    # Gráfico de pastel
    os.makedirs(REPORT_PATH, exist_ok=True)
    plt.figure(figsize=(6, 6))
    colores = ['#38bdf8', '#818cf8', '#fb7185', '#34d399', '#fbbf24']
    plt.pie(solicitudes_tipo.values, labels=solicitudes_tipo.index, autopct='%1.1f%%',
            startangle=140, colors=colores[:len(solicitudes_tipo)])
    plt.title("Tipos de Apoyo más Solicitados en Bienestar (HUPAW1)", fontsize=13, pad=15)

    pie_file = os.path.join(REPORT_PATH, "solicitudes_bienestar_pastel.png")
    plt.tight_layout()
    plt.savefig(pie_file, dpi=150)
    plt.close()
    print(f"\n-> Gráfico de pastel guardado en: {pie_file}")

    # HUPAW2: Extraer mes del timestamp y contar solicitudes
    df['fecha'] = pd.to_datetime(df['fecha'])
    df['mes'] = df['fecha'].dt.strftime('%Y-%m (%B)')
    frecuencia_mes = df['mes'].value_counts().sort_index().reset_index()
    frecuencia_mes.columns = ['mes', 'total_solicitudes']

    print("\n[HUPAW2] Frecuencia de solicitudes por mes:")
    print(frecuencia_mes.to_string(index=False))

if __name__ == "__main__":
    run()