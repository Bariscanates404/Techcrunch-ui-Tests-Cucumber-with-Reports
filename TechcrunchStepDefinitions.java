package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import pages.TechcruchPage;
import utilities.Driver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TechcrunchStepDefinitions {

    static final String constantCompanyTag = "| TechCrunch";    // If the company or name changes we have to set the company browser title
    static final int constantCompanyTagLength = 12;             // The length of company browser title
    static int workingLinks = 0;
    TechcruchPage techcrunchPage = new TechcruchPage();
    JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();


    @Given("kullanici techcrunch anasayfasina gider")
    public void kullanici_techcrunch_anasayfasina_gider() {
        Driver.getDriver().get("https://techcrunch.com/");

    }

    @Then("kullanici the latest news listesindeki her haberin en az bir resmi ve yazari oldugunu teyit eder")
    public void kullaniciTheLatestNewsListesindekiHerHaberinEnAzBirResmiVeYazariOldugunuTeyitEder() {
        boolean articleAuthorImageCheck = false;
        for (int i = 1; i <= 20; i++) {
            //to check every article has News title, Author and image. The site has 20 article at the same time.
            String sayac = String.valueOf(i);
            if (Driver.getDriver().findElement(By.xpath("//article[" + sayac + "]//header//h2//a")).isDisplayed()
                    && Driver.getDriver().findElement(By.xpath("//article[" + sayac + "]//header//span//a[1]")).isDisplayed()
                    && Driver.getDriver().findElement(By.xpath("//article[" + sayac + "]//footer//figure[1]")).isDisplayed()) {
                articleAuthorImageCheck = true;
                Assert.assertTrue(articleAuthorImageCheck);
                articleAuthorImageCheck = false;
            } else {
                Assert.assertTrue(articleAuthorImageCheck);
            }
        }
    }


    @And("kullanici the latest news listesindeki herhangi bir habere tiklar")
    public void kullaniciTheLatestNewsListesindekiHerhangiBirHabereTiklar() throws InterruptedException {
        js.executeScript("arguments[0].scrollIntoView();", techcrunchPage.mainPageArticle);
        Thread.sleep(500);
        techcrunchPage.mainPageArticle.click();
    }

    @Then("kullanici acilan haber sayfasinda browser title dogruluk kontrolu yapar")
    public void kullanici_acilan_haber_sayfasinda_browser_title_dogruluk_kontrolu_yapar() {
        String fullContentNewsTitle = techcrunchPage.detailedNewPageNewTitle.getText();
        String browserTitle = Driver.getDriver().getTitle();
        boolean check = false;
        if (browserTitle.substring(browserTitle.length() - constantCompanyTagLength).equals(constantCompanyTag)) {
            browserTitle = browserTitle.substring(0, browserTitle.length() - (constantCompanyTagLength + 1));
            browserTitle = browserTitle.replaceAll("[/r]", "");
            fullContentNewsTitle = fullContentNewsTitle.replaceAll("[/r]", "");
            Assert.assertEquals(fullContentNewsTitle, browserTitle);
        } else {

        }
    }

    @Then("kullanici acilan haber sayfasindaki linklerin kontrolunu yapar")
    public void kullanici_acilan_haber_sayfasindaki_linklerin_kontrolunu_yapar() throws IOException, InterruptedException {

        List<WebElement> linkList = Driver.getDriver().findElements(By.cssSelector(".article-content>p a"));
        for (WebElement links : linkList) {
            String url = links.getAttribute("href");
            verifyLinkActive(url);
            Thread.sleep(100);
        }
        System.out.println("Total link count: " + linkList.size() + "  ||   Working link count: " + workingLinks);
        Assert.assertTrue(linkList.size() == workingLinks);
    }

    public static void verifyLinkActive(String linkUrl) {
        //  We are using this method to read URL responses to check problems in links
        //  Page responses 2XX: generally "OK" - 3XX: relocation/redirect "OK"
        //  -4XX: client error(some of the pages that sends 4XX responses can be work but need attention) - 5XX: server error
        try {
            URL url = new URL(linkUrl);
            HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();
            httpURLConnect.setConnectTimeout(5000);
            httpURLConnect.connect();
            if (httpURLConnect.getResponseCode() >= 1 && httpURLConnect.getResponseCode() <= 399) {
                workingLinks++;
                System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage());
            }
            if (httpURLConnect.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage() + " - " + HttpURLConnection.HTTP_NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("Exception thrown!!");
        }
    }
}



