package com.brunosottomayor;

import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "extractor", mixinStandardHelpOptions = true, version = "1.0", description = "Extracts all classes of the project", sortOptions = false)
public class App implements Runnable {

    @CommandLine.Option(names = { "-i", "--input" }, order=1, description = "Path for the source directory", required = true)
    private Path inputPath;

    @CommandLine.Option(names = { "-o", "--output" }, order = 2, description = "Path for the output directory", required = true)
    private Path outputPath;

    @CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, order = 3, description = "Displays this help message and quits.")
    private boolean helpRequested;

    @Override
    public void run() {
        ClassExtractor classExtractor = new ClassExtractor(inputPath);
        try {
            classExtractor.extractClasses(outputPath);
        } catch (ClassExtractorException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new App());
        commandLine.execute(args);
        System.exit(0);
    }
}
