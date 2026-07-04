SISTEMA BIBLIOTECA MARC21

REQUISITOS

* Java JDK 11
* Apache Maven 3.8 ou superior
* Apache Tomcat 9
* MySQL 8.0 ou superior

---

ESTRUTURA DO PROJETO

codigofonte/
│
├── ambiente/
│   ├── iniciar_ambiente.ps1
│   └── iniciar_ambiente.sh
│
├── database/
│   ├── ddl.sql
│   ├── dml.sql
│   └── dicionario_dados.txt
│
└── project-main/

---

CONFIGURAÇÃO DO BANCO

Criar o banco:

CREATE DATABASE biblioteca_marc21 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

Executar os scripts nesta ordem:

1. ddl.sql
2. dml.sql

Configurar o arquivo:

codigofonte/project-main/src/main/resources/db.properties

Informando:

* URL do banco
* Usuário
* Senha

---

COMPILAÇÃO

Entrar em:

codigofonte/project-main

Executar:

mvn clean package

Será gerado:

target/biblioteca.war

---

EXECUÇÃO

Copiar:

biblioteca.war

para:

tomcat/webapps/

Iniciar o Tomcat.

A aplicação estará disponível em:

http://localhost:8080/biblioteca/

---

INICIALIZAÇÃO AUTOMÁTICA

Windows:

iniciar_ambiente.ps1

Linux:

./iniciar_ambiente.sh

Os scripts iniciam automaticamente o ambiente utilizando o Apache Tomcat na porta 8080.

---

TECNOLOGIAS

* Java 11
* Maven
* Apache Tomcat 9
* MySQL 8
* JDBC
* JSP
* Servlets

---

BANCO DE DADOS

Executar obrigatoriamente:

* ddl.sql
* dml.sql

antes da primeira execução do sistema.

O arquivo ddl.sql já cria a estrutura atualizada do banco, incluindo o status dos empréstimos.
O arquivo dml.sql já contém a carga inicial completa compatível com essa estrutura.
