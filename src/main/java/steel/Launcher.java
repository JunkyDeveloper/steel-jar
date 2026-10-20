package steel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

/** Extracts the Steel binary matching this OS from the jar and runs it attached to the terminal. */
public final class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        String name = binaryName();
        Path dir = Files.createTempDirectory("steel");
        Path bin = dir.resolve(name);
        // deleted in reverse order on JVM exit, also after SIGTERM/Ctrl+C when the finally below is skipped
        dir.toFile().deleteOnExit();
        bin.toFile().deleteOnExit();
        try (InputStream in = Launcher.class.getResourceAsStream("/bin/" + name)) {
            if (in == null) throw new IOException("no bundled binary: " + name);
            Files.copy(in, bin, StandardCopyOption.REPLACE_EXISTING);
        }
        if (!bin.toFile().setExecutable(true)) throw new IOException("cannot chmod " + bin);

        String[] cmd = new String[args.length + 1];
        cmd[0] = bin.toString();
        System.arraycopy(args, 0, cmd, 1, args.length);

        // inheritIO: child shares our stdin/stdout/stderr, so the Steel console is the user's terminal.
        // cwd is inherited too, so Steel's config/world land where the user ran the jar.
        int code;
        try {
            code = new ProcessBuilder(cmd).inheritIO().start().waitFor();
        } finally {
            Files.deleteIfExists(bin);
            Files.deleteIfExists(dir);
        }
        System.exit(code);
    }

    private static String binaryName() throws IOException {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (os.contains("win")) return "steel-windows.exe";
        if (os.contains("mac")) return "steel-macos";
        if (os.contains("linux")) {
            // musl distros (Alpine) ship /lib/ld-musl-*
            try (var libs = Files.newDirectoryStream(Path.of("/lib"), "ld-musl-*")) {
                if (libs.iterator().hasNext()) return "steel-linux-musl";
            } catch (IOException ignored) {
                // no /lib -> assume glibc
            }
            return "steel-linux";
        }
        throw new IOException("unsupported OS: " + os);
    }
}
