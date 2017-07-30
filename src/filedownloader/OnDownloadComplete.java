package filedownloader;

import java.util.ArrayList;

/**
 * call to caller once download for perticular url is finished
 *
 * @author rohitkhirid
 *
 */
public interface OnDownloadComplete {
	void onDownloadComplete(ArrayList<String> filePaths);
}
