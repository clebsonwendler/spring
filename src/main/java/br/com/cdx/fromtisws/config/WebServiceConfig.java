package br.com.cdx.fromtisws.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    public final static String NAMESPACE_URI = "http://webservices.portal.fidc.fromtis.com.br/";

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/fromtis/ws/*");
    }

    @Bean(name = "requisicaoCertificacaoDigital")
    public Wsdl11Definition requisicaoCertificacaoDigitalWsdl11Definition(XsdSchema requisicaoCertificacaoDigitalSchema) {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("wsdl/requisicaoCertificacaoDigital.wsdl"));
        return wsdl11Definition;
    }


    @Bean(name = "retornoCertificacaoDigital")
    public DefaultWsdl11Definition retornoCertificacaoDigitalWsdl11Definition(XsdSchema retornoCertificacaoDigitalSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("RetornoCertificacaoDigitalPort");
        wsdl11Definition.setLocationUri("/fromtis/ws");
        wsdl11Definition.setTargetNamespace(NAMESPACE_URI);
        wsdl11Definition.setSchema(retornoCertificacaoDigitalSchema);
        wsdl11Definition.setCreateSoap12Binding(Boolean.TRUE);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema requisicaoCertificacaoDigitalSchema() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/requisicaoCertificacaoDigital.xsd"));
    }

    @Bean
    public XsdSchema retornoCertificacaoDigitalSchema() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/retornoCertificacaoDigital.xsd"));
    }
}
