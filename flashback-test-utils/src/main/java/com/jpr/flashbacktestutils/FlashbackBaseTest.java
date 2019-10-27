/*
 * Made by Jan Procházka
 * https://github.com/jprochazk
 */

package com.jpr.flashbacktestutils;

import com.linkedin.flashback.factory.SceneFactory;
import com.linkedin.flashback.scene.Scene;
import com.linkedin.flashback.scene.SceneConfiguration;
import com.linkedin.flashback.scene.SceneMode;
import com.linkedin.mitm.model.CertificateAuthority;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * Base class with Flashback functionality.
 *
 * Do not re-use scenes. Flashback doesn't support changing SceneMode on the go,
 * which means that the Flashback instance has to be re-created each time you change the scene.
 *
 * @author Jan Procházka
 */
public class FlashbackBaseTest {
    /** Default settings */
    private static final String DEFAULT_PROXY_HOST = "localhost";
    private static final int DEFAULT_PROXY_PORT = 5556;
    private static final String DEFAULT_SCENE_PATH = System.getProperty("user.dir").replace("\\", "/") + "/flashback_scenes";

    /** Proxy settings - customize to your needs */
    protected String proxyHost = DEFAULT_PROXY_HOST;
    protected int proxyPort = DEFAULT_PROXY_PORT;

    /** SSL Settings - set using {@link #setSslSettings(InputStream, String, CertificateAuthority)} */
    private InputStream rootCertificateInputStream;
    private String rootCertificatePassphrase;
    private CertificateAuthority certificateAuthority;
    private boolean sslInitialized = false;

    /** Flashback wrapper class instance */
    protected FlashbackContainer Flashback;

    /**
     * Call before anything else
     * If you are recording a HTTPS request, you have to set SSL settings first using {@link #setSslSettings}
     */
    protected void initializeFlashback() {
        if(sslInitialized) {
            this.Flashback = new FlashbackContainer(proxyHost, proxyPort, rootCertificateInputStream, rootCertificatePassphrase, certificateAuthority);
        } else {
            this.Flashback = new FlashbackContainer(proxyHost, proxyPort);
        }
    }

    /** Call after everything else */
    protected void cleanupFlashback() {
        if(this.Flashback.isRunning()) this.Flashback.stopScene();
    }

    /**
     * @param rootCertificateInputStream A stream or file path to the CA certificate
     * @param rootCertificatePassphrase The passphrase created for the CA certificate
     * @param certificateAuthority CA certificate's properties
     */
    protected void setSslSettings(InputStream rootCertificateInputStream, String rootCertificatePassphrase, CertificateAuthority certificateAuthority) {
        this.rootCertificateInputStream = rootCertificateInputStream;
        this.rootCertificatePassphrase = rootCertificatePassphrase;
        this.certificateAuthority = certificateAuthority;
        sslInitialized = true;
    }

    /**
     * Used to create a Scene object.
     * Automatically sets the scene mode based on whether or not the scene already exists.
     *
     * @param sceneName name of the scene (e.g. "GoogleOAuth2AuthCodeRequest")
     * @param scenePath Optional. The full path to the scene directory.
     *                  Default: defaultScenePath
     */
    protected Scene createFlashbackScene(String sceneName, String scenePath) throws IOException {
        // Scenes are stored in JSON format, so append the extension to them
        sceneName = sceneName + ".json";

        // Try to find this file and set SceneMode accordingly
        SceneMode sceneMode = (findScene(scenePath, sceneName)) ? SceneMode.PLAYBACK : SceneMode.RECORD;

        // Create scene configuration
        return SceneFactory.create(new SceneConfiguration(scenePath, sceneMode, sceneName));
    }

    /**
     * Used to create a Scene object.
     * Automatically sets the scene mode based on whether or not the scene already exists.
     *
     * @param sceneName name of the scene (e.g. "GoogleOAuth2AuthCodeRequest")
     */
    protected Scene createFlashbackScene(String sceneName) throws IOException {
        // Scenes are stored in JSON format, so append the extension to them
        sceneName = sceneName + ".json";

        // Try to find this file and set SceneMode accordingly
        SceneMode sceneMode = (findScene(DEFAULT_SCENE_PATH, sceneName)) ? SceneMode.PLAYBACK : SceneMode.RECORD;

        // Create scene configuration
        return SceneFactory.create(new SceneConfiguration(DEFAULT_SCENE_PATH, sceneMode, sceneName));
    }

    private static boolean findScene(String scenePath, String sceneName) {
        // Use Java File class to check if the scene file already exists
        return new File(scenePath+"/"+sceneName).exists();
    }
}
