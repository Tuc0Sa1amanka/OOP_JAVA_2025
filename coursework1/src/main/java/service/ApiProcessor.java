package service;

import api.ApiPair;
import save.CsvSave;
import save.JsonSave;
import api.ApiRegistry;
import java.time.Instant;
import java.time.Duration;
import save.CsvConvertible;
import java.io.IOException;
import java.util.concurrent.*;
import java.net.ConnectException;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import com.opencsv.exceptions.CsvValidationException;

public class ApiProcessor {
    private final ApiRegistry registry;
    private final long t;
    private final ExecutorService workerPool;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean isRunning;
    private final ConcurrentHashMap<String, Boolean> activeApis = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Instant> lastCompletionTime = new ConcurrentHashMap<>();
    public ApiProcessor(ApiRegistry registry, int n, long t) {
        this.registry = registry;
        this.t = t;
        this.workerPool = Executors.newFixedThreadPool(n);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        isRunning = new AtomicBoolean(false);
    }
    public void start(String[] apiNames, String format, boolean append) {
        JsonSave jsonSave = new JsonSave(append);
        CsvSave csvSave = new CsvSave(append);
        isRunning.set(true);
        System.out.println("started polling");
        for (String apiName : apiNames) {
            ApiPair pair = registry.getPair(apiName);
            if (pair == null) {
                System.out.printf("unregistered api continued: %s\n", apiName);
                continue;
            }
            scheduleNext(apiName, pair, format, jsonSave, csvSave);
        }
    }
    private void scheduleNext(String apiName, ApiPair pair,
                              String format, JsonSave jsonSave, CsvSave csvSave) {
        if (!isRunning.get()) return;
        if (t > 0) {
            if (activeApis.putIfAbsent(apiName, Boolean.TRUE) != null) {
                scheduler.schedule(() -> scheduleNext(apiName, pair, format, jsonSave, csvSave), 200, TimeUnit.MILLISECONDS);
                return;
            }
        }
        Instant now = Instant.now();
        Instant last = lastCompletionTime.get(apiName);
        long delaySeconds = 0;
        if (last != null) {
            long elapsed = Duration.between(last, now).toSeconds();
            delaySeconds = Math.max(0, t - elapsed);
        }
        if (delaySeconds == 0) {
            runTask(apiName, pair, format, jsonSave, csvSave);
        } else {
            scheduler.schedule(
                    () -> runTask(apiName, pair, format, jsonSave, csvSave),
                    delaySeconds, TimeUnit.SECONDS
            );
        }
    }
    private void runTask(String apiName, ApiPair pair,
                         String format, JsonSave jsonSave, CsvSave csvSave) {
        workerPool.submit(() -> {
            if (!isRunning.get()) {
                activeApis.remove(apiName);
                return;
            }
            try {
                String json = pair.client().extract();
                Record data = pair.transform().transform(json);
                if (format.equals("json")) {
                    jsonSave.save(data, apiName);
                } else {
                    csvSave.save((CsvConvertible) data, apiName);
                }
            } catch (ConnectException e) {
                System.out.println("[" + apiName + "][" + Thread.currentThread().getName()
                        + "][" + Instant.now() + "] Connection error");
            } catch (HttpTimeoutException e) {
                System.out.println("[" + apiName + "][" + Thread.currentThread().getName()
                        + "][" + Instant.now() + "] Timeout");
            } catch (InterruptedException e) {
                System.out.println("[" + apiName + "][" + Thread.currentThread().getName()
                        + "][" + Instant.now() + "] Interrupted");
            } catch (IOException e) {
                System.out.println("[" + apiName + "][" + Thread.currentThread().getName()
                        + "][" + Instant.now() + "] IO error: " + e.getMessage());
            } catch (CsvValidationException e) {
                System.out.println("[" + apiName + "][" + Thread.currentThread().getName()
                        + "][" + Instant.now() + "] CSV error: " + e.getMessage());
            } finally {
                lastCompletionTime.put(apiName, Instant.now());
                activeApis.remove(apiName);
                if (isRunning.get()) {
                    scheduleNext(apiName, pair, format, jsonSave, csvSave);
                }
            }
        });
    }
    public void stop() {
        if (isRunning.getAndSet(false)) {
            scheduler.shutdownNow();
            workerPool.shutdown();
            try {
                if (!workerPool.awaitTermination(2, TimeUnit.SECONDS)) {
                    workerPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                workerPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
            activeApis.clear();
            lastCompletionTime.clear();
            System.out.println("polling stopped");
        }
    }
}