#!/bin/bash
# Shell Script para Configuração e Execução do Ambiente Portátil do Projeto
# Disciplina: APO2 — Sistema de Gestão de Biblioteca MARC21
set -e

# 1. Definição de caminhos absolutos
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TOOLS_DIR="$SCRIPT_DIR/tools"
MAVEN_HOME="$TOOLS_DIR/apache-maven-3.9.6"
MVN_CMD="$MAVEN_HOME/bin/mvn"
TOMCAT_HOME="$TOOLS_DIR/apache-tomcat-9.0.89"
TOMCAT_START="$TOMCAT_HOME/bin/startup.sh"
TOMCAT_WEBAPPS="$TOMCAT_HOME/webapps"

PROJECT_MAIN_DIR="$(cd "$SCRIPT_DIR/../project-main" && pwd)"
POM_FILE="$PROJECT_MAIN_DIR/pom.xml"
WAR_FILE="$PROJECT_MAIN_DIR/target/biblioteca.war"

echo "================================================================================"
echo "   SISTEMA DE GESTÃO DE BIBLIOTECA MARC21 — INICIALIZADOR DE AMBIENTE (BASH)"
echo "================================================================================"

# 2. Verificar se o Java está instalado
echo "[1/5] Verificando requisitos de ambiente..."
if ! command -v java &> /dev/null; then
    echo "ERRO: O Java não foi detectado no sistema. Certifique-se de instalar o JDK 11 ou superior antes de rodar este script."
    exit 1
fi
echo "Java detectado com sucesso."

# Criar pasta tools se não existir
mkdir -p "$TOOLS_DIR"

# 3. Baixar Maven de forma portátil se necessário
echo "[2/5] Verificando dependências do Maven portátil..."
if [ ! -f "$MVN_CMD" ]; then
    MVN_ZIP="$TOOLS_DIR/maven.zip"
    MVN_URL="https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz"
    
    echo "Baixando Maven 3.9.6..."
    curl -L -o "$MVN_ZIP" "$MVN_URL" || wget -O "$MVN_ZIP" "$MVN_URL"
    
    echo "Extraindo Maven..."
    tar -xzf "$MVN_ZIP" -C "$TOOLS_DIR"
    rm "$MVN_ZIP"
    chmod +x "$MVN_CMD"
    echo "Maven configurado com sucesso."
else
    echo "Maven portátil já está configurado em: $MAVEN_HOME"
fi

# 4. Baixar Tomcat 9.0 de forma portátil se necessário
echo "[3/5] Verificando dependências do Apache Tomcat 9.0 portátil..."
if [ ! -f "$TOMCAT_START" ]; then
    TOMCAT_ZIP="$TOOLS_DIR/tomcat.tar.gz"
    TOMCAT_URL="https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.89/bin/apache-tomcat-9.0.89.tar.gz"
    
    echo "Baixando Apache Tomcat 9.0.89..."
    curl -L -o "$TOMCAT_ZIP" "$TOMCAT_URL" || wget -O "$TOMCAT_ZIP" "$TOMCAT_URL"
    
    echo "Extraindo Tomcat..."
    tar -xzf "$TOMCAT_ZIP" -C "$TOOLS_DIR"
    rm "$TOMCAT_ZIP"
    chmod +x "$TOMCAT_HOME"/bin/*.sh
    echo "Tomcat configurado com sucesso."
else
    echo "Tomcat portátil já está configurado em: $TOMCAT_HOME"
fi

# 5. Compilar o projeto usando o Maven portátil
echo "[4/5] Compilando e empacotando o projeto (mvn clean package)..."
if ! "$MVN_CMD" clean package -f "$POM_FILE"; then
    echo "ERRO: Falha ao compilar o projeto. Verifique os logs do Maven."
    exit 1
fi
echo "Projeto compilado com sucesso. Arquivo WAR gerado."

# 6. Realizar o deploy do arquivo WAR no Tomcat
echo "[5/5] Implantando o projeto no Tomcat portátil..."
if [ -f "$WAR_FILE" ]; then
    cp "$WAR_FILE" "$TOMCAT_WEBAPPS/"
    echo "Deploy realizado com sucesso em: $TOMCAT_WEBAPPS"
else
    echo "ERRO: O arquivo biblioteca.war não foi encontrado na pasta target do projeto."
    exit 1
fi

# 7. Iniciar o Tomcat
echo ""
echo "================================================================================"
echo "   AMBIENTE PRONTO!"
echo "   Iniciando o Apache Tomcat 9.0..."
echo "   Endereço da aplicação: http://localhost:8080/biblioteca/"
echo "   Para encerrar o servidor, execute: $TOMCAT_HOME/bin/shutdown.sh"
echo "================================================================================"

# Iniciar Tomcat em segundo plano
"$TOMCAT_START"

