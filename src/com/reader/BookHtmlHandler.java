package com.reader;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class BookHtmlHandler {
    private static final String RES_FOLDER = "res/";
    private static final String CONTENTS_FOLDER = "catalogue";
    private static final String CHAPTER_FOLDER = "catalogue/chapters";

    private BookHanlder book = null;

    public BookHtmlHandler() {
        if (!new File(RES_FOLDER + CONTENTS_FOLDER).exists()) {
            new File(RES_FOLDER + CONTENTS_FOLDER).mkdirs();
        }
        if (!new File(RES_FOLDER + CHAPTER_FOLDER).exists()) {
            new File(RES_FOLDER + CHAPTER_FOLDER).mkdirs();
        }

    }

    public void generateHtml(String fileName) throws Exception {
        this.generateHtml(new File(fileName));
    }

    public void generateHtml(File file) throws Exception {
        book = new BookHanlder(file.getAbsolutePath());
        generateDetails(book);
        generateCatalog(book);
        System.out.println("Generate Successful for " + book.getNovelName());
    }

    // Create all chapters's html file
    private void generateDetails(BookHanlder book) throws Exception {

        ArrayList<String> arrContent = book.getArrayContent();
        ArrayList<String> arrChapter = book.getArrayChapter();
        for (int i = 0; i < arrChapter.size(); i++) {
            // System.out.println("Current Title: " + arrChapter.get(i));
            writeContent(arrContent.get(i), arrChapter, i, book);
        }
    }

    // It will write the current chapter into a html file
    private void writeContent(String bodyContent, ArrayList<String> arrayChapter, int currentIndex, BookHanlder book) throws Exception {
        String lastPageHtml = "";
        String nextPageHtml = "";
        if (currentIndex != 0) {
            String lastChapter = book.getNovelName() + "-" + arrayChapter.get(currentIndex - 1);
            lastPageHtml = "<td><a id='previous' href='./" + lastChapter + ".html'>上一页</a></td>";
        }

        String currentChapter = book.getNovelName() + "-" + arrayChapter.get(currentIndex);
        if (currentIndex != arrayChapter.size() - 1) {
            String nextChapter = book.getNovelName() + "-" + arrayChapter.get(currentIndex + 1);
            nextPageHtml = "<td><a id='next' href='./" + nextChapter + ".html'>下一页</a></td>";
        }

        String script = "<script type=\"text/javascript\" language=JavaScript charset=\"UTF-8\">document.onkeydown=function(event){	var e = event || window.event || arguments.callee.caller.arguments[0];	if(e.keyCode==37) 	{ 	document.getElementById(\"previous\").click(); 	return false; 	} else if(e.keyCode == 39){ document.getElementById(\"next\").click(); 	return false; 	}	} </script>";
        String pageContent = "<html><head>" + "<style type='text/css'>" + "*{font-family: 'ABeeZee' !important;}"
                + ".text-wrap{min-height: 600px;margin-bottom: 24px;border: 1px solid #d8d8d8;background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyAgMAAABjUWAiAAAACVBMVEX28ef48+n69esoK7jYAAAB4UlEQVQozw2OsW4bQQxEhwLXkDrysGdEqRRgVShfQQq8wOr2jD0jSpXCLvwXbtKfADlFqgSwC/9ljqweZgYzQFnb/QGepYhA9jzmTc1WaSEtQpbFgjWATI00ZZtIckXx8q2Oe5yEByBy+RHOTcM+VVTadULsvxvRC/q8WTwgcWGD+Mnaqa0oy2gw2pKFzK+PzEsus5hP9AHojKslVynLlioVTBEN8cjDNnZoR1uMGTiZAAN47HxMtEkGUE9b8HWzkqNX5Lpk0yVziAJOs46rK1pG/xNuXLjz95fSDoJE5IqG23MAYPtWoeWPvfVtIV/Ng9oH3W0gGMPIOqd4MK4QZ55dV61gOb8Zxp7I9qayaGxp6Q91cmC0ZRdBwEQVHWzSAanlZwVWc9yljeTCeaHjBVvlPSLeyeBUT2rPdJegQI103jVS3uYkyIx1il6mslMDedZuOkwzolsagvPuQAfp7cYg7k9V1NOxfq64PNSvMdwONV4VYEmqlbpZy5OAakRKkjPnL4CBv5/OZRgoWHBmNbxB0LgB1I4vXFj93UoF2/0TPEsWwV9EhbIiTPqYoTHYoMn3enTDjmrFeDTIzaL1bUC/PBIMuF+vSSYSaxoVt90EO3Gu1zrMuMRGUk7Ffv3L+A931Gsb/yBoIgAAAABJRU5ErkJggg==);padding: 60px 64px;}"
                + "p{display: block;-webkit-margin-before: 1em;-webkit-margin-after: 1em;-webkit-margin-start: 0px;-webkit-margin-end: 0px;font-size: 18px;color: #262626;}" + "</style>" + script + "<meta http-equiv='content-type' content='text/html;charset=utf-8'>" + "</head><body class='text-wrap' bgcolor='#e6f3ff'  style='width: 80%;margin-left: 150px;'>" + bodyContent + "</br>" + "<table align='center'>" + "<tr>" + lastPageHtml + "<td><a href='../" + book.getNovelName() + " - contents.html'>目录</a></td>" + nextPageHtml + "</tr>" + "</table>" + "</body></html>";
        if (currentChapter.length() > 50) {
            currentChapter = UUID.randomUUID().toString();
        }
        String filePath = RES_FOLDER + CHAPTER_FOLDER + File.separator + currentChapter.replace(File.separator, "") + ".html";

        // PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath), "UTF-8"));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
        out.print(pageContent);
        out.flush();
        out.close();
    }

    // Create a html file contain chapter's reference.
    private void generateCatalog(BookHanlder book) throws Exception {
        String pageContent = "<html><head>" + "<meta http-equiv='content-type' content='text/html;charset=utf-8'>" + "</head><body bgcolor='#e6f3ff'>" + "<table align='center' width='75%' border=1>" + "<tr align='center'>";
        ArrayList<String> arrayList = book.getArrayChapter();
        for (int i = 0; i < arrayList.size(); i++) {
            String item = arrayList.get(i);
            pageContent += "<td width=33% color='green'><a href='../" + CHAPTER_FOLDER + File.separator + book.getNovelName() + "-" + item + ".html" + "'>" + item + "</a></td>";
            if ((i + 1) % 3 == 0) {
                pageContent += "</tr><tr align='center'>";
            }
        }
        pageContent += "</table></body></html>";

        String name = RES_FOLDER + CONTENTS_FOLDER + File.separator + book.getNovelName() + " - contents.html";
        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(name), "UTF-8"));
        out.print(pageContent);
        out.flush();
        out.close();
        System.out.println(name);

    }

    public static void main(String[] args) throws Exception {
        BookHtmlHandler txtToHtml = new BookHtmlHandler();
        String bookPath = "/Users/youyuzui/Downloads/全宇宙都是我好友.txt";
        File file = new File(bookPath);
        txtToHtml.generateHtml(file);
    }

}
