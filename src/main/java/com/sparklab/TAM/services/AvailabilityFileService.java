package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.GetRatesDTO;
import com.sparklab.TAM.dto.OpenAI.File.FileUploadResponse;
import com.sparklab.TAM.dto.OpenAI.UploadJSON.ApartmentDataSimple;
import com.sparklab.TAM.dto.smoobu.SmoobuGetApartmentsResponse;
import com.sparklab.TAM.dto.smoobu.SmoobuShortApartmentDTO;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AvailabilityFileService {

    private ApartmentAvailabilityService apartmentAvailabilityService;
    private ApartmentService apartmentService;
    private ApiCallService apiCallService;

    public List<Map.Entry<String, List<ApartmentDataSimple>>> getRates(String userId, Integer apartId) {


        SmoobuGetApartmentsResponse apartments = apartmentService.getAllApartments(userId);

        if (apartId != null) {
            List<SmoobuShortApartmentDTO> filteredApartment = apartments.getApartments().stream().filter(a -> a.getId() == apartId).toList();

            apartments.setApartments(filteredApartment);
        }

        List<Integer> apartmentIds = apartments.getApartments()
                .stream()
                .map(SmoobuShortApartmentDTO::getId)
                .toList();


        LocalDate dateToday = LocalDate.now();
        LocalDate dateAfterThreeMonths = dateToday.plusMonths(3);

        GetRatesDTO ratesDTO = apartmentAvailabilityService.getAllRates(userId, dateToday, dateAfterThreeMonths, apartmentIds);

        Map<String, List<ApartmentDataSimple>> result = new HashMap<>();

        ratesDTO.getData().forEach((apartmentId, dateDataMap) -> {
            dateDataMap.forEach((date, apartmentData) -> {
                List<ApartmentDataSimple> apartmentDataList = result.computeIfAbsent(date, k -> new ArrayList<>());

                int apartmentIdInt = Integer.parseInt(apartmentId);

                String apartmentName = apartments.getApartments()
                        .stream()
                        .filter(a -> a.getId() == apartmentIdInt)
                        .findFirst()
                        .get()
                        .getName();

                ApartmentDataSimple apartmentDataSimple = new ApartmentDataSimple();
                apartmentDataSimple.setName(apartmentName);
                apartmentDataSimple.setPrice(apartmentData.getPrice());
                apartmentDataSimple.setMinLengthOfStay(apartmentData.getMin_length_of_stay());
                apartmentDataSimple.setAvailable(apartmentData.getAvailable());

                apartmentDataList.add(apartmentDataSimple);
            });
        });


        List<Map.Entry<String, List<ApartmentDataSimple>>> resultList = new ArrayList<>(result.entrySet());

        // Sort the list based on the date
        resultList.sort(Map.Entry.comparingByKey());

        // Display the sorted result
        resultList.forEach(entry -> {
            String date = entry.getKey();
            List<ApartmentDataSimple> apartmentDataList = entry.getValue();
            System.out.println("Date: " + date);
            apartmentDataList.forEach(apartmentDataSimple ->
                    System.out.println("  Apartment: " + apartmentDataSimple.getName() +
                            ", Price: " + apartmentDataSimple.getPrice() +
                            ", Min Length of Stay: " + apartmentDataSimple.getMinLengthOfStay() +
                            ", Available: " + apartmentDataSimple.getAvailable()));
        });


        return resultList;
    }

    public String createFile(Integer apartmentId) {


        List<Map.Entry<String, List<ApartmentDataSimple>>> dataSet = getRates("3", apartmentId);
        String directoryPath = "dataFiles";
        String fileName = "clientDataFile.txt";

        // Create the directory if it doesn't exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Creates directories including any necessary but nonexistent parent directories.
        }

        // Create the file
        File file = new File(directory, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            Map.Entry<String, List<ApartmentDataSimple>> firstRow = dataSet.get(1);

            writer.write("Year\tMonth\tDay\t");
            for (ApartmentDataSimple apartment : firstRow.getValue()) {

                writer.write(apartment.getName().replace(" ", "") +
                        "PricePerNight" +
                        "\t" +
                        apartment.getName().replace(" ", "") +
                        "Availability" +
                        "\t" +
                        apartment.getName().replace(" ", "") +
                        "MinLengthOfStay" +
                        "\t");

            }

            writer.write("\n");

            for (Map.Entry<String, List<ApartmentDataSimple>> row : dataSet) {
                String dateString = row.getKey();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate formattedDate = LocalDate.parse(dateString, formatter);

                int year = formattedDate.getYear();
                int month = formattedDate.getMonthValue();
                int day = formattedDate.getDayOfMonth();


                writer.write(year +
                        "\t" +
                        month + "\t" +
                        day + "\t"
                );

                for (ApartmentDataSimple apartment : row.getValue()) {

                    writer.write(apartment.getPrice() + "\t" +
                            apartment.getAvailable() + "\t" +
                            apartment.getMinLengthOfStay() + "\t"
                    );

                }

                writer.write("\n");

            }
            System.out.println("File uploaded. Time: " + LocalDate.now());

            writer.close();

            return uploadFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Failed";
    }

    private String uploadFile() {
        //Refactored
        String apiUrl = "https://api.openai.com/v1/files";

        File fileToUpload = new File("dataFiles/clientDataFile.txt");


        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(fileToUpload));
        builder.part("purpose", "assistants");


        FileUploadResponse response = apiCallService.postCallMultipartOpenAI(apiUrl, FileUploadResponse.class, builder);

        return response.getId();
    }

}
