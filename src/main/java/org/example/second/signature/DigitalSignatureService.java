package org.example.second.signature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Service
public class DigitalSignatureService {

    private final KeyGeneratorUtil keyGeneratorUtil;

    @Autowired
    public DigitalSignatureService(KeyGeneratorUtil keyGeneratorUtil) {
        this.keyGeneratorUtil = keyGeneratorUtil;
    }

    public String sign(String data, String base64PrivateKey) throws Exception {
        PrivateKey privateKey = decodePrivateKey(base64PrivateKey);
        byte[] signedData = keyGeneratorUtil.signData(data.getBytes(), privateKey);
        return keyGeneratorUtil.encodeToBase64(signedData);
    }

    public boolean verify(String data, String signatureString, PublicKey publicKey) throws Exception {
        byte[] signatureBytes = keyGeneratorUtil.decodeFromBase64(signatureString);
        return keyGeneratorUtil.verifySignature(data.getBytes(), signatureBytes, publicKey);
    }

    private PrivateKey decodePrivateKey(String base64PrivateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public PrivateKey getPrivateKey() throws NoSuchAlgorithmException {
        return generateKeyPair().getPrivate();
    }

    public PublicKey getPublicKey() throws NoSuchAlgorithmException {
        return generateKeyPair().getPublic();
    }
}
