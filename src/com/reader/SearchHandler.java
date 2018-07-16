package com.reader;

import com.reader.util.HttpClientUtil;
import com.reader.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class SearchHandler {

    private String searchWeb = "http://www.zxcs8.com/";

    public String getSearchWeb() {
        return searchWeb;
    }

    public String findBook(String bookName) throws IOException {
        String bookLink = findBookFromZxcs(bookName);
        System.out.println("Find the book [" + bookName + "] link = [" + bookLink + "]");
        return bookLink;
    }

    private String findBookFromZxcs(String bookName) throws IOException {
        String searchPageSource = HttpClientUtil.httpGet("http://www.zxcs8.com/index.php?keyword=" + bookName);
        AtomicReference<String> bookPageLink = new AtomicReference<>("");
        Document docuemnt = Jsoup.parse(searchPageSource);
        Elements aElements = docuemnt.getElementsByTag("a");
        aElements.forEach(element -> {
            if (element.text().contains(bookName)) {
                bookPageLink.set(element.attr("href"));
            }
        });

        String[] arr = bookPageLink.get().split(File.separator);
        String bookId = arr[arr.length - 1];
        String detailDownloadPage = "http://www.zxcs8.com/download.php?id=" + bookId;

        String result = HttpClientUtil.httpGet(detailDownloadPage);
        docuemnt = Jsoup.parse(result);
        Elements elements = docuemnt.getElementsMatchingText("线路一");
        AtomicReference<String> downloadLink = new AtomicReference<>("");
        elements.forEach(element -> {
            if (element.tag().toString().equals("a")) {
                downloadLink.set(element.attr("href"));
            }
        });

        return downloadLink.get();
        // System.err.println(result);

    }

    public ArrayList<String> findBooks(Integer bookNumber) throws IOException {
        ArrayList<String> bookNames = new ArrayList<>();
        String url = getSearchWeb() + "?plugin=TTS_map&security_verify_data=313638302c31303530";
        String result = HttpClientUtil.httpGet(url);
        Document doc = Jsoup.parse(result);
        Element elementId = doc.getElementById("content");
        Elements elementLis = elementId.getElementsByTag("li");
        if (bookNumber > elementLis.size()) bookNumber = elementLis.size();
        for (int i = 0; i < bookNumber; i++) {
            Element element = elementLis.get(i);
            Element aElement = element.getElementsByTag("a").get(0);
            String bookName = aElement.text();
            bookName =StringUtils.getMatchedStr(bookName,"《(.*)》");

            bookNames.add(bookName);
        }
        return bookNames;
    }
}
