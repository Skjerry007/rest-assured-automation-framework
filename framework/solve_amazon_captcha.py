import sys
import time
import requests
from amazoncaptcha import AmazonCaptcha
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# URL of Amazon error captcha page (example)
amazon_captcha_url = "https://www.amazon.com/errors/validateCaptcha"

# Set up Selenium (headless for automation, remove headless for debugging)
chrome_options = Options()
# chrome_options.add_argument('--headless')  # Remove headless for debugging
chrome_options.add_argument('--no-sandbox')
chrome_options.add_argument('--disable-dev-shm-usage')
chrome_options.add_argument('user-agent=FakeBot/1.0')
# chrome_options.add_argument('--proxy-server=http://190.61.88.147:8080')
driver = webdriver.Chrome(options=chrome_options)

try:
    driver.get("https://www.amazon.in/s?k=laptop")
    driver.get("https://www.amazon.in/s?k=iphone")
    driver.get("https://www.amazon.in/gp/bestsellers")
    for i in range(5):
        driver.refresh()
        time.sleep(1)
    # After this, continue with captcha logic as before
    # Wait for the captcha image to be present (up to 15 seconds)
    wait = WebDriverWait(driver, 15)
    captcha_img_elem = wait.until(
        EC.presence_of_element_located((By.XPATH, "//div[@class = 'a-row a-text-center']//img"))
    )
    captcha_link = captcha_img_elem.get_attribute('src')
    print(f"Captcha link src: {captcha_link}")


    # Solve the captcha using amazoncaptcha
    captcha = AmazonCaptcha.fromlink(captcha_link)
    captcha_value = AmazonCaptcha.solve(captcha)

    print(f"Solved captcha text: {captcha_value}")
   
finally:
    driver.quit() 