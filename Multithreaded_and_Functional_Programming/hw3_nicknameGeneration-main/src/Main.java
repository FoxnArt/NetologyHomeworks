import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {
    private static AtomicInteger count3 = new AtomicInteger(0);
    private static AtomicInteger count4 = new AtomicInteger(0);
    private static AtomicInteger count5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>(3);
        Thread thread1 = new Thread(() -> {
            for(String text : texts) {
                if (isPalindrome(text) && !isSameLetterWord(text))
                    increasesСounters(text);
            }
        });
        Thread thread2 = new Thread(() -> {
            for(String text : texts) {
                if (isSameLetterWord(text))
                    increasesСounters(text);
            }
        });
        Thread thread3 = new Thread(() -> {
            for(String text : texts) {
                if (isSortedWord(text) && !isSameLetterWord(text))
                    increasesСounters(text);
            }
        });
        threads.add(thread1);
        threads.add(thread2);
        threads.add(thread3);

        for(Thread thread : threads) {
            thread.start();
        }

        for(Thread thread : threads) {
            thread.join();
        }

        System.out.println("Красивых слов с длиной 3: " + count3 + " шт");
        System.out.println("Красивых слов с длиной 4: " + count4 + " шт");
        System.out.println("Красивых слов с длиной 5: " + count5 + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void increasesСounters(String text) {
        switch (text.length()) {
            case (3):
                count3.getAndIncrement();
                break;
            case (4):
                count4.getAndIncrement();
                break;
            case (5):
                count5.getAndIncrement();
                break;
        }
    }

    public static boolean isPalindrome(String text) {
        return IntStream.range(0, text.length() / 2)
                .noneMatch(i -> text.charAt(i) != text.charAt(text.length() - i - 1));
    }

    public static boolean isSameLetterWord(String text) {
        return IntStream.range(1, text.length())
                .noneMatch(i -> text.charAt(0) != text.charAt(i));
    }

    public static boolean isSortedWord(String text) {
        return IntStream.range(1, text.length())
                .noneMatch(i -> text.charAt(i - 1) > text.charAt(i));
    }
}