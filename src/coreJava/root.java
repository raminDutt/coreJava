package coreJava;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public final class root extends Applet {

	public static void main(String args[]) throws Exception{

			ch10q10();

		/*
		 * String dummyFile = "dummyFile";
		 * 
		 * if(Paths.get(dummyFile).toFile().getName().equals(dummyFile)) {
		 * System.out.println("Yes"); }else {
		 * System.out.println(Paths.get(dummyFile).getFileName()); }
		 */
	}

	private static void ch10q10(){
		BlockingQueue<Path> blockingQueue = new LinkedBlockingQueue(1000);
		Path directory = Paths.get("/home/ramin/GitViewstore");
		String dummyFile = "dummyFile";
		String word = "git";
		
		
		Callable<Void> producerTask = () -> {
			try (Stream<Path> stream = Files.list(directory);) {
				List<Path> paths = stream.collect(Collectors.toList());

				int i = 0;
				while (i < paths.size()) {
					Path path = paths.get(i);
					if (path.toFile().isDirectory()) {
						List<Path> subDirectoryPath = Files.list(path).collect(
								Collectors.toList());
						paths.addAll(subDirectoryPath);

					} else {
						//System.out.println(Thread.currentThread().getName() + "Producer Putting = " + path);
						blockingQueue.put(path);
						
					}
					i++;
				}
				blockingQueue.put(Paths.get(dummyFile));
				return null;

			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return null;
		};

		AtomicBoolean finished = new AtomicBoolean(true);
		// Consumer
		Callable<Void> consumerTask = () -> {

			while (finished.get()) {
				
				Path path = blockingQueue.take();
				//System.out.println(Thread.currentThread().getName() + ": Consumer Taking = " + finished.get() + " " + path);
				File file = path.toFile();
				if (file.getName().equals(dummyFile)) {
					finished.set(false);
					System.out.println("CONSUMER COMPLETED !!!!");
					break;
					
				}
				

				try(Stream<String> lines = Files.lines(path);)
				{
					boolean found = lines.anyMatch(s -> s.contains(word));
					if(found)
					{
						System.out.println(Thread.currentThread().getName() +  ": Consumer Match Found = " + finished.get() + " " + path);
					}
					
				}
				catch(Exception exception)
				{
					//Expected Error duw to different file types
					if(!(exception.getCause() instanceof MalformedInputException))
					{
						exception.printStackTrace();
					}
				
				}
				//System.out.println(Thread.currentThread().getName() + ": Consumer Finished = " + finished.get() + " " + path);

			}
			return null;
		};

		//Producer Thread
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.submit(producerTask);

		
		//Consumer Thread
		int logicalCore = Runtime.getRuntime().availableProcessors();
		List<Future<Void>> consumers = new ArrayList<Future<Void>>();
		int i = 0;
		while(i < logicalCore)
		{
			Future<Void> future = executorService.submit(consumerTask);
			consumers.add(future);
			i++;
		}
		
		try {
			executorService.shutdown();
			if(!executorService.awaitTermination(60, TimeUnit.SECONDS))
			{
				System.out.println("shutting Down Now");
				executorService.shutdownNow();
				System.out.println("Pool shutdown");
			}
			if(!executorService.awaitTermination(60, TimeUnit.SECONDS))
			{
				System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	/**
	 * Find all files in directories and sub directories via collection
	 */
	private static void ch10q10a() {
		Path directory = Paths.get("/home/ramin/GitViewstore");

		List<Path> paths = null;
		;
		try {
			paths = Files.list(directory).collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AtomicBoolean flag = new AtomicBoolean(true);
		while (flag.get()) {
			flag.set(false);

			// Using parallel stream is faster
			paths = paths.stream().flatMap(path -> {
				File file = path.toFile();
				Stream<Path> result = null;
				if (!file.isDirectory()) {
					result = Stream.<Path> of(path);
				} else {

					try {
						result = Files.list(path);
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
				return result;
			}).peek(p -> {
				if (p.toFile().isDirectory()) {
					flag.set(true);
				}
			}).collect(Collectors.toList());

		}

		paths.forEach(System.out::println);
		System.out.println("size = " + paths.size());

	}

	/**
	 * Find all files in directories and sub directories via collection
	 */
	private static void ch10q10b() {
		Path directory = Paths.get("/home/ramin/GitViewstore");

		try (Stream<Path> stream = Files.list(directory);) {
			// List<Path> paths = stream.collect(Collectors.toList());
			List<Path> paths = stream.collect(Collectors
					.toCollection(() -> new LinkedList<Path>()));

			int i = 0;
			while (i < paths.size()) {
				Path path = paths.get(i);
				if (path.toFile().isDirectory()) {
					paths.remove(i);
					List<Path> subDirectoryPath = Files
							.list(path)
							.collect(
									Collectors
											.toCollection(() -> new LinkedList<Path>()));
					// List<Path> subDirectoryPath =
					// Files.list(path).collect(Collectors.toList());
					paths.addAll(subDirectoryPath);

				} else {
					i++;
				}
			}
			paths.forEach(System.out::println);
			System.out.println("size = " + paths.size());

		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}

	public static void ch10q9() {
		List<Integer> vector = new Vector<Integer>();
		LongAccumulator max = new LongAccumulator((x, y) -> (x >= y) ? x : y,
				-1);

		LongAccumulator min = new LongAccumulator((x, y) -> (x == -1) ? y
				: (x < y) ? x : y, -1);

		LongAccumulator accumulator = min;
		Supplier<Callable<Void>> supplier = () -> {
			Callable<Void> task = () -> {
				int number = new Random().nextInt(100);
				accumulator.accumulate(number);
				vector.add(number);
				return null;
			};
			return task;

		};
		List<Callable<Void>> tasks = Stream.generate(supplier).limit(1000)
				.collect(Collectors.toList());
		ExecutorService executorService = Executors.newCachedThreadPool();
		try {
			List<Future<Void>> futures = executorService.invokeAll(tasks);
			System.out.println(vector);
			System.out.println(accumulator.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executorService.shutdown();

	}

	private static void ch10q8a() {
		AtomicLong atomicLong = new AtomicLong();

		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Callable<Void>> tasks = new ArrayList<>();
		int i = 0;
		while (i < 1000) {
			Callable<Void> task = () -> {
				int j = 0;
				while (j < 100000) {
					atomicLong.incrementAndGet();
					j++;
				}
				return null;
			};
			tasks.add(task);
			i++;
		}

		try {
			long start = System.currentTimeMillis();
			executorService.invokeAll(tasks);
			long end = System.currentTimeMillis();

			System.out.println("atomicLong time: "
					+ Math.subtractExact(end, start) + "ms + atomicLong="
					+ atomicLong.get());
			executorService.shutdown();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void ch10q8b() {
		LongAdder longAdder = new LongAdder();
		List<Callable<Void>> tasks = IntStream.generate(() -> 1).limit(1000)
				.boxed().map(index -> {
					Callable<Void> task = () -> {
						int j = 0;
						while (j < 100000) {
							longAdder.increment();
							j++;
						}
						return null;
					};
					return task;
				}).collect(Collectors.toList());

		ExecutorService executorService = Executors.newCachedThreadPool();

		try {
			long start = System.currentTimeMillis();
			executorService.invokeAll(tasks);
			long end = System.currentTimeMillis();
			System.out.println("longAdder time: "
					+ Math.subtractExact(end, start) + " ms + longAdder="
					+ longAdder.sum());
			executorService.shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void ch10q7() {
		ConcurrentHashMap<String, Long> concurrentHashMap = new ConcurrentHashMap<>();
		int i = 0;
		while (i < 10) {
			Random generator = new Random();
			String key = generateRandomWord(1 + generator.nextInt(10));
			long value = generator.nextInt(100);
			concurrentHashMap.put(key, value);
			i++;
		}
		System.out.println(concurrentHashMap);
		BiFunction<Entry<String, Long>, Entry<String, Long>, Entry<String, Long>> reducer = (
				e1, e2) -> {
			return e1.getValue() > e2.getValue() ? e1 : e2;
		};
		Entry<String, Long> result = concurrentHashMap
				.reduceEntries(2, reducer);
		System.out.println(result);

	}

	private static String generateRandomWord(int length) {
		Random genRandom = new Random();
		StringBuilder builder = new StringBuilder();
		int i = 0;
		while (i < length) {
			char letter = (char) (97 + genRandom.nextInt(26));
			builder.append(letter);
			i++;
		}
		return builder.toString();
	}

	public static void ch10q6() {
		String directory = "/home/ramin/workspace/ch10";
		Path directoryPath = Paths.get(directory);
		ConcurrentHashMap<String, Set<Path>> concurrentHashMap = new ConcurrentHashMap<>();

		try (Stream<Path> files = Files.list(directoryPath);) {
			// Creating a List of tasks
			List<CompletableFuture<Void>> tasks = files.map(
					p -> {
						CompletableFuture<Void> task = CompletableFuture
								.runAsync(getTask(p, concurrentHashMap));
						return task;
					}).collect(Collectors.toList());

			// creating a barrier and wait untill all concurrent tasks have been
			// completed
			CompletableFuture<Void> barrier = CompletableFuture.allOf(tasks
					.toArray(new CompletableFuture[tasks.size()]));
			barrier.join(); // This is important

			// Printing it out result

			concurrentHashMap.forEach((k, v) -> {
				System.out.println("Key: (" + k + ") Value= " + v);
			});

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private static Runnable getTask(Path p, ConcurrentMap<String, Set<Path>> map) {
		Runnable task = () -> {
			try (Stream<String> lines = Files.lines(p);) {
				Stream<String> words = lines.flatMap(w -> {
					return Arrays.stream((w.split("[\\s]")));
				});

				words.forEach(w -> {
					map.computeIfPresent(w, (k, v) -> {
						v.add(p);
						return v;
					});
					map.computeIfAbsent(w, k -> new HashSet<>()).add(p);
				});
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		};
		return task;
	}

	public static void ch10q5() {
		String directory = "/home/ramin/workspace/ch10";
		Path directoryPath = Paths.get(directory);

		ConcurrentHashMap<String, Set<Path>> concurrentHashMap = new ConcurrentHashMap();
		// Getting the list of all files
		try (Stream<Path> files = Files.list(directoryPath);) {
			List<Callable<Void>> tasks = files.map(
					p -> {
						Callable<Void> task = () -> {

							// Making a stream of all words in the document
							Stream<String> words = Files.lines(p).flatMap(
									line -> {
										String[] wordsArray = line
												.split("[\\s]");
										Stream<String> wordsStream = Stream
												.of(wordsArray);
										return wordsStream;
									});

							// Modifying the shared object: concurrentHashMap
							words.forEach(w -> {
								Set<Path> set = new HashSet();
								set.add(p);
								concurrentHashMap.merge(w, set, (
										Set<Path> existingValue,
										Set<Path> newValue) -> {
									if (existingValue == null) {
										return newValue;
									} else {
										existingValue.addAll(newValue);
										return existingValue;
									}
								});
							});

							return null;
						};
						return task;
					}).collect(Collectors.toList());

			ExecutorService executorService = Executors.newCachedThreadPool();
			try {
				List<Future<Void>> results = executorService.invokeAll(tasks);
				Iterator<Entry<String, Set<Path>>> iterator = concurrentHashMap
						.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Set<Path>> entry = iterator.next();
					String key = entry.getKey();
					Set paths = entry.getValue();
					System.out.println("key: " + key + ": " + paths.toString());
				}

				System.out.println("*******");
				System.out.println(concurrentHashMap.get("git"));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void ch10q3g() {
		String word = "git";
		String directory = "/home/ramin/workspace/ch10";
		Path directoryPath = Paths.get(directory);
		// Create a stream of callables
		try (Stream<Path> files = Files.list(directoryPath);) {
			Stream<Callable<Path>> callables = files.map((Path path) -> {
				Callable<Path> callable = () -> {
					try (Stream<String> content = Files.lines(path);) {
						boolean isFound = content.anyMatch((String w) -> w
								.contains(word));
						if (isFound) {
							return path;
						} else {
							throw new ExecutionException(
									"Something went wrong", null);
						}
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					return null;
				};
				return callable;
			});

			List<Callable<Path>> tasks = callables.collect(Collectors.toList());
			ExecutorService executorService = Executors.newCachedThreadPool();
			tasks.forEach((Callable<Path> task) -> {
				Future<Path> future = executorService.submit(task);
				try {
					System.out.println(future.get());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Finding the first document containing the searched word using
	 * completableFuture: using filter and findAny with streams
	 */
	public static void ch10q3f() {
		String word = "git";
		String directory = "/home/ramin/workspace/ch10";
		Path directoryPath = Paths.get(directory);
		// Create a stream of callables
		try (Stream<Path> files = Files.list(directoryPath);) {
			Stream<Callable<Path>> callables = files.map((Path path) -> {
				Callable<Path> callable = () -> {
					try (Stream<String> content = Files.lines(path);) {
						boolean isFound = content.anyMatch((String w) -> w
								.contains(word));
						if (isFound) {
							return path;
						} else {
							throw new ExecutionException(null);
						}
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					return null;
				};
				return callable;
			});

			List<Callable<Path>> tasks = callables.collect(Collectors.toList());
			ExecutorService executorService = Executors.newCachedThreadPool();
			try {
				Path p = executorService.invokeAny(tasks);
				System.out.println("Result = " + p);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Finding the first document containing the searched word using
	 * completableFuture: using filter and findAny with streams
	 */
	public static void ch10q3e() {
		String word = "git";
		String directory = "/home/ramin/workspace/ch10";
		Path directoryPath = Paths.get(directory);

		try (Stream<Path> files = Files.list(directoryPath);) {
			Stream<Path> searchResult = files
					.filter((Path path) -> {
						CompletableFuture<Optional<Path>> completableFuture = CompletableFuture.supplyAsync(() -> {
							Optional<Path> optional = Optional.empty();
							try (Stream<String> content = Files.lines(path)) {
								boolean isFound = content.parallel().anyMatch(
										w -> w.contains(word));
								if (isFound) {
									optional = Optional.of(path);
								}

							} catch (IOException ioException) {
								ioException.printStackTrace();
							}
							return optional;
						});

						try {
							Optional<Path> result = completableFuture.get();
							return result.isPresent() ? true : false;
						} catch (Exception e) {

							e.printStackTrace();
						}

						return false;
					});

			// Printing result: all documents containing the word
			long start = System.currentTimeMillis();
			// searchResult.forEach(System.out::println);

			// Finding the first document only
			System.out.println(searchResult.findAny().get());
			long end = System.currentTimeMillis();

			System.out
					.println("Time: " + Math.subtractExact(end, start) + "ms");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	/**
	 * This api finds the documents containing the word but it searches through
	 * all the document. It does not cancel the other threads once the first
	 * document containing the word has been found. Hence, it finds all
	 * documents containing the word
	 */
	public static void ch10q3d() {
		String word = "git";
		String stringPath = "/home/ramin/workspace/ch10";
		Path path = Paths.get(stringPath);

		try (Stream<Path> files = Files.list(path)) {

			List<CompletableFuture<Optional<Path>>> searchResult = files
					.map((Path p) -> {
						CompletableFuture<Optional<Path>> completableFutureOptional = CompletableFuture
								.supplyAsync(() -> {
									boolean result = false;
									try {
										result = Files.lines(p).anyMatch(
												(String s) -> s.contains(word));
									} catch (IOException exception) {
										exception.printStackTrace();
									}
									Optional<Path> optional = Optional.empty();
									if (result) {
										optional = Optional.of(p);
									}
									return optional;
								});

						return completableFutureOptional;
					}).collect(Collectors.toList());

			searchResult.forEach(completableFuture -> {

				try {

					if (!completableFuture.isCompletedExceptionally()) {

						Optional<Path> opt = completableFuture.get();

						if (opt.isPresent()) {
							System.out.println(opt.get()
									+ " : "
									+ completableFuture
											.isCompletedExceptionally());
						}

					}
				} catch (Exception e) { // TODO Auto-generated catch block
						e.printStackTrace();
					}

				});

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public static void ch10q3c() {
		String word = "git";
		String p = "/home/ramin/workspace/ch10";
		Path p2 = Paths.get(p);

		try {
			Stream<CompletableFuture<String>> stream = Files
					.list(p2)
					.map(path -> {
						CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
							String result = "NOT FOUND";
							try (Stream<String> content = Files.lines(path)) {
								boolean found = content.parallel().anyMatch(
										s -> s.contains(word));
								if (found) {
									result = path.toString();
								} else {
									throw new Exception();
								}

							} catch (Exception e) {

								e.printStackTrace();
							}

							return result;
						});
						return future;
					});

			CompletableFuture<String>[] completableFutures = stream
					.toArray(CompletableFuture[]::new);
			CompletableFuture<Object> result = CompletableFuture
					.anyOf(completableFutures);
			System.out.println(result.get());
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public static void ch10q3b() {
		String word = "git";
		String p = "/home/ramin/workspace/ch10";
		Path p2 = Paths.get(p);

		try {
			Optional<Path> result = Files
					.list(p2)
					.filter(path -> {
						boolean isFound = false;
						try (Stream<String> lines = Files.lines(path)) {
							isFound = lines.parallel().anyMatch(
									line -> line.contains(word));
						} catch (Exception exception) {
							exception.printStackTrace();

						}
						return isFound;
					}).findAny();

			// stream.forEach(System.out::println);
			System.out.println(result.get());

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void ch10q3a() {
		String word = "git";
		String p = "/home/ramin/workspace/ch10";
		Path p2 = Paths.get(p);

		try {
			Stream<CompletableFuture<String>> stream = Files
					.list(p2)
					.map(path -> {
						CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
							String result = "NOT FOUND";
							try (Stream<String> content = Files.lines(path)) {
								boolean found = content.parallel().anyMatch(
										s -> s.contains(word));
								if (found) {
									result = path.toString();
								}

							} catch (Exception e) {

								e.printStackTrace();
							}
							return result;
						});
						return future;
					});

			CompletableFuture<String>[] completableFutures = stream
					.toArray(CompletableFuture[]::new);
			CompletableFuture<Void> result = CompletableFuture
					.allOf(completableFutures);
			result.join();

			result.thenApply(v -> {
				System.out.println("ramin");

				for (CompletableFuture<String> completableFuture : completableFutures) {
					try {
						System.out.println(completableFuture.get());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			});
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	static int[] array1 = null;
	static int[] array2 = null;

	public static void ch10q2c() throws InterruptedException,
			ExecutionException {
		Object lock = new Object();
		Function<Consumer<Void>, Long> function = consumer -> {
			synchronized (lock) {
				long startTime = System.currentTimeMillis();
				consumer.accept(null);
				long endTime = System.currentTimeMillis();
				return Math.subtractExact(endTime, startTime);
			}

		};

		int i = 0;
		boolean flag = true;
		while (flag) {
			array1 = new Random().ints().limit(i).toArray();
			array2 = Arrays.copyOf(array1, array1.length);

			CompletableFuture<Long> sortArray = CompletableFuture
					.supplyAsync(() -> {
						return function.apply(v -> Arrays.sort(array1));
					});

			CompletableFuture<Long> parallelArray = CompletableFuture
					.<Long> supplyAsync(() -> {
						return function.apply(v -> Arrays.parallelSort(array2));
					});

			flag = sortArray.thenCombine(parallelArray, (s, p) -> {
				if (s <= p) {
					return true;
				} else {
					System.out.println("sort = " + s);
					System.out.println("parallel = " + p);
					return false;
				}
			}).get();

			if (!flag) {
				System.out.println("Size = " + i);
			}
			i++;

		}

	}

	public static void ch10q2b() throws InterruptedException,
			ExecutionException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		int size = 8193;
		boolean flag = true;
		while (flag) {
			Random generator = new Random();
			int[] array1 = generator.ints(size, 0, size + 1).toArray();
			int[] array2 = Arrays.copyOf(array1, array1.length);

			Function<Consumer<?>, Long> function = action -> {
				long start = System.currentTimeMillis();
				action.accept(null);
				long end = System.currentTimeMillis();
				long executionTime = Math.subtractExact(end, start);
				return executionTime;
			};

			// Thread for Arrays.sort
			Callable<Long> task_1 = () -> {
				return function.apply(v -> Arrays.sort(array1));
			};
			Future<Long> future_1 = executorService.submit(task_1);

			Callable<Long> task_2 = () -> {
				return function.apply(v -> Arrays.parallelSort(array2));
			};

			Future<Long> future_2 = executorService.submit(task_2);

			if (future_1.get() > future_2.get()) {
				flag = false;
				System.out.println("ArraysSort = " + future_1.get());
				System.out.println("ArraysParallel = " + future_2.get());
				System.out.println("size = " + size);
			}
			size++;
		}

		executorService.shutdown();
	}

	public static void ch10q2() {
		boolean flag = true;
		int i = 0;
		while (flag) {
			Random generator = new Random();
			int[] array1 = generator.ints(i, 0, 10000).toArray();
			int[] array2 = Arrays.copyOf(array1, array1.length);
			// System.out.println(Arrays.toString(array1));

			long startTime = System.currentTimeMillis();
			Arrays.sort(array1);
			long endTime = System.currentTimeMillis();
			long delta1 = Math.subtractExact(endTime, startTime);

			startTime = System.currentTimeMillis();
			Arrays.parallelSort(array2);
			endTime = System.currentTimeMillis();
			long delta2 = Math.subtractExact(endTime, startTime);

			System.out.println("delta 1 = " + delta1);
			System.out.println("delta 2 = " + delta2);
			System.out.println("**************");

			if (delta2 < delta1) {
				flag = false;
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
				System.out.println("delta1 = " + delta1);
				System.out.println("delta2 = " + delta2);
				System.out.println("Size = " + i);
			}
			i++;
		}

	}

	// Surprisingly executing this in parallel is slower than sequentially
	// because of overhead
	private static void ch10Q1b() {
		String p = "/home/ramin/workspace/ch10";
		File file = new File(p);
		String word = "git";

		try (Stream<Path> files = Files.list(file.toPath());) {
			System.out.println("cpu="
					+ Runtime.getRuntime().availableProcessors());
			long startTime = System.currentTimeMillis();

			Optional<Path> result = files.parallel().filter((Path path) -> {
				try (Stream<String> contents = Files.lines(path);) {
					return contents.anyMatch(s -> s.contains(word));
				} catch (IOException exception) {
					exception.printStackTrace();
				}
				return false;
			}).findAny();
			long endTime = System.currentTimeMillis();
			long perfo = Math.subtractExact(endTime, startTime);
			System.out.println("time = " + perfo + "ms");
			System.out.println(result.get());

		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	// Surprisingly executing this in parallel is slower than sequentially
	// because of overhead
	private static void ch10Q1a() {
		// http://www.baeldung.com/java-lambda-exceptions

		String p = "/home/ramin/workspace/ch10";
		File file = new File(p);
		String word = "git";

		try (Stream<Path> files = Files.list(file.toPath());) {
			System.out.println("cpu="
					+ Runtime.getRuntime().availableProcessors());
			long startTime = System.currentTimeMillis();

			Stream<Path> result = files.parallel().filter((Path path) -> {
				try (Stream<String> contents = Files.lines(path);) {
					return contents.anyMatch(s -> s.contains(word));
				} catch (IOException exception) {
					exception.printStackTrace();
				}
				return false;
			});
			long endTime = System.currentTimeMillis();
			long perfo = Math.subtractExact(endTime, startTime);
			System.out.println("time = " + perfo + "ms");
			result.forEach(System.out::println);

		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void ch9Q15() {

		String file = "/home/ramin/workspace/points.ser";
		Path path = Paths.get(file);
		Point[] clonedPoints = null;
		try (InputStream inputStream = new FileInputStream(path.toFile());
				ObjectInputStream objectInputStream = new ObjectInputStream(
						inputStream)) {
			System.out.println("Desirializing array from " + file);
			clonedPoints = (Point[]) objectInputStream.readObject();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		System.out.println(Arrays.toString(clonedPoints));
	}

	public static void ch9Q14() {

		int size = 10;
		Point[] points = new Point[size];
		int i = 0;
		while (i < size) {
			Random generator = new Random();
			double x = generator.nextDouble() * 100;
			double y = generator.nextInt(100);
			Point point = new Point();
			point.setX(x);
			point.setY(y);
			points[i] = point;
			i++;
		}
		System.out.println(Arrays.toString(points));
		String file = "/home/ramin/workspace/points.ser";
		Path path = Paths.get(file);
		try (OutputStream outputStream = new FileOutputStream(path.toFile());
				ObjectOutputStream objectInputStream = new ObjectOutputStream(
						outputStream)) {
			System.out.println("Serializing Array at location: " + file);
			objectInputStream.writeObject(points);

		} catch (Exception exception) {
			exception.printStackTrace();

		}

		Point[] clonedPoints = null;
		try (InputStream inputStream = new FileInputStream(path.toFile());
				ObjectInputStream objectInputStream = new ObjectInputStream(
						inputStream)) {
			System.out.println("Desirializing array from " + file);
			clonedPoints = (Point[]) objectInputStream.readObject();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		boolean isEqual = Arrays.equals(points, clonedPoints);
		System.out.println("points: " + points);
		System.out.println("clonedPoints: " + clonedPoints);
		System.out.println("isEqual: " + isEqual);

	}

	public void paint(Graphics page) {
		// paintch9Q8(page);
		paintch9Q9(page);
	}

	public static void ch9Q13() {
		Message message = new Message();
		Message cloneMessage = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (ObjectOutputStream outputStream = new ObjectOutputStream(
				byteArrayOutputStream);) {
			outputStream.writeObject(message);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				byteArrayOutputStream.toByteArray());
		try (ObjectInputStream inputStream = new ObjectInputStream(
				byteArrayInputStream)) {
			cloneMessage = (Message) inputStream.readObject();
		} catch (IOException | ClassNotFoundException exception) {
			exception.printStackTrace();
		}
		System.out.println("Message:" + message);
		message.print();
		System.out.println("cloneMessage:" + cloneMessage);
		cloneMessage.print();

		Message clone2Message = message.clone();
		System.out.println("Message_clone: " + clone2Message);
		message.recipients.remove(0);
		message.print();
		System.out.println("******");
		clone2Message.print();
	}

	public static void ch9Q12() {
		Pattern regex = Pattern.compile("(\\d{1,2})");
		Matcher regexMatcher = regex.matcher("12 54 1 65");
		try {
			String resultString = regexMatcher.replaceAll("$1");
			System.out.println(resultString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void ch9Q11() {
		String file = "/home/ram\\in/ch\\9q+++11.zip";
		Pattern pattern = Pattern
				.compile("[\\p{Alnum}\\p{Punct}&&[^/]]+.\\w+$");
		Matcher matcher = pattern.matcher(file);
		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}

	public static void ch9Q10() {
		String line = "--56--5-hello+kr 389rk-from +105 ++++----89++++ramin -96565 hererl++++389";
		String regx = "[+-]*[[\\D]&&[^+-]]+[+-]*";
		// Solution 1
		Pattern pattern = Pattern.compile("[+-]?[0-9]+");
		Matcher matcher = pattern.matcher(line);
		List<Integer> integers = new ArrayList<Integer>();
		while (matcher.find()) {
			integers.add(Integer.parseInt(matcher.group()));

		}

		System.out.println(integers);

		// Solution2
		String x = line.replaceAll("[[\\D]&&[^+-]]", "");
		System.out.println(x);
		x = x.replaceAll("[+]+", "+");
		System.out.println(x);
		x = x.replaceAll("[-]+", "-");
		System.out.println(x);
		x = x.replaceAll("[-][+]", "+");
		System.out.println(x);
		x = x.replaceAll("[+][-]", "-");
		System.out.println(x);
		String y[] = x.split("[-]");
		System.out.println(Arrays.toString(y));
		List<Integer> integers2 = new ArrayList<Integer>();
		for (String z : y) {
			if (z.isEmpty()) {
				continue;
			}
			if (z.contains("+")) {
				String[] w = z.split("[+]");
				integers.add(Integer.parseInt(w[0]) * -1);
				int i = 1;
				while (i < w.length) {
					integers2.add(Integer.parseInt(w[i]));
					i++;
				}
			} else {
				integers2.add(Integer.parseInt(z) * -1);
			}
		}

		System.out.println(integers2);
		// String solution3[] = Arrays.stream(solution2).filter((t) ->
		// t.length()!=0?true:false).toArray(String[]::new);

		// System.out.println(Arrays.toString(solution3));
		// Arrays.asList(solution3).forEach(t ->
		// System.out.println(t.length()));
	}

	public static void paintch9Q9(Graphics page) {
		// see
		// http://www.httpwatch.com/httpgallery/authentication/#showExample10
		try {
			URL url = new URL(
					"http://www.httpwatch.com/httpgallery/authentication/authenticatedimage/default.aspx");
			URLConnection urlConnection = url.openConnection();
			String userpass = "httpwatch" + ":" + "changeme";
			String basicAuth = "Basic "
					+ new String(Base64.getEncoder().encode(
							userpass.getBytes(StandardCharsets.UTF_8)));
			urlConnection.setRequestProperty("Authorization", basicAuth);

			// Loading an Image into Applet
			BufferedImage image = ImageIO.read(urlConnection.getInputStream());
			page.drawImage(image, 50, 50, null);

			/*
			 * //Reading a file BufferedReader bufferedReader = new
			 * BufferedReader(new
			 * InputStreamReader(urlConnection.getInputStream())); String
			 * line=null; while((line=bufferedReader.readLine()) != null) {
			 * System.out.println(line); } bufferedReader.close();
			 * urlConnection.connect(); Map<String,List<String>>
			 * headers=urlConnection.getHeaderFields(); headers.forEach((k,v)->
			 * System.out.println(k + ": " + v));
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void ch9Q8() throws URISyntaxException {
		String file = "/home/ramin/workspace/ch9q8.zip";
		Path zipPath = Paths.get(file);
		try {
			Files.deleteIfExists(zipPath);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		URI uri = new URI("jar", zipPath.toUri().toString(), null);
		// Constructs the URI jar:file://myfile.zip
		try (FileSystem zipfs = FileSystems.newFileSystem(uri,
				Collections.singletonMap("create", "true"))) {

			// To add files, copy them into the ZIP file system
			String sourceFile = "/home/ramin/workspace";
			Path sourcePath = Paths.get(sourceFile);
			if (Files.isDirectory(sourcePath)) {

				Path[] paths = Files.walk(sourcePath).toArray(Path[]::new);
				for (Path p : paths) {
					if (p.equals(sourcePath)) {
						continue;
					}
					Path source = sourcePath.relativize(p);
					Path target = zipfs.getPath("/").resolve(source.toString());
					Files.copy(p, target);
				}
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void ch9Q7() {
		String file = "/home/ramin/workspace/Superman-dcuo.bmp";
		Path path = Paths.get(file);
		byte[] bytes = null;
		if (path.toFile().length() < Integer.MAX_VALUE) {
			bytes = new byte[Long.valueOf(path.toFile().length()).intValue()];
			try (InputStream inputStream = Files.newInputStream(path)) {
				inputStream.read(bytes);
				MessageDigest messageDigest = MessageDigest
						.getInstance(MessageDigestAlgorithms.SHA_1);
				byte[] bs = messageDigest.digest(bytes);
				String sha = "";
				for (Byte byte1 : bs) {

					sha = sha + Integer.toHexString(Byte.toUnsignedInt(byte1));
				}

				System.out.println("sha=" + sha);

			} catch (IOException | NoSuchAlgorithmException exception) {
				exception.printStackTrace();
			}
		}
	}

	private void paintch9Q8(Graphics page) {
		// ch9Q6
		// see
		// http://www.ece.ualberta.ca/~elliott/ee552/studentAppNotes/2003_w/misc/bmp_file_format/bmp_file_format.htm
		String file = "/home/ramin/workspace/Superman-dcuo.bmp";
		Path path = Paths.get(file);
		int horizontalWidth = 0;
		int verticalWidth = 0;
		int bitsPerPixel = 0;
		int sizeHeader = 0;
		int dataOffset = 0;
		double imageSize = 0;
		double fileSize = 0;
		byte[] bs = null;
		long size = path.toFile().length();
		if (size < Integer.MAX_VALUE) {
			bs = new byte[(int) size];
			try (InputStream inputStream = new FileInputStream(path.toFile())) {
				inputStream.read(bs);
			} catch (IOException exception) {
				exception.printStackTrace();
			}

		} else {

			try (InputStream inputStream = new FileInputStream(path.toFile())) {
				byte[] bytes = new byte[1024];
				int n = -1;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				while ((n = inputStream.read(bytes)) != -1) {
					byteArrayOutputStream.write(bytes, 0, n);
				}
				bs = byteArrayOutputStream != null ? byteArrayOutputStream
						.toByteArray() : null;
				int x = 10;
			} catch (IOException exception) {
				exception.printStackTrace();
			}

		}

		sizeHeader = (int) (Byte.toUnsignedInt(bs[14])
				+ Byte.toUnsignedInt(bs[15]) * Math.pow(2, 8)
				+ Byte.toUnsignedInt(bs[16]) * Math.pow(2, 16) + Byte
				.toUnsignedInt(bs[17]) * Math.pow(2, 24));
		horizontalWidth = (int) (Byte.toUnsignedInt(bs[18])
				+ Byte.toUnsignedInt(bs[19]) * Math.pow(2, 8)
				+ Byte.toUnsignedInt(bs[20]) * Math.pow(2, 16) + Byte
				.toUnsignedInt(bs[21]) * Math.pow(2, 24));
		verticalWidth = (int) (Byte.toUnsignedInt(bs[22])
				+ Byte.toUnsignedInt(bs[23]) * Math.pow(2, 8)
				+ Byte.toUnsignedInt(bs[24]) * Math.pow(2, 16) + Byte
				.toUnsignedInt(bs[25]) * Math.pow(2, 24));
		bitsPerPixel = Byte.toUnsignedInt(bs[28]) + Byte.toUnsignedInt(bs[29])
				* (int) Math.pow(2, 8);
		imageSize = Byte.toUnsignedInt(bs[34]) + Byte.toUnsignedInt(bs[35])
				* Math.pow(2, 8) + Byte.toUnsignedInt(bs[36]) * Math.pow(2, 16)
				+ Byte.toUnsignedInt(bs[37]) * Math.pow(2, 24);
		fileSize = Byte.toUnsignedInt(bs[2]) + Byte.toUnsignedInt(bs[3])
				* Math.pow(2, 8) + Byte.toUnsignedInt(bs[4]) * Math.pow(2, 16)
				+ Byte.toUnsignedInt(bs[5]) * Math.pow(2, 24);
		dataOffset = (int) (Byte.toUnsignedInt(bs[10])
				+ Byte.toUnsignedInt(bs[11]) * Math.pow(2, 8)
				+ Byte.toUnsignedInt(bs[12]) * Math.pow(2, 16) + Byte
				.toUnsignedInt(bs[13]) * Math.pow(2, 24));

		System.out.println("sizeHeader=" + sizeHeader);
		System.out.println("horizontalWidth=" + horizontalWidth + " pixels");
		System.out.println("verticalWidth=" + verticalWidth + " pixels");
		System.out.println("bitsPerPixel=" + bitsPerPixel + " bits");
		System.out.println("imageSize=" + imageSize + " bytes");
		System.out.println("fileSize=" + fileSize + " bytes");
		System.out.println("path.toFile().length()=" + size + " bytes");
		System.out.println("dataOffset" + dataOffset);

		int i = dataOffset;
		int x = 1;
		int y = verticalWidth;
		int row = horizontalWidth * 3;
		int padding = (int) Math.ceil((double) row / 4) * 4 - row;

		int k = 0;
		while (i < imageSize) {
			int blue = 0;
			int green = 0;
			int red = 0;
			if (k < row) {

				blue = Byte.toUnsignedInt(bs[i]);
				green = Byte.toUnsignedInt(bs[i + 1]);
				red = Byte.toUnsignedInt(bs[i + 2]);
				Color pixel = new Color(red, green, blue);

				page.setColor(pixel);
				page.fillRect(x, y, 1, 1);

				if (x == horizontalWidth) {
					x = 1;
					y--;
				} else {
					x++;
				}

				i = i + 3;
				k = k + 3;
			} else {
				i = i + padding;
				k = 0;
			}
		}
	}

	private static void ch9Q5() {
		Map<String, Charset> map = Charset.availableCharsets();
		Map<String, String> result = new HashMap<String, String>();
		for (Entry<String, Charset> entry : map.entrySet()) {
			Charset charset = entry.getValue();
			try {
				String replacement = new String(charset.newEncoder()
						.replacement(), charset);
				result.merge(replacement, charset.name(), (v1, v2) -> v1 + ", "
						+ v2);
			} catch (Exception exception) {
				result.merge("NONE", charset.name(), (v1, v2) -> v1 + ", " + v2);
			}

		}

		result.forEach((k, v) -> System.out.println(k + "->" + v));

	}

	private static void ch9Q4() {

		String file = "/usr/share/dict/american-english";
		Path path = Paths.get(file);

		long bufferedReaderTime = 0;
		try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
			long start = System.currentTimeMillis();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				// nothing
			}
			long end = System.currentTimeMillis();
			bufferedReaderTime = Math.subtractExact(end, start);
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		long bufferedReaderTime2 = 0;
		try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
			long start = System.currentTimeMillis();
			Stream<String> line = bufferedReader.lines();
			long end = System.currentTimeMillis();
			bufferedReaderTime2 = Math.subtractExact(end, start);
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		long scannerTime = 0;
		try (Scanner scanner = new Scanner(path)) {
			long start = System.currentTimeMillis();
			while (scanner.hasNextLine()) {
				scanner.nextLine();
			}
			long end = System.currentTimeMillis();
			scannerTime = Math.subtractExact(end, start);
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		System.out.println("**** Read File Analysis ****");
		System.out.println("bufferedReaderTime: " + bufferedReaderTime + "ms");
		System.out
				.println("bufferedReaderTime2: " + bufferedReaderTime2 + "ms");
		System.out.println("scannerTime: " + scannerTime + "ms");

	}

	private static void ch9Q3() {

		// String fileName = "/home/ramin/workspace/ch9Q3_utf16";
		String fileName = "/home/ramin/workspace/ch9Q3_utf8_bom";
		Map<Charset, Double> results = ch9q3_1(fileName);
		System.out.println("***** Analysis Result *******");
		System.out.println(results);
		Double highestPrecentage = results.entrySet().iterator().next()
				.getValue();
		if (highestPrecentage != 0) {
			for (Entry<Charset, Double> result : results.entrySet()) {
				Charset standardCharset = result.getKey();
				Double percentage = result.getValue();
				if (percentage.equals(highestPrecentage)) {
					System.out.println("The file is encoded (" + percentage
							+ "% accuracy) with: " + standardCharset);
				}
			}
		} else {
			System.out.println("Unable to find encoder for file: " + fileName);
		}

	}

	private static Map<Charset, Double> ch9q3_1(String file) {
		byte[] bytes = ch9q3_2(file);
		List<String> dictionnary = ch9q3_2();
		Queue<Charset> standardCharSet = ch9q3_3();

		Map<Charset, Double> result = new HashMap<Charset, Double>();

		Charset charset = null;
		boolean found = false;
		while (!found && !standardCharSet.isEmpty()) {
			charset = standardCharSet.poll();
			String text = new String(bytes, charset);
			System.out.println("*** Charset: " + charset + "***");
			System.out.println(text.length());
			List<String> words = Stream.of(text.split("[\\P{Alnum}]+"))
					.map(x -> x.toLowerCase()).collect(Collectors.toList());
			System.out.println(words);
			if (words.isEmpty()) {
				result.put(charset, new Double(0));
			} else {
				if (dictionnary.containsAll(words)) {
					result.put(charset, new Double(100));
					found = true;
				} else {
					int count = 0;
					for (String word : words) {
						if (dictionnary.contains(word)) {
							count++;
						}
					}
					double percentage = (double) count / words.size();
					result.put(charset, percentage);
				}

			}
		}
		Comparator<Entry<Charset, Double>> comparator = (x, y) -> {
			if (x.getValue() > y.getValue()) {
				return -1;
			}
			if (x.getValue() == y.getValue()) {
				return 0;
			}
			return 1;
		};
		result = result
				.entrySet()
				.stream()
				.sorted(comparator)
				.collect(
						Collectors.toMap(Entry::getKey, Entry::getValue,
								(x, y) -> x, LinkedHashMap::new));
		return result;
	}

	private static byte[] ch9q3_2(String file) {
		Path path = Paths.get(file);
		byte[] bs = new byte[1000];
		try (InputStream inputStream = Files.newInputStream(path)) {
			inputStream.read(bs);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return bs;
	}

	private static List<String> ch9q3_2() {
		String file = "/usr/share/dict/american-english";
		List<String> americainEnglishDict = null;
		try {
			americainEnglishDict = Files.readAllLines(Paths.get(file));

		} catch (IOException e) {

			e.printStackTrace();
		}

		return americainEnglishDict;
	}

	public static Queue<Charset> ch9q3_3() {
		Deque<Charset> standardCharsets = new ArrayDeque<Charset>();
		standardCharsets.push(StandardCharsets.US_ASCII);
		standardCharsets.push(StandardCharsets.ISO_8859_1);
		standardCharsets.push(StandardCharsets.UTF_8);
		standardCharsets.push(StandardCharsets.UTF_16BE);
		standardCharsets.push(StandardCharsets.UTF_16LE);
		return standardCharsets;
	}

	private static void ch9Q2() {
		String file = "/home/ramin/workspace/AMERICAN_MISSIONARY.html";
		try {
			List<String> lines = Files.readAllLines(Paths.get(file));
			Map<String, Set<Integer>> map = new TreeMap<String, Set<Integer>>();
			int i = 1;
			for (String line : lines) {
				String[] words = line.split("[\\s]");
				int j = 0;
				while (j < words.length) {
					Set<Integer> value = new HashSet<Integer>();
					value.add(i);
					map.merge(words[j], value, (t, u) -> {
						t.addAll(u);
						return t;
					});
					j++;
				}
				i++;
			}
			map.forEach((k, v) -> System.out.println(k + " = " + v));

		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}

	private static void ch9Q1b() throws FileNotFoundException, IOException {

		// copying content into memory
		String file = "/home/ramin/workspace/AMERICAN_MISSIONARY.html";
		InputStream inputStream = new FileInputStream(Paths.get(file).toFile());
		byte b[] = new byte[1024];
		int len;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while ((len = inputStream.read(b)) != -1) {
			outputStream.write(b, 0, len);
		}
		byte[] bytes = outputStream.toByteArray();
	}

	private static void ch9q1a() {
		String file = "/home/ramin/workspace/AMERICAN_MISSIONARY.html";
		String file2 = "/home/ramin/workspace/AMERICAN_MISSIONARY_2.html";
		Path path = Paths.get(file);
		byte array[] = null;
		try (InputStream inputStream = new FileInputStream(path.toFile());) {
			array = new byte[(int) Files.size(path)];
			inputStream.read(array);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		try (OutputStream outputStream = new FileOutputStream(Paths.get(file2)
				.toFile())) {
			outputStream.write(array);

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	private static void ch8q17() {
		Path path = Paths.get("/home/ramin/Documents/warAndPeace.htm");
		try (Stream<String> stream = Files.lines(path)) {
			Map<Integer, List<String>> map = stream.flatMap(
					t -> Stream.of(t.split("\\s"))).collect(
					Collectors.groupingBy(String::length));

			map.entrySet()
					.stream()
					.sorted(Comparator
							.<Entry<Integer, List<String>>, Integer> comparing(
									Entry<Integer, List<String>>::getKey)
							.reversed()).flatMap(e -> e.getValue().stream())
					.sequential().distinct().limit(500)
					.forEach(System.out::println);

		} catch (IOException exception) {
			exception.printStackTrace();
			StringWriter stringWritter = new StringWriter();
			exception.printStackTrace(new PrintWriter(stringWritter));
		}
	}

	private static void ch8q16() {
		Path path = Paths.get("/home/ramin/Documents/warAndPeace.htm");
		try (Stream<String> stream = Files.lines(path)) {
			Map<Integer, List<String>> map = stream.flatMap(
					t -> Stream.of(t.split("\\s"))).collect(
					Collectors.groupingBy(String::length));

			map.entrySet()
					.stream()
					.parallel()
					.sorted(Comparator
							.<Entry<Integer, List<String>>, Integer> comparing(
									Entry<Integer, List<String>>::getKey)
							.reversed()).flatMap(e -> e.getValue().stream())
					.sequential().limit(500).forEach(System.out::println);

		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private static void ch8q15() {
		Stream.<BigInteger> iterate(BigInteger.ONE.pow(50), t -> {
			do {
				t = t.add(BigInteger.ONE);
			} while (!t.isProbablePrime(100));
			return t;
		}).limit(500).forEach(System.out::println);
	}

	private static void ch8q14a() {

		Random random = new Random();
		List<Double> list = random.doubles(0, 100).limit(500).boxed()
				.collect(Collectors.toList());
		// System.out.println("list = " + list);
		double average = list.stream().mapToDouble(t -> t).average()
				.getAsDouble();
		System.out.println("average = " + average);

		class Averager {
			int count = 0;
			double sum = 0;

			public synchronized Averager sum(double d) {

				sum = sum + d;
				count++;
				return this;
			}

			public Averager combine(Averager d) {
				return this;
			}

			public double average() {
				return count == 0 ? 0 : sum / count;
			}

			public String toString() {
				String result = "Sum=" + sum + " Count=" + count + " Average="
						+ average();
				return result;
			}
		}

		BiFunction<Averager, Double, Averager> biFunction = Averager::sum;
		BinaryOperator<Averager> binaryOperator = Averager::combine;
		Averager averager = list.stream().reduce(new Averager(), biFunction,
				binaryOperator);
		System.out.println(averager);

	}

	private static void ch8q14b() {

		Random random = new Random();
		List<Double> list = random.doubles(0, 100).limit(500).boxed()
				.collect(Collectors.toList());
		// System.out.println("list = " + list);
		double average = list.stream().mapToDouble(t -> t).average()
				.getAsDouble();
		System.out.println("average = " + average);

		class Averager {
			int count = 0;
			double sum = 0;

			public Averager() {
				this.sum = 0;
				this.count = 0;
			}

			public Averager(Averager avg) {
				this.sum = avg.sum;
				this.count = avg.count;
			}

			public void sum(double d) {
				sum = sum + d;
				count++;
			}

			public Averager accumulate(double d) {
				Averager averager = new Averager(this);
				averager.sum(d);
				return averager;

			}

			public Averager combine(Averager avg) {
				Averager averager = new Averager(this);
				averager.sum = averager.sum + avg.sum;
				averager.count = averager.count + avg.count;
				return averager;
			}

			public double average() {
				return count == 0 ? 0 : sum / count;
			}

			public String toString() {
				String result = "Sum=" + sum + " Count=" + count + " Average="
						+ average();
				return result;
			}
		}

		Averager averager = list
				.stream()
				.parallel()
				.reduce(new Averager(), Averager::accumulate, Averager::combine);
		System.out.println(averager);

	}

	private static void ch8q13() {
		Random random = new Random();

		Stream<List<Integer>> stream = new ArrayList<List<Integer>>(
				Collections.nCopies(random.nextInt(5), null)).stream();

		List<List<Integer>> l2 = stream.map(
				t -> random.ints(0, 1000).limit(5).boxed()
						.collect(Collectors.toList())).collect(
				Collectors.toList());

		// Methodology 1: accumulator
		BinaryOperator<List<Integer>> accumulator = (x, y) -> {
			List<Integer> z = new ArrayList(x);
			z.addAll(y);
			return z;
		};

		List<Integer> list = l2.stream().reduce(accumulator).get();
		System.out.println("list =" + list);
		System.out.println("l2 = " + l2);

		// Methodology 2: accumulator + identity
		List<Integer> identity = new ArrayList<Integer>();
		List<Integer> l3 = l2.stream().reduce(identity, accumulator);
		System.out.println("l2 = " + l2);
		System.out.println("l3 = " + l3);
		System.out.println(list.equals(l3));

		// Methodology 3: accumulator + identity + combiner
		BinaryOperator<List<Integer>> combiner = (p, q) -> {
			System.out.println("p=" + p);
			System.out.println("q=" + q);
			List<Integer> r = new ArrayList(p);
			r.addAll(q);
			return r;
		};

		List<Integer> l4 = l2.stream().parallel()
				.reduce(identity, accumulator, combiner);
		System.out.println("l2 = " + l2);
		System.out.println("l4 = " + l4);
		System.out.println(list.equals(l4));
	}

	public static <T> Stream<T> zip(Stream<T> stream, Stream<T> stream2) {
		List<T> l1 = stream.collect(Collectors.toList());
		List<T> l2 = stream2.collect(Collectors.toList());
		List<T> l3 = new ArrayList<T>();

		int s1 = l1.size();
		int s2 = l2.size();
		int smax = 0;
		if (s1 >= s2) {
			smax = s1;
		} else {
			smax = s2;
		}
		int i = 0;
		while (i < smax) {
			if (i < s1) {
				l3.add(l1.get(i));
			} else {
				l3.add(null);
			}

			if (i < s2) {
				l3.add(l2.get(i));
			} else {
				l3.add(null);
			}
			i++;
		}
		return l3.stream();
	}

	public static <T> boolean isFinite(Stream<T> stream) {
		long l = stream.count();
		System.out.println("l = " + l);
		return true;
	}

	private static void ch8q10a() {
		Path path = Paths.get("/usr/share/dict/words");
		try (Stream<String> stream = Files.lines(path)) {
			Map<Integer, List<String>> map = stream.collect(Collectors
					.groupingBy(String::length));
			Optional<Entry<Integer, List<String>>> opt = map
					.entrySet()
					.stream()
					.max(Comparator
							.<Entry<Integer, List<String>>, Integer> comparing(Entry::getKey));
			opt.ifPresent(System.out::println);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private static void ch8q10b() {
		Path path = Paths.get("/usr/share/dict/words");
		try (Stream<String> stream = Files.lines(path)) {
			Map<Integer, List<String>> map = stream.collect(Collectors
					.groupingBy(String::length));
			TreeMap<Integer, List<String>> treeMap = new TreeMap<Integer, List<String>>(
					Comparator.<Integer, Integer> comparing(t -> t));
			treeMap.putAll(map);
			treeMap.forEach((Integer k, List<String> v) -> System.out.println(k
					+ "= " + v));
			System.out.println(treeMap.get(treeMap.lastKey()));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private static void ch8q9() {
		Path path = Paths.get("/usr/share/dict/words");
		try (Stream<String> stream = Files.lines(path)) {
			Double d = stream.map(t -> t.length()).collect(
					Collectors.averagingInt(t -> t));
			System.out.println("average = " + d);

		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private static void ch8q8b() {

		Predicate<String> exactlyLeastSixVowels = t -> {
			t = t.toLowerCase();
			Character[] v = { 'a', 'e', 'i', 'o', 'u', 'y' };
			Set<Character> cp = new HashSet<Character>(Arrays.asList(v));
			Set<Character> vowels = new HashSet<Character>(cp);
			for (int i = 0; i < t.length(); i++) {
				Character c = t.charAt(i);
				if (vowels.contains(c)) {
					if (!cp.remove(c))
						return false;
				}
			}

			if (cp.isEmpty()) {
				return true;
			}
			return false;
		};

		Path path = Paths.get("/usr/share/dict/words");
		try (Stream<String> lines = Files.lines(path)) {
			lines.filter(exactlyLeastSixVowels).forEach(System.out::println);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private static void ch8q8a() {
		Path path = Paths.get("/usr/share/dict/words");
		try (Stream<String> lines = Files.lines(path)) {
			Predicate<String> atLeastSixVowels = t -> {
				t = t.toLowerCase();
				Character[] v = { 'a', 'e', 'i', 'o', 'u', 'y' };
				Set<Character> vowels = new HashSet<Character>(Arrays.asList(v));
				for (int i = 0; i < t.length(); i++) {
					if (vowels.contains(t.charAt(i))) {
						vowels.remove(t.charAt(i));
					}
				}
				return vowels.isEmpty() ? true : false;
			};

			lines.filter(atLeastSixVowels).forEach(System.out::println);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private static void ch8q7() {
		Path path = Paths.get("/home/ramin/.bashrc");
		mostFreqWords(path);
	}

	private static void mostFreqWords(Path path) {
		try (Stream<String> lines = Files.lines(path)) {

			// Transforming a stream of lines to a stream of words
			Stream<String> stream = lines.flatMap(
					x -> Stream.of(x.split("[\\s]+")))
					.filter(y -> !y.isEmpty());

			// Transforming a stream of words to a Map with key words and value
			// the frequency count of the word
			Map<String, Long> map = stream.collect(Collectors.groupingBy(
					Function.identity(), Collectors.counting()));
			System.out.println(map);

			// Finding the most used word
			Optional<Entry<String, Long>> opt = map
					.entrySet()
					.stream()
					.collect(
							Collectors.maxBy(Comparator
									.<Entry<String, Long>> comparingLong(Entry::getValue)));
			opt.ifPresent(System.out::println);

			// Finding the most used words
			Stream<Entry<String, Long>> stream2 = map.entrySet().stream();
			stream2.sorted(
					Comparator.<Entry<String, Long>> comparingLong(
							Entry::getValue).reversed()).limit(10)
					.forEachOrdered(System.out::println);

			// https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
			// https://www.mkyong.com/java/how-to-sort-a-map-in-java/

		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}

	private static void first100Tokens(Path path) {
		try (Stream<String> stream = Files.lines(path)) {
			Stream<String> stream2 = stream.flatMap(w -> Stream.<String> of(
					w.split("[\\s]+")).filter(x -> !x.isEmpty()));
			stream2.filter(w -> isWord(w)).limit(100)
					.forEach(System.out::println);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isWord(String w) {
		return w.codePoints().allMatch(Character::isAlphabetic);
	}

	public static boolean isJavaIdentifier(String w) {
		return Character.isJavaIdentifierStart(w.charAt(0))
				&& w.substring(1, w.length() - 1).codePoints()
						.allMatch(x -> Character.isJavaIdentifierPart(x));
	}

}
