package com.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.reader.util.UnZipOrRarUtils;

public class DownloadHandler {

	private String downloadFile = "";
	public static final String DOWNLOAD_PATH = "res/download/";
	public static final String UNZIP_PATH = "res/download/unzip/";

	public DownloadHandler() {

		File file = new File(DOWNLOAD_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public String getDownloadFile() {
		return this.downloadFile;
	}

	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
	}

	public boolean httpDownload(String httpUrl) {

		String[] temp = httpUrl.split("/");
		String saveFile = DOWNLOAD_PATH + File.separator + temp[temp.length - 1];
		setDownloadFile(saveFile);
		if (FileUtils.getFile(saveFile).exists()) {
			System.out.println("no need download.");
			return true;
		}

		return httpDownload(httpUrl, getDownloadFile());
	}

	/** http���� */
	@SuppressWarnings("resource")
	public boolean httpDownload(String httpUrl, String saveFile) {
		// ���������ļ�
		// int bytesum = 0;
		int byteread = 0;

		URL url = null;
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		try {
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			int maxLength = conn.getContentLength();
			FileOutputStream fs = new FileOutputStream(saveFile);

			byte[] buffer = new byte[1204];

			NumberFormat nf = NumberFormat.getPercentInstance();
			nf.setMaximumFractionDigits(1);
			int bytesum = 0;
			System.out.println("Start download from [" + httpUrl + "]");
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);

//				System.out.println("Download: " + nf.format((float) bytesum / (float) maxLength));
			}
			System.out.println("Download successfully. File: [" + saveFile + "]");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public ArrayList<String> dispose() {

		if (this.getDownloadFile().contains(".txt")) {
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add(this.getDownloadFile());
			return arrayList;
		} else if (this.getDownloadFile().contains(".rar")) {

			return UnZipOrRarUtils.unRarFile(this.getDownloadFile(), UNZIP_PATH);

		} else {
			System.out.println("NO IMPLEMENT for " + this.getDownloadFile().split("\\.")[1]);
			return null;
		}

	}

}