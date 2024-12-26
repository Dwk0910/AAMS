package org.airwhale.aams.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

}
