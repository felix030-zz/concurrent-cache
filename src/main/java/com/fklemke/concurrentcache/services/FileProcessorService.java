package com.fklemke.concurrentcache.services;

import com.fklemke.concurrentcache.domain.Word;
import com.fklemke.concurrentcache.domain.WordEntryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.cache.Cache;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Class handles the processing of a text file and populates a shared cache.
 */
@Service
public class FileProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessorService.class);
    private Cache<String, Word> cache;
    private WordParseService wordParseService;
    @Value("${file.directory}")
    private Path fileDirectory;

    @Autowired
    public FileProcessorService(Cache<String, Word> cache, WordParseService wordParseService) {
        this.cache = cache;
        this.wordParseService = wordParseService;
    }

    /**
     * Asynchronously processes a text file and populates a shared cache with words.
     *
     * @param filename the filename of the processing request.
     * @return a CompletableFuture as a flag that the processing finished.
     * @throws IOException if the filename could not be resolved to an actual file.
     */
    @Async
    public CompletableFuture<Void> processTextFile(String filename) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try (BufferedReader br = Files.newBufferedReader(getAbsolutePathFromFilename(filename), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> wordList = wordParseService.getWordsFromTextLine(line);

                for (String word : wordList) {
                    cache.invoke(word, new WordEntryProcessor(), filename);
                }
            }
        }
        stopWatch.stop();
        LOGGER.info(String.format("File '%s' processed by thread: %s time in seconds: %s", filename,
                Thread.currentThread().getName(), stopWatch.getTotalTimeSeconds()));
        return completedFuture(null);
    }

    /**
     * Resolves the filename to a file in a predefined directory.
     */
    private Path getAbsolutePathFromFilename(String filename) {
        return fileDirectory.resolve(Paths.get(filename));
    }
}
