package my.tamagochka.Main;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Sheet sh = null;
        try (Workbook wb = WorkbookFactory.create(new File("input.xlsx"))) {
            sh = wb.getSheet("Лист1");
            if (sh == null) throw new IOException();
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        String TEST_NAME = null;
        String SECTION_NAME = null;
        List<Question> questions = new ArrayList<>();
        int row_num = 0;
        Row row = sh.getRow(row_num);
        while (row != null) {
            String cellValue1 = row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue();
            String cellValue2 = row.getCell(1).getStringCellValue();
            switch (cellValue1) {
                case "t":
                    TEST_NAME = cellValue2;
                    break;
                case "s":
                    SECTION_NAME = cellValue2;
                    break;
                case "q":
                    questions.add(new Question(cellValue2));
                    break;
                case "":
                    questions.get(questions.size() - 1).addAnswer(new Answer(cellValue2, false));
                    break;
                case "at":
                    questions.get(questions.size() - 1).addAnswer(new Answer(cellValue2, true));
                    break;
            }
            row_num++;
            row = sh.getRow(row_num);
        }
//        questions.forEach(System.out::println);

        // very bad code =)




        WebDriver driver = new InternetExplorerDriver();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get("http://10.49.14.15/System/Enter.asp");
        WebElement login = driver.findElement(By.name("user_name"));
        login.sendKeys("test");
        WebElement password = driver.findElement(By.name("user_psw"));
        password.sendKeys("test17");

        WebElement button = driver.findElement(By.name("subm"));
        button.click();


        driver.get("http://10.49.14.15/TD/HeadingList.asp");
        button = driver.findElement(By.name("ViewAllButton"));
        button.click();
        WebElement dynamicElement = driver.findElement(By.linkText("Ad"));
        dynamicElement.click();
        WebElement parentTD = dynamicElement.findElement(By.xpath(".."));
        WebElement parentTR = parentTD.findElement(By.xpath(".."));
        button = parentTR.findElement(By.name("ViewTestsButton"));
        button.click();

        driver.get("http://10.49.14.15/TD/TD/TestButt.asp");
        button = driver.findElement(By.name("CreateTest"));
        button.click();

        dynamicElement = driver.findElement(By.name("Test_Name"));
        dynamicElement.clear();

        dynamicElement.sendKeys(TEST_NAME);

        button = driver.findElement(By.name("UpdateTest"));
        button.click();

        button = driver.findElement(By.name("DesignerBack"));
        button.click();


        driver.switchTo().frame("LeftFrame");
        dynamicElement = driver.findElement(By.partialLinkText(TEST_NAME));

        if(dynamicElement.getAttribute("onClick") != null) dynamicElement.click();

        driver.switchTo().defaultContent();
        driver.switchTo().frame("ButtFrame");
        button = driver.findElement(By.name("CreateSect"));
        button.click();

        dynamicElement = driver.findElement(By.name("Sect_Name"));
        dynamicElement.clear();
        dynamicElement.sendKeys(SECTION_NAME);
        button = driver.findElement(By.name("UpdateSection"));
        button.click();
        button = driver.findElement(By.name("Back"));
        button.click();


// создание вопроса
        for(Question question : questions) {

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println((questions.indexOf(question) + 1) + "/" + questions.size());

            driver.switchTo().defaultContent();
//            new WebDriverWait(driver, 15).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ButtFrame"));
            driver.switchTo().frame("ButtFrame");
            button = driver.findElement(By.name("CreateQuest"));
            button.click();

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            WebElement radio = new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(By.id("QType_ID_1")));
//                    driver.findElement(By.id("QType_ID_1"));
            radio.click();
            button = driver.findElement(By.name("UpdateQuest"));
            button.click();

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // вопрос

//            new WebDriverWait(driver, 15).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("BottomFrameQ"));
            driver.switchTo().frame("BottomFrameQ");
            WebElement divQuestText = driver.findElement(By.id("div_Quest_Text"));
            divQuestText.click();
            String QUESTION_TEXT = question.getQuestionText(); //"Это текст вопроса?"; //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            String INSERT_QUESTION_TEXT = "<span style=\"font-size:14.0pt;font-family:\\'Times New Roman\\',serif;mso-fareast-font-family:\\'Times New Roman\\';mso-ansi-language:RU;mso-fareast-language:RU;mso-bidi-language:AR-SA\"><strong><font color=#000000>" + QUESTION_TEXT + "</font></strong></span>";
            ((JavascriptExecutor) driver).executeScript("var e=arguments[0]; e.innerHTML='" + INSERT_QUESTION_TEXT + "';", divQuestText);
            driver.switchTo().defaultContent();
            driver.switchTo().frame("TopFrameQ");
            dynamicElement = driver.findElement(By.id("GoToNext"));
            dynamicElement.findElement(By.tagName("img")).click();



            // ответы
            for(Answer answer : question.getAnswers()) {

                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                driver.switchTo().defaultContent();
//                new WebDriverWait(driver, 15).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("LeftFrame"));
                driver.switchTo().frame("LeftFrame");
                divQuestText = driver.findElement(By.id("div_Answer_Text"));
                divQuestText.click();
                String ANSWER_TEXT = answer.getAnswerText();//answers.get(j); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                String INSERT_ANSWER_TEXT = "<span style=\"font-size:14.0pt;font-family:\\'Times New Roman\\',serif;mso-fareast-font-family:\\'Times New Roman\\';mso-ansi-language:RU;mso-fareast-language:RU;mso-bidi-language:AR-SA\">" + ANSWER_TEXT + "</span>";
                ((JavascriptExecutor) driver).executeScript("var e=arguments[0]; e.innerHTML='" + INSERT_ANSWER_TEXT + "';", divQuestText);
                driver.switchTo().defaultContent();
                driver.switchTo().frame("TopFrame");
                dynamicElement = driver.findElement(By.tagName("tr"));
                dynamicElement.findElement(By.tagName("td")).findElement(By.tagName("img")).click();

                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                driver.switchTo().defaultContent();
                driver.switchTo().frame("TopFrame");
//                new WebDriverWait(driver, 15).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("TopFrame"));
                dynamicElement = new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(By.tagName("tr")));
                dynamicElement.findElements(By.tagName("td")).get(3).findElement(By.tagName("img")).click();
            }

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // выбор правильного ответа
            driver.switchTo().defaultContent();
//            new WebDriverWait(driver, 15).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("RightFrame"));
            driver.switchTo().frame("RightFrame");
            dynamicElement = driver.findElement(By.partialLinkText("Ответ № " + (question.getNumberAnswer(question.getRightAnswer()) + 1)));
            button = dynamicElement.findElement(By.xpath("..")).findElement(By.xpath("preceding-sibling::*"));
            button.click();
            button = driver.findElement(By.tagName("th"));
            button.click();

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            driver.switchTo().defaultContent();
            driver.switchTo().frame("LeftFrame");
            ((JavascriptExecutor) driver).executeScript("GoBackFunction();");

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            driver.switchTo().defaultContent();
            //new WebDriverWait(driver, 20).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ButtFrame"));
            driver.switchTo().frame("ButtFrame");

            button = new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(By.name("ViewSect")));
            button.click();







        }

        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.switchTo().defaultContent();
//        new WebDriverWait(driver, 15).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ButtFrame"));
        driver.switchTo().frame("ButtFrame");
        button = driver.findElement(By.name("Exit"));
        button.click();



        driver.quit();


    }
}
