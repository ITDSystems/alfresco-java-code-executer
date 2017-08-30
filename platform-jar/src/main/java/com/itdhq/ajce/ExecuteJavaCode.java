package com.itdhq.ajce;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.service.ServiceRegistry;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class ExecuteJavaCode extends AbstractWebScript {

    private ServiceRegistry serviceRegistry;
    private boolean enabled;

    @Required
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {

        if (!enabled)
            throw new AlfrescoRuntimeException("AJCE is disabled");

        byte[] bytes;

        try {
            bytes = IOUtils.toByteArray(req.getContent().getInputStream());
        } catch (IOException e) {
            throw new AlfrescoRuntimeException("Failed to read byte-code", e);
        }

        Class cls = (new ByteClassLoader()).load(bytes);
        Job instance;
        try {
            instance = ((Job) cls.newInstance());
        } catch (Exception e) {
            throw new AlfrescoRuntimeException("Failed to load class from byte-code", e);
        }

        Serializable result = instance.run(serviceRegistry);

        ObjectOutputStream oos = new ObjectOutputStream(res.getOutputStream());
        oos.writeObject(result);
        res.setContentType("application/octet-stream");
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}