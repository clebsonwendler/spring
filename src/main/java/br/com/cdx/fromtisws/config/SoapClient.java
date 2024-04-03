package br.com.cdx.fromtisws.config;

import br.com.cdx.fromtis.domain.ws.retorno.RetornoResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SoapClient extends WebServiceGatewaySupport {

    public RetornoResponse getRetorno(String url, Object request, String basicAuthorization) {
        getWebServiceTemplate().setMessageSender(new WebServiceMessageSenderWithAuth(basicAuthorization));
        return (RetornoResponse) getWebServiceTemplate().marshalSendAndReceive(url, request);
    }

    public RetornoResponse getRetorno(String url, Object request) {
        return (RetornoResponse) getWebServiceTemplate().marshalSendAndReceive(url, request);
    }
}
