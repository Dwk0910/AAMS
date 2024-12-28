package org.airwhale.aams;

import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.net.*;

import java.util.*;

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

            Console console = System.console();
            if (console == null) PrintMessage.Error(0, "0003A", "Console에서 null값을 반환하였습니다. CMD나 PowerShell등을 이용하여 다시 시도해주세요.");

            System.out.println("         " + a + ColorText.text("irwhale ", "gray", "none", false) + a + ColorText.text("ircraft ", "gray", "none", false) + m + ColorText.text("anagement ", "gray", "none", false) + s + ColorText.text("ystem", "gray", "none", false));
            System.out.println("---------- [ 에어웨일 항공기 관리 시스템 ] ----------");
            System.out.println();
            System.out.println("Current : v." + ColorText.text(version, "green", "none", true));
            System.out.println(ColorText.text("Copyright 2024. Airwhale all rights reserved.", "gray", "none", true));
            System.out.println();
            System.out.println(ColorText.text("* 에어웨일에 오신 것을 환영합니다! 입사시에 받으신 사원번호와 비밀번호로 로그인해 주세요!", "cyan", "none", false));
            System.out.println(ColorText.text("* 종료하시려면 사원번호 입력란에 'q'를 입력하세요", "cyan", "none", false));
            System.out.print(ColorText.text("사원번호 : ", "green", "none", false));

            String id = "", pwd = "";

            if (f.exists()) {
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject obj = (JSONObject) parser.parse(new FileReader(f));
                    id = obj.get("id").toString();
                    pwd = obj.get("pwd").toString();
                    System.out.println(ColorText.text(id, "black", "white", false));
                } catch (ParseException e) {
                    if (f.delete()) PrintMessage.Error(1, "0001", "autologin.dat", true);
                }
            } else {
                id = scan.nextLine();
                if (id.equals("q") | id.equals("Q")) PrintMessage.shutDown();

                // For developer
                else if (id.equals("newid")) {
                    System.out.print("ID : ");
                    String newid = scan.nextLine();
                    System.out.print("PWD : ");
                    String newpwd = scan.nextLine();
                    System.out.print("NAME : ");
                    String newname = scan.nextLine();

                    Map<String, String> params = new HashMap<>();
                    params.put("id", newid);
                    params.put("pwd", MiniUtils.encrypt(newpwd));
                    params.put("name", newname);

                    JSONObject response = MiniUtils.serverPostConnection(new URI(urlhost + "Login.php?type=create").toURL(), params);
                    if (!(boolean) response.get("status")) {
                        System.out.println("ERROR : " + response.get("cause"));
                        MiniUtils.pause(2000);
                    }

                    continue;
                } else if (id.equals("delid")) {
                     System.out.print("ID : ");
                     String typedid = scan.nextLine();
                     if (MiniUtils.ask("Confirm Delete ID " + typedid + "?")) {
                         Map<String, String> params = new HashMap<>();
                         params.put("id", typedid);

                         JSONObject response = MiniUtils.serverPostConnection(new URI(urlhost + "Login.php?type=delete").toURL(), params);
                         if (!(boolean) response.get("status")) {
                             System.out.println("ERROR : " + response.get("cause"));
                             MiniUtils.pause(2000);
                         }
                     }

                     continue;
                }

                System.out.print(ColorText.text("비밀번호 : ", "green", "none", false));
                char[] pwd_org_ary = Objects.requireNonNull(console).readPassword();

                StringBuilder sb = new StringBuilder();
                for (char i : pwd_org_ary) sb.append(i);

                String pwd_org = sb.toString();
                pwd = MiniUtils.encrypt(pwd_org);
                System.out.println(PrintMessage.get("로그인을 시도중입니다...", "info"));
            }

            // Request Login
            Map<String, String> params = new HashMap<>();
            params.put("id", id);
            params.put("pwd", pwd);

            JSONObject response = MiniUtils.serverPostConnection(new URI(urlhost + "Login.php").toURL(), params);
            if ((boolean) response.get("status")) break;
            else {
                switch ((String) response.get("cause")) {
                    case "idpwdincorrect" -> {
                        System.out.println(PrintMessage.get("아이디 또는 비밀번호가 잘못되었습니다. 이 메시지는 2초 후 사라집니다.", "error"));
                        MiniUtils.pause(2000);
                    }

                    case "Key incorrect" -> {
                        System.out.println(PrintMessage.get("AAMS internal error : Key incorrect", "error"));
                        System.exit(-1);
                    }

                    default -> {
                        System.out.println(PrintMessage.get("백엔드 서버에서 예상하지 못한 응답을 보냈습니다. 관리자에게 문의해주세요 : " + response.get("cause"), "error"));
                        System.exit(-1);
                    }
                }
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