package edu.shapo.testtransfer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

public class SingleStatisticCallThread extends Thread {

    Client client = null;
    WebResource resource = null;

    public SingleStatisticCallThread(String url) {

        client = Client.create();
        resource = client.resource(url);

    }

    public void run() {

        ClientResponse response = resource.type(MediaType.APPLICATION_XML).get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);
    }

}
