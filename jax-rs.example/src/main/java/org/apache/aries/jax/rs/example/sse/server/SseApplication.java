package org.apache.aries.jax.rs.example.sse.server;

import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationBase;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtensionSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;

@Component(
    property = {
        HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_INIT_PARAM_PREFIX +
            "transportId=http://cxf.apache.org/transports/http/sse"
    },
    service = Application.class
)
@JaxrsApplicationBase("/sse")
@JaxrsExtensionSelect("(osgi.jaxrs.name=jackson-json)")
@JaxrsName("sse")
public class SseApplication extends Application {
}
