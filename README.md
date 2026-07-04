# Sistema Biblioteca MARC21

Sistema web desenvolvido em Java para gerenciamento de biblioteca utilizando o padrão MARC21.

## Requisitos

* Java JDK 11
* Apache Maven 3.8 ou superior
* Apache Tomcat 9
* MySQL 8.0 ou superior

---

## Estrutura do projeto

```
codigofonte/
├── ambiente/
│   ├── iniciar_ambiente.ps1
│   └── iniciar_ambiente.sh
├── database/
│   ├── ddl.sql
│   ├── dml.sql
│   └── dicionario_dados.txt
└── project-main/
```

---

## Configuração do banco de dados

Crie o banco de dados:

```sql
CREATE DATABASE biblioteca_marc21 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Execute os scripts na seguinte ordem:

```sql
SOURCE caminho/codigofonte/database/ddl.sql;
SOURCE caminho/codigofonte/database/dml.sql;
```

Configure o arquivo:

```
codigofonte/project-main/src/main/resources/db.properties
```

Informando:

* URL do banco
* Usuário
* Senha

---

## Compilando o projeto

Dentro da pasta:

```
codigofonte/project-main
```

Execute:

```bash
mvn clean package
```

Ao final será gerado:

```
target/biblioteca.war
```

---

## Executando no Tomcat

Copie o arquivo:

```
biblioteca.war
```

para:

```
tomcat/webapps/
```

Inicie o Tomcat.

A aplicação estará disponível em:

```
http://localhost:8080/biblioteca/
```

---

## Inicialização automática

Também é possível utilizar os scripts disponibilizados em:

```
codigofonte/ambiente/
```

Windows:

```powershell
iniciar_ambiente.ps1
```

Linux:

```bash
./iniciar_ambiente.sh
```

Os scripts realizam a inicialização do ambiente utilizando o **Apache Tomcat na porta 8080**.

---

## Tecnologias utilizadas

* Java 11
* Maven
* Apache Tomcat 9
* MySQL 8
* JDBC
* JSP
* Servlets

---

## Estrutura do banco

O projeto possui:

* Script de criação (`ddl.sql`);
* Script de carga inicial (`dml.sql`);
* Dicionário de dados (`dicionario_dados.txt`).

Execute `ddl.sql` e depois `dml.sql` antes da primeira utilização do sistema.
O `ddl.sql` já cria a estrutura atualizada do banco, incluindo o status dos empréstimos.
O `dml.sql` já contém a carga inicial completa compatível com essa estrutura.
