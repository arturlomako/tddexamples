package selenium;

import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumSimpleTest {

  private WebDriver driver;

  @Before
  public void before() {
    driver = new ChromeDriver();
  }

  @After
  public void after() {
    driver.quit();
  }

  @Test
  @SneakyThrows
  public void winTheGame() {

    driver.get("http://x-terminal.blogspot.com/2014/10/click-test-10-seconds.html");
    final WebElement button = driver.findElement(By.cssSelector("#ClickButton"));

    final Thread thread = new Thread(() -> {
      long now = System.currentTimeMillis();
      long finishTime = now + 12000;
      while (System.currentTimeMillis() < finishTime) {
        button.click();
      }
    });
    thread.setDaemon(true);
    thread.start();
    thread.join();
  }

}
