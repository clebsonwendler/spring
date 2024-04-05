package br.com.cdx.fromtisws.endpoint;

import br.com.cdx.fromtisws.dto.RequisicaoWrapper;
import br.com.cdx.fromtisws.service.RabbitMQService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
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
    public void requisicao(@RequestPayload JAXBElement<RequisicaoWrapper> request) {

        log.info("Received Fromtis Request with id {}", request.getValue().getRequisicaoCertificado().getId());

        try {

            final JAXBContext jaxbContext = JAXBContext.newInstance(RequisicaoWrapper.class);

            final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            final StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(request.getValue(), stringWriter);

            final String xmlContent = stringWriter.toString();

            rabbitMQService.sendMessage(xmlContent);

        } catch (Exception e) {
            log.error("Error parsing XML data to JSON. FromtisRequestCode: {}, Organization: {}, Details: ",
                    request.getValue().getRequisicaoCertificado().getId(),
                    request.getValue().getRequisicaoCertificado().getFundo().getCnpjFundo(),
                    e);
        }
    }
}
