package com.apo2.biblioteca.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fabrica de conexoes JDBC para o banco de dados MySQL 8.
 * Carrega dinamicamente os parametros do arquivo db.properties.
 * 
 * @version 1.0
 */
public class ConexaoFactory {

    private static String url;
    private static String usuario;
    private static String senha;
    private static boolean driverCarregado = false;

    static {
        try (InputStream input = ConexaoFactory.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            
            Properties prop = new Properties();
            if (input == null) {
                // Configura defaults caso db.properties nao esteja presente no classpath de teste
                url = "jdbc:mysql://localhost:3306/biblioteca_marc21?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                usuario = "root";
                senha = "root";
            } else {
                prop.load(input);
                url = prop.getProperty("db.url");
                usuario = prop.getProperty("db.user");
                senha = prop.getProperty("db.password");
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha ao carregar as configuracoes do banco de dados.", e);
        }
    }

    private static synchronized void carregarDriver() {
        if (!driverCarregado) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                driverCarregado = true;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Driver JDBC do MySQL nao encontrado.", e);
            }
        }
    }

    /**
     * Retorna uma conexao ativa com o banco de dados MySQL.
     * 
     * @return Connection ativa
     * @throws SQLException caso ocorra erro ao tentar se conectar ao banco de dados
     */
    public static Connection getConexao() throws SQLException {
        carregarDriver();
        return DriverManager.getConnection(url, usuario, senha);
    }
}

