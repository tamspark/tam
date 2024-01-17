package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.photoWebScraping.ImageInfoDTO;
import com.sparklab.TAM.dto.photoWebScraping.RoomPhotos;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ImageInfoService {

    public List<RoomPhotos> extractImageInfo(String hostUrl) {


        List<String> roomUrls = extractRoomHrefs(hostUrl);

        List<RoomPhotos> rooms = new ArrayList<>();

        for (String url : roomUrls) {
            RoomPhotos roomPhotos = new RoomPhotos();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");

            WebDriver driver = new ChromeDriver(options);
            try {
                List<ImageInfoDTO> imageInfoList = new ArrayList<>();

                // Set the path to your ChromeDriver executable
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Downloads\\chromedriver-win64/chromedriver.exe");


                // Set an implicit wait to give time for dynamic content to load
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

                // Navigate to the URL
                driver.get(url);

                String title = driver.getTitle();
                roomPhotos.setTitle(title);

                roomPhotos.setUrl(url);

                // Find all image elements after the page has fully loaded
                List<WebElement> imageElements = driver.findElements(By.tagName("img"));

                for (WebElement imageElement : imageElements) {
                    String imageUrl = imageElement.getAttribute("src");
                    String fileType = getImageFileType(imageUrl);

                    // Use the new method to get base64 representation of the image
                    String base64Image = getImageAsBase64(imageUrl);

                    // Extracting filename from URL
                    String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

                    ImageInfoDTO imageInfoDTO = new ImageInfoDTO(base64Image, fileType, filename);
                    imageInfoList.add(imageInfoDTO);
                }

                roomPhotos.setPhotos(imageInfoList);

                rooms.add(roomPhotos);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close the WebDriver
                driver.quit();
            }
        }
        return rooms;
    }

    public List<String> extractRoomHrefs(String hostUrl) {

        List<String> roomUrls = new ArrayList<>();
        // Set the path to your ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Downloads\\chromedriver-win64/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        try {
            // Set an implicit wait to give time for dynamic content to load
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Navigate to the host URL
            driver.get(hostUrl);

            // Find all anchor elements with href containing "rooms/"
            List<WebElement> roomElements = driver.findElements(By.cssSelector("a[href*='rooms/']"));

            for (WebElement roomElement : roomElements) {
                String roomHref = roomElement.getAttribute("href");
                String roomUrl = roomHref + "&modal=PHOTO_TOUR_SCROLLABLE";
                roomUrls.add(roomUrl);
            }

        } finally {
            // Close the WebDriver
            driver.quit();
        }

        return roomUrls;
    }

    private String getImageAsBase64(String imageUrl) throws IOException {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            byte[] imageBytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }

    private String getImageFileType(String imageUrl) {
        // Extracting file extension from URL
        String fileExtension = imageUrl.substring(imageUrl.lastIndexOf('.') + 1).toLowerCase();

        // Some additional mapping for common image file types
        switch (fileExtension) {
            case "jpg":
                return "jpeg";
            default:
                return fileExtension;
        }
    }
}
