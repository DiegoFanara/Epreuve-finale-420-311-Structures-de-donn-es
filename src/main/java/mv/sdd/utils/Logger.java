package mv.sdd.utils;

import java.io.PrintWriter;

// NE PAS MODIFIER
public class Logger {
    private final PrintWriter out;
    private final boolean echoConsole;

    // DOC PrintWriter : https://docs.oracle.com/javase/8/docs/api/java/io/PrintWriter.html
    // DOC Writer : https://docs.oracle.com/javase/8/docs/api/java/io/Writer.html
    public Logger(PrintWriter out, boolean echoConsole) {
        this.out = out;
        this.echoConsole = echoConsole;
    }

    public void logLine(String message) {
        out.println(message);
        if (echoConsole) {
            System.out.println(message);
        }
    }

    public void logEmpty() {
        logLine("");
    }
}
