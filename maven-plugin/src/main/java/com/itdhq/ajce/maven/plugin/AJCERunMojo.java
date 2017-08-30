package com.itdhq.ajce.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

@Mojo(name = "run", defaultPhase = LifecyclePhase.INTEGRATION_TEST, threadSafe = true)
public class AJCERunMojo extends AbstractMojo {
    @Parameter
    private String className;

    @Parameter
    private String baseUrl;

    @Parameter
    private String adminPassword;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;


    public void execute() throws MojoExecutionException, MojoFailureException {
        File classFile = new File(project.getBuild().getOutputDirectory());
        String[] parts = className.split("\\.");

        for (int i = 0; i < parts.length-1; i++)
            classFile = new File(classFile, parts[i]);

        classFile = new File(classFile, parts[parts.length-1] + ".class");

        byte[] bytes;

        try {
            bytes = Files.readAllBytes(classFile.toPath());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to read class file", e);
        }

        URL url;
        try {
            url = new URL(baseUrl + "/s/api/ajce");
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Invalid base URL specified", e);
        }

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to open connection to server", e);
        }

        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            //
        }
        connection.setRequestProperty("Content-Type", "application/octet-stream");

        String encoded = Base64.getEncoder().encodeToString(("admin:"+adminPassword).getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic "+encoded);

        connection.setDoOutput(true);
        int responseCode;
        try {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.write(bytes);
            wr.flush();
            wr.close();
            responseCode = connection.getResponseCode();
        }catch (Exception e)
        {
            throw new MojoExecutionException("Failed to submit request", e);
        }

        Object obj;
        try {
            ObjectInputStream oos = new ObjectInputStream(connection.getInputStream());
            obj = oos.readObject();
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to read response from server");
        }


        System.out.println(obj.toString());
    }
}
