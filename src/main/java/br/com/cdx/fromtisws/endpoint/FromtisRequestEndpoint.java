package br.com.cdx.fromtisws.endpoint;

import br.com.cdx.fromtis.domain.ws.requisicao.Requisicao;
import br.com.cdx.fromtisws.service.RabbitMQService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import java.io.StringWriter;

@Slf4j
@Endpoint
public class FromtisRequestEndpoint {

    public static final String NAMESPACE_URI = "http://webservices.portal.fidc.fromtis.com.br/";

    private final RabbitMQService rabbitMQService;

    public FromtisRequestEndpoint(RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "requisicao")
    public void requisicao(@RequestPayload JAXBElement<Requisicao> request) {

        log.info("Received Fromtis Request with id {}", request.getValue().getRequisicaoCertificado().getId());

        try {
            final String xmlContent = new XmlMapper().writeValueAsString(request.getValue());
            rabbitMQService.sendMessage(xmlContent);

        } catch (JsonProcessingException e) {
            log.error("Error parsing XML data to JSON. FromtisRequestCode: {}, Organization: {}, Details: ",
                    request.getValue().getRequisicaoCertificado().getId(),
                    request.getValue().getRequisicaoCertificado().getFundo().getCnpjFundo(),
                    e);
        }
    }
}
