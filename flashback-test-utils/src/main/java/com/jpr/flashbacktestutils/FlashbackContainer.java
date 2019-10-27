/*
 * Made by Jan Procházka
 * https://github.com/jprochazk
 */

package com.jpr.flashbacktestutils;

import com.linkedin.flashback.SceneAccessLayer;
import com.linkedin.flashback.matchrules.MatchRule;
import com.linkedin.flashback.matchrules.MatchRuleUtils;
import com.linkedin.flashback.scene.DummyScene;
import com.linkedin.flashback.scene.Scene;
import com.linkedin.flashback.smartproxy.FlashbackRunner;
import com.linkedin.mitm.model.CertificateAuthority;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Objects;

public final class FlashbackContainer implements Closeable
{
    /** Proxy settings */
    private String host;
    private int port;

    /** Proxy SSL Settings */
    private InputStream rootCertificateInputStream;
    private String rootCertificatePassphrase;
    private CertificateAuthority certificateAuthority;
    private boolean sslInitialized = false;

    /** State variables */
    private FlashbackRunner currentFlashbackInstance;
    private Scene currentScene;
    private MatchRule currentMatchRule;
    private Boolean running = false;
    private Boolean dirty = false;

    public FlashbackContainer(
            final String host,
            final int port
    ) {
        if(host.equals("")) throw new IllegalArgumentException("Host cannot be empty!");
        this.host = Objects.requireNonNull(host);
        this.port = port;
    }

    public FlashbackContainer(
            final String host,
            final int port,
            InputStream rootCertificateInputStream,
            String rootCertificatePassphrase,
            CertificateAuthority certificateAuthority
    ) {
        if(host == null || host.equals("")) throw new IllegalArgumentException("Host cannot be empty!");
        if(rootCertificateInputStream == null) throw new IllegalArgumentException("Certificate input stream cannot be null!");
        if(rootCertificatePassphrase == null) throw new IllegalArgumentException("Certificate passphrase cannot be null!");
        if(certificateAuthority == null) throw new IllegalArgumentException("Certificate authority cannot be null!");

        this.host = host;
        this.port = port;
        this.rootCertificateInputStream = Objects.requireNonNull(rootCertificateInputStream);
        this.rootCertificatePassphrase = Objects.requireNonNull(rootCertificatePassphrase);
        this.certificateAuthority = Objects.requireNonNull(certificateAuthority);
        sslInitialized = true;
    }

    /** Start the Flashback scene. */
    public void startScene() throws InterruptedException {
        if(this.running) throw new IllegalStateException("Already running");
        if(currentFlashbackInstance == null || this.dirty) rebuildInstance();
        if(this.currentScene.equals(new DummyScene())) throw new IllegalStateException("Scene must be set");

        currentFlashbackInstance.start();
        this.running = true;
    }

    /** Stop the Flashback scene. */
    public void stopScene() {
        if(!this.running) throw new IllegalStateException("Not running");
        if(currentFlashbackInstance == null) throw new IllegalStateException("Scene not set");

        currentFlashbackInstance.stop();
        currentFlashbackInstance.close();
        this.running = false;
    }

    /** Set the current scene. */
    public void setScene(Scene scene) {
        this.currentScene = scene;
        this.dirty = true;
    }

    /** Set the current match rule. */
    public void setMatchRule(MatchRule rule) {
        this.currentMatchRule = rule;
        this.dirty = true;
    }

    /** Get Flashback proxy host */
    public String getProxyHost() {
        return host;
    }

    /** Get Flashback proxy port */
    public int getProxyPort() {
        return port;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void close() {
        currentFlashbackInstance.close();
    }

    private FlashbackRunner buildInstance(String host, int port, Scene scene, MatchRule matchRule) {
        if(host == null) throw new IllegalArgumentException("Host cannot be null!");
        if(scene == null) scene = new DummyScene();

        FlashbackRunner.Builder instanceBuilder = new FlashbackRunner.Builder()
            .sceneAccessLayer(new SceneAccessLayer(scene, (matchRule != null) ? matchRule : MatchRuleUtils.matchEntireRequest()))
            .host(host)
            .port(port)
            .mode(scene.getSceneMode())
        ;

        if(this.sslInitialized) {
            instanceBuilder
                .certificateAuthority(this.certificateAuthority)
                .rootCertificateInputStream(this.rootCertificateInputStream)
                .rootCertificatePassphrase(this.rootCertificatePassphrase);
        }

        this.dirty = false;
        return instanceBuilder.build();
    }

    private void rebuildInstance() {
        this.currentFlashbackInstance = buildInstance(host, port, this.currentScene, this.currentMatchRule);
    }
}
