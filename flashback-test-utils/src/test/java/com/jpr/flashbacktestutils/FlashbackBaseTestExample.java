/*
 * Made by Jan Procházka
 * https://github.com/jprochazk
 */

package com.jpr.flashbacktestutils;

import com.linkedin.flashback.scene.Scene;
import com.linkedin.mitm.model.CertificateAuthority;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class FlashbackBaseTestExample extends FlashbackBaseTest {

    private static final String TEST_URI = "http://svatky.adresa.info/json";

    /**
     * The base test class does not depend on any testing library,
     * which means you have to call the appropriate functions yourself before your tests.
     *
     * If you are recording HTTPS requests, you have to set the SSL settings using {@link FlashbackBaseTest#setSslSettings(InputStream, String, CertificateAuthority)}
     * before calling {@link FlashbackBaseTest#initializeFlashback()}. If you don't do this, Flashback won't be able to make
     * the request, and your test will stall infinitely (unless you set a timeout property for your Http client)
     * To learn how to do get a CA certificate setup, read https://jamielinux.com/docs/openssl-certificate-authority/introduction.html.
     */
    @Before
    public void init() {
        initializeFlashback();
    }

    /**
     * Same as @Before. Flashback needs to be cleaned up after you're done.
     */
    @After
    public void cleanup() {
        cleanupFlashback();
    }

    @Test
    public void ProperFlashbackUsageExampleTest() throws IOException, InterruptedException {
        // First, create a scene
        // You can also change the storage directory like so:
        // Scene scene = createFlashbackScene("SCENE_NAME", "C:/FULL/PATH/TO/SCENE");
        Scene scene = createFlashbackScene("SCENE_NAME");

        // Once you are ready, set the scene
        Flashback.setScene(scene);
        // You can also set a custom match rule, but this is not required.
        // The default is to match the entire request.
        // Flashback.setMatchRule(MatchRuleUtils.matchEntireRequest());

        // To begin, start the scene
        Flashback.startScene();

        // Here I am using Apache HttpClient, but anything that allows you to use a proxy will work
        // Create your client with Flashback as the proxy
        HttpClient httpClient = HttpClientBuilder.create()
            .setProxy(new HttpHost(Flashback.getProxyHost(), Flashback.getProxyPort()))
            .build();

        // Send your actual request, then receive the response and assert
        var request = new HttpGet(TEST_URI);
        request.setHeader("Accept", "application/json");
        var response = httpClient.execute(request);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);

        // At the end of the test, stop the scene
        Flashback.stopScene();
        // Now the scene file can be distributed (on github, for example)
        // And you can run these tests offline.
    }
}
