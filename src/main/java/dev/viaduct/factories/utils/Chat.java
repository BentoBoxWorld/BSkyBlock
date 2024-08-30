package dev.viaduct.factories.utils;

import dev.viaduct.factories.FactoriesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {

    public static void log(String... messages) {
        for (final String message : messages)
            log(message);
    }

    public static void log(String messages) {
        tell(Bukkit.getConsoleSender(), "[" + FactoriesPlugin.getInstance().getName() + "] " + messages);
    }

    public static void tell(CommandSender toWhom, String... messages) {
        for (final String message : messages)
            tell(toWhom, message);
    }

    public static void tell(CommandSender toWhom, List<String> messages) {
        for (final String message : messages)
            tell(toWhom, message);
    }

    public static void tell(CommandSender toWhom, String message) {
        toWhom.sendMessage(colorize(message));
    }

    public static void tellFormatted(CommandSender toWhom, String message, Object... args) {
        tell(toWhom, String.format(message, args));
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String colorizeHex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorizeList(List<String> list) {
        List<String> temp = new ArrayList<>();
        for (String s : list)
            temp.add(colorize(s));
        return temp;
    }

    public static String strip(String text) {
        return ChatColor.stripColor(colorize(text));
    }

    public static List<String> strip(List<String> list) {
        List<String> temp = new ArrayList<>();
        for (String s : colorizeList(list)) {
            temp.add(ChatColor.stripColor(s));
        }
        return temp;
    }

    public static int getLength(String text, boolean ignoreColorCodes) {
        return ignoreColorCodes ? strip(text).length() : text.length();
    }

    public static String listToString(List<?> strings) {
        String d = ", ";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            if (i != strings.size() - 1) {
                sb.append(d);
            }
        }
        return sb.toString();
    }

    public static String listToCommaSeperatedStringNoSpace(List<?> strings) {
        String d = ",";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            if (i != strings.size() - 1) {
                sb.append(d);
            }
        }
        return sb.toString();
    }

}