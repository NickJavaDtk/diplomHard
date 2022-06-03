import java.util.Map;

public class PageEntry implements Comparable<PageEntry> {
    protected Map<String, Integer> world;
    private final String pdfName;
    private final int page;
    private final int count;


    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public PageEntry(Map<String, Integer> word, String pdfName, int page, int count) {
        this.world = word;
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
        this.world = word;
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public Map<String, Integer> getWorld() {
        return world;
    }

    @Override
    public int compareTo(PageEntry o) {
        return -(count - o.count);
    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}';
    }

    // ???
}
