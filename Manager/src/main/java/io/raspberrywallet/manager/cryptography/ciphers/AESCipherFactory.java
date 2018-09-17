package io.raspberrywallet.manager.cryptography.ciphers;


import io.raspberrywallet.manager.cryptography.wrappers.data.Password;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class AESCipherFactory extends CipherFactory implements Serializable {
    
    private String hashAlgorithmName;
    private byte[] ivBytes;
    private byte[] keySalt;
    
    private int iterationsAmount;
    
    public AESCipherFactory(AlgorithmFactory algorithmFactory) {
        AESFactory aesAlgorithmData = (AESFactory)algorithmFactory;
        
        SecureRandom random = new SecureRandom();
        byte keySalt[] = new byte[16];
        ivBytes = new byte[16];
        random.nextBytes(keySalt);
        random.nextBytes(ivBytes);
        
        algorithmName = aesAlgorithmData.getAlgorithmName();
        algorithmFullName = aesAlgorithmData.getFullAlgorithmName();
        hashAlgorithmName = aesAlgorithmData.getHashAlgorithmName();
        this.keySalt = keySalt;
        keySize = aesAlgorithmData.getKeySize();
        iterationsAmount = aesAlgorithmData.getKeyHashIterationsAmount();
    }
    
    public Cipher getCipher(Password password, int cipherMode) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        
        Cipher cipher = Cipher.getInstance(algorithmFullName);
        
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(hashAlgorithmName);
        PBEKeySpec spec = new PBEKeySpec(password.getSecret(), keySalt, iterationsAmount, keySize);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), algorithmName);
        cipher.init(cipherMode, secret, ivParameterSpec);
        
        return cipher;
    }
    
    public String getHashAlgorithmName() {
        return hashAlgorithmName;
    }
    
    public byte[] getIvBytes() {
        return ivBytes;
    }
    
    public byte[] getKeySalt() {
        return keySalt;
    }
    
}
