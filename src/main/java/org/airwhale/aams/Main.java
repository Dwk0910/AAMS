package org.airwhale.aams;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.net.*;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.HttpURLConnection;

import org.airwhale.aams.utils.ColorText;
import org.airwhale.aams.utils.MiniUtils;
import org.airwhale.aams.utils.PrintMessage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    public static String version = "ALPHA-1.0";
    public static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(PrintMessage.get("Airwhale Aircraft Management System [ " + version + " ]", "info"));
        System.out.format(PrintMessage.get("데이터베이스 서버를 등록합니다...", "info") + "\r");

        /*
        추후 :
            기능 사용시 해당 기능에 필요한 서버만 검사함.
            ex) 로그인 시 : Login.php만 검사, 메일 확인 시 : SysMail.php만 검사 등등...

            일부 서버가 오프라인이여도 AAMS 이용이 가능하게 된다.
         */

        List<String> urllist = new ArrayList<>();
        String urlhost = "https://port-9000-aamsbackend-m55ddohi02e930d2.sel4.cloudtype.app/";
        urllist.add(urlhost + "Login.php");
        urllist.add(urlhost + "Notification.php");
        urllist.add(urlhost + "SysMail.php");
        urllist.add(urlhost + "AAMS_update.php");
        urllist.add(urlhost + "Aircrafts.php");
        urllist.add(urlhost + "Employees.php");
        MiniUtils.pause(500);
        System.out.println(PrintMessage.get("데이터베이스 서버를 등록합니다... 완료", "info"));

        MiniUtils.pause(1000);

        // icons
        String waiting = ColorText.text("[ ", "white", "none", false) + ColorText.text("●", "gray", "none", false) + ColorText.text(" ]", "white", "none", false);
        String connecting = ColorText.text("[ ", "white", "none", false) + ColorText.text("●", "yellow", "none", false) + ColorText.text(" ]", "white", "none", false);
        String offline = ColorText.text("[ ", "white", "none", false) + ColorText.text("●", "red", "none", false) + ColorText.text(" ]", "white", "none", false);
        String online = ColorText.text("[ ", "white", "none", false) + ColorText.text("●", "green", "none", false) + ColorText.text(" ]", "white", "none", false);

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
            connection.setConnectTimeout(5000);
            try {
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
            } catch (SocketTimeoutException | ConnectException e) {
                str = offline +
                        ColorText.text(" 오프라인   ", "red", "none", true) +
                        " | " +
                        i.split("/")[i.split("/").length - 1] +
                        System.lineSeparator();
                serveroffline = true;
                serverstatus.remove(idx);
                serverstatus.add(idx, str);

                MiniUtils.clearConsole();
                connection_print(serverstatus);
//                PrintMessage.Error(1, "0002", "Connection Timed out. 서버가 오프라인이거나 인터넷 연결이 불안정합니다.");
            }
        }

        if (serveroffline) {
            System.out.println(PrintMessage.get("접속불가한 서버가 존재합니다.", "error"));
            System.out.println(PrintMessage.get("인터넷 연결 상태를 확인하시거나, 관리자에게 문의하여 주십시오.", "error"));
            System.exit(-1);
        }

        System.out.println(PrintMessage.get("서버와 성공적으로 연결되었습니다.", "info"));
        System.out.println(PrintMessage.get("자동로그인 여부를 확인중입니다...", "info"));
        MiniUtils.pause(800);

        File f = new File(System.getProperty("user.dir") + File.separator + "autologin.dat");

        String a = ColorText.text("A", "blue", "none", true);
        String m = ColorText.text("M", "blue", "none", true);
        String s = ColorText.text("S", "blue", "none", true);

        do {
            MiniUtils.clearConsole();
            System.out.println(PrintMessage.get("프로그램의 정상적인 사용을 위하여 항상 최신 버전으로 유지해주시기 바랍니다.", "warning"));
            System.out.println();
            System.out.println("         " + a + ColorText.text("irwhale ", "gray", "none", false) + a + ColorText.text("ircraft ", "gray", "none", false) + m + ColorText.text("anagement ", "gray", "none", false) + s + ColorText.text("ystem", "gray", "none", false));
            System.out.println("---------- [ 에어웨일 항공기 관리 시스템 ] ----------");
            System.out.println();
            System.out.println("Current : v." + ColorText.text(version, "green", "none", true));
            System.out.println(ColorText.text("Copyright 2024. Airwhale all rights reserved.", "gray", "none", true));
            System.out.println();
            System.out.println(ColorText.text("* 에어웨일에 오신 것을 환영합니다! 입사시에 받으신 사원번호와 비밀번호로 로그인해 주세요!", "cyan", "none", false));
            System.out.println(ColorText.text("* 종료하시려면 사원번호 입력란에 'q'를 입력하세요", "cyan", "none", false));
            System.out.print(ColorText.text("사원번호 : ", "green", "none", false));

            if (f.exists()) {
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject obj = (JSONObject) parser.parse(new FileReader(f));
                    String id = obj.get("id").toString();
                    String pwd = obj.get("pwd").toString();
                    System.out.println(ColorText.text(id, "black", "white", false));
                } catch (ParseException e) {
                    if (f.delete()) PrintMessage.Error(1, "0001", "autologin.dat", true);
                }
            } else {
                String id = scan.nextLine();
                if (id.equals("q") | id.equals("Q")) {
                    System.out.println(PrintMessage.get("시스템을 종료합니다...", "info"));
                    System.exit(0);
                }

                System.out.print(ColorText.text("비밀번호 : ", "green", "none", false));
                String pwd_org = scan.nextLine();
                String pwd = MiniUtils.encrypt(pwd_org);
                System.out.println(PrintMessage.get("로그인을 시도중입니다...", "info"));

            }

        } while (true);
    }

    private static void connection_print(List<String> serverstatus) {
        System.out.println(PrintMessage.get("서버와 통신을 시도중입니다. 잠시만 기다려 주십시오...", "info"));
        System.out.println("==========[ Server Connection Check ]==========");
        StringBuilder str = new StringBuilder();
        for (String i : serverstatus) {
            str.append(i);
        }
        System.out.format(str + "\r");
    }
}