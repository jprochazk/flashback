# Flashback Test Utilities

Flashback is made by LinkedIn. This repository includes all the Flashback modules converted from Gradle to Maven. https://github.com/linkedin/flashback

## How to use:

1. **Clone the repository.**
2. **Add the test utils as a maven dependency:**

&nbsp;&nbsp;&nbsp;&nbsp;In Intellij IDEA by JetBrains, do File -> New -> Module from existing sources  
&nbsp;&nbsp;&nbsp;&nbsp;and select the pom.xml file in the cloned repository, leaving all default settings.
  
&nbsp;&nbsp;&nbsp;&nbsp;Afterwards, the test utils can be added as a maven dependency:
  ```xml
  <dependency>
    <groupId>com.jpr</groupId>
    <artifactId>flashback-test-utils</artifactId>
    <version>0.0.4</version>
  </dependency>
  ```
&nbsp;&nbsp;&nbsp;&nbsp;For other IDEs, google "how to create maven module from existing pom.xml".

3. **Check out the example to see how it works:** [FlashbackBaseTestExample](flashback-test-utils/src/test/java/com/jpr/flashbacktestutils/FlashbackBaseTestExample.java)

The files produced by Flashback look like [this](flashback-test-utils/flashback_scenes/SCENE_NAME.json).  
Please note that Flashback files by default have no extension. The base test class adds this extension for convenience.
