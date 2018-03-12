package org.apache.aries.jax.rs.example.sse.server;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Component(service = {MessageBodyReader.class, MessageBodyWriter.class})
@JaxrsExtension
@JaxrsName("jackson-json")
public class JsonProvider extends JacksonJsonProvider {
}
