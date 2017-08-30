package com.itdhq.ajce.example;

import com.itdhq.ajce.Job;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;

import java.io.Serializable;

public class HelloWorld implements Job {
    public Serializable run(ServiceRegistry services) {
        NodeRef nodeRef = services.getNodeService().getRootNode(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
        return "Root node ref is " + nodeRef.toString();
    }
}
