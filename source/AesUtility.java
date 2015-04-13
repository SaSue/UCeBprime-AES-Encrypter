package de.ucb.prime.util;


import java.io.IOException;
import java.io.OutputStream;
import java.io.*;
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
	
	// Eingabestrom
    static InputStreamReader input  = new InputStreamReader(System.in);
 
    // Eingabepuffer
    static BufferedReader keyboardInput = new BufferedReader(input);
     
    public static void main(String[] args) throws Exception
    {
        String PASSWD_input;
 
        System.out.print("Passwort: ");
 
        // hier wird auf Eingaben gewartet ..
        PASSWD_input = keyboardInput.readLine();
		
		String BASE_DIR_input;
        System.out.print("Verzeichnis: ");
		
		        // hier wird auf Eingaben gewartet ..
        BASE_DIR_input = keyboardInput.readLine();

		String FILE_NAME_input;
        System.out.print("Dateiname: ");
		
		        // hier wird auf Eingaben gewartet ..
        FILE_NAME_input = keyboardInput.readLine();
		String FILE_NAME_output;
		FILE_NAME_output = FILE_NAME_input + ".aes";
        System.out.println("Datei:" + BASE_DIR_input + "/" + FILE_NAME_input);
		        System.out.println("Dateiausgabe:" + BASE_DIR_input + "/" + FILE_NAME_output);
		 System.out.println("Passwort: " + PASSWD_input);
				
		encrypt(PASSWD_input, Paths.get(BASE_DIR_input, FILE_NAME_input), Paths.get(BASE_DIR, FILE_NAME_output));
        
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
