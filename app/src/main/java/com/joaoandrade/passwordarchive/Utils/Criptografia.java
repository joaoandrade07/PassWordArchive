package com.joaoandrade.passwordarchive.Utils;


import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class Criptografia {

    public static KeyPair geraRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(3072);
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] encripta(String mensagem, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(mensagem.getBytes(StandardCharsets.UTF_8));
    }

    public static String decripta(byte[] mensagemCifrada, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] mensagemAberta = cipher.doFinal(mensagemCifrada);
        return new String(mensagemAberta);
    }
}
