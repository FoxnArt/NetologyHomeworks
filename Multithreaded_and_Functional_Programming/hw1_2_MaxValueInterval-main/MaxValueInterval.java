import java.util.*;
import java.util.concurrent.*;

public class MaxValueInterval {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        final ExecutorService threadPool = Executors.newFixedThreadPool(4);
        List<Future<Integer>> threads = new ArrayList<>(texts.length);
        Integer max = 0;
        long startTs = System.currentTimeMillis(); // start time
        for (String text : texts) {
            Callable<Integer> logic = () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
                return (Integer) maxSize;
            };
            Future<Integer> task = threadPool.submit(logic);
            threads.add(task);
        }
        for (Future<Integer> thread : threads) {
            final Integer resultOfTask = thread.get();
            max = (resultOfTask > max) ? resultOfTask : max;
        }
        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms; максимальный интервал значений среди всех строк - " + max);
        threadPool.shutdown();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}