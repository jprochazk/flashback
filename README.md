# Flashback Test Utilities

Flashback is made by LinkedIn. All code in this repo belongs to LinkedIn.
https://github.com/linkedin/flashback

## How to use:

1. Clone the repository into your project.
2. Add the test utils as a maven dependency:
```xml
<dependency>
  <groupId>com.jpr</groupId>
  <artifactId>flashback-test-utils</artifactId>
  <version>0.0.4</version>
</dependency>
```

3. Check out the example to see how it works: [FlashbackBaseTestExample](flashback-test-utils/src/test/java/com/jpr/flashbacktestutils/FlashbackBaseTestExample.java)

The files produced by Flashback look like [this](flashback-test-utils/flashback_scenes/SCENE_NAME.json).  
Please note that Flashback files by default have no extension. The base test class adds this extension for convenience.