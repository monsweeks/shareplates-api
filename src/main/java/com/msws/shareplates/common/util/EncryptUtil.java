package com.msws.shareplates.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;

@Component
public class EncryptUtil {
    public String getEncrypt(String source, byte[] salt) {
        String result = "";
        try {

            byte[] a = source.getBytes();
            byte[] bytes = new byte[a.length + salt.length];
            System.arraycopy(a, 0, bytes, 0, a.length);
            System.arraycopy(salt, 0, bytes, a.length, salt.length);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);

            byte[] byteData = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; ++i) {
                sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
            }

            result = sb.toString();
        }catch(NoSuchAlgorithmException e) {
        	//임시 다른 Exception 으로 변경예정
        	throw new ServiceException(ServiceExceptionCode.BAD_REQUEST);
        }

        return result;
    }

    public String getHash(String source) throws NoSuchAlgorithmException {
        String result = "";
        byte[] a = source.getBytes();
        byte[] bytes = new byte[a.length];
        System.arraycopy(a, 0, bytes, 0, a.length);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);

        byte[] byteData = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; ++i) {
            sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
        }

        result = sb.toString();

        return result;
    }

    public byte[] getSaltByteArray() {
        // salt 생성
        java.util.Random random = new java.util.Random();
        byte[] saltBytes = new byte[8];
        random.nextBytes(saltBytes);

        return saltBytes;
    }

    public String getSaltString(byte[] saltBytes) {
        return new java.math.BigInteger(saltBytes).toString(16);
    }
}

