package com.fklemke.concurrentcache;

import com.fklemke.concurrentcache.services.FileProcessorService;
import com.fklemke.concurrentcache.services.WordStatisticsPrintOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Runs the processing of text files into a cache and prints out statistics after the application is fully started.
 */
@Component
public class ConcurrentCacheRunner implements CommandLineRunner {

    private FileProcessorService fileProcessorService;
    private WordStatisticsPrintOutService wordStatsPrintOutService;


    @Autowired
    public ConcurrentCacheRunner(FileProcessorService fileProcessorService, WordStatisticsPrintOutService wordStatsPrintOutService) {
        this.fileProcessorService = fileProcessorService;
        this.wordStatsPrintOutService = wordStatsPrintOutService;
    }

    @Override
    public void run(String... args) throws Exception {
        CompletableFuture flag1 = fileProcessorService.processTextFile("iliad-book-1.txt");
        CompletableFuture flag2 = fileProcessorService.processTextFile("iliad-book-2.txt");

        CompletableFuture.allOf(flag1, flag2).join();
        wordStatsPrintOutService.printOutCache();
    }
}
