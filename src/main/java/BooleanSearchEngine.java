import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    //???
    private Map<String, Integer> freqs;
    private List<PageEntry> pageEntryList;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы

        pageEntryList = new ArrayList<>( );
        File[] filePDF = pdfsDir.listFiles( );
        for (File file : filePDF) {
            PdfDocument doc = new PdfDocument(new PdfReader(file));
            int countPage = doc.getNumberOfPages( );
            for (int i = 1; i < countPage; i++) {
                freqs = new HashMap<>( );
                String text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                String[] words = text.split("\\P{IsAlphabetic}+");
                for (String word : words) {
                    if (word.isEmpty( )) {
                        continue;
                    }
                    freqs.put(word.toLowerCase( ), freqs.getOrDefault(word.toLowerCase( ), 0) + 1);
                }
                pageEntryList.add(new PageEntry(freqs, file.getName( ), i, 0));

            }

        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову

        List<PageEntry> sortPage = new ArrayList<>( );
        for (PageEntry wordPDF : pageEntryList) {
            Map<String, Integer> map = wordPDF.getWorld( );
            for (Map.Entry<String, Integer> mapEntry : map.entrySet( )) {
                if (word.equals(mapEntry.getKey( ))) {
                    int count = mapEntry.getValue( );
                    sortPage.add(new PageEntry(wordPDF.getPdfName( ), wordPDF.getPage( ), count));
                }
            }

        }

        Collections.sort(sortPage);
        return sortPage;
    }


}
