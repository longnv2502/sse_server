package com.saltlux.sseclientjava.utils;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * @author dangngocson
 */
public final class StringUtils {

    /**
     * Empty String
     */
    public static final String EMPTY = "";

    /**
     * Regex wildcard
     */
    private static final String REGEX_WILDCARD = ".*";

    private StringUtils() {
        // utility class
    }

    public static String convertToSearchString(String filter) {
        if (filter.startsWith("\"") && filter.endsWith("\"")) {
            if (filter.length() > 1) {
                filter = filter.substring(1, filter.length() - 1);
            } else {
                filter = "";
            }
        } else {
            filter = REGEX_WILDCARD + filter + REGEX_WILDCARD;
        }

        return filter;
    }

    public static String joinStrings(String delimiter, Object... values) {
        StringJoiner stringJoiner = new StringJoiner(delimiter);
        Arrays.stream(values).map(Object::toString).forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    public static String chopNewLine(String rawText) {
        if (Objects.isNull(rawText) || rawText.isEmpty()) return rawText;
        String newText = rawText.replaceAll("\\uDB80\\uDEEF", "");
        return newText.replaceAll("\\r\\n|\\r|\\n", "");
    }

    public static boolean isNullOrEmpty(String text) {
        if (Objects.isNull(text) || text.isEmpty() || text.length() == 0)
            return true;
        return false;
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "");
    }

    public static void main(String[] args) throws Exception {
        String text = "대중교통 정보입니다.<br>\uDB80\uDEEF광주 → 영광 → 불갑(불갑사) → 축제장(셔틀버스 이용)<br>(광주 → 영광 1일 36회, 1시간), (영광 → 불갑(불갑사) 1일 9회)<br>\uDB80\uDEEF서울 → 영광 : 1일 17회, 3시간 20분 <br>\uDB80\uDEEF부산 → 광주(1일 18회, 3시간 10분) → 영광 (광주 → 영광 1일 36회, 1시간)";
        String newText = chopNewLine(text);

        System.out.println("New text: " + newText);
    }
}
