<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Tạo Cặp RSA Key</title>
</head>
<body>
    <h2>Tạo Cặp Key</h2>
    <form action="${pageContext.request.contextPath}/rsa" method="get">
        <input type="hidden" name="action" value="genKey" >
        <input type="submit" value="Tạo cặp Key">
    </form>
    
        <label for="publicKey">Public Key:</label><br>
        <textarea id="publicKey" name="publicKey" rows="10" cols="50" readonly>${publicKey}
        </textarea><br><br>
        <label for="publicKeyFile">Tải lên Public Key:</label>
        <input type="file" id="publicKeyFile" name="publicKeyFile" accept=".pem,.key,.txt"><br><br>

        <label for="privateKey">Private Key:</label><br>
        <textarea id="privateKey" name="privateKey" rows="10" cols="50" readonly>
        ${privateKey}
        </textarea><br><br>
        <label for="privateKeyFile">Tải lên Private Key:</label>
        <input type="file" id="privateKeyFile" name="privateKeyFile" accept=".pem,.key,.txt"><br><br>
      <form action="${pageContext.request.contextPath}/rsa" method="post" enctype="multipart/form-data" >
        <input type="hidden" name="action" value="savePrivateKey" >
        <input type="hidden" name="privateKey" value="${privateKey}" >
        <input type="submit" value="Download Private Key">
    </form>
    <br><br>

    <!-- Lưu Public Key vào Database -->
    <form action="${pageContext.request.contextPath}/rsa" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="savePublicKey">
        <input type="hidden" name="publicKey" value="${publicKey}">
        <input type="hidden" name="id" value="${publicKey}">
        <input type="submit" value="Hoàn tất">
    </form>
   
</body>
</html>
