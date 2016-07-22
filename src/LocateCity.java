import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class LocateCity {

	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {
		
		String city = "Detroit";

		String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city
				+ "&key=AIzaSyDM0lmlS-ptLTR9KnDZSGUyijPQ5H1fsZs";

		HttpClient client = HttpClientBuilder.create().build();

		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line.trim());
		}

		// still want to ask peter about this
		
		request.releaseConnection();
		
		JsonElement jelement = new JsonParser().parse(result.toString());

		JsonObject jobject = jelement.getAsJsonObject();

		JsonArray jarray = jobject.getAsJsonArray("results");
		
		jelement = jarray.get(0);

		jobject = jelement.getAsJsonObject();
		
		JsonElement placeID = jobject.get("place_id");

		System.out.println(placeID);

	}

}