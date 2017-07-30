package utils;

import java.util.ArrayList;

/**
 * @author rohitkhirid
 *
 */
public class Constants {
	// turn flag on if you only want to read data from one file, while submitting turn this off else assignment will be marked invalid ;-)
	public static final boolean IS_DEVELOPEMENT_ON = false;
	
	// base url from which we have to start crawling
	public static final String BASE_URL = "http://www.metoffice.gov.uk/";

	// page endpoint on web
	public static final String PAGE_ENDPOINT = "climate/uk/summaries/datasets#Yearorder";

	// selected regions
	public static final String[] SELECTED_REGIONS = { "UK", "England", "Wales", "Scotland" };

	// selected criterias
	public static final String[] SELECTED_CRITERIAS = { "Tmax", "Tmin", "Tmean", "Sunshine", "Rainfall" };

	// there are two stastitics type date and ranking, currently we only need
	// date
	public static final String[] SELECTED_STATISTICS_TYPE = { "date" };

	// list of strings which should not be presnt in url
	public static final String[] UNSUPPORTED_SUBSTRINGS = { "_" };

	// base directory to store downloaded files
	public static final String DIRECTORY_PATH_OUTPUT = "src/outputfiles/";
	
	// base directory to store output file ex. weather.csv
	public static final String DIRECTORY_PATH_INPUT = "src/downloadedfiles/";
	
	// output file where we'll write all the data in expected format
	public static final String OUTPUT_FILE_NAME = "weather.csv";

	// counter for naming temp files if no valid name is found
	public static int COUNTER = 0;
	
	// we write N/A if no data point is present at that point
	public static final String NA_PLACEHOLDER = "N/A";
	
	// length of data from every cell
	public static final int LENGTH_OF_CHUNK = 7;
	
	// separator used while writing to output file
	public static final String SEPARATOR = ",";
	
	// used to get month name from index passed
	public static final ArrayList<String> MONTHS_LIST = new ArrayList<String>() {{
	    add("JAN");
	    add("FEB");
	    add("MAR");
	    add("APR");
	    add("MAY");
	    add("JUN");
	    add("JUL");
	    add("AUG");
	    add("SEP");
	    add("OCT");
	    add("NOV");
	    add("DEC");
	}};
}
