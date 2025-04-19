package com.code_words.api.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public final class Utility {

    public static String generateIdString(int activeGames) {
        String id;
        int idNum = incrementId(activeGames);
        if(idNum < 10) {
            id = "00" + idNum;
        } else if (idNum < 100) {
            id = "0" + idNum;
        } else {
            id = String.valueOf(idNum);
        }
        return id;
    }

    public static Integer extractGameIdNumeric(String gameId) {
        String numbers = gameId.replaceAll("(\\D+)(\\d+)", "$2");
        return Integer.parseInt(numbers);
    }

    private static Integer incrementId(int idNum) {
        return (idNum == 0) ? 1 : ++idNum;
    }

    public static String maskWord(String word) {
        return String.join(" ", word.replaceAll(".", "_").split(""));
    }

    public static String buildStringFromCharList(List<Character> charList) {
        StringBuilder sb = new StringBuilder();
        charList.forEach(c -> {
            sb.append(c);
            sb.append(" ");
        });
        return sb.toString();
    }
}
