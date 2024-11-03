package org.airwhale.aams;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.HttpURLConnection;

import org.airwhale.aams.utils.ColorText;
import org.airwhale.aams.utils.MiniUtils;
import org.airwhale.aams.utils.PrintMessage;

public class Main {
    public static String version = "ALPHA-1.0";
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(PrintMessage.get("Airwhale Aircraft Management System [ " + version + " ]", "info"));
        System.out.format(PrintMessage.get("데이터베이스 서버를 등록합니다...", "info") + "\r");

        List<String> urllist = new ArrayList<>();
        urllist.add("https://neatorebackend.kro.kr/aams/Login.php");
        urllist.add("https://neatorebackend.kro.kr/aams/Notification.php");
        urllist.add("https://neatorebackend.kro.kr/aams/SysMail.php");
        urllist.add("https://neatorebackend.kro.kr/aams/AAMS_update.php");
        urllist.add("https://neatorebackend.kro.kr/aams/Aircrafts.php");
        urllist.add("https://neatorebackend.kro.kr/aams/Employees.php");
        MiniUtils.pause(500);
        System.out.println(PrintMessage.get("데이터베이스 서버를 등록합니다... 완료", "info"));

        MiniUtils.pause(1000);

        // icons
        String waiting = ColorText.text("[ ", "whie", "none", false) + ColorText.text("●", "gray", "none", false) + ColorText.text(" ]", "whie", "none", false);
        String connecting = ColorText.text("[ ", "whie", "none", false) + ColorText.text("●", "yellow", "none", false) + ColorText.text(" ]", "whie", "none", false);
        String offline = ColorText.text("[ ", "whie", "none", false) + ColorText.text("●", "red", "none", false) + ColorText.text(" ]", "whie", "none", false);
        String online = ColorText.text("[ ", "whie", "none", false) + ColorText.text("●", "green", "none", false) + ColorText.text(" ]", "whie", "none", false);

        MiniUtils.clearConsole();
        List<String> serverstatus = new ArrayList<>();
        for (String i : urllist) {
            String str = waiting +
                    " 연결 대기중" +
                    " | " +
                    i.split("/")[i.split("/").length - 1] +
                    System.lineSeparator();

            serverstatus.add(str);
        }

        boolean serveroffline = false;
        for (String i : urllist) {
            int idx = urllist.indexOf(i);
            String str = connecting +
                    ColorText.text(" 연결중     ", "yellow", "none", false) +
                    " | " +
                    i.split("/")[i.split("/").length - 1] +
                    System.lineSeparator();
            serverstatus.remove(idx);
            serverstatus.add(idx, str);

            MiniUtils.clearConsole();
            connection_print(serverstatus);

            URL url = new URI(urllist.get(idx)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                str = online +
                        ColorText.text(" 온라인     ", "green", "none", true) +
                        " | " +
                        i.split("/")[i.split("/").length - 1] +
                        System.lineSeparator();
            } else {
                str = offline +
                        ColorText.text(" 오프라인   ", "red", "none", true) +
                        " | " +
                        i.split("/")[i.split("/").length - 1] +
                        System.lineSeparator();
                serveroffline = true;
            }
            serverstatus.remove(idx);
            serverstatus.add(idx, str);

            MiniUtils.clearConsole();
            connection_print(serverstatus);
        }

        if (serveroffline) {
            System.out.println(PrintMessage.get("접속불가한 서버가 존재합니다.", "error"));
            System.out.println(PrintMessage.get("인터넷 연결 상태를 확인하시거나, 관리자에게 문의하여 주십시오.", "error"));
            System.exit(-1);
        }

        System.out.println(PrintMessage.get("서버와 성공적으로 연결되었습니다.", "info"));
        MiniUtils.pause(800);
        MiniUtils.clearConsole();

        // TODO : 로그인 창
    }

    public static void connection_print(List<String> serverstatus) {
        System.out.println(PrintMessage.get("서버와 통신을 시도중입니다. 잠시만 기다려 주십시오...", "info"));
        System.out.println("==========[ Server Connection Check ]==========");
        StringBuilder str = new StringBuilder();
        for (String i : serverstatus) {
            str.append(i);
        }
        System.out.format(str + "\r");
    }
}