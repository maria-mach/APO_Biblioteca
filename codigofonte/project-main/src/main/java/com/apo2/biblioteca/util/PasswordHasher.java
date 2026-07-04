package com.apo2.biblioteca.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe utilitaria responsavel pela criptografia e comparacao de senhas.
 * Implementa o algoritmo SHA-256 nativo da JDK para seguranca de credenciais.
 * 
 * @version 1.0
 */
public class PasswordHasher {

    /**
     * Gera o hash SHA-256 de uma string de senha.
     * 
     * @param senha Texto da senha em formato limpo.
     * @return String contendo o hash em formato hexadecimal.
     */
    public static String hash(String senha) {
        if (senha == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao carregar o algoritmo SHA-256 para hashing.", e);
        }
    }

    /**
     * Compara uma senha limpa com o hash persistido.
     * 
     * @param senhaLimpa Senha digitada pelo usuario.
     * @param senhaHash  Hash salvo no banco de dados.
     * @return true se forem correspondentes, false caso contrario.
     */
    public static boolean verificar(String senhaLimpa, String senhaHash) {
        if (senhaLimpa == null || senhaHash == null) {
            return false;
        }
        return hash(senhaLimpa).equalsIgnoreCase(senhaHash);
    }
}

