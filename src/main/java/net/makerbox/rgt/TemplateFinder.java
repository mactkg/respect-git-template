package net.makerbox.rgt;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemplateFinder {
    static String getTemplateCommand = "git config --get commit.template";
    static String getGlobalTemplateCommand = "git config --global --get commit.template";

    private String basePath;
    private boolean forceGlobal;

    TemplateFinder() {
        basePath = "";
        forceGlobal = false;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setForceGlobal(boolean forceGlobal) {
        this.forceGlobal = forceGlobal;
    }

    public String getTemplate() {
        String res;
        Path path = getTemplatePath();

        try {
            res = Files.readString(path);
        } catch(IOException e) {
            return "";
        }

        return res;
    }

    @Nullable
    protected Path getTemplatePath() {
        String pathString = getTemplatePathString();
        if(pathString == null) {
            return null;
        }

        pathString = pathString.replaceFirst("^~", System.getProperty("user.home"));
        Path path = Path.of(pathString);
        if(Files.isSymbolicLink(path)) {
            path = digSymlink(path, 5);
        }

        if(path == null) {
            return null;
        }

        return path;
    }

    @Nullable
    protected String getTemplatePathString() {
        String res;

        try {
            String command = "";
            if(!this.basePath.isBlank()) {
                command = "GIT_DIR=" + this.basePath + " ";
            }
            command += (this.forceGlobal ? getGlobalTemplateCommand : getTemplateCommand);

            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            if(p.exitValue() != 0) {
                return null;
            }

            StringWriter writer = new StringWriter();
            IOUtils.copy(p.getInputStream(), writer, Charset.defaultCharset());
            res = writer.toString();
            res = StringUtils.chomp(res);
        } catch(IOException|InterruptedException e) {
            return null;
        }

        return res;
    }

    @Nullable
    static protected Path digSymlink(Path path, int count) {
        if (count < 0) {
            return null;
        } else if (Files.isSymbolicLink(path) && Files.isReadable(path)) {
            return path;
        }

        try {
            Path next = Files.readSymbolicLink(path);
            return digSymlink(next, count - 1);
        } catch (IOException e) {
            return null;
        }
    }
}
