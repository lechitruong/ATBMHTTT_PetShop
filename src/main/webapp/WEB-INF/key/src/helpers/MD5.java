package helpers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String hashMD5(String input) {
        try {
            // Tạo instance của MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Hash chuỗi đầu vào và trả về kết quả dưới dạng mảng byte
            byte[] hashBytes = md.digest(input.getBytes());

            // Chuyển mảng byte sang chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                // Mỗi byte được chuyển sang 2 ký tự hex
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Thêm số 0 nếu cần để đủ 2 ký tự
                }
                hexString.append(hex);
            }
            return hexString.toString(); // Trả về chuỗi hex
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 Algorithm not found", e);
        }
    }

    public static void main(String[] args) {
        String input = "acc1";
        String hash = hashMD5(input);
        System.out.println("Original: " + input);
        System.out.println("MD5 Hash: " + hash);
    }
}
