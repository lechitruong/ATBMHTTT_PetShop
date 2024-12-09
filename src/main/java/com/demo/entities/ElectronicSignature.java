package com.demo.entities;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ElectronicSignature {
	private static Signature signature;

    static {
        try {
            signature = Signature.getInstance("SHA256withRSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String doSignature(String key, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] digitalSignature = signature.sign();
        return Base64.getEncoder().encodeToString(digitalSignature);
    }
    public static boolean checkSignature(String key, String data, String userSignature) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        byte[] signatureDecrypt = Base64.getDecoder().decode(userSignature);
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        return signature.verify(signatureDecrypt);
    }
    public static boolean isPrivateKey(String key) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            try {
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
                keyFactory.generatePrivate(privateKeySpec);
                return true;
            } catch (InvalidKeySpecException e) {
                return false;
            }
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            return false;
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        String key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDEi0knDTIzYRxzyWZ/sXwMcYc3++k9AhuAiTBf3NfdmJOF/tzRfKPxB+M/nR4PuDPJLnTQv9ptmUoDP35xZ2jhPZQPab1MIMS54d8XyPbIH2oN7h+7UuF2dh6l4fSzbynJhJklS43FMkWgWwLuPgsIztnDOHYGahZXEVGwrYfVbsgjlmoqZeDM0xn4iPKhTGEXW2ZSvRv/2AhArGh36DrfCirtZDaKV84Ddanrn4elPvzY0LCjPzaOUWuhvBjdd84vn97cLMGheC5KDjZHvVa9IbSuOX+09qtatxR+jvHlmFvqxfxIG192b6p1kfyyXl/XNYebxA2FBFMX1AUqSFbjAgMBAAECggEAFXcW0q6ExIq/FkAxMxH5t8wwVeNryi9wPH3/LAENDFUNC43VpQVlTD4tyfVJYrMd6MNrm57QZrbel/M3xn/iOvNEN9i3BVjw01JBULIwjZOsu/+9NHKtUAg/eaNvW6dw22LhbOrO/XHrm8NE0yswflJFAyan8TRl4zVvhAm3s434R4v5cl/byE8c3Nj2fTpC8s0CeqYKIBsPktt2i7Dkd7K1gH6UL/1y3MRi97aQLdY8FI3M9SKZ8lEHm1qIh2ueZ3R34x1UUQWL8Z4/ex80sQzfBxFs6aMpKL3mhCGsb2O3ELhAzI3TrVpE4Z4V/RDSnJOfY7k+yYoWYvg2G6LyFQKBgQDmKz7yuPpzgRXJmhhWvO6IcHRH2uqT7bx6FWV7SswBYEVwc+4AeO3660u3Xy7obADNH9TxmYLHc33iBPypSf20ZtnNECuL9CXKvWykyVPOiwQe7EUtgPIwigD8tQfgS1RViANhD6vg1X5HzmWNrvHwEDQzXexYYkCyOQTRGGKP9QKBgQDamf97FCo8jsmzcauzi37Miu9MsNoIYOUcry7poTqe32LGMtr+xTCRQhLQ3uYejnpd56nEw1PHHJRpf3xS6SaXgGLypCKQh5xbB+4NRN/2T+zCvIT/fZGtDZWEpFdjr1OxPttfw+c3H+qijeNAUIaBEvut3Ei/bSnRAqBPfZI8dwKBgFy8Tddzqg0BlGquuGGyK5UzYdZVoK/LWGYD2uhrAXkIddHSE7GDB7dSOCaApiCk60m6KozRIf0ETlLTWY1Hr32Q9u4FNtZjnxppaa2XJDoSjq162oBz9KCT6cPnmG3JTAhODbZ8nu6udfuucAI+22Gy1aVgkUonBBQKnyMz5PpFAoGBAL93hvgMj3n/LteHRna6RdNuFW88r5wLEmHvZs2nNCsXSfKDdKEVohZ4ovZjZXd6H9/EG0SGOQj7FVraGNCd+flUsFYKQWQKA38QEQd6PhgFpUBj0rHdEA1dCorlTs23MTzb61WTxx7XS7IZSOR6I3VGZT7A5M8WFDxHapZ1S/K9AoGBANXOdAhsHuva7IQiQ+oKgMKwElDY/75CLyAJQVbCyAeYNmP4eCJaQiG0FpE9E0CjDDJG6KmEQ2md4QmmMV3ybVaWpTY27C3wz3BjvpiNLxUmIPhlWqlFXkgreogUIMXtV+PSHy9oAKpls0u8cmLHQLUMhl/cEmEee8V4hjHhMLS8";
        String data = "Em G";
        System.out.println(data);
        String signature = doSignature(key, data);
        System.out.println(signature);
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxItJJw0yM2Ecc8lmf7F8DHGHN/vpPQIbgIkwX9zX3ZiThf7c0Xyj8QfjP50eD7gzyS500L/abZlKAz9+cWdo4T2UD2m9TCDEueHfF8j2yB9qDe4fu1LhdnYepeH0s28pyYSZJUuNxTJFoFsC7j4LCM7Zwzh2BmoWVxFRsK2H1W7II5ZqKmXgzNMZ+IjyoUxhF1tmUr0b/9gIQKxod+g63woq7WQ2ilfOA3Wp65+HpT782NCwoz82jlFrobwY3XfOL5/e3CzBoXguSg42R71WvSG0rjl/tParWrcUfo7x5Zhb6sX8SBtfdm+qdZH8sl5f1zWHm8QNhQRTF9QFKkhW4wIDAQAB";
        System.out.println(checkSignature(publicKey, data, signature));
    }
}
