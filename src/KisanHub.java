import java.util.ArrayList;

import filedownloader.FileDownloader;
import filedownloader.OnDownloadComplete;
import filewriter.FileWriter;
import filewriter.OnFileWriteComplete;
import urlfetcher.UrlFetcher;
import urlfetcher.OnValidUrlFound;
import utils.Constants;
import utils.Utils;

/**
 * main class, everything starts from here :)
 * 
 * step 1 : load all valid urls from given link with help of {@link UrlFetcher}.
 * step 2 : downloads files from urls which are valid {@link FileDownloader}.
 * step 3 : creates weather.csv file and writes data in expected format from all
 * downloaded files {@link FileWriter}.
 * 
 * @author rohitkhirid
 *
 */
public class KisanHub {
	private UrlFetcher mDataFetcher;

	private OnValidUrlFound mOnFetchingComplete = new OnValidUrlFound() {
		@Override
		public void onValidUrlFound(ArrayList<String> urls) {
			if (!Utils.isEmpty(urls)) {
				System.out.println(urls.size() + " valid urls found, starting to fetch actual data");
				FileDownloader downloader = new FileDownloader(urls, mOnDownloadComplete);
				downloader.run();
			} else {
				System.out.println("no valid urls, have to terminate");
			}
		}
	};

	private OnDownloadComplete mOnDownloadComplete = new OnDownloadComplete() {
		@Override
		public void onDownloadComplete(ArrayList<String> filePaths) {
			if (!Utils.isEmpty(filePaths)) {
				System.out.println("Writing downloaded files in weather.csv");
				FileWriter fileWriter = new FileWriter(filePaths, mOnFileWriteComplete);
				fileWriter.run();
			}
		}
	};

	private OnFileWriteComplete mOnFileWriteComplete = new OnFileWriteComplete() {
		@Override
		public void onFileWriteComplete() {
			System.out.println("output file is ready");
		}
	};

	public static void main(String[] args) {
		KisanHub kisanHub = new KisanHub();

		try {
			Utils.initInputDirectory();
			Utils.initOutputDirectory();
		} catch (Exception e) {
			System.out.println("crash while init directory");
			e.printStackTrace();
		}

		try {
			kisanHub.mDataFetcher.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public KisanHub() {
		mDataFetcher = new UrlFetcher(Constants.BASE_URL + Constants.PAGE_ENDPOINT, mOnFetchingComplete);
	}
}
