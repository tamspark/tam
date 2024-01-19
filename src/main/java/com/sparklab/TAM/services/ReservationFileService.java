package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.OpenAI.File.FileUploadResponse;
import com.sparklab.TAM.dto.calendar.ApartmentCalendarDTO;
import com.sparklab.TAM.dto.calendar.CalendarResponseDTO;
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
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationFileService {

    private final ReservationService reservationService;
    private final ApiCallService apiCallService;


    public String writeApartmentsToFile() {

        LocalDate dateToday = LocalDate.now();
        LocalDate dateAfterThreeMonths = dateToday.plusMonths(3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the dates into strings
        String formattedDateToday = dateToday.format(formatter);
        String formattedDateAfterThreeMonths = dateAfterThreeMonths.format(formatter);
        
        List<ApartmentCalendarDTO> apartments = getReservations(formattedDateToday, formattedDateAfterThreeMonths);

        String directoryPath = "dataFiles";
        String fileName = "hostDataFile.txt";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Creates directories including any necessary but nonexistent parent directories.
        }

        // Create the file
        File file = new File(directory, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write the header
            writer.write("GuestName\t" +
                    "Contact\t" +
                    "NumberOfAdults\t" +
                    "NumberOfChildren\t" +
                    "NumberOfInfants\t" +
                    "ArrivalDate\t" +
                    "DepartureDate\t" +
                    "NumberOfNights\t" +
                    "BookedDate\t" +
                    "ListingName\t" +
                    "ChannelName\n");

            // Write apartment data
            for (ApartmentCalendarDTO apartment : apartments) {
                for (CalendarResponseDTO reservation : apartment.getReservations()) {
                    // Extract relevant reservation data
                    String guestName = reservation.getGuestName();
                    String contact = reservation.getEmail(); // Assuming email is used as contact
                    int adults = reservation.getAdults();
                    int children = reservation.getChildren();
                    int infants = 0; // Assuming infants are not provided in the data
                    String startDate = reservation.getArrival();
                    String endDate = reservation.getDeparture();
                    // Calculate nights (assuming dates are in ISO format)
                    long nights = java.time.temporal.ChronoUnit.DAYS.between(
                            java.time.LocalDate.parse(startDate),
                            java.time.LocalDate.parse(endDate)
                    );
                    String bookedDate = reservation.getCreatedAt();
                    String listing = apartment.getApartmentName();
                    String channelName = reservation.getChannel().getName();

                    // Write the data to the file
                    writer.write(String.format("%s\t%s\t%d\t%d\t%d\t%s\t%s\t%d\t%s\t%s\t%s\n",
                            guestName, contact, adults, children, infants, startDate, endDate, nights, bookedDate, listing, channelName));
                }
            }

            writer.close();
            System.out.println("File created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadFile();
    }

    private String uploadFile() {
        //Refactored
        String apiUrl = "https://api.openai.com/v1/files";

        File fileToUpload = new File("dataFiles/hostDataFile.txt");


        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(fileToUpload));
        builder.part("purpose", "assistants");


        FileUploadResponse response = apiCallService.postCallMultipartOpenAI(apiUrl, FileUploadResponse.class, builder);

        return response.getId();
    }


    private List<ApartmentCalendarDTO> getReservations(String fromDate, String toDate) {
        return reservationService.getAllReservationsByDateCalendar("2", fromDate, toDate);
    }

}
