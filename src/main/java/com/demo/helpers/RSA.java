package com.demo.helpers;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;

public class RSA {

    // Hàm mã hóa dữ liệu bằng private key (để ký dữ liệu)
    public String encryptWithPrivateKey(String data, String privateKeyString) throws Exception {
        // Giải mã chuỗi private key thành đối tượng PrivateKey
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        // Mã hóa dữ liệu
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] output = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Trả về dữ liệu đã mã hóa dưới dạng Base64
        return Base64.getEncoder().encodeToString(output);
    }

    // Hàm giải mã dữ liệu bằng public key (để xác minh chữ ký)
    public String decryptWithPublicKey(String data, String publicKeyString) throws Exception {
        // Giải mã chuỗi public key thành đối tượng PublicKey
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        // Giải mã dữ liệu
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] output = cipher.doFinal(Base64.getDecoder().decode(data));

        // Trả về dữ liệu đã giải mã dưới dạng chuỗi
        return new String(output, StandardCharsets.UTF_8);
    }

    // Hàm kiểm tra chữ ký
    public boolean verify(String data, String signature, String publicKeyStr) {
        try {
            // Kiểm tra và chuyển đổi chuỗi public key từ Base64 URL-safe sang chuẩn Base64
            String publicKeyBase64 = publicKeyStr.replace('_', '/').replace('-', '+');  // Chuyển Base64 URL-safe sang chuẩn Base64
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);  // Giải mã Base64 chuỗi public key
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Kiểm tra và chuyển đổi chuỗi signature từ Base64 URL-safe sang chuẩn Base64
            String signatureBase64 = signature.replace('_', '/').replace('-', '+');  // Chuyển Base64 URL-safe sang chuẩn Base64
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);  // Giải mã Base64 chuỗi signature

            // Kiểm tra độ dài chữ ký (RSA 2048 với SHA256 thì chữ ký sẽ có chiều dài 256 byte)
            if (signatureBytes.length != 256) {
                System.err.println("Error: Invalid signature length. Expected 256 bytes but got " + signatureBytes.length);
                return false;
            }

            // Khởi tạo Signature object với thuật toán SHA256withRSA
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            
            // Cung cấp dữ liệu đã hash vào Signature để kiểm tra chữ ký
            sig.update(data.getBytes(StandardCharsets.UTF_8));

            // Kiểm tra chữ ký, trả về true nếu hợp lệ
            return sig.verify(signatureBytes);

        } catch (IllegalArgumentException e) {
            System.err.println("Error: Invalid Base64 encoded data.");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        // Ví dụ sử dụng lớp RSA
        RSA rsa = new RSA();
        
        // Giả sử bạn đã có dữ liệu và public key, private key ở đây
        String data = "5d3317caeb69737848d90bbbe282c57a";
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC8Ruh5WF6Vf1b2k+9PVNv7GRN21FYd4s0hcmRKjPQxjpuT36gAWm5hMdFym1lRzneHj5d69wN6UFA/2a0opDkiWnXIWQqArFCFrpjngTDJg0M5EQ5jqM7z3mqFkQXLOgZzTVmxZY8/UCXRJKCVuIDGx8mCQ63WBAxHc+XuYWCDwqajuxz8m+NjG8ExzwDWoTcB+fusyfzPtStjnn14sKAin7RWHdmlvhd1Yh+VIHuEhH+TWNeDPGrTnb2OisJ3y7zxN+1MW/VMjLI1eF/w7PPKDH7K00FMdiqY3NBTh5Cs/yoxIJyQc9DXo5pHoSlxNtATaQSRmFXyZ9IgExEofqzxAgMBAAECggEAHgMzpbxHf49H/pCAcmX1lyZdKvEYNTVlO7KveA7WElxIQp9BeaTwuX+mgp1u9JJ0DWD/ZbOgk0vgLv11E+hxzx8abXB8MxzEzTqS1zyJ2WWaPquQXqSXMNrAokAG62l5g8HNzXCCEDangJpVGujJZmaMob5GJLqKdo1sFr7hEr+bCPI90xi23mITBHEoTsxXkhscBo/7h5mA/V9nV+DYPjCaFWHYTXs2n19u8NPjxP8PFOJmOvNhjX0cJ1/bJCKqh+ExZOPy3fwAdWPPuggGMcl46INz6/Kgswuai//d86QE+hkQ/UhkMsIpWF+y7r/Czje2DZuO3bkjynYeAM9lnQKBgQD7qpns6Mi+c6KvpmteQk/hUg7pqh2t9Aq1mua5WYHNmsh5dKkHOqjI4IOu/W+e8AwoTElXHsGT5AAd8KO4MmUWi+na/rGfj7Gli3PJi94aDA7TZn8BXFqZLgh8EYh7Ih6olwjyNTeLuoSQ1p1XlwcT36bgigCN8CYrvF1c+7HnRwKBgQC/hN9yjN1Qht0vR80VrcMZyW93OP/qZjbbdPOKNd6dn89hvtVBZE6rdtlu5TQFNiGkP4BXtMqEL+AKMzvJ/uGfWXnaxhjn4pJPD+bPvU4qPplz9ndH3huZ52dRZoXP1r56iq2O5xL3xf+YSlhzhd+dq3/4uN53L4QxJQEw9FTWBwKBgBcdBQQIIR+0uRVOwH+osXNtOOdm81Ddn/or3tLZEJ3UgGL/PuLmFUhVrmJs7kp3m3Y8A2RX88lkYEpfpnmtTzFx6iltyNxxswwUMa8qBFz6DIaH3FRAqiV1X9wdNB6y9IPhUzYDgzV3DOPMgijgOwstOENZCVH/41GS/DQk/aLTAoGBAK40UnuDnhuNUMuc5/L8Jybhjz5G3qF7cBKWgqAI+AEwSBaE2r2sW9KjPIwed/71igw4pqkgjcVHNuaQS6Yk7V1gqfJRln32HVdFA5Gag+rxHevZGA6K/Vg0oEnuk3DFyGT2l4AQt7QgqakAlwwdjZWBVdoLbm/DDN8W5cp9tF0pAoGBAIY516e19MWRxlnUAjK3jutj0wct9vCn3tSJXzoQNUC2f96jPh8/FgBfDa+s9DPZ1nPZ68e+8ybmd5ptNF+hLXlkhYMEuGi0ACVuazbrY/oNFAGMQVgrmqz3C5yg0QdME/ase8UCg3dGlixt3T3Ij0l4/kZWCQMHDJbZLGPb95ML";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvEboeVhelX9W9pPvT1Tb+xkTdtRWHeLNIXJkSoz0MY6bk9+oAFpuYTHRcptZUc53h4+XevcDelBQP9mtKKQ5Ilp1yFkKgKxQha6Y54EwyYNDOREOY6jO895qhZEFyzoGc01ZsWWPP1Al0SSglbiAxsfJgkOt1gQMR3Pl7mFgg8Kmo7sc/JvjYxvBMc8A1qE3Afn7rMn8z7UrY559eLCgIp+0Vh3Zpb4XdWIflSB7hIR/k1jXgzxq0529jorCd8u88TftTFv1TIyyNXhf8Ozzygx+ytNBTHYqmNzQU4eQrP8qMSCckHPQ16OaR6EpcTbQE2kEkZhV8mfSIBMRKH6s8QIDAQAB";

        // Mã hóa bằng private key
        String encryptedData = rsa.encryptWithPrivateKey(data, privateKey);
        System.out.println("Encrypted data: " + encryptedData);

        // Giải mã bằng public key
        String decryptedData = rsa.decryptWithPublicKey(encryptedData, publicKey);
        System.out.println("Decrypted data: " + decryptedData);

        // Kiểm tra chữ ký (ví dụ)
        String signature = encryptedData;
        boolean isValid = rsa.verify(data, signature, publicKey);
        System.out.println("Is signature valid: " + isValid);
    }
}
