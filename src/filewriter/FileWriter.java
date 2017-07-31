package filewriter;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import utils.Constants;
import utils.Utils;

/**
 * writes contents of file from {@link Constants#DIRECTORY_PATH_INPUT} to file
 * at {@link Constants#DIRECTORY_PATH_OUTPUT}
 * 
 * @author rohitkhirid
 *
 */
public class FileWriter {
	private ArrayList<String> mFilePaths;
	private OnFileWriteComplete mOnFileWriteComplete;

	public FileWriter(ArrayList<String> filePaths, OnFileWriteComplete onFileWriteComplete) {
		mFilePaths = filePaths;
		mOnFileWriteComplete = onFileWriteComplete;
	}

	public void run() {
		if (!Utils.isEmpty(mFilePaths)) {
			try {
				PrintWriter writer = new PrintWriter(getOutputFile(), "UTF-8");
				writer.write("region_code, weather_param, year, key, value\n");
				for (String filePath : mFilePaths) {
					System.out.println("reading file " + filePath);
					File file = new File(filePath);

					FileReader fileReader = new FileReader(file);
					Scanner scanner = new Scanner(fileReader);

					// we skip first 8 lines as they are description about the
					// file
					// TODO : this is a hack, if someday the data provider
					// changes the way in which they
					// write files then this logic will no longer work
					for (int i = 0; i < 8; i++) {
						scanner.nextLine();
					}

					String regionCode = getRegionCode(filePath);
					String weatherParam = getWeatherParam(filePath);

					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						processLineAndWriteToFile(writer, regionCode, weatherParam, line);
					}
					scanner.close();

					if (Constants.IS_DEVELOPEMENT_ON) {
						break;
					}
				}
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("writing to output file done.");
			if (mOnFileWriteComplete != null) {
				mOnFileWriteComplete.onFileWriteComplete();
			} else {
				System.out.println("cant give callback, as interface is not present!");
			}
		}
	}

	/**
	 * removes first four characters of file (which are for year) and then split
	 * line into subStirng of length
	 * 
	 * then pass splitted substrings to
	 * {@link FileWriter#writeToFile(PrintWriter, String, String, String, String, String)}
	 * 
	 * @param writer
	 * @param regionCode
	 * @param weatherParam
	 * @param lineWithYear
	 */
	private void processLineAndWriteToFile(PrintWriter writer, String regionCode, String weatherParam,
			String lineWithYear) {
		String year = lineWithYear.substring(0, 4);
		String lineWithoutYear = lineWithYear.substring(4, lineWithYear.length());
		ArrayList<String> values = splitStringInFixedSizeChunks(lineWithoutYear, Constants.LENGTH_OF_CHUNK, 12);
		for (int i = 0; i < values.size(); i++) {
			writeToFile(writer, regionCode, weatherParam, year, getKeyForIndex(i), values.get(i));
		}
	}

	/**
	 * gets month from index passed example 0 for JAN, 1 for FEB..
	 * 
	 * @param index
	 * @return
	 */
	private String getKeyForIndex(int index) {
		String keyString = Constants.NA_PLACEHOLDER;
		try {
			keyString = Constants.MONTHS_LIST.get(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyString;
	}

	/**
	 * before writing to file it preprocesses value, removes empty places from
	 * value and inits it with {@link Constants#NA_PLACEHOLDER} if it empty
	 * 
	 * writes data in expected format to {@link Constants#OUTPUT_FILE_NAME}
	 * 
	 * @param writer
	 * @param regionCode
	 * @param weatherParam
	 * @param year
	 * @param key
	 * @param value
	 */
	private void writeToFile(PrintWriter writer, String regionCode, String weatherParam, String year, String key,
			String value) {
		value = value.replace(" ", "");
		if (Utils.isEmpty(value)) {
			value = Constants.NA_PLACEHOLDER;
		}

		writer.append(regionCode + Constants.SEPARATOR + weatherParam + Constants.SEPARATOR + year + Constants.SEPARATOR
				+ key + Constants.SEPARATOR + value + "\n");
	}

	/**
	 * given an line splits it to lengthOfChunk provided while storing
	 * subStrings takes maxSubStringsToTake into consideration
	 * 
	 * for example if maxSubStringsToTake = 3, then array of 3 substrings is
	 * returned and rest substirngs after that are ignored
	 * 
	 * @param string
	 * @param lengthOfChunk
	 * @param maxSubStringsToTake
	 * @return
	 */
	private ArrayList<String> splitStringInFixedSizeChunks(String string, int lengthOfChunk, int maxSubStringsToTake) {
		ArrayList<String> subStrings = null;
		int j = 0;
		for (int i = 0; i < string.length() && j < maxSubStringsToTake; i += lengthOfChunk) {
			j++;
			int endIndex = i + lengthOfChunk;
			if (endIndex > string.length() - 1) {
				endIndex = string.length();
			}
			String subString = string.substring(i, endIndex);
			if (subStrings == null) {
				subStrings = new ArrayList<>();
			}
			subStrings.add(subString);
		}
		return subStrings;
	}

	/**
	 * removes filePath then removes extension and send string array by
	 * splitting with "_"
	 * 
	 * example if filePath is src/downloadedfiles/Tmax_date_Scotland.txt then
	 * returns {"Tmax", "date", "Scotland"}
	 * 
	 * @param filePath
	 * @return
	 */
	private String[] getSubStringsFromFileName(String filePath) {
		String fileName = filePath.substring(Constants.DIRECTORY_PATH_INPUT.length(), filePath.length());
		String fileNameWithoutExtension = fileName.substring(0, fileName.indexOf("."));
		return fileNameWithoutExtension.split("_");
	}

	/**
	 * returns weatherParam from fileName example if fileName is
	 * Rainfall_date_England.txt then removes directory path
	 * {@link Constants#DIRECTORY_PATH_INPUT} and splits fileName with '_' and
	 * then returns 0th string from string array
	 * 
	 * @param filePath
	 * @return
	 */
	private String getWeatherParam(String filePath) {
		String regionCode = Constants.NA_PLACEHOLDER;
		try {
			String[] subStrings = getSubStringsFromFileName(filePath);
			regionCode = subStrings[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regionCode;
	}

	/**
	 * returns regionCode from fileName example if fileName is
	 * Rainfall_date_England.txt then removes directory path
	 * {@link Constants#DIRECTORY_PATH_INPUT} and splits fileName with '_' and
	 * then returns 2nd string from string array
	 * 
	 * @param filePath
	 * @return
	 */
	private String getRegionCode(String filePath) {
		String regionCode = Constants.NA_PLACEHOLDER;
		try {
			String[] subStrings = getSubStringsFromFileName(filePath);
			regionCode = subStrings[2];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regionCode;
	}

	/**
	 * gets output filepath with name weather.csv
	 * 
	 * @param url
	 * @return
	 */
	private String getOutputFile() {
		String fileName = Constants.DIRECTORY_PATH_OUTPUT + Constants.OUTPUT_FILE_NAME;
		System.out.println("Output Filename : " + fileName);
		return fileName;
	}
}
