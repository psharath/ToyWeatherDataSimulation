package com.toy.weather.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementation of current conditions in a given area Returns the current
 * temperature,elevation,lattitude,longitude,weather condition, humidity,
 * location time, barometric pressure, and visibility of a location.
 * 
 * @author Sharath Pappula
 * @date 10/05/2017
 * @version 0.1
 */
public class ToyWeatherRest {

	private static String url = "";
	private JSONObject result;
	// Variables generated from the download
	private String termsUrl;
	private String obsEpoch;
	private String tempString;
	private String weather;
	private String city;
	private String relativeHumidity;
	private String pressureMB;
	private long epoch;
	private String obsElev;
	private String obsCtry;
	private String obsCity;
	private String obsLat;
	private String obsLon;
	private String obUrl;
	private String position;
	private String[] locationdatas;

	/**
	 * Get the locations using properties file to find current conditions (city
	 * and state) Get the destination file location,apikey for giving input to
	 * the Wunderground RestAPI
	 * 
	 * @param location
	 *            Place to get conditions - preferred format: (STATE/City)
	 * @return The WUConditions object with parameters set
	 */

	public static void main(String[] args) throws JSONException {

		Properties prop = new Properties();
		InputStream input = null;
		String filepath = null;
		String[] location = null;
		String apikey;

		ToyWeatherRest tw = new ToyWeatherRest();
		try {

			input = new FileInputStream("weather.properties");

			// load a properties file
			prop.load(input);

			//get the property values filePath,apikey,city and set to local variables
			filepath = prop.getProperty("filepath");
			apikey = prop.getProperty("apiKey");
			location = prop.getProperty("city").split(",");
			url = "http://api.wunderground.com/api/" + apikey + "/conditions/q/AU/";
			tw.downloadData(filepath, apikey, location, url);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Downloads and parses all the weather data from the given information
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */

	public void downloadData(String filePath, String apiKey, String[] location, String url)
			throws JSONException, IOException {
		locationdatas = null;
		BufferedWriter bw = null;
		try {
			for (int i = 0; i < location.length; i++) {

				url += location[i] + ".json";
				result = constructJSON(url);
				
                //Getting the weather details from JSON  Object
				JSONObject jObj = new JSONObject(result.toString());
				termsUrl = jObj.getJSONObject("response").getString("termsofService");
				jObj = jObj.getJSONObject("current_observation");

				obsEpoch = jObj.getString("observation_epoch");
				tempString = jObj.getString("temperature_c");
				weather = jObj.getString("weather");
				relativeHumidity = jObj.getString("relative_humidity");
				city = jObj.getString("city");
				pressureMB = jObj.getString("pressure_mb");
				obsElev = jObj.getJSONObject("observation_location").getString("elevation");
				obsCtry = jObj.getJSONObject("observation_location").getString("country");
				obsCity = jObj.getJSONObject("observation_location").getString("city");
				obsLat = jObj.getJSONObject("observation_location").getString("latitude");
				obsLon = jObj.getJSONObject("observation_location").getString("longitude");
				position = "obsLat+','+obsLon+','+obsElev";
				
				//Converting epoch date format to date format
				epoch = Long.parseLong(obsEpoch);
				Date date = new Date(epoch);
				
                 //Making pipe separated  weather data and placing into external file
				locationdatas[i] = city+'|'+position+'|'+date+'|'+weather+'|'+tempString+'|'+pressureMB+'|'+relativeHumidity;

			}

			bw = new BufferedWriter(new FileWriter(filePath));

			for (int j = 0; j < locationdatas.length; j++) {
				bw.write(locationdatas[j]);
				bw.newLine();
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			bw.close();
		}

	}

	/**
	  * Makes an HTTP GET call to download the JSON from the given URL 
	  * @param URL The URL to connect to 
	  * @return A JSONObject containing the HTTP GET results 
	  */ 
	
	public static JSONObject constructJSON(String URL) {

		String content = "";
		URL.replace(" ", "%20");

		try {

			URL Url = new URL(URL);
			URLConnection conn = Url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content += line + "\n";
			}
			bufferedReader.close();

			return new JSONObject(content);

		} catch (Exception e) {
			System.out.println(content);
			System.out.println("Error occurred: " + e);
			return new JSONObject();
		}

	}

	/**
	 * Returns the raw data that was downloaded from the API
	 * 
	 * @return A JSONObject representation of what was downloaded
	 */
	public String getRawData() {
		if (result != null) {
			return result.toString();
		} else {
			return "No data";
		}
	}

	@Override
	public String toString() {
		return getRawData();
	}

	// ---------------------------------------
	// Begin data getters
	// ---------------------------------------

	/**
	 * The url to Weather Undergrounds Terms of Service
	 * 
	 * @return the terms URL
	 */
	public String getTermsUrl() {
		return termsUrl;
	}

	/**
	 * The time at which the current conditions were observed
	 * 
	 * @return string representation at the time of recording
	 */
	public String getObservationEpoch() {
		return obsEpoch;
	}

	/**
	 * The temperature in degrees Celsius
	 * 
	 * @return temperature (C)
	 */

	/**
	 * URL to information at the observation location
	 * 
	 * @return url to observation location
	 */
	public String getObUrl() {
		return obUrl;
	}

	/**
	 * Current visibility in miles
	 * 
	 * @return visibility (mi)
	 */

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

	public String getObsEpoch() {
		return obsEpoch;
	}

	public void setObsEpoch(String obsEpoch) {
		this.obsEpoch = obsEpoch;
	}

	public String getTempString() {
		return tempString;
	}

	public void setTempString(String tempString) {
		this.tempString = tempString;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(String relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public String getPressureMB() {
		return pressureMB;
	}

	public void setPressureMB(String pressureMB) {
		this.pressureMB = pressureMB;
	}

	public void setTermsUrl(String termsUrl) {
		this.termsUrl = termsUrl;
	}

	public void setObUrl(String obUrl) {
		this.obUrl = obUrl;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getEpoch() {
		return epoch;
	}

	public void setEpoch(long epoch) {
		this.epoch = epoch;
	}

	public String getObsElev() {
		return obsElev;
	}

	public void setObsElev(String obsElev) {
		this.obsElev = obsElev;
	}

	public String getObsCtry() {
		return obsCtry;
	}

	public void setObsCtry(String obsCtry) {
		this.obsCtry = obsCtry;
	}

	public String getObsCity() {
		return obsCity;
	}

	public void setObsCity(String obsCity) {
		this.obsCity = obsCity;
	}

	public String getObsLat() {
		return obsLat;
	}

	public void setObsLat(String obsLat) {
		this.obsLat = obsLat;
	}

	public String getObsLon() {
		return obsLon;
	}

	public void setObsLon(String obsLon) {
		this.obsLon = obsLon;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
}