package filedownloader;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import utils.Constants;

/**
 * downloads file and saves it on the storage
 * 
 * @author rohitkhirid
 *
 */
public class FileDownloader {
	private ArrayList<String> mUrls;
	private OnDownloadComplete mOnDownloadComplete;

	private ArrayList<String> mFilePaths;

	private Scanner mScanner;

	public FileDownloader(ArrayList<String> urls, OnDownloadComplete onDownloadComplete) {
		mUrls = urls;
		mOnDownloadComplete = onDownloadComplete;
	}

	public void run() {
		try {
			for (String urlString : mUrls) {
				System.out.println("strating download for : " + urlString);
				String fileName = getFileName(urlString);
				URL url = new URL(urlString);
				mScanner = new Scanner(url.openStream());
				PrintWriter writer = new PrintWriter(fileName, "UTF-8");
				while (mScanner.hasNextLine()) {
					writer.append(mScanner.nextLine() + "\n");
				}
				writer.close();
				System.out.println("download finished for : " + urlString);
				if (mFilePaths == null) {
					mFilePaths = new ArrayList<>();
				}
				mFilePaths.add(fileName);

				if (Constants.IS_DEVELOPEMENT_ON) {
					break;
				}
			}
			System.out.println("files downloaded.");
			if (mOnDownloadComplete != null) {
				mOnDownloadComplete.onDownloadComplete(mFilePaths);
			} else {
				System.out.println("cant give callback, as interface is not present!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets filename for file with {@link Constants#SELECTED_CRITERIAS} and
	 * {@link Constants#SELECTED_REGIONS} in it
	 * 
	 * if for soem reason valid fileName is not found then gives temp_0.txt,
	 * temp_1.txt...
	 * 
	 * @param url
	 * @return
	 */
	private String getFileName(String url) {
		String fileName = null;
		for (String criteria : Constants.SELECTED_CRITERIAS) {
			int index = url.indexOf(criteria);
			if (index != -1) {
				fileName = url.substring(index, url.length());
				break;
			}
		}

		if (fileName != null) {
			fileName = fileName.replace("/", "_");
			fileName = Constants.DIRECTORY_PATH_INPUT + fileName;
		} else {
			System.out.println("some weird fileName");
			fileName = "temp_" + Constants.COUNTER + ".txt";
			Constants.COUNTER++;
		}

		System.out.println("Filename : " + fileName);
		return fileName;
	}
}
