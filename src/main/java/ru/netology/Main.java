package ru.netology;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

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

		long startTs = System.currentTimeMillis();

		List<Thread> threads = new ArrayList<>();

		Thread threadPrint = new Thread(() -> {
			int maxKey = 0;
			int maxValue = 0;
			try {
				while (!Thread.interrupted()) {
					synchronized (sizeToFreq) {
						sizeToFreq.wait();
						for (int key : sizeToFreq.keySet()) {
							int value = sizeToFreq.get(key);
							if (value > maxValue) {
								maxValue = value;
								maxKey = key;
							}
						}
						System.out.println(
								"Самое частое количество повторений " + maxKey + "(встретилось " + maxValue + " раз)");
					}
				}
			} catch (InterruptedException e) {
				return;
			}
		});

		for (int n = 0; n < 1000; n++) {

			String text = generateRoute("RLRFR", 100);
			threads.add(new Thread(() -> {

				int count = 0;

				for (int i = 0; i < text.length(); i++) {
					if (text.charAt(i) == 'R') {
						count++;
					}
				}

				synchronized (sizeToFreq) {
					if (sizeToFreq.containsKey(count)) {
						sizeToFreq.put(count, sizeToFreq.get(count) + 1);
					} else {
						sizeToFreq.put(count, 1);
					}
					sizeToFreq.notify();
				}
			}));
		}
		threadPrint.start();
		threads.forEach(thread -> thread.start());
		threads.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		long endTs = System.currentTimeMillis();

		System.out.println("Time: " + (endTs - startTs) + "ms");

		threadPrint.interrupt();

	}
}
