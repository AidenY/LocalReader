package com.reader.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static void createNewFileIfNotExist(String filePath) {
		if (!isFileExist(filePath)) {
			try {
				File file = new File(filePath);
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void appendStr(String filePath, String wStr) {
		write(filePath, wStr, true);
	}

	public static void overrideStr(String filePath, String wStr) {
		write(filePath, wStr, false);
	}

	public static void write(String filePath, String wStr, boolean isAppend) {
		try {
			createNewFileIfNotExist(filePath);
			FileWriter fw = new FileWriter(filePath, isAppend);
			fw.write(wStr);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getAllContent(String filePath, String lineBreakTag) {
		String fileContent = "";
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String temp;

			temp = br.readLine();

			while (temp != null) {
				fileContent += temp + lineBreakTag;
				temp = br.readLine();
			}
			br.close();
			fr.close();
			if ("".equals(lineBreakTag)) {
				return fileContent;
			}
			return fileContent.substring(0, fileContent.length() - 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContent;
	}

	public static String getAllContent(String filePath) {
		return getAllContent(filePath, "");
	}

	public static void mkdir(String strFolderPath) {
		File file = new File(strFolderPath);
		file.mkdirs();
	}

	public static boolean rename(String strFilepath, String strRenameFilePath) {
		boolean isRename = false;
		String strAll = "";
		if (isFileExist(strFilepath)) {
			strAll = getAllContent(strFilepath, "\n");
		}

		write(strRenameFilePath, strAll, false);
		if (new File(strRenameFilePath).exists()) {
			new File(strFilepath).delete();
			isRename = true;
		}
		return isRename;

	}

	public static String getParentPath(String strFilePath) {
		File file = new File(strFilePath);
		return file.getParent();
	}

	public static String getFileName(String strFilePath) {
		File file = new File(strFilePath);
		return file.getName();
	}

	public static String createNewDirIfNotExist(String filePath) {
		if (!isFileExist(filePath)) {
			File file = new File(filePath);
			file.mkdirs();
			return file.getAbsolutePath();
		} else {
			return (new File(filePath)).getAbsolutePath();
		}

	}

	public static void main(String[] args) {
		FileUtil.moveToBackupForHtml("TestReport");
	}

	public static void moveToBackupForHtml(String dir) {
		File file = new File(dir);
		String[] listFiles = file.list();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].contains("html")) {
				String backupDir = dir + File.separator + "OldResults/";
				FileUtil.createNewDirIfNotExist(backupDir);
				FileUtil.rename(dir + File.separator + listFiles[i], backupDir + listFiles[i]);
				System.out.println("Move file [" + listFiles[i] + "]");
			}
		}

	}

	public static void copyFile(File source, File targetFile) {
		int byteread = 0; // 读取的字节数
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(targetFile);
			byte[] buffer = new byte[1024];

			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
