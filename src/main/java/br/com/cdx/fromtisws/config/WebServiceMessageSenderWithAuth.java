package br.com.cdx.fromtisws.config;

import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import java.io.IOException;
import java.net.HttpURLConnection;

public class WebServiceMessageSenderWithAuth extends HttpUrlConnectionMessageSender {

	private final String basicAuthorization;

    public WebServiceMessageSenderWithAuth(String basicAuthorization) {
        this.basicAuthorization = basicAuthorization;
    }

    @Override
	protected void prepareConnection(HttpURLConnection connection)
			throws IOException {

		connection.setRequestProperty("Authorization", "Basic " + basicAuthorization);

		super.prepareConnection(connection);
	}
}
