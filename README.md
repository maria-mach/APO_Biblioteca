# Sistema Biblioteca MARC21

Sistema web desenvolvido em Java para gerenciamento de biblioteca utilizando o padrão MARC21.

## Identificação do projeto

**Nome do projeto:** Sistema de Gestão de Biblioteca MARC21

**Integrantes:**

* Halan Willian Da Costa Sousa
* Maria Eduarda Machado Silva
* Naiany Evelyn Lima Sobral

**Datas importantes:**

* Revisão de aderência aos requisitos: 29/06/2026
* Data de entrega: 04/07/2026
* Apresentação em vídeo: 04/07/2026

---

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
USE biblioteca_marc21;
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

## Configuração do Mailtrap

O sistema usa Mailtrap para testar o envio de e-mails de ativação de cadastro e recuperação de senha.

No mesmo arquivo:

```
codigofonte/project-main/src/main/resources/db.properties
```

Configure os dados SMTP do Mailtrap:

```properties
smtp.host=sandbox.smtp.mailtrap.io
smtp.port=2525
smtp.user=SEU_USUARIO_MAILTRAP
smtp.password=SUA_SENHA_MAILTRAP
```

Esses dados ficam disponíveis no painel do Mailtrap, na aba SMTP/Integration do sandbox.

---

## Executando pelo Eclipse

1. Abra o Eclipse.
2. Acesse **File > Import > Maven > Existing Maven Projects**.
3. Em **Root Directory**, selecione:

   ```
   codigofonte/project-main
   ```

4. Confirme se o `pom.xml` foi encontrado e clique em **Finish**.
5. Configure o Apache Tomcat 9 em **Window > Preferences > Server > Runtime Environments**.
6. Na aba **Servers**, crie ou selecione o Tomcat 9.
7. Clique com o botão direito no Tomcat e escolha **Add and Remove**.
8. Adicione o projeto `biblioteca`.
9. Clique com o botão direito no projeto e use **Maven > Update Project**.
10. Use **Project > Clean**.
11. Inicie o Tomcat pela aba **Servers**.

A aplicação estará disponível em:

```
http://localhost:8080/biblioteca/
```

Se alterar arquivos ou scripts, use **Refresh**, **Project > Clean** e **Publish** no servidor.

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
* jQuery
* Bootstrap
* AJAX
* Gson

---

## Credenciais de teste

Administrador:

```
E-mail: admin@biblioteca.com
Senha: admin123
```

Leitor ativo:

```
E-mail: mariana@email.com
Senha: mariana123
```

Leitor pendente:

```
E-mail: carlos@email.com
Senha: carlos123
```

---

## Estrutura do banco

O projeto possui:

* Script de criação (`ddl.sql`);
* Script de carga inicial (`dml.sql`);
* Dicionário de dados (`dicionario_dados.txt`).

Execute `ddl.sql` e depois `dml.sql` antes da primeira utilização do sistema.
O `ddl.sql` já cria a estrutura atualizada do banco, incluindo o status dos empréstimos.
O `dml.sql` já contém a carga inicial completa compatível com essa estrutura.
