package com.reader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

public class BookHanlder {
	private String bookPath = "";
	private String encoding = "";
	private String novelName = "";
	private String chapter = "";
	private String content = "";
	private ArrayList<String> arrayChapter = null;
	private ArrayList<String> arrayContent = null;
	public static final String CHAPTER_PATTERN = "第([0-9〇一二三四五六七八九十零壹贰叁肆伍陆柒捌玖拾佰百千仟０１２３４５６７８９]+)[章草集][\\s]*(.*)";
	public static final String CHAPTER_PATTERN_2 = "([0-9〇一二三四五六七八九十零壹贰叁肆伍陆柒捌玖拾佰百千仟０１２３４５６７８９]+)[章草集][\\s]*(.*)";

	public BookHanlder(String bookPath) {
		setBookPath(bookPath);
		arrayChapter = new ArrayList<>();
		arrayContent = new ArrayList<>();
		handleBook();
	}

	public ArrayList<String> getArrayChapter() {
		return arrayChapter;
	}

	public ArrayList<String> getArrayContent() {
		return arrayContent;
	}

	public String getEncoding() {
		return encoding;
	}

	public void handleBook() {
		try {
			setEncoding(getCharset(getBookPath()));
			setNovelName(FilenameUtils.getBaseName(getBookPath()));
			
			retieveChapterAndContent();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void retieveChapterAndContent() throws FileNotFoundException {
		boolean isFirstTitle = true;
		String strCurrentContent = "";
		String strCurrentLine = "";
		InputStreamReader read;
		try {
			read = new InputStreamReader(new FileInputStream(getBookPath()), getEncoding());
			BufferedReader bufferedReader = new BufferedReader(read);
			while ((strCurrentLine = bufferedReader.readLine()) != null) {
				if (isChapter(strCurrentLine)) {
					if (!isFirstTitle) {
						arrayContent.add(strCurrentContent);
						strCurrentContent = "";
					}
					arrayChapter.add(getChapter());
					isFirstTitle = false;

				}

				if (strCurrentLine.contains("乱回身一掌对上辛寒，")) {
					strCurrentContent += "<p>" + strCurrentLine + "</br></p>";
				}
				if (!strCurrentLine.trim().equals("")) {
					strCurrentContent += "<p>" + strCurrentLine + "</br></p>";
				}

			}
			arrayContent.add(strCurrentContent);

			read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getCharset(String fileName) throws IOException {

		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
		int p = (bin.read() << 8) + bin.read();

		bin.close();
		String code = null;

		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		case 15677:
			code = "GBK";
			break;
		default:
			code = "GBK";
		}
		return code;
	}

	public void setBookPath(String bookPath) {
		this.bookPath = bookPath;
	}

	public String getBookPath() {
		return bookPath;
	}

	public boolean isChapter(String text) {

		Pattern r = Pattern.compile(CHAPTER_PATTERN);

		Matcher m = r.matcher(text);
		if (m.find()) {
			setChapter(m.group(0));
			return true;
			// } else {
			// r = Pattern.compile(CHAPTER_PATTERN_2);
			//
			// m = r.matcher(text);
			// if (m.find()) {
			// String chapter = m.group(0);
			// if (chapter.length() < 40 ) {
			// setChapter(chapter);
			// return true;
			// } else {
			// return false;
			// }
			//
			// } else {
			// return false;
			// }
			//
		}
		return false;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getChapter() {
		return chapter;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getNovelName() {
		return novelName;
	}

	public void setNovelName(String novelName) {
		if (novelName.contains(".")) {
			novelName = novelName.split("\\.")[0];
		}
		this.novelName = novelName;
	}

	public static void main(String[] args) {
		String bookPath = "/Users/youyuzui/Downloads/全宇宙都是我好友.txt";
		BookHanlder bookHanlder = new BookHanlder(bookPath);
		System.out.println(bookHanlder.getArrayChapter());

	}

}
