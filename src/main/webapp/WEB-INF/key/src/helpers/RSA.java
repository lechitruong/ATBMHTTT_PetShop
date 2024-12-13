package helpers;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {
	KeyPair keyPair;
	PublicKey publicKey;
	PrivateKey privateKey;

	public KeyPair genKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		keyPair = keyPairGenerator.generateKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
		return keyPair;
	}

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


//	public void fileEncrypt(String inputPath, String outputPath, PublicKey publicKey)
//			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
//			InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
//		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//		keyGenerator.init(256);
//		byte[] iv = new byte[16];
//		IvParameterSpec spec = new IvParameterSpec(iv);
//		SecretKey secretKey = keyGenerator.generateKey();
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
//
//		CipherInputStream inputStream = new CipherInputStream(new BufferedInputStream(new FileInputStream(inputPath)),
//				cipher);
//		DataOutputStream dataOutputStream = new DataOutputStream(
//				new BufferedOutputStream(new FileOutputStream(outputPath)));
//
//		String keyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//		dataOutputStream.writeUTF(encrypt(keyString, publicKey));
//		dataOutputStream.writeLong(new File(inputPath).length());
//		dataOutputStream.writeUTF(Base64.getEncoder().encodeToString(iv));
//		byte[] buff = new byte[1024];
//		int i;
//		while ((i = inputStream.read(buff)) != -1) {
//			dataOutputStream.write(buff, 0, i);
//		}
//		inputStream.close();
//		dataOutputStream.flush();
//		dataOutputStream.close();
//	}

//	public void fileDecrypt(String inputPath, String outputPath, PrivateKey privateKey)
//			throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
//			BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
//		DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(inputPath)));
//		String keyString = dis.readUTF();
//		long size = dis.readLong();
//		byte[] iv = Base64.getDecoder().decode(dis.readUTF());
//
//		SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(decrypt(keyString, privateKey)), "AES");
//
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
//		CipherInputStream cis = new CipherInputStream(dis, cipher);
//		BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(outputPath));
//
//		byte[] buff = new byte[1024];
//		int i;
//		while ((i = cis.read(buff)) != -1) {
//			bof.write(buff, 0, i);
//		}
//
//		cis.close();
//		bof.flush();
//		bof.close();
//	}

	public String convertKeyToString(java.security.Key key) throws Exception {
		StringWriter stringWriter = new StringWriter();
		PemWriter pemWriter = new PemWriter(stringWriter);
		pemWriter.writeObject(new PemObject("KEY", key.getEncoded()));
		pemWriter.close();
		return stringWriter.toString();
	}

	public PublicKey convertStringToPublicKey(String publicKeyString) throws Exception {
		// Loại bỏ các dấu cách và các ký tự không mong muốn khỏi chuỗi
		String formattedKey = publicKeyString.replace("-----BEGIN KEY-----", "").replace("-----END KEY-----", "")
				.replaceAll("\\s", "");

		// Giải mã chuỗi Base64 để có mảng byte
		byte[] keyBytes = Base64.getDecoder().decode(formattedKey);

		// Tạo đối tượng X509EncodedKeySpec từ mảng byte
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// Sử dụng KeyFactory để chuyển đổi thành PublicKey
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	public PrivateKey convertStringToPrivateKey(String privateKeyString) throws Exception {
		// Loại bỏ các dấu cách và các ký tự không mong muốn khỏi chuỗi
		String formattedKey = privateKeyString.replace("-----BEGIN KEY-----", "").replace("-----END KEY-----", "")
				.replaceAll("\\s", "");

		// Giải mã chuỗi Base64 để có mảng byte
		byte[] keyBytes = Base64.getDecoder().decode(formattedKey);

		// Tạo đối tượng X509EncodedKeySpec từ mảng byte
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

		// Sử dụng KeyFactory để chuyển đổi thành PublicKey
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	public static void main(String[] args) throws Exception {
		RSA rsa = new RSA();
		rsa.genKey();
//		String publicKeyString;
//		System.out.println(publicKeyString = rsa.convertKeyToString(rsa.publicKey));
//		// test public
////        rsa.publicKey = rsa.convertStringToPublicKey(publicKeyString);
//		System.out.println("---------------------");
//		String privateKeyString;
//		System.out.println(privateKeyString = rsa.convertKeyToString(rsa.privateKey));
//		// test private
////        rsa.privateKey = rsa.convertStringToPrivateKey(privateKeyString);
//
//		rsa.publicKey = rsa.convertStringToPublicKey(
//				"-----BEGIN KEY-----\n" + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoPs0N4UhLD/O5S16cMLk\n"
//						+ "V1SZhg/tWqtUzSRQgvhnzO25e+l9oNiwBPuXQyKJFk9A4NOrRoD2Wu/Qt3FqxlOe\n"
//						+ "oq2h1GN0jDff5yua0FWEYPiLF25BWUdBVf1HRdv6/HGJ6Vs4MuBf3TdPzLWn2oaP\n"
//						+ "rspmPR4ggujJdbr/T5pji33A6P1PhrR/08QFHHatGqAEqtoHw0gy+y8s1tm3oSu/\n"
//						+ "ZK6sn0sVB1/F3e2u1156+3mia0qPaOw7mmQyuMMdqvSL/9NDVFWx3YrZAnnU5kXj\n"
//						+ "7PKVcbG/CzMqEWSHVBx8cdoHttTuQzVBNsoi/YtHpYlVScw7XBN9mUI7JH/rFRdz\n" + "BwIDAQAB\n"
//						+ "-----END KEY-----\n");
//		rsa.privateKey = rsa.convertStringToPrivateKey(
//				"-----BEGIN KEY-----\n" + "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCg+zQ3hSEsP87l\n"
//						+ "LXpwwuRXVJmGD+1aq1TNJFCC+GfM7bl76X2g2LAE+5dDIokWT0Dg06tGgPZa79C3\n"
//						+ "cWrGU56iraHUY3SMN9/nK5rQVYRg+IsXbkFZR0FV/UdF2/r8cYnpWzgy4F/dN0/M\n"
//						+ "tafaho+uymY9HiCC6Ml1uv9PmmOLfcDo/U+GtH/TxAUcdq0aoASq2gfDSDL7LyzW\n"
//						+ "2behK79krqyfSxUHX8Xd7a7XXnr7eaJrSo9o7DuaZDK4wx2q9Iv/00NUVbHditkC\n"
//						+ "edTmRePs8pVxsb8LMyoRZIdUHHxx2ge21O5DNUE2yiL9i0eliVVJzDtcE32ZQjsk\n"
//						+ "f+sVF3MHAgMBAAECggEAAdxVcqzVwNKt/AUmDKacCAxgx+VoO2uOi953FcR0QrCe\n"
//						+ "u7lrq8/FXRFhYrPD/N+GovKfYK296zgpY+1qLAesZjn2jGNn5fUBYAUngW/zm0FU\n"
//						+ "Lj2mgLcroKhQShTsVj2jt/BHeoMQaJiqkfWpjoOOppkNTHiMLKcvKO6z48Ink2Wo\n"
//						+ "f7PmXW2kp8pn6n+oXA07Mlzdqhz5eSwFg6IMZy0V3XxsNZA3NzpWCApu6UxaEezu\n"
//						+ "LBttKO7Yh+wAiyirKYEngRz70AlOUI9BzfvkY6Te+1jdeQ2T9+8b47sEWIJPhW4u\n"
//						+ "KTA6WQeUXtPiNvLHqx3FcC5+M70Kv+oHZTUyNOKr3QKBgQC77qLYVT3e1mwBdfaI\n"
//						+ "QZylNAIE348ojfBoOi1EfEH5PomPZnfick1NA/H8Y95NDRrXyG/js3Y/xGiDhBc6\n"
//						+ "NuMsm+hY8bHe/oruSPylgVBS7LBvntWiIxFEFzBSc1YX0B1M8fykZ9GJLOSIFCt0\n"
//						+ "IDDwSrIE9jsWB/uMlmfIiur0pQKBgQDbSaNFknQQ3+vMmJ6bkVYWbzf7H6midNnT\n"
//						+ "bgPMdQMbSgouSxmRDzmWS4wwQr8VVfP3EHyhHF2pyqaIHiA0gPjeM5gkVm77+lmF\n"
//						+ "QclvwykP1DUHRDnjSBXKqEgeu7dYC8lfOlJHEPrRaqKBynQQYzt7Jf2GlWoREO2y\n"
//						+ "Bgl2fmL9OwKBgHmHG+9hUB0l87oOGlqKc02xkAyutNi40NnVpShAYjlXY4VD8gqR\n"
//						+ "d8Nx5a4ctsiEzPL1PsW0a6dYz20cohiXHdjfCSVbRna5jAYDVL2CHftB6+aChAvR\n"
//						+ "OX0asbjftLC5d/kmEwYNzMY0LaSBxofVuU/qPaxPvm3Ew7pqGLrqen2xAoGAcK8F\n"
//						+ "bIHWjLlURT2D+3MSptKeFLhvhiC/smsNHqpeX06LrsA+shD9yaKmtkIGSSNzC1l4\n"
//						+ "gZ2+77pIGxhH+lLwD9a5bqrPu0IpcFWe+oRx/EVSvpormQR41jvlW42gqGjeIMYq\n"
//						+ "c4RhLu259jIftDAZ561c+ySabYyiaGG7yR0nJvMCgYAJoZj9pgCbOXKCon6qSP4w\n"
//						+ "TkDRPz7Y3xO/1PS5Iis6ZAOp0gQUL6WJNXxszIQqNpkGgcWe3TrmE7zqaTjhe/Al\n"
//						+ "cwu39bh1greccOlEkpHwqgf5SbSFQPucs1Hwmx/7UL282ZfceN+GV0mU3gUN8bMI\n"
//						+ "sqYOZwbMVPpH+u1NoAf3PA==\n" + "-----END KEY-----\n");
//		System.out.println("---------------------");
//		System.out.println(rsa.decrypt(
//				"G/iC0oBP7oGAm7N7JVPAQDdrY51l2vjmkMj6UAQRh3yprup4uu0OBn0j5Ydet90QVrRI6JCzyYWhxigsJzDg5o2VXG0WfdblQszgaatEtNVR7aNSrMTpxGxnVrGaZFZBOK5rtpFfA3XeCInZmGkUQ+UeDP6UoKFjFhJn295l0W6/IrvmrUcOl24/J4rFoo321ggPVe5uHSU6bmvPEYAMm8hsD8wru93VU3v6egGmdNMEEoOOzeW6ohEYumSNvUAQpshYBrOeovSnzgCajN3gbiiPJvgZbwgWe++VuFM/yGdmjTXZsOknviWhDOG2iku+vbd7pl0LHmP7LevGLdcKiQ==",
//				rsa.privateKey));
//        String en = rsa.encrypt("CNTT DHNL");
//        System.out.println(en);
//        System.out.println("---------------------");
//        System.out.println(rsa.decrypt(en));
//        rsa.fileEncrypt("D:\\Downloads\\21130543.jpg", "D:\\Downloads\\en-21130543.jpg", rsa.publicKey);
//        rsa.fileDecrypt("D:\\Downloads\\en-21130543.jpg", "D:\\Downloads\\de-21130543.jpg", rsa.privateKey);
		System.out.println(new RSA().convertKeyToString(rsa.publicKey));
	}
}