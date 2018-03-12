package org.apache.aries.jax.rs.example.sse.server;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(
    property = {
        HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PATTERN + "=/static/*",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX + "=/web-ui"
    },
    service = WebResources.class
)
public class WebResources {}
