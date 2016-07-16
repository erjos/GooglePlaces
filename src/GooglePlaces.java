import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GooglePlaces {

	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {

		// storing the place ID's in this ArrayList - this will be retrieved via
		// hibernate from the database
		ArrayList<String> placeID = new ArrayList();
		
		//retrieved these placeID's manually from the Google Places API - we are allowed to store these indefinitely 
		// placeID for Ashe supply co
		placeID.add("ChIJtzwfLTItO4gRxwpKgcgFomE");
		// placeID for Roasting Plant
		placeID.add("ChIJUwca7i8tO4gRC6azALL0VXY");
		// placeID for Urban Bean
		placeID.add("ChIJt7odBjEtO4gRgRETxl3QoLE");
		// placeID for Dessert Oasis Coffee Roasters
		placeID.add("ChIJzZVcTTAtO4gRe5sIIK-MgJs");
		// placeID for WBC coffee
		placeID.add("ChIJTUeIlTAtO4gRV1Z0iQFvN-g");

		// this will be the specific id for the location that we want to display

		// using enhanced for loop to iterate through the different Id's we have
		// stored and make the API call
		for (String id : placeID) {

			String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id
					+ "&key=AIzaSyDM0lmlS-ptLTR9KnDZSGUyijPQ5H1fsZs";

			// building a client (this is all based on the Apache HttpClient
			// library - added as maven dependency)
			// this is the only way to instantiate the HttpClient?
			HttpClient client = HttpClientBuilder.create().build();
			// creating a new HttpGet request object using the URL as the
			// parameters
			HttpGet request = new HttpGet(url);
			// creating a response object by executing the request executing the
			// request
			HttpResponse response = client.execute(request);

			// this will let us know the response code and if the request was
			// successful or not
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

			// imported Buffered reader to take in large amounts of text
			// need to get the entity and the content of the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line.trim());

				// may need to store it in another array after the first run,
				// because otherwise won't be able to drop it in the view using
				// MVC?
				// how can we have a flexible Spring output? it needs to be able
				// to read all of these pieces of data but also be flexible
				// based on
				// how many shops we're sending at once... this is where it's
				// going to get kind of tricky

			}

			// not sure if we need this - ask Peter or Kamel about this concept
			// - doesn't change my output either way
			request.releaseConnection();

			// tried to parse line, but was getting a null pointer exception,
			// needed to change it to result.toString
			// System.out.println(result.toString()); <--- shows you how the
			// result.tostring looks before you create the JsonElement
			JsonElement jelement = new JsonParser().parse(result.toString());
			// turn in the entire element into a Json object
			JsonObject jobject = jelement.getAsJsonObject();
			// pull out the first object according to the keyword
			jobject = jobject.getAsJsonObject("result");

			JsonElement address = jobject.get("formatted_address");
			JsonElement phoneNumber = jobject.get("formatted_phone_number");
			JsonElement name = jobject.get("name");
			System.out.println(address + "\n" + phoneNumber + "\n" + name);

			// JSONObject obj = new JSONObject(line);

			// JsonParser parser = new JsonParser();
			// JsonElement element = parser.parse(line);
		}

	}

}
