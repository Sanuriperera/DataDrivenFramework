package DataDrivenTests;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class LoginUsingDataProviderByReadingExcel {
    WebDriver driver;

    //Read Excel Data
    //Original path ->F:\GitHub Projects\DataDrivenFramework\DataDrivenFramework\TestData
//    public void readExcelData() throws IOException {
//        //Read the file location
//        FileInputStream file=new FileInputStream(System.getProperty("user.dir")+"\\TestData\\credentials1.xlsx");
//       //Create object for workbook
//        XSSFWorkbook workbook = new XSSFWorkbook(file);
//        //Goto the sheet to be worked on
//        XSSFSheet sheet = workbook.getSheet("Sheet1"); // work.getSheetAt(0);
//        //Get rows
//        int totalRows = sheet.getLastRowNum();
//        //Access the first row and get the total columns
//        int totalColumns= sheet.getRow(0).getLastCellNum();
//        System.out.println("Total Rows count : " + totalRows); // 4 (actual num of rows 5)
//        System.out.println("Total Columns count : " + totalColumns);
//
//        for(int r =0; r<=totalColumns;r++){
//            XSSFRow currentRow = sheet.getRow(r);
//            for(int c=0;c< totalColumns;c++){
//               XSSFCell currentCell= currentRow.getCell(c);
//               String cellValue = currentCell.toString();
//                System.out.print(cellValue+"\t\t");
//            }
//            System.out.println();
//        }
//
//    }

    @BeforeMethod
    public void openPage(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @DataProvider(name="loginData")
    public String[][] getExcelData() throws IOException {
        FileInputStream file=new FileInputStream(System.getProperty("user.dir")+"\\TestData\\credentials1.xlsx");
        //Create object for workbook
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        //Goto the sheet to be worked on
        XSSFSheet sheet = workbook.getSheet("Sheet1"); // work.getSheetAt(0);
        //Get rows
        int totalRows = sheet.getLastRowNum();
        //Access the first row and get the total columns
        int totalColumns= sheet.getRow(0).getLastCellNum();
        System.out.println("Total Rows count : " + totalRows); // 4 (actual num of rows 5)
        System.out.println("Total Columns count : " + totalColumns);

        String[][] testData = new String[totalRows][totalColumns];

        for(int r=1; r<=totalRows;r++){
            for(int c=0;c<totalColumns;c++){
                testData[r-1][c]=sheet.getRow(r).getCell(c).toString();
            }
        }

        workbook.close();
        file.close();

        return testData;
    }


//    @DataProvider(name = "loginData")
//    public String [][] loginDataProvider(){
//        String[][] data= {
//                {"Admin","admin123","valid"},
//                {"Dummy Admin","Dummy admin123","invalid"},
//                {"Dummy Admin","admin123","invalid"},
//                {"Admin","Dummy admin123","invalid"}
//        };
//        return data;
//    }

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
