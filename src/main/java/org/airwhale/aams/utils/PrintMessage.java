package org.airwhale.aams.utils;

public class PrintMessage {
    // Message Print Method
    public static String get(String message, String type) {
        String color = "";

        switch (type) {
            case "info" -> color = "green";
            case "warning" -> color = "yellow";
            case "error" -> color = "red";
        }

        return ColorText.text("[", "gray", "none", false) +
                ColorText.text("AAMS", "cyan", "none", true) +
                ColorText.text("/", "orange", "none", false) +
                ColorText.text(type.toUpperCase(), color, "none", true) +
                ColorText.text("]", "gray", "none", false) + " : " +
                ColorText.text(message, "white", "none", true);
    }

    /*
    [ERRORCODE]

    - TYPE D (-edly, AAMS가 Crash될 정도의 치명적인 오류) -

    D-0001류 : (IOException, ParseException) cause에 발생파일 명시하기!
    ㄴ D-0001A : write 오류
    ㄴ D-0001B : read 오류
    ㄴ D-0001C : create 오류
    ㄴ D-0001D : (ParseException) 파일 손상으로 간주

    D-0002 : 서버연결오류
    ㄴ D-0002A : Server Connection Timed out

    D-0003 : Local System Error
    ㄴ D-0003A : Console을 구할 수 없음. (Console == null, PowerShell이나 CMD로 재시도)

    - TYPE G (-eneral, 일반적인 경고식 오류) -
    G-0001 : IOException 발생

    */

    // Error Print Mehod
    public static void Error(int type, String errorcode, String cause, Boolean... waitsec) {
        switch (type) {
            case 0 -> {
                System.out.println(PrintMessage.get("치명적인 오류가 발생하여 종료합니다. (Code " + ColorText.text("D-" + errorcode, "yellow", "none", true) + ColorText.text(") : ", "white", "none", true) + cause, "error"));
                System.exit(-1);
            }

            case 1 -> {
                System.out.println(PrintMessage.get("오류가 발생했습니다. (Code " + ColorText.text("G-" + errorcode, "yellow", "none", true) + ColorText.text(") : ", "white", "none", true) + cause, "error"));
                if (waitsec[0] != null) {
                    System.out.print(
                            ColorText.text("[ ", "gray", "none", false) +
                            ColorText.text("이전 화면으로 돌아가기까지.. ", "green", "none", false) +
                            ColorText.text("6", "red", "none", true) +
                            ColorText.text(" ]", "gray", "none", false)
                    );

                    for (int i = 5; i >= 0; i--) {
                        MiniUtils.pause(1000);
                        System.out.print("\b\b\b" +
                            ColorText.text(Integer.toString(i), "red", "none", true) +
                            ColorText.text(" ]", "gray", "none", false)
                        );
                    }
                }
            }
        }
    }

    public static void shutDown() {
        System.out.println(PrintMessage.get("시스템을 종료합니다...", "info"));
        System.exit(0);
    }
}