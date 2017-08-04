package org.apache.aries.jax.rs.whiteboard.internal;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicLong;

import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

class ChangeCounter {

    private static final String changecount = "service.changecount";
    private final AtomicLong _atomicLong = new AtomicLong();
    private ServiceRegistration<?> _serviceRegistration;
    private final Hashtable<String, Object> _properties;

    public ChangeCounter(ServiceRegistration<?> serviceRegistration) {
        _serviceRegistration = serviceRegistration;

        ServiceReference<?> serviceReference =
            _serviceRegistration.getReference();

        _properties = new Hashtable<>();

        for (String propertyKey : serviceReference.getPropertyKeys()) {
            _properties.put(
                propertyKey, serviceReference.getProperty(propertyKey));
        }
    }

    public void inc() {
        long l = _atomicLong.incrementAndGet();

        @SuppressWarnings("unchecked")
        Hashtable<String, Object> properties =
            (Hashtable<String, Object>)_properties.clone();

        properties.put(changecount, l);

        _serviceRegistration.setProperties(properties);
    }
}