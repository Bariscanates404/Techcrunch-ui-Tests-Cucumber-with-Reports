package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class TechcruchPage {
    public TechcruchPage(){
        PageFactory.initElements(Driver.getDriver(),this);
    }

    //Article
    @FindBy(xpath = "//article[1]//header//h2//a")
    public WebElement mainPageArticle;

    //Author
    @FindBy(xpath = "//article[1]//header//span//a[1]")
    public WebElement mainPageAuthor;


    //Image
    @FindBy(xpath = "//article[1]//footer//figure[1]")
    public WebElement mainPageImage;

    //detailedNewPage
    @FindBy(css = "article header h1")
    public WebElement detailedNewPageNewTitle;


}
