package fd.theme;

final class DirectoryInfo {

    private final int size;
    private final int scale;
    private final String context;
    private final String type;
    private final int maxSize;
    private final int minSize;
    private final int threshold;

    public DirectoryInfo(String size, String scale, String context, String type, String maxSize, String minSize, String threshold) {
        this.size = Integer.parseInt(size);
        this.scale = Integer.parseInt(scale);
        this.context = context;
        this.type = type;
        this.maxSize = Integer.parseInt(maxSize);
        this.minSize = Integer.parseInt(minSize);
        this.threshold = Integer.parseInt(threshold);
    }

    public int getSize() {
        return size;
    }

    public int getScale() {
        return scale;
    }

    public String getContext() {
        return context;
    }

    public String getType() {
        return type;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getMinSize() {
        return minSize;
    }

    public int getThreshold() {
        return threshold;
    }
}
