package pl.javastart.task;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String YEAR = "y";
    private static final String MONTH = "M";
    private static final String DAY = "d";
    private static final String HOUR = "h";
    private static final String MINUTES = "m";
    private static final String SECONDS = "s";

    public static void main(String[] args) {

        Main main = new Main();
        main.run(new Scanner(System.in));
    }

    public void run(Scanner scanner) {
        // uzupełnij rozwiązanie. Korzystaj z przekazanego w parametrze scannera
        String originalText = getStringFromUser(scanner);
        List<String> patterns = List.of("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "dd.MM.yyyy HH:mm:ss");
        if (originalText.contains("t")) {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(patterns.get(0));
            Queue<String> charters = findPositiveOrNegativeCharter(originalText);
            Queue<Long> numbers = findNumbersInTheString(originalText);
            localDateTime = changeLocalDateTimeAsRequired(originalText, localDateTime, charters, numbers);
            System.out.println("Czas lokalny: " + dateTimeFormatter.format(localDateTime));
        } else {
            changeTimeToRequiredTimeZonesAndPrint(originalText, patterns);
        }
    }

    private static void changeTimeToRequiredTimeZonesAndPrint(String originalText, List<String> patterns) {
        if (originalText.length() < 11) {
            originalText = originalText + " 00:00:00";
        }
        for (String pat : patterns) {
            try {
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern(pat);
                TemporalAccessor temporalAccessor = pattern.parse(originalText);
                LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
                ZoneId warsawZoneId = ZoneId.of(TimeZone.TIME_FROM_COMPUTER.getZoneId());
                ZonedDateTime warsawZoneDateTime = localDateTime.atZone(warsawZoneId);
                for (TimeZone value : TimeZone.values()) {
                    ZoneId idOfRequestedZone = ZoneId.of(value.getZoneId());
                    ZonedDateTime requestedDateTime = warsawZoneDateTime.withZoneSameInstant(idOfRequestedZone);
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(patterns.get(0));
                    System.out.println(value.getName() + dateTimeFormatter.format(requestedDateTime));
                }
            } catch (DateTimeException e) {
                //
            }
        }
    }

    private static LocalDateTime changeLocalDateTimeAsRequired(String originalText, LocalDateTime localDateTime, Queue<String> charterQueue, Queue<Long> numbers) {
        String[] orgText = originalText.split("");
        for (int i = 1; i < orgText.length; i++) {
            if (orgText[i].equals(YEAR)) {
                if (checkIfItsPositiveCharter(charterQueue)) {
                    localDateTime = localDateTime.plusYears(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                } else {
                    localDateTime = localDateTime.minusYears(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                }
            }
            if (orgText[i].equals(MONTH)) {
                if (checkIfItsPositiveCharter(charterQueue)) {
                    localDateTime = localDateTime.plusMonths(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                } else {
                    localDateTime = localDateTime.minusMonths(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                }
            }
            if (orgText[i].equals(DAY)) {
                if (checkIfItsPositiveCharter(charterQueue)) {
                    localDateTime = localDateTime.plusDays(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                } else {
                    localDateTime = localDateTime.minusDays(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                }
            }
            if (orgText[i].equals(HOUR)) {
                if (checkIfItsPositiveCharter(charterQueue)) {
                    localDateTime = localDateTime.plusHours(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                } else {
                    localDateTime = localDateTime.minusHours(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                }
            }
            if (orgText[i].equals(MINUTES)) {
                if (checkIfItsPositiveCharter(charterQueue)) {
                    localDateTime = localDateTime.plusMinutes(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                } else {
                    localDateTime = localDateTime.minusMinutes(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                }
            }
            if (orgText[i].equals(SECONDS)) {
                if (checkIfItsPositiveCharter(charterQueue)) {
                    localDateTime = localDateTime.plusSeconds(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                } else {
                    localDateTime = localDateTime.minusSeconds(getNumberFromTheQueue(numbers));
                    charterQueue.poll();
                }
            }
        }
        return localDateTime;
    }

    private static boolean checkIfItsPositiveCharter(Queue<String> charters) {
        return Objects.requireNonNull(charters.peek()).equals("+");
    }

    private static Long getNumberFromTheQueue(Queue<Long> numbers) {
        return Objects.requireNonNull(numbers.poll());
    }

    private static Queue<String> findPositiveOrNegativeCharter(String originalText) {
        String text = originalText.replaceAll("[0-9,A-Z,a-z]", "");
        text = text.trim();
        String[] data = text.split("");
        Queue<String> charters = new LinkedList<>();
        Collections.addAll(charters, data);
        return charters;
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

