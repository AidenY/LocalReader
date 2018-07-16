package com.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BookApp {
    private SearchHandler searchHandler = null;

    public BookApp() {
        searchHandler = new SearchHandler();

    }

    public static void main(String[] args) throws Exception {

        BookApp app = new BookApp();
//        String bookName = "混元剑帝";
//        app.downloadBook(bookName);
        app.downloadBooks(30);
//		app.downloadUrl("http://d3.zxcs1.xyz/201802/gnwz,wjbdd.rar");

    }

    private void downloadBooks(Integer bookNumber) throws IOException {
        ArrayList<String> bookNames = searchHandler.findBooks(bookNumber);
        bookNames.forEach(bookName -> {
            try {
                this.downloadBook(bookName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void downloadBook(String bookName) throws Exception {
        String downloadLink = searchHandler.findBook(bookName);
        this.downloadUrl(downloadLink);

    }

    private void downloadUrl(String httpUrl) throws Exception {
        DownloadHandler downloadFiles = new DownloadHandler();
        downloadFiles.httpDownload(httpUrl);
        ArrayList<String> array = downloadFiles.dispose();
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                String file = array.get(i);
                if (file.contains(".txt") && !file.toLowerCase().contains("readme") && !file.contains("˵��")) {
                    BookHtmlHandler txtToHtml = new BookHtmlHandler();
                    txtToHtml.generateHtml(new File(file));
                }
            }
        }

    }

    public void generateHtml(String path) throws Exception {
        BookHtmlHandler txtToHtml = new BookHtmlHandler();
        txtToHtml.generateHtml(new File(path));

    }

}
