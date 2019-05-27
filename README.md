##Concurrent Cache 
This project is an exemplary implementation of a caching solution, within a concurrent environment.
Two producer are simultaneously parsing text-files and populating a shared cache. A PrintOutService will print the results 
to the console when the processing is finished.

Producer (2 x TextFileProcessor) -> Cache -> Consumer (PrintOutService)

###Implementation details
#####Threads
The ThreadPool is set to a maximum of 3 Threads, if more than two files are ought to be concurrently processed, then the
pool size should be adjusted.

#####FileProcessorService
The directory containing the files are set to an absolute path in the `application.properties`. To run the application 
locally, this path needs to be updated to the hosting system.

The process method returns an empty CompletableFuture in order to flag when the processing is finished and signal the 
PrintOutService that it can traverse the Cache to print out the statistics.

#####WordParseService
The service is a basic implementation and implements the custom `WordParser` interface. The purpose is to be easily 
replaceable by a more sophisticated implementation.

#####Cache
For this project the Hazelcast implementation of the JCache standard is used. Only methods defined in the JSR-107 are being
used, it should therefore be easily replaceable by other implementations c.f.: [JSR-107 implementations](https://jcp.org/aboutJava/communityprocess/implementations/jsr107/index.html)

