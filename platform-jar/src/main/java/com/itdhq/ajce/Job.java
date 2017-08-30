package com.itdhq.ajce;

import org.alfresco.service.ServiceRegistry;

import java.io.Serializable;

public interface Job {
    Serializable run(ServiceRegistry services);
}
