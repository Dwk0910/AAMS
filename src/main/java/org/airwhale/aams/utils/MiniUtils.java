package org.airwhale.aams.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Map;
import java.util.Scanner;

public class MiniUtils {
    public static boolean ask(String message) {
        Scanner scan = new Scanner(System.in);
        System.out.print(ColorText.text(message, "yellow", "none", true) + " " + ColorText.text("[Y/N] ", "white", "none", false) + " : ");
        String input = scan.nextLine();
        return input.equalsIgnoreCase("Y");
    }

    public static void clearConsole() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    public static void pause(int milisec) {
        try {
            Thread.sleep(milisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());

            byte[] bytes = md.digest();
            StringBuilder builder = new StringBuilder();

            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject serverPostConnection(URL url, Map<String, String> params) {
        JSONObject result = new JSONObject();
        try {
            params.put("key", "dcaa780b4a1c4821f8482c4cdcbed19bf6a8aa12ab42954053141cc0a988cf210d9255fc899632c3a728c0e5167683188c0183e9e8fe100f05640dd478c9d0b72d2c6670716a17ae0b8f97bceaf7e3250f53c2aca9243f61afa4483bde2dc24e8d8756b5e4690f8a574188e2cdad27731786e6062628d017e6e6ec858ec240eb60f1452ae22253456f4953a2ba8edbbaaeacbffac06868c03168590db9b75301");
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (!postData.isEmpty()) postData.append("&");
                postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), StandardCharsets.UTF_8));
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataBytes.length));
            connection.setDoOutput(true);
            connection.getOutputStream().write(postDataBytes);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) PrintMessage.Error(0, "0002", "Server Response : " + responseCode);

            BufferedReader bis = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String inputline;
            while ((inputline = bis.readLine()) != null) {
                builder.append(inputline);
            }

            // Parse and return
            String str_result = builder.toString();
            JSONParser parser = new JSONParser();
            result = (JSONObject) parser.parse(str_result);

            bis.close();
        } catch (IOException e) { PrintMessage.Error(0, "0001", "알 수 없는 오류가 발생했습니다.");
        } catch (ParseException e) { PrintMessage.Error(0, "0001D", "서버의 응답이 손상되었습니다."); }

        return result;
    }
}