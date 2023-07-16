import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String text = generateText("abc", 100_000);
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        List<Thread> threadList = new ArrayList<>();
        threadList.add(myThread(queueA, 'a'));
        threadList.add(myThread(queueB, 'b'));
        threadList.add(myThread(queueC, 'c'));

        for (Thread thread : threadList) {
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread myThread(BlockingQueue<String> queue, char ch) {
        return new Thread(
                () -> {
                    int max = 0;
                    int count = 0;
                    String maxString = null;
                    for (int i = 0; i < 10_000; i++) {
                        try {
                            String string = queue.take();
                            count = (int) (countOccurrences(string, ch));
                            if (count > max) {
                                max = count;
                                maxString = string;
                            }
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    System.out.println("Текст с максимальным количеством символов -"
                            + ch + " выглядит так: \n"
                            + maxString + "\nКоличество символов " + max + "\n");
                }
        );
    }

    public static long countOccurrences(String str, char ch) {
        return str.chars()
                .filter(c -> c == ch)
                .count();
    }
}