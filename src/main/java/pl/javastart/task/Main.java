package pl.javastart.task;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final char PLUS_SIGN = '+';
    private static final char MINUS_SIGN = '-';

    public static void main(String[] args) {

        Main main = new Main();
        main.run(new Scanner(System.in));
    }

    public void run(Scanner scanner) {
        // uzupełnij rozwiązanie. Korzystaj z przekazanego w parametrze scannera
        String stringFromUser = getStringFromUser(scanner);
        List<String> patterns = List.of("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "dd.MM.yyyy HH:mm:ss");
        Queue<Character> charters = findPositiveOrNegativeCharter(stringFromUser);
        Queue<Long> numbers = findNumbersInTheString(stringFromUser);
        try {
            if (stringFromUser.startsWith("t")) {
                if (charters.size() != numbers.size()) {
                    throw new NullPointerException();
                } else {
                    LocalDateTime changedDateTime = changeLocalDateTimeAsRequired(stringFromUser, charters, numbers);
                    printDateTime(patterns, changedDateTime);
                }
            } else {
                changeTimeToRequiredTimeZonesAndPrint(stringFromUser, patterns);
            }
        } catch (NullPointerException e) {
            System.err.println("Niewłaściwy format");
        }
    }

    private static void printDateTime(List<String> patterns, LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(patterns.get(0));
        System.out.println("Czas lokalny: " + dateTimeFormatter.format(localDateTime));
    }

    private static void changeTimeToRequiredTimeZonesAndPrint(String originalText, List<String> patterns) {
        if (originalText.length() < 11) {
            originalText = originalText + " 00:00:00";
        }
        for (String pat : patterns) {
            try {
                ZonedDateTime warsawZoneDateTime = getZonedDateTime(originalText, pat);
                printTimeZones(patterns, warsawZoneDateTime);
            } catch (DateTimeException e) {
                //
            }
        }
    }

    private static ZonedDateTime getZonedDateTime(String originalText, String pat) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(pat);
        TemporalAccessor temporalAccessor = pattern.parse(originalText);
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZoneId warsawZoneId = ZoneId.of(TimeZone.TIME_FROM_COMPUTER.getZoneId());
        return localDateTime.atZone(warsawZoneId);
    }

    private static void printTimeZones(List<String> patterns, ZonedDateTime warsawZoneDateTime) {
        for (TimeZone value : TimeZone.values()) {
            ZoneId idOfRequestedZone = ZoneId.of(value.getZoneId());
            ZonedDateTime requestedDateTime = warsawZoneDateTime.withZoneSameInstant(idOfRequestedZone);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(patterns.get(0));
            System.out.println(value.getName() + dateTimeFormatter.format(requestedDateTime));
        }
    }

    private static LocalDateTime changeLocalDateTimeAsRequired(String originalText, Queue<Character> charterQueue, Queue<Long> numbers) {
        LocalDateTime localDateTime = LocalDateTime.now();
        char[] charArray = charArrayFromOriginalText(originalText);
        ChronoUnit unit = null;
        for (int i = 1; i < charArray.length; i++) {
            if (Character.isLetter(charArray[i])) {
                TimeOperators timeOperators = TimeOperators.grtFromDescription(charArray[i]);
                unit = getTemporalUnitFromTimeOperator(Objects.requireNonNull(timeOperators));
                if (!checkIfItsPositiveCharter(charterQueue)) {
                    localDateTime = getLocalDateTime(charterQueue, numbers, localDateTime, unit, false);
                } else {
                    localDateTime = getLocalDateTime(charterQueue, numbers, localDateTime, unit, true);
                }

            }
        }
        return localDateTime;
    }

    private static LocalDateTime getLocalDateTime(Queue<Character> charterQueue, Queue<Long> numbers,
                                                  LocalDateTime localDateTime, ChronoUnit unit, boolean isPlus) {
        charterQueue.poll();
        long number = 0;
        if (!isPlus) {
            number = getNumberFromTheQueue(numbers) * -1;
        } else {
            number = getNumberFromTheQueue(numbers);
        }
        return localDateTime.plus(number, unit);
    }

    private static ChronoUnit getTemporalUnitFromTimeOperator(TimeOperators s) {
        switch (s) {
            case YEAR -> {
                return ChronoUnit.YEARS;
            }
            case MONTH -> {
                return ChronoUnit.MONTHS;
            }
            case DAY -> {
                return ChronoUnit.DAYS;
            }
            case HOUR -> {
                return ChronoUnit.HOURS;
            }
            case MINUTES -> {
                return ChronoUnit.MINUTES;
            }
            case SECONDS -> {
                return ChronoUnit.SECONDS;
            }
            default -> throw new IllegalStateException("Unexpected value:");
        }
    }

    private static boolean checkIfItsPositiveCharter(Queue<Character> charters) {
        return PLUS_SIGN == charters.peek();
    }

    private static Long getNumberFromTheQueue(Queue<Long> numbers) {
        return Objects.requireNonNull(numbers.poll());
    }

    private static Queue<Character> findPositiveOrNegativeCharter(String originalText) {
        Queue<Character> plusMinusQueue = new LinkedList<>();
        char[] charArray = charArrayFromOriginalText(originalText);
        for (char c : charArray) {
            if (c == PLUS_SIGN || c == MINUS_SIGN) {
                plusMinusQueue.add(c);
            }
        }
        return plusMinusQueue;
    }

    private static char[] charArrayFromOriginalText(String originalText) {
        return originalText.toCharArray();
    }

    private static Queue<Long> findNumbersInTheString(String originalText) {
        Queue<Long> numbers = new LinkedList<>();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(originalText);
        while (m.find()) {
            numbers.add(Long.parseLong(m.group()));
        }
        return numbers;
    }

    private static String getStringFromUser(Scanner scanner) {
        System.out.println("Wczytaj dane");
        return scanner.nextLine();
    }
}

