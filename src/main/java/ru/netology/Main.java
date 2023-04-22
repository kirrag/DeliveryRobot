package ru.netology;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static String generateRoute(String letters, int length) {
		Random random = new Random();
		StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
   	        route.append(letters.charAt(random.nextInt(letters.length())));
        }
   	    return route.toString();
	}

	public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

	public static void main(String[] args) {
		int maxKey = 0;
		int maxValue = 0;

		long startTs = System.currentTimeMillis();

		new Thread( () -> {
			int count = 0;
			while (!Thread.interrupted()) {
				if (sizeToFreq.containsKey(count)) {
					for (int key : sizeToFreq.keySet()) {
						int value = sizeToFreq.get(key);
						if (value > maxValue) {
							maxValue = value;
							maxKey = key;
						}
					}
					return maxKey;
				} else {
					System.out.println("Список пуст.");
				}
			}
		});

		for (int n = 0; n < 1000; n++) {

			String text = generateRoute("RLRFR", 100);




			new Thread(() -> {


				for (int i = 0; i < text.length(); i++) {
					if (text.charAt(i) == 'R' ) {
						count++;
					}
				}

				synchronized(sizeToFreq) {
					if (sizeToFreq.containsKey(count)) {
						sizeToFreq.put(count, sizeToFreq.get(count) + 1);
					} else {
						sizeToFreq.put(count, 1);
					}
				}
			}).start();
		}

		long endTs = System.currentTimeMillis();

		System.out.println("Time: " + (endTs - startTs) + "ms");

	
		System.out.println("Самое частое количество повторений " + maxKey + "(встретилось " + maxValue +" раз) Другие размеры:");

		for (int key : sizeToFreq.keySet()) {
			System.out.println("- " + key + "(" + sizeToFreq.get(key) + ")");
		}
	}
}
