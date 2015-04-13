package de.ucb.prime.util;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;

public class AesUtility
{
    private static final String PASSWD = "myKey123$";
    
    private static final String BASE_DIR = "C:/test";
     
     
    public static void main(String[] args) throws Exception
    {
        encrypt(PASSWD, Paths.get(BASE_DIR, "input.txt"), Paths.get(BASE_DIR, "output.aes"));
        
    }

    public static void encrypt(String key, Path inputFile, Path outputFile) throws Exception
    {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static void decrypt(String key, Path inputFile, Path outputFile) throws Exception
    {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static void doCrypto(int cipherMode, String key, Path inputFile, Path outputFile)
            throws Exception
    {
        try
        {
            byte[] keyBytes = MessageDigest.getInstance("SHA-256").digest(key.getBytes("UTF-8"));

            Key secretKey = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(cipherMode, secretKey);

            CipherInputStream ci = new CipherInputStream(Files.newInputStream(inputFile), cipher);

            OutputStream outputStream = Files.newOutputStream(outputFile);
            IOUtils.copy(ci, outputStream);

            outputStream.close();
            ci.close();
        }
        catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
                | IOException ex)
        {
            throw new Exception("Error encrypting/decrypting file", ex);
        }
	}
}
