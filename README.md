# Flashback Test Utilities

Flashback is made by LinkedIn. All code in this repo belongs to LinkedIn.
https://github.com/linkedin/flashback

## How to use:

1. Clone the repository.
2. Add the test utils as a maven dependency:

&nbsp;&nbsp;&nbsp;&nbsp;In Intellij by JetBrains, you go into File -> New -> Module from existing sources  
&nbsp;&nbsp;&nbsp;&nbsp;and create the module by selecting the pom.xml file in the cloned repository.
  
&nbsp;&nbsp;&nbsp;&nbsp;Then, you can add this to your own pom.xml dependencies:
  ```xml
  <dependency>
    <groupId>com.jpr</groupId>
    <artifactId>flashback-test-utils</artifactId>
    <version>0.0.4</version>
  </dependency>
  ```
&nbsp;&nbsp;&nbsp;&nbsp;I can't help with any other IDE, you will have to look up something like "how to create maven module from existing pom.xml" for your IDE.

3. Check out the example to see how it works: [FlashbackBaseTestExample](flashback-test-utils/src/test/java/com/jpr/flashbacktestutils/FlashbackBaseTestExample.java)

The files produced by Flashback look like [this](flashback-test-utils/flashback_scenes/SCENE_NAME.json).  
Please note that Flashback files by default have no extension. The base test class adds this extension for convenience.
