package fd.theme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import fd.theme.icons.BatteryIcon;
import fd.theme.icons.FolderColor;
import fd.theme.icons.FolderIcon;
import fd.theme.icons.Icon;
import fd.theme.icons.NetworkIcon;

public final class IconTheme {

    private static final String DEFAULT_THEME_NAME = "hicolor";

    private static final File[] iconsDir = new File[] {
            /* User themes */
            //TODO : new File(System.getProperty("user.home", ""), ".local/share/icons"),

            /* System themes */
            new File("/usr/share/icons")
    };

    /* Supported formats */
    private static final String[] extensions = new String[] { ".svg", ".png" };

    /* Theme folder */
    private final File themeDir;

    /* Theme name */
    private final String name;

    /* Theme comment */
    private final String comment;

    /* Example icon for this theme */
    private final String exampleIcon;

    private final Map<String, DirectoryInfo> directories;

    public IconTheme(final String name) throws IOException {
        this.themeDir = new File(iconsDir[0], name);

        if (!(themeDir.exists() && themeDir.isDirectory())) {
            throw new IllegalArgumentException("theme '" + name + "' not found");
        }

        File themeFile = new File(themeDir, "index.theme");

        if (!(themeFile.exists() && themeFile.isFile())) {
            throw new FileNotFoundException("index.theme not found in " + themeDir);
        }

        Ini ini = new Ini(themeFile);

        Section iconThemeSection = ini.get("Icon Theme");

        this.name        = iconThemeSection.get("Name", name);
        this.comment     = iconThemeSection.get("Comment", "");
        this.exampleIcon = iconThemeSection.get("Example", "folder");

        // TODO (parents): iconThemeSection.get("Inherits", "hicolor").split(",");

        this.directories = new HashMap<>();

        String[] directoryList = iconThemeSection.get("Directories").split(",");

        for (String directory : directoryList) {
            Section s = ini.get(directory);

            String size      = s.get("Size");
            String scale     = s.get("Scale", "1");
            String context   = s.get("Context", "");
            String type      = s.get("Type", "Threshold");
            String maxSize   = s.get("MaxSize", size);
            String minSize   = s.get("MinSize", size);
            String threshold = s.get("Threshold", "2");

            DirectoryInfo directoryInfo = new DirectoryInfo(size, scale, context, type, maxSize, minSize, threshold);

            this.directories.put(directory, directoryInfo);
        }
    }

    public File findIcon(Icon icon) {
        return findIcon(icon.getName());
    }

    public File findIcon(String icon) {
        return findIcon(icon, 64, 1);
    }

    public File findIcon(String icon, int size, int scale) {
        File file = findIconHelper(icon, size, scale, name);

        if (file != null)
            return file;

        //file = findIconHelper(icon, size, scale, DEFAULT_THEME_NAME);
        //if (file != null)
        //    return file;

        return lookupFallbackIcon(icon);
    }

    private File findIconHelper(String icon, int size, int scale, String theme) {
        // TODO: use "theme"

        File file = lookupIcon(icon, size, scale, theme);

        if (file != null)
            return file;

        // TODO: check parents
        return null;
    }

    private File lookupIcon(String icon, int size, int scale, String theme) {
        File icons = themeDir; // TODO

        for (Entry<String, DirectoryInfo> entry : directories.entrySet()) {
            if (directoryMatchesSize(entry.getValue(), size, scale)) {
                File directory = new File(icons, entry.getKey());
                for (String ext : extensions) {
                    File file = new File(directory, icon + ext);

                    if (file.exists()) {
                        return file;
                    }
                }
            }
        }

        File closest_filename = null;
        int minimal_size = Integer.MAX_VALUE;

        for (Entry<String, DirectoryInfo> entry : directories.entrySet()) {
            for (String ext : extensions) {
                File directory = new File(icons, entry.getKey());
                File file = new File(directory, icon + ext);

                int distance;
                if (file.exists() && (distance = directorySizeDistance(entry.getValue(), size, scale)) < minimal_size) {
                    closest_filename = file;
                    minimal_size = distance;
                }
            }
        }

        return closest_filename;
    }

    private File lookupFallbackIcon(String icon) {
        // TODO
        return null;
    }

    private boolean directoryMatchesSize(DirectoryInfo subdir, int iconsize, int iconscale) {
        if (subdir.getScale() != iconscale)
            return false;
        if ("Fixed".equals(subdir.getType()))
            return subdir.getSize() == iconsize;
        if ("Scaled".equals(subdir.getType()))
            return subdir.getMinSize() <= iconsize && iconsize <= subdir.getMaxSize();
        if ("Threshold".equals(subdir.getType()))
            return subdir.getSize() - subdir.getThreshold() <= iconsize && iconsize <= subdir.getSize() + subdir.getThreshold();

        return false;
    }

    private int directorySizeDistance(DirectoryInfo subdir, int iconsize, int iconscale) {
        if ("Fixed".equals(subdir.getType()))
            return Math.abs((subdir.getSize() * subdir.getScale()) - (iconsize - iconscale));
        if ("Scaled".equals(subdir.getType())) {
            if (iconsize * iconscale < subdir.getMinSize() * subdir.getScale())
                return (subdir.getMinSize() * subdir.getScale()) - (iconsize * iconscale);
            if (iconsize * iconscale > subdir.getMaxSize() * subdir.getScale())
                return (subdir.getMaxSize() * subdir.getScale()) - (iconsize * iconscale);
            return 0;
        }
        if ("Threshold".equals(subdir.getType())) {
            if (iconsize * iconscale < (subdir.getSize() - subdir.getThreshold()) * subdir.getScale())
                return (subdir.getMinSize() * subdir.getScale()) - (iconsize * iconscale);
            if (iconsize * iconsize > (subdir.getSize() + subdir.getThreshold()) * subdir.getScale())
                return (iconsize * iconscale) - (subdir.getMaxSize() * subdir.getScale());
            return 0;
        }

        return 0;
    }

    /*=========================================================*/
    /* getters */
    /*=========================================================*/

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getExampleIcon() {
        return exampleIcon;
    }

    public File getThemeDir() {
        return themeDir;
    }

    /*=========================================================*/
    /* public static */
    /*=========================================================*/

    public static void main(String[] args) {
        IconTheme theme = getCurrentThemeUnsafe();
        for (BatteryIcon i : BatteryIcon.values()) {
            File icon = theme.findIcon(i.getName());
            System.out.println(icon);
        }
        for (NetworkIcon i : NetworkIcon.values()) {
            File icon = theme.findIcon(i.getName());
            System.out.println(icon);
        }
        for (FolderIcon i : FolderIcon.values()) {
            File f = theme.findIcon(i.getName());
            System.out.println(f);
        }
        for (FolderColor c : FolderColor.values()) {
            FolderIcon.setGlobalColor(c);
            for (FolderIcon i : FolderIcon.values()) {
                File f = theme.findIcon(i.getName());
                System.out.println(f);
            }
        }
    }

    public static String getCurrentThemeName() {
        try {
            return execLine(
                    "gsettings",
                    "get",
                    "org.gnome.desktop.interface",
                    "icon-theme"
                ).replaceAll("'", "");
        } catch (Exception e) {
            System.err.printf("Unable to get the current theme name, using %s - %s\n", DEFAULT_THEME_NAME, e.getMessage());
        }
        return DEFAULT_THEME_NAME;
    }

    public static IconTheme getCurrentTheme() throws IOException {
        return new IconTheme(getCurrentThemeName());
    }

    public static IconTheme getCurrentThemeUnsafe() {
        try {
            return getCurrentTheme();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*=========================================================*/
    /* private static */
    /*=========================================================*/

    private static String execLine(final String... command) throws IOException {
        final Process proc = Runtime.getRuntime().exec(command);
        final InputStream in = proc.getInputStream();
        final BufferedReader buff = new BufferedReader(new InputStreamReader(in));

        try {
            return buff.readLine();
        } finally {
            buff.close();
            in.close();
        }
    }
}
