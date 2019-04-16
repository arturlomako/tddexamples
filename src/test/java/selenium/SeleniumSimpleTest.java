package selenium;

import lombok.SneakyThrows;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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

  @SuppressWarnings("InfiniteLoopStatement")
  @Test
  @SneakyThrows
  public void winTheGame() {

    driver.get("http://x-terminal.blogspot.com/2014/10/click-test-10-seconds.html");
    final WebElement button = driver.findElement(By.cssSelector("#ClickButton"));

    final Thread thread = new Thread(() -> {
      while (true) {
        button.click();
      }
    });
    thread.start();

    Thread.sleep(12 * 1000);
    thread.interrupt();

  }

}
