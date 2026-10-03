"""
HUP1: Analizar cuantos usuarios nuevos se registran por semana.
HUP2: Identificar el tipo de usuarios mas registrados (value_counts, porcentajes).
"""
import os
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

DATA_PATH = os.path.join(os.path.dirname(__file__), "..", "data", "usuarios.csv")
REPORT_PATH = os.path.join(os.path.dirname(__file__), "..", "reports")

def run():
    print("=" * 60)
    print("ANÁLISIS DE INGRESO Y REGISTRO (HUP1 & HUP2)")
    print("=" * 60)
    df = pd.read_csv(DATA_PATH)
    df['fecha_registro'] = pd.to_datetime(df['fecha_registro'])

    # HUP1: Agrupar por semana y contar registros
    df['semana'] = df['fecha_registro'].dt.to_period('W').astype(str)
    usuarios_por_semana = df.groupby('semana').size().reset_index(name='nuevos_usuarios')
    print("\n[HUP1] Usuarios registrados por semana:")
    print(usuarios_por_semana.to_string(index=False))

    # HUP2: Identificar tipo de usuarios mas registrados
    conteo_tipos = df['rol'].value_counts()
    porcentajes_tipos = (df['rol'].value_counts(normalize=True) * 100).round(2)
    resumen_roles = pd.DataFrame({'Total': conteo_tipos, 'Porcentaje (%)': porcentajes_tipos})
    print("\n[HUP2] Distribución de usuarios por rol/tipo:")
    print(resumen_roles.to_string())

    # Generar gráfico con Seaborn
    os.makedirs(REPORT_PATH, exist_ok=True)
    plt.figure(figsize=(8, 5))
    sns.set_theme(style="whitegrid")
    ax = sns.barplot(x=conteo_tipos.index, y=conteo_tipos.values, hue=conteo_tipos.index, palette="Blues_r", legend=False)
    plt.title("Distribución de Usuarios por Rol (HUP2)", fontsize=14, pad=15)
    plt.xlabel("Rol de Usuario", fontsize=11)
    plt.ylabel("Cantidad de Registros", fontsize=11)
    for p in ax.patches:
        ax.annotate(f"{int(p.get_height())}", (p.get_x() + p.get_width() / 2., p.get_height()),
                    ha='center', va='center', xytext=(0, 5), textcoords='offset points')
    
    chart_file = os.path.join(REPORT_PATH, "distribucion_usuarios_rol.png")
    plt.tight_layout()
    plt.savefig(chart_file, dpi=150)
    plt.close()
    print(f"\n-> Gráfico guardado en: {chart_file}")

if __name__ == "__main__":
    run()