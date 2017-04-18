package edu.bsu.pvgestwicki.ld28.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import edu.bsu.pvgestwicki.ld28.core.LD28Game;

public class LD28GameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform platform = HtmlPlatform.register(config);
    platform.assets().setPathPrefix("ld28/");
    PlayN.run(new LD28Game());
  }
}
