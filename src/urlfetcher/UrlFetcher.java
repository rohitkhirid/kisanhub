package urlfetcher;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.Constants;
import utils.Utils;

/**
 * fetches data in text format from given URL
 * 
 * @author rohitkhirid
 *
 */
public class UrlFetcher {

	private String mUrl;
	private OnValidUrlFound mOnFetchingComplete;

	private Elements mElements = null;
	private ArrayList<String> mValidUrls = null;

	public UrlFetcher(String url, OnValidUrlFound onFetchingComplete) {
		mUrl = url;
		mOnFetchingComplete = onFetchingComplete;
	}

	/**
	 * starts the reursive call to {@link UrlFetcher#fetchUrls(String)} and at
	 * the end gives callback to caller by interface {@link OnValidUrlFound}
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {
		System.out.println("starting crawling");
		fetchUrls(mUrl);
		if (Utils.isEmpty(mValidUrls)) {
			System.out.println("fetchign compeleted, found " + mValidUrls.size() + " links in crawling");
		}
		
		if (mOnFetchingComplete != null) {
			System.out.println("giving callback");
			mOnFetchingComplete.onValidUrlFound(mValidUrls);
		}
	}

	/**
	 * recursive function
	 * 
	 * finds all href links from given page checks if founded links on page are
	 * satisfying given conditions.
	 * 
	 * if yes storing them in list
	 * 
	 * @param URL
	 * @throws IOException
	 */
	public void fetchUrls(String URL) throws IOException {
		Document doc = Jsoup.connect(URL).get();
		mElements = doc.select("a[href]");
		for (Element link : mElements) {
			if (link.attr("href").contains(Constants.BASE_URL) && isUrlValid(link.attr("href"))) {
				try {
					addUrl(link.attr("abs:href"));
					fetchUrls(link.attr("abs:href"));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			} else {
				continue;
			}
		}

	}

	/**
	 * adds url to list
	 * 
	 * checks if url is already not added and is non-empty, then only adds, else
	 * skips it.
	 * 
	 * @param url
	 */
	private void addUrl(String url) {
		if (mValidUrls == null) {
			mValidUrls = new ArrayList<>();
		}

		if (!Utils.isEmpty(url) && !mValidUrls.contains(url)) {
			mValidUrls.add(url);
		} else {
			System.out.println("not adding " + url + " to list");
		}
	}

	// TODO : this function has 4 nested for loops, first thing in version-1,
	// optimize this function :-(
	/**
	 * checks if url is relevent for given list of regions and criterias
	 * 
	 * @param url
	 * @return
	 */
	private boolean isUrlValid(String url) {
		boolean isReleavent = false;

		for (String region : Constants.SELECTED_REGIONS) {
			if (url.contains(region)) {
				for (String criteria : Constants.SELECTED_CRITERIAS) {
					if (url.contains(criteria)) {
						for (String stasticsType : Constants.SELECTED_STATISTICS_TYPE) {
							if (url.contains(stasticsType)) {
								for (String unSupportedChar : Constants.UNSUPPORTED_SUBSTRINGS) {
									if (!url.contains(unSupportedChar)) {
										System.out.println("url valid : " + url);
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return isReleavent;
	}
}
