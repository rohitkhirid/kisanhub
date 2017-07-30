package utils;

import java.io.File;
import java.util.ArrayList;

/**
 * common utility functions
 * 
 * @author rohitkhirid
 *
 */
public class Utils {

	/**
	 * checks given string is non-null and non-empty
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		boolean isEmpty = true;
		if (string != null) {
			if (string.length() > 0) {
				isEmpty = false;
			}
		}
		return isEmpty;
	}

	/**
	 * checks if list is non-null and non-empty
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(ArrayList list) {
		boolean isEmpty = true;
		if (list != null) {
			if (list.size() > 0) {
				isEmpty = false;
			}
		}
		return isEmpty;
	}

	/**
	 * created directory at {@link Constants#DIRECTORY_PATH_INPUT}
	 * 
	 * deletes files inside the directory.
	 * 
	 * use to fresh start the process of downloading.
	 * 
	 * ideally should be called from main()
	 */
	public static void initInputDirectory() {
		File directory = new File(Constants.DIRECTORY_PATH_INPUT);
		if (!directory.exists()) {
			System.out.println("directory not present, creating new one");
			directory.mkdir();
		} else {
			System.out.println("directory present, deleting contents");
			for (File file : directory.listFiles()) {
				file.delete();
			}
		}
	}

	/**
	 * created directory at {@link Constants#DIRECTORY_PATH_OUTPUT}
	 * 
	 * deletes files inside the directory.
	 * 
	 * use to fresh start the process of writing output
	 * 
	 * ideally should be called from main()
	 */
	public static void initOutputDirectory() {
		File directory = new File(Constants.DIRECTORY_PATH_OUTPUT);
		if (!directory.exists()) {
			System.out.println("directory not present, creating new one");
			directory.mkdir();
		} else {
			System.out.println("directory present, deleting contents");
			for (File file : directory.listFiles()) {
				file.delete();
			}
		}
	}
}
