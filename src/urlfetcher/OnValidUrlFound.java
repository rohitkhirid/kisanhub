package urlfetcher;

import java.util.ArrayList;

/**
 * called by {@link UrlFetcher} after finding valid url
 * 
 * implemented by {@link KisanHub}
 * 
 * @author rohitkhirid
 *
 */
public interface OnValidUrlFound {
	void onValidUrlFound(ArrayList<String> urls);
}
