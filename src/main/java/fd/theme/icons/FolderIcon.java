package fd.theme.icons;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import fd.theme.IconTheme;

public final class FolderIcon implements Icon {

    private static final Set<FolderIcon> VALUES = new HashSet<FolderIcon>();

    public static final FolderIcon CUT                 = new FolderIcon("cut");
    public static final FolderIcon FILEOPEN            = new FolderIcon("fileopen");
    public static final FolderIcon FOLDER              = new FolderIcon("folder");
    public static final FolderIcon FOLDER_DOCUMENTS    = new FolderIcon("folder-documents");
    public static final FolderIcon FOLDER_DOWNLOAD     = new FolderIcon("folder-download");
    public static final FolderIcon FOLDER_MOVE         = new FolderIcon("folder-move");
    public static final FolderIcon FOLDER_MUSIC        = new FolderIcon("folder-music");
    public static final FolderIcon FOLDER_OPEN         = new FolderIcon("folder-open");
    public static final FolderIcon FOLDER_PICTURES     = new FolderIcon("folder-pictures");
    public static final FolderIcon FOLDER_PUBLICSHARE  = new FolderIcon("folder-publicshare");
    public static final FolderIcon FOLDER_RECENT       = new FolderIcon("folder-recent");
    public static final FolderIcon FOLDER_SAVED_SEARCH = new FolderIcon("folder-saved-search");
    public static final FolderIcon FOLDER_SYSTEM       = new FolderIcon("folder-system");
    public static final FolderIcon FOLDER_TEMPLATES    = new FolderIcon("folder-templates");
    public static final FolderIcon FOLDER_VIDEO        = new FolderIcon("folder-video");
    public static final FolderIcon NETWORK             = new FolderIcon("network");
    public static final FolderIcon USER_HOME           = new FolderIcon("user-home");
    public static final FolderIcon USER_DESKTOP        = new FolderIcon("user-desktop");

    private static String globalColor = "";

    private final String name;
    private String color = "";

    private FolderIcon(String name) {
        VALUES.add(this);
        this.name = name;
    }

    public static void main(String[] args) throws IOException {
        IconTheme t = IconTheme.getCurrentTheme();
        for (FolderIcon i : FolderIcon.values()) {
            File f = t.findIcon(i.getName());
            System.out.println(f);
        }
        for (FolderColor c : FolderColor.values()) {
            FolderIcon.setGlobalColor(c);
            for (FolderIcon i : FolderIcon.values()) {
                File f = t.findIcon(i.getName());
                System.out.println(f);
            }
        }
    }

    @Override
    public String getName() {
        return (color.isEmpty() ? globalColor : color) + name;
    }

    public void setColor(FolderColor color) {
        this.color = color.getName() + "-";
    }

    public static void setGlobalColor(FolderColor color) {
        globalColor = color.getName() + "-";
    }

    public static FolderIcon[] values() {
        return VALUES.toArray(new FolderIcon[VALUES.size()]);
    }
}
