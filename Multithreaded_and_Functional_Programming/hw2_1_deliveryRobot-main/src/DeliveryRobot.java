import java.util.*;

public class DeliveryRobot {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Runnable logic = () -> {
                String string = generateRoute("RLRFR", 100);
                Integer count = (int) countOccurrences(string, 'R');
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(count)) {
                        sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                }
            };
            Thread task = new Thread(logic);
            threads.add(task);
            task.start();
        }
        for(Thread thread : threads) {
            thread.join();
        }

        Integer maxKeyMap = maxKey(sizeToFreq);
        System.out.println("Самое частое количество повторений " + maxKeyMap
                 + " (встретилось " + sizeToFreq.get(maxKeyMap)  + " раз)");
        System.out.println("Другие размеры:");
        sizeToFreq.forEach((key, value) -> System.out.println("- " + key + " (" + value + " раз)"));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    private static long countOccurrences(String string, char ch) {
        return string.chars()
                .filter(c -> c == ch)
                .count();
    }

    private static Integer maxKey(Map<Integer, Integer> map) {
        return map.keySet().stream()
                .max(Comparator.comparing(map::get))
                .orElse(null);
    }
}