package edu.shapo.testtransfer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.util.Random;

public class SingleCallThread extends Thread {

    Client client = null;
    WebResource resource = null;

    public SingleCallThread() {

        client = Client.create();
        resource = client.resource("http://localhost:8080/bank/api/transfer/make");

    }

    public void run() {
        Random ids = new Random();
        int srcId = ids.nextInt(10);
        int dstId = ids.nextInt(10);

        Random amo = new Random();
        int amount = amo.nextInt(50);


        System.out.println("src: " + srcId + " dst: " + dstId + " amount: " + amount);


        String input = "{\n" +
                "    \"sourceAccountId\": \"" + srcId + "\", \n" +
                "    \"targetAccountId\": \"" + dstId + "\", \n" +
                "    \"transferAmount\": \""  + amount + "\",\n" +
                "    \"initiator\": \"john\"\n" +
                "}";
        ClientResponse response = resource.type(MediaType.APPLICATION_XML).post(ClientResponse.class, input);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);
    }

}
