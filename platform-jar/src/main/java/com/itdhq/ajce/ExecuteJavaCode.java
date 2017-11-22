package com.itdhq.ajce;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.service.ServiceRegistry;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.*;


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

        Serializable result;
        try {
            result = instance.run(serviceRegistry);
            if (result == null)
                result = "--- empty response ---";
        } catch (Throwable e)
        {
            StringWriter writer = new StringWriter();
            writer.write(String.format("Error occurred: %s\n\n", e.getMessage()));
            e.printStackTrace(new PrintWriter(writer));
            e.getCause()
            result = writer.toString();
        }

        ObjectOutputStream oos = new ObjectOutputStream(res.getOutputStream());
        oos.writeObject(result);
        res.setContentType("application/octet-stream");
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}