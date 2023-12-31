package test;

import static org.junit.Assert.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import services.Enums;
import services.bean.ReadabilityResponse;

public class TestGeneral {

	Gson gson = new Gson();
	
	@Test
	public void test() {
		try {
			WebTarget target = ClientBuilder.newClient().target("http://localhost:8008/server/ws"); 
			Response response = target
					.request(MediaType.TEXT_PLAIN)
					.post(Entity.entity("http://wikipedia.org", MediaType.TEXT_PLAIN_TYPE));
			String html = response.readEntity(String.class);
			System.out.println( html.substring(0, 10) 
								+ "[...]" 
								+ html.substring(html.length() - 8,html.length())
								+ "\n"
					);
			Assert.assertNotNull(html);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testReadability() {
		try {
			String endpoint = Enums.ENDPOINT_RAPI.getProp();
			String token = Enums.TOKEN_RAPI.getProp();
			String finalEndpoint = endpoint
							+ "?url=" + "http://www.wikipedia.org"
							+ "&token=" + token;
			
			WebTarget target = ClientBuilder.newClient().target(finalEndpoint); 
			Response response = target
					.request(MediaType.TEXT_PLAIN)
					.get(Response.class);
			String objectStr = response.readEntity(String.class);
			ReadabilityResponse readObj = gson.fromJson(objectStr, ReadabilityResponse.class);
			
			System.out.println(readObj.getContent().substring(0, 20) + "[...]\n");
			Assert.assertNotNull(objectStr);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testReadabilityWebService() {
		try {
			String endpoint = "http://localhost:8008/server/ws/readapi";
			String url = "http://www.wikipedia.org";
			
			WebTarget target = ClientBuilder.newClient().target(endpoint); 
			Response response = target
					.request(MediaType.TEXT_PLAIN)
					.post(Entity.entity(url, MediaType.TEXT_PLAIN_TYPE), Response.class);
			if (response.getStatus() == 200){
				String objectStr = response.readEntity(String.class);
				ReadabilityResponse readObj = gson.fromJson(objectStr, ReadabilityResponse.class);
				System.out.println("Sucessful call: "+ readObj.toString());
				Assert.assertTrue(true);
			} else {
				System.err.println("HTTP ERROR: ");
				System.err.println(response.readEntity(String.class));
				Assert.fail();
			}
			
		} catch (Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
}
