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
USE biblioteca_marc21;

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

EXECUÇÃO PELO ECLIPSE

1. Abra o Eclipse.
2. Vá em File > Import > Maven > Existing Maven Projects.
3. Em Root Directory, selecione:

codigofonte/project-main

4. Confirme se o pom.xml foi encontrado e clique em Finish.
5. Configure um servidor Apache Tomcat 9 em Window > Preferences > Server > Runtime Environments.
6. Na aba Servers, crie ou selecione o Tomcat 9.
7. Clique com o botão direito no Tomcat > Add and Remove.
8. Adicione o projeto biblioteca.
9. Clique com o botão direito no projeto > Maven > Update Project.
10. Use Project > Clean.
11. Inicie o Tomcat pela aba Servers.

A aplicação estará disponível em:

http://localhost:8080/biblioteca/

Se alterar arquivos ou scripts, use Refresh, Project > Clean e Publish no servidor.

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
* jQuery
* Bootstrap
* AJAX
* Gson

---

CREDENCIAIS DE TESTE

Administrador:

E-mail: admin@biblioteca.com
Senha: admin123

Leitor ativo:

E-mail: mariana@email.com
Senha: mariana123

Leitor pendente:

E-mail: carlos@email.com
Senha: carlos123

---

BANCO DE DADOS

Executar obrigatoriamente:

* ddl.sql
* dml.sql

antes da primeira execução do sistema.

O arquivo ddl.sql já cria a estrutura atualizada do banco, incluindo o status dos empréstimos.
O arquivo dml.sql já contém a carga inicial completa compatível com essa estrutura.
