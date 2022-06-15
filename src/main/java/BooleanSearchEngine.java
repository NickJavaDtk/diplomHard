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
    private Map<String, List<PageEntry>> resultMap;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы


        resultMap = new HashMap<>( );
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
                for (Map.Entry<String, Integer> mapEntry : freqs.entrySet( )) {
                    String tmpString = mapEntry.getKey( );
                    int tmpInt = mapEntry.getValue( );
                    PageEntry pageTmp = new PageEntry(file.getName( ), i, tmpInt);
                    pageEntryList = new ArrayList<>( );
                    pageEntryList.add(pageTmp);
                    if (resultMap.isEmpty( )) {
                        resultMap.put(tmpString, pageEntryList);
                    } else if (!resultMap.containsKey(tmpString)) {
                        resultMap.put(tmpString, pageEntryList);
                    } else {
                        for (Map.Entry<String, List<PageEntry>> mapPageEntry : resultMap.entrySet( )) {
                            if (tmpString.equals(mapPageEntry.getKey( ))) {
                                List<PageEntry> page = new ArrayList<>(mapPageEntry.getValue( ));
                                page.add(pageTmp);
                                resultMap.put(tmpString, page);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову

        List<PageEntry> sortPage = new ArrayList<>( );//
        for (Map.Entry<String, List<PageEntry>> entryMap : resultMap.entrySet( )) {
            if (word.equals(entryMap.getKey( ))) {
                sortPage = entryMap.getValue( );
            }
        }
        Collections.sort(sortPage);
        return sortPage;
    }


}
