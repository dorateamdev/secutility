package dev.dorateam.secutility;

import java.io.*;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Configuration file format: idx_1:idx_2;idx_i:idx_j...
 * Example: 1:10;5:6;3:10
 */
public class DefaultSecutilityImpl implements Secutility {

    Set<Swap> swaps = new HashSet<>(10);

    /**
     * Default constructor is trying to load config from ${user.home}/.secutility file
     */
    public DefaultSecutilityImpl() {
        this(new File(System.getProperty("user.home"), ".secutility"));
    }

    public DefaultSecutilityImpl(File cfgFile) {
        this(inFromFile(cfgFile));
    }

    private static InputStream inFromFile(File cfgFile) {
        if (!cfgFile.exists()) throw new RuntimeException("Passed " + cfgFile.getAbsolutePath() + " is not existed. Please passed valid secutility config file.");
        try {
            return Files.newInputStream(cfgFile.toPath());
        } catch (IOException ex) {
            throw new RuntimeException(cfgFile.getAbsolutePath() + " fail to read secutility config.");
        }
    }

    public DefaultSecutilityImpl(InputStream inCfg) {
        try {
            String content = fileAsString(inCfg);
            String[] _swaps = content.split(";");
            for (String _swap : _swaps) {
                String[] idxs = _swap.split(":");
                if (idxs.length != 2) throw new IllegalArgumentException("Invalid configuration inside .secutility");
                int from = Integer.parseInt(idxs[0]);
                int to = Integer.parseInt(idxs[1]);
                swaps.add(new Swap(from, to));
            }
        } catch (IOException ex) {
            throw new RuntimeException("Fail to initialize secutility.", ex);
        }
    }

    public String restoreS(String str) {
        String[] s = str.split(" ");
        swaps.forEach(swap -> swap.execute(s));
        return String.join(" ", s);
    }

    private String fileAsString(InputStream in) throws IOException {
        try (Scanner scanner = new Scanner(in)) {
            return scanner.nextLine();
        }
    }

    private static class Swap {
        int from;
        int to;

        Swap(int from, int to) {
            this.from = from;
            this.to = to;
        }

        void execute(String[] s) {
            String tmp = s[to];
            s[to] = s[from];
            s[from] = tmp;
        }
    }
}
