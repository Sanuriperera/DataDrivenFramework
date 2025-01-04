package DataDrivenTests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginUsingDataProvider {
    WebDriver driver;

    @BeforeMethod
    public void openPage(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @DataProvider(name = "loginData")
    public String [][] loginDataProvider(){
        String[][] data= {
                {"Admin","admin123","valid"},
                {"Dummy Admin","Dummy admin123","invalid"},
                {"Dummy Admin","admin123","invalid"},
                {"Admin","Dummy admin123","invalid"}
        };
        return data;
    }

    @Test(dataProvider = "loginData")
    public void loginTestScenario(String uName,String pass,String expValidation){

        WebElement userName = driver.findElement(By.xpath("//input[@name='username']"));
        userName.sendKeys(uName);

        WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
        password.sendKeys(pass);

        WebElement loginBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        loginBtn.click();

        boolean urlVerification = driver.getCurrentUrl().contains("dashboard");
        if(expValidation.equals("valid")){
            Assert.assertTrue(urlVerification,"Expecting login sucsess but not navigated to dashboard");

        }else{
            Assert.assertFalse(urlVerification,"Expecting login failed but navigated to dashboard");
        }


    }

    @AfterMethod
    public void closeBrowser(){
        driver.quit();
    }
}
