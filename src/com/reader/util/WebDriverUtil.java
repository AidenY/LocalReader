
package com.reader.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

public class WebDriverUtil {

	Logger logger = Logger.getLogger(WebDriverUtil.class.getName());
	private WebDriver driver = null;

	public static void main(String[] args) {
		WebDriverUtil driverUtil = new WebDriverUtil();
		driverUtil.navigateToUrl("https://www.baidu.com");
	}

	public WebDriverUtil() {

	}

	public WebDriverUtil(WebDriver driver) {
		this.driver = driver;
	}

	private void setDriver(WebDriver driver) {
		if (driver == null) {
			logger.error("please set up driver before store driver");
		}
		this.driver = driver;
	}

	private WebDriver getDriver() {
		if (driver == null) {
			driverInitialization();
		}
		return driver;
	}

	private void driverInitialization() {
		System.setProperty("webdriver.gecko.driver", "res\\configs\\geckodriver.exe");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		if (!FileUtil.isFileExist("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe")) {
			try {
				PropertiesUtil propertiesUtil = new PropertiesUtil("runtime.properties");
				propertiesUtil.getValue("firefox.exe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.setProperty("webdriver.firefox.bin", "");
		}

		setDriver(new FirefoxDriver(capabilities));
		driver.manage().window().maximize();
		logger.info("Driver Initialization");
	}

	public void quitDriver() {
		if (driver != null) {
			driver.quit();
		}
	}

	public void navigateToUrl(String url) {

		getDriver().get(url);
		logger.info("Open URL = [" + url + "]");
	}

	public void waitTillObjectDisappear(By by) {

		boolean isFoundObject = true;
		int intTryTime = 1;
		while (isFoundObject && intTryTime < 30) {
			try {
				WebElement obj = driver.findElement(by);
				if (obj == null) {
					isFoundObject = false;
				} else {
					intTryTime++;
				}
			} catch (NoSuchElementException e) {
				isFoundObject = false;
			}
		}
		if (intTryTime >= 30) {
			logger.warn("Time out to wait object disappear, By = [" + by.toString() + "]");
		}

	}

	public void waitTillObjectAppear(By by) {
		boolean isFoundObject = false;
		int intTryTime = 1;
		while (!isFoundObject && intTryTime < 30) {
			try {
				WebElement obj = driver.findElement(by);

				if (obj != null && obj.isDisplayed()) {
					isFoundObject = true;
					break;
				}
			} catch (NoSuchElementException e) {
				intTryTime++;
			}
			int sleepTime = 200 * intTryTime;
			if (sleepTime > 2000) {
				sleepTime = 2000;
			}
			sleep(sleepTime);

		}
		if (intTryTime >= 30) {
			logger.warn("Time out to wait object appear, By = [" + by.toString() + "]");
		}

	}

	// WebElement Action

	private void actions(String actions, By by, String value) {
		waitTillObjectAppear(by);
		WebElement element = getElement(by);
		if (element != null) {

			if (actions.toLowerCase().trim().equals("sendkeys")) {
				element.sendKeys(value);
			} else if (actions.toLowerCase().trim().equals("click")) {
				element.click();
			} else if (actions.toLowerCase().trim().equals("selectbyvisiabletext")) {
				Select select = new Select(getElement(by));
				select.selectByVisibleText(value);
			} else if (actions.toLowerCase().trim().equals("selectbyindex")) {
				Select select = new Select(getElement(by));
				select.selectByIndex(Integer.parseInt(value));
			} else if (actions.toLowerCase().trim().equals("cleartext")) {
				element.clear();
			} else if (actions.toLowerCase().trim().equals("submit")) {
				element.submit();
			}

			else {
				logger.warn("No Action available, Action = [" + actions + "]");
			}

			// unHighlightElement(element);
		} else {
			logger.error("Action = [" + actions + "], Unable locate the element on location : [" + by.toString() + "]");
		}
	}

	public void sendKeys(By by, String value) {
		logger.trace("Send Keys to text box, By = [" + by + "], Value = [" + value + "]");
		actions("SendKeys", by, value);
	}

	public void clickButton(String byText) {
		this.clickButton(getBy(byText));
	}

	public void clickButton(By by) {
		logger.trace("Click Button, By [" + by + "]");
		actions("click", by, "");
	}

	public void clearText(By by) {
		actions("cleartext", by, "");
	}

	public void acceptAlert() {
		if (isAlertPresent()) {
			driver.switchTo().alert().accept();
		} else {

		}

	}

	public void select(By by, String strVisibleText) {
		actions("SelectByVisiableText", by, strVisibleText);

	}

	public void select(String byText, int index) {
		this.select(getBy(byText), index);
	}

	public void select(By by, int index) {
		actions("SelectByIndex", by, index + "");
	}

	public String getElementText(By by) {
		WebElement element = getElement(by);
		return element.getText();
	}

	public WebElement getElement(String objectIndex) {
		return getElement(getBy(objectIndex));
	}

	public WebElement getElement(By by, boolean isWait) {

		if (isWait) {
			waitTillObjectAppear(by);
		}

		WebElement element = null;
		try {
			element = driver.findElement(by);
			// highlight element
			highLightElement(element);
		} catch (NoSuchElementException e) {

		} catch (Exception e) {

		}

		return element;
	}

	protected void highLightElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "border: 2px solid yellow;");
		// (new Thread(new DisHighlight(element, js, global))).start();
	}

	protected void unHighlightElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
	}

	class DisHighlight implements Runnable {
		WebElement element = null;
		JavascriptExecutor javascriptExecutor = null;

		public DisHighlight(WebElement element, JavascriptExecutor js) {
			this.element = element;
			this.javascriptExecutor = js;

		}

		public void run() {
			try {
				Thread.sleep(3000);

				if (element.isDisplayed()) {
					javascriptExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {

			}

		}

	}

	public boolean isElementExist(By by) {
		return isElementExist(by, false);
	}

	public boolean isElementExist(By by, boolean isWait) {

		WebElement element = getElement(by, isWait);
		if (element == null) {
			return false;
		} else {
			return true;
		}

	}

	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			logger.error("Sleep : " + millis);
		}

	}

	public void switchToIframe(WebElement element) {
		driver.switchTo().frame(element);
	}

	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	public void sendKeys(String objectIndex, String value) {
		By by = getBy(objectIndex);
		this.sendKeys(by, value);
	}

	public By getBy(String objectIndex) {
		By by = null;
		if (objectIndex.contains("=") && !objectIndex.contains("@")) {
			String[] arrObj = objectIndex.split("=");
			String value = arrObj[1];
			String key = arrObj[0];
			if (arrObj[0].equals("name")) {
				by = By.name(value);
			} else if (key.equals("css")) {
				by = By.cssSelector(value);
			} else if (key.equals("xpath")) {
				by = By.xpath(value);
			} else if (key.equals("link")) {
				by = By.linkText(value);
			} else if (key.equals("id")) {
				by = By.id(value);
			}
		} else {
			by = By.xpath(objectIndex);
		}

		return by;
	}

	public void click(String objectIndex) {
		this.clickButton(getBy(objectIndex));
	}

	public void waitTillObjectDisappear(String objectIndex) {
		this.waitTillObjectDisappear(getBy(objectIndex));

	}

	public void waitTillObjectAppear(String objectIndex) {
		this.waitTillObjectAppear(getBy(objectIndex));

	}

	public String getElementText(String objectIndex) {
		return getElementText(getBy(objectIndex));
	}

	public String getTableText(String xpathOfTable, int row, int column) {
		String xpathOfElement = xpathOfTable + "tr[" + row + "]/td[" + column + "]";
		return getElementText(xpathOfElement);
	}

	public WebElement getElement(By by) {
		return getElement(by, true);
	}

	public void clickIfExist(String objectIndex) {
		if (isElementExist(getBy(objectIndex))) {
			click(objectIndex);
		} else {
			logger.warn("CLICK ACTION, Objects NOT Existing. Object Index = [" + objectIndex + "]");
		}

	}

	public boolean isElementExist(String objectIndex) {
		return isElementExist(getBy(objectIndex));
	}

	public boolean isElementExist(String objectIndex, boolean isWait) {
		return isElementExist(getBy(objectIndex), isWait);
	}

	public void sendkeysByKeyboard(WebElement element, String value) {
		Actions actions = new Actions(driver);
		actions.sendKeys(element, value).perform();
	}

	public void sendkeysByKeyboard(String objectIndex, String value) {
		sendkeysByKeyboard(getElement(objectIndex), value);
	}

	public void clickByActions(String objectIndex) {
		Actions actions = new Actions(driver);
		actions.click(getElement(objectIndex)).perform();
	}

	public void waitTillObjectEquals(String objectIndex, String val) {
		int intTry = 1;
		while (isElementExist(objectIndex) && intTry < 30) {
			if (getElementText(objectIndex).trim().equals(val.trim())) {
				break;
			}
			intTry++;
		}
		if (intTry == 30) {
			logger.warn("Wait Till Object Not Equals [" + val + "], Time Out!");
		}

	}

	public void waitTillObjectNotEquals(String objectIndex, String val) {
		int intTry = 1;
		while (isElementExist(objectIndex) && intTry < 30) {
			if (!getElementText(objectIndex).trim().equals(val.trim())) {
				break;
			}
			intTry++;
			sleep(1000);
		}
		if (intTry == 30) {
			logger.warn("Wait Till Object Not Equals [" + val + "], Time Out!");
		}
	}

	public void enter() {
		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.ENTER).perform();

	}

	public int getSelectMaxIndexs(String string) {
		Select select = new Select(getElement(getBy(string)));
		List<WebElement> options = select.getOptions();

		return options.size();
	}

	public void submit(String objectIndex) {
		this.submit(getBy(objectIndex));

	}

	private void submit(By by) {
		this.actions("submit", by, "");
	}

	public String takeScreenShot(String screenshotName) {
		try {
			String fileFolder = "testresult" + File.separator + "screenshots" + File.separator;
			String filePath = fileFolder + screenshotName + "_" + DateUtil.getDateID() + ".jpg";

			File file = new File(filePath);
			if (!file.exists()) {
				logger.info("File created " + file);
				String parent = file.getParent();
				FileUtil.createNewDirIfNotExist(parent);
				// file.mkdirs();
			}

			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File targetFile = new File(filePath);
			FileUtil.copyFile(screenshotFile, targetFile);

			return filePath;
		} catch (Exception e) {
			logger.error("An exception occured while taking screenshot " + e.getCause());
			return null;
		}
	}

	public String getSelectedText(String string) {
		Select select = new Select(getElement(getBy(string)));
		return select.getAllSelectedOptions().get(0).getText();
	}
}
