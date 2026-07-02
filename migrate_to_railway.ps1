# Script de migración de base de datos PostgreSQL local a Railway
# Modo de uso: .\migrate_to_railway.ps1 -RailwayUrl "TU_URL_DE_CONEXION_DE_RAILWAY"

param (
    [Parameter(Mandatory=$true)]
    [string]$RailwayUrl
)

# Ruta detectada de PostgreSQL en tu sistema
$PG_DIR = "C:\Program Files\PostgreSQL\18\bin"
$LOCAL_DB = "agroconnect"
$LOCAL_USER = "postgres"
$LOCAL_PASS = "password" # Contraseña local en application.properties

Write-Host "=============================================================" -ForegroundColor Cyan
Write-Host " MIGRACION DE POSTGRESQL LOCAL A RAILWAY " -ForegroundColor Cyan
Write-Host "=============================================================" -ForegroundColor Cyan

# 1. Exportar la base de datos local
Write-Host "1. Exportando base de datos local '$LOCAL_DB'..." -ForegroundColor Yellow
$env:PGPASSWORD = $LOCAL_PASS
& "$PG_DIR\pg_dump.exe" -h localhost -p 5432 -U $LOCAL_USER -F p -f backup.sql $LOCAL_DB

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Fallo al exportar la base de datos local. Por favor verifica que PostgreSQL local este encendido." -ForegroundColor Red
    exit $LASTEXITCODE
}
Write-Host "[OK] Exportacion exitosa generada en: backup.sql" -ForegroundColor Green

# 2. Importar la base de datos a Railway
Write-Host "2. Importando datos a tu base de datos de Railway..." -ForegroundColor Yellow
& "$PG_DIR\psql.exe" -d $RailwayUrl -f backup.sql

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Fallo al importar la base de datos a Railway. Verifica la URL de conexion de Railway." -ForegroundColor Red
    # Limpiar el archivo de backup por seguridad
    Remove-Item backup.sql -ErrorAction SilentlyContinue
    exit $LASTEXITCODE
}

# 3. Limpieza
Remove-Item backup.sql -ErrorAction SilentlyContinue
Write-Host "=============================================================" -ForegroundColor Green
Write-Host " [EXITO] Migracion completada con exito en Railway! " -ForegroundColor Green
Write-Host "=============================================================" -ForegroundColor Green
