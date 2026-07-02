# PowerShell Script para Configuracao e Execucao do Ambiente Portatil do Projeto
# Disciplina: APO2 -- Sistema de Gestao de Biblioteca MARC21
$ErrorActionPreference = "Stop"

# 1. Definicao de caminhos absolutos
$ScriptDir = $PSScriptRoot
$ToolsDir = Join-Path $ScriptDir "tools"
$MavenHome = Join-Path $ToolsDir "apache-maven-3.9.6"
$MvnCmd = Join-Path $MavenHome "bin\mvn.cmd"
$TomcatHome = Join-Path $ToolsDir "apache-tomcat-9.0.89"
$TomcatStart = Join-Path $TomcatHome "bin\startup.bat"
$TomcatWebapps = Join-Path $TomcatHome "webapps"

$ProjectMainDir = Resolve-Path (Join-Path $ScriptDir "..\project-main")
$PomFile = Join-Path $ProjectMainDir "pom.xml"
$WarFile = Join-Path $ProjectMainDir "target\biblioteca.war"

# Auto-detectar JAVA_HOME se estiver vazio ou invalido
if ([string]::IsNullOrEmpty($env:JAVA_HOME) -or !(Test-Path $env:JAVA_HOME)) {
    $JavaRoot = "C:\Program Files\Java"
    if (Test-Path $JavaRoot) {
        $LatestJdk = Get-ChildItem -Path $JavaRoot -Filter "jdk*" | Sort-Object Name -Descending | Select-Object -First 1
        if ($LatestJdk -ne $null) {
            $env:JAVA_HOME = $LatestJdk.FullName
        }
    }
}

Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host "   SISTEMA DE GESTAO DE BIBLIOTECA MARC21 - INICIALIZADOR DE AMBIENTE" -ForegroundColor Cyan
Write-Host "================================================================================" -ForegroundColor Cyan

# 2. Verificar se o Java esta instalado
Write-Host "[1/5] Verificando requisitos de ambiente..." -ForegroundColor Yellow
java -version
Write-Host "Java detectado com sucesso." -ForegroundColor Green

# Criar pasta tools se nao existir
if (!(Test-Path $ToolsDir)) {
    New-Item -ItemType Directory -Path $ToolsDir | Out-Null
}

# 3. Baixar Maven de forma portatil se necessario
Write-Host "[2/5] Verificando dependencias do Maven portatil..." -ForegroundColor Yellow
if (!(Test-Path $MvnCmd)) {
    $mvnZip = Join-Path $ToolsDir "maven.zip"
    $mvnUrl = "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
    
    Write-Host "Baixando Maven 3.9.6 de forma silenciosa..." -ForegroundColor Yellow
    Invoke-WebRequest -Uri $mvnUrl -OutFile $mvnZip
    
    Write-Host "Extraindo Maven..." -ForegroundColor Yellow
    Expand-Archive -Path $mvnZip -DestinationPath $ToolsDir
    Remove-Item $mvnZip
    Write-Host "Maven configurado com sucesso." -ForegroundColor Green
} else {
    Write-Host "Maven portatil ja esta configurado em: $MavenHome" -ForegroundColor Green
}

# 4. Baixar Tomcat 9.0 de forma portatil se necessario
Write-Host "[3/5] Verificando dependencias do Apache Tomcat 9.0 portatil..." -ForegroundColor Yellow
if (!(Test-Path $TomcatStart)) {
    $tomcatZip = Join-Path $ToolsDir "tomcat.zip"
    $tomcatUrl = "https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.89/bin/apache-tomcat-9.0.89.zip"
    
    Write-Host "Baixando Apache Tomcat 9.0.89 de forma silenciosa..." -ForegroundColor Yellow
    Invoke-WebRequest -Uri $tomcatUrl -OutFile $tomcatZip
    
    Write-Host "Extraindo Tomcat..." -ForegroundColor Yellow
    Expand-Archive -Path $tomcatZip -DestinationPath $ToolsDir
    Remove-Item $tomcatZip
    Write-Host "Tomcat configurado com sucesso." -ForegroundColor Green
} else {
    Write-Host "Tomcat portatil ja esta configurado em: $TomcatHome" -ForegroundColor Green
}

# 5. Compilar o projeto usando o Maven portatil
Write-Host "[4/5] Compilando e empacotando o projeto (mvn clean package)..." -ForegroundColor Yellow
try {
    # Executar mvn clean package
    & $MvnCmd clean package -f $PomFile
    Write-Host "Projeto compilado com sucesso. Arquivo WAR gerado." -ForegroundColor Green
} catch {
    Write-Host "ERRO: Falha ao compilar o projeto. Verifique os logs do Maven." -ForegroundColor Red
    Exit 1
}

# 6. Realizar o deploy do arquivo WAR no Tomcat
Write-Host "[5/5] Implantando o projeto no Tomcat portatil..." -ForegroundColor Yellow
if (Test-Path $WarFile) {
    # Copia o arquivo .war para o webapps do Tomcat
    Copy-Item -Path $WarFile -Destination $TomcatWebapps -Force
    Write-Host "Deploy realizado com sucesso em: $TomcatWebapps" -ForegroundColor Green
} else {
    Write-Host "ERRO: O arquivo biblioteca.war nao foi encontrado na pasta target do projeto." -ForegroundColor Red
    Exit 1
}

# 7. Iniciar o Tomcat
Write-Host ''
Write-Host '================================================================================' -ForegroundColor Green
Write-Host '   AMBIENTE PRONTO!' -ForegroundColor Green
Write-Host '   Iniciando o Apache Tomcat 9.0 em uma nova janela de terminal...' -ForegroundColor Green
Write-Host '   Endereco da aplicacao: http://localhost:8080/biblioteca/' -ForegroundColor Green
Write-Host '   Use CTRL+C na janela do Tomcat para parar o servidor.' -ForegroundColor Green
Write-Host '================================================================================' -ForegroundColor Green

# Iniciar Tomcat em processo separado
$TomcatBinDir = Join-Path $TomcatHome 'bin'
Start-Process -FilePath $TomcatStart -WorkingDirectory $TomcatBinDir

