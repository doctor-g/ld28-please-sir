package edu.bsu.pvgestwicki.ld28.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import edu.bsu.pvgestwicki.ld28.core.LD28Game;

public class LD28GameJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    PlayN.run(new LD28Game());
  }
}
