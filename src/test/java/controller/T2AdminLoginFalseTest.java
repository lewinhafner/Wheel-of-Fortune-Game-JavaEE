// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;


public class T2AdminLoginFalseTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
      System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void t2AdminLoginFalse() {
    driver.get("http://localhost:22353/HafLew_LB_M151_Applikation/");
    driver.manage().window().setSize(new Dimension(1050, 660));
    driver.findElement(By.id("j_idt21:j_idt26")).click();
    driver.findElement(By.id("form-login:username")).click();
    driver.findElement(By.id("form-login:username")).sendKeys("AccountAdmin");
    driver.findElement(By.id("form-login:password")).sendKeys("Password123False");
    driver.findElement(By.id("form-login:submitBtn")).click();
    driver.findElement(By.cssSelector("html")).click();
    assertThat(driver.findElement(By.cssSelector("body")).getText(), is("Ups, we dun goofed!"));
  }
}
