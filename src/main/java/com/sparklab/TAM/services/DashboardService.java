package com.sparklab.TAM.services;

import com.sparklab.TAM.configuration.SmoobuConfiguration;
import com.sparklab.TAM.dto.Dashboard.DashboardDTO;
import com.sparklab.TAM.dto.Dashboard.OccupancyRevenueReportDTO;
import com.sparklab.TAM.dto.GetRatesDTO;
import com.sparklab.TAM.dto.report.Date;
import com.sparklab.TAM.dto.reservation.ReservationDTO;
import com.sparklab.TAM.dto.smoobu.SmoobuAllReservationsResponseDTO;
import com.sparklab.TAM.dto.smoobu.SmoobuShortApartmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ApartmentAvailabilityService apartmentAvailabilityService;
    private final ApartmentService apartmentService;
    private final ApiCallService apiCallService;
    private final SmoobuConfiguration smoobuConfiguration;


    private int numberOfApartments;
    private List<ReservationDTO> reservations;


    public DashboardDTO generateDashboard(String userId, String period) {

        DashboardDTO dashboard = new DashboardDTO();

        LocalDate startDate = null;
        LocalDate endDate = null;


        if (period.equals("thismonth")) {
            // Get the first day of the current month
            startDate = LocalDate.now().withDayOfMonth(1);
            // Get the last day of the current month
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }
        if (period.equals("nextmonth")) {
            // Get the first day of the next month
            startDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);
            // Get the last day of the next month
            endDate = LocalDate.now().plusMonths(1).withDayOfMonth(LocalDate.now().plusMonths(1).lengthOfMonth());
        }
        if (period.equals("plusthreemonths")) {
            // Get the first day of the month after next two months
            startDate = LocalDate.now();
            // Get the last day of the month after next two months
            endDate = LocalDate.now().plusMonths(3).withDayOfMonth(LocalDate.now().plusMonths(3).lengthOfMonth());
        }
        reservations = getReservations(userId, startDate, endDate);

        Double occupancyPercentage = generateOccupancyForPeriod(userId, startDate, endDate);
        double roundedOccupancy = Math.round(occupancyPercentage * 100.0) / 100.0;
        dashboard.setOccupancyPercentage(roundedOccupancy);

        List<OccupancyRevenueReportDTO> occupancyRevenueReport = generateOccupancyRevenueReport(userId, startDate, endDate);
        dashboard.setOccupancyRevenueReport(occupancyRevenueReport);

        Map<String, Double> nightPortalReport = generateNightsPortalReport(reservations);
        dashboard.setNightsPortalReport(nightPortalReport);

        return dashboard;
    }

    private List<OccupancyRevenueReportDTO> generateOccupancyRevenueReport(String userId, LocalDate startDate, LocalDate endDate) {

        List<OccupancyRevenueReportDTO> occupancyRevenueReport = new ArrayList<>();

        List<Map<LocalDate, LocalDate>> monthsList = generateMonthsList(startDate, endDate);

        for (Map<LocalDate, LocalDate> dateRange : monthsList) {
            for (Map.Entry<LocalDate, LocalDate> month : dateRange.entrySet()) {
                occupancyRevenueReport.add(generateMonthReport(userId, month.getKey(), month.getValue()));
            }
        }


        return occupancyRevenueReport;

    }

    private OccupancyRevenueReportDTO generateMonthReport(String userId, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth) {

        OccupancyRevenueReportDTO occupancyRevenueReportPerMonth = new OccupancyRevenueReportDTO();

        String monthYear = firstDayOfMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        occupancyRevenueReportPerMonth.setMonth(monthYear);

        Double occupancy = generateOccupancyForPeriod(userId, firstDayOfMonth, lastDayOfMonth);
        double roundedOccupancy = Math.round(occupancy * 100.0) / 100.0;

        Double revenue = generateRevenueForPeriod(firstDayOfMonth, lastDayOfMonth);
        double roundedRevenue = Math.round(revenue * 100.0) / 100.0;

        occupancyRevenueReportPerMonth.setData(roundedRevenue, roundedOccupancy);

        return occupancyRevenueReportPerMonth;
    }

    private static List<Map<LocalDate, LocalDate>> generateMonthsList(LocalDate startDate, LocalDate endDate) {
        List<Map<LocalDate, LocalDate>> dateRanges = new ArrayList<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            LocalDate endOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

            Map<LocalDate, LocalDate> monthDateRange = new HashMap<>();
            monthDateRange.put(currentDate, endOfMonth);
            dateRanges.add(monthDateRange);

            if (currentDate.isBefore(endDate)) {
                currentDate = endOfMonth.plusDays(1);
            }
        }

        return dateRanges;
    }

    private Double generateOccupancyForPeriod(String userId, LocalDate startDate, LocalDate endDate) {

        int notAvailableDates = 0;

        List<Date> dates = getDates(userId, startDate, endDate);

        for (Date date : dates) {
            notAvailableDates += date.getAvailable() != 0 ? 0 : 1;
        }

        return ((double) notAvailableDates / dates.size()) * 100;

    }


    private Double generateRevenueForPeriod(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Double> datePriceMap = createMapFromReservations();
        return calculateRevenue(datePriceMap, startDate, endDate);
    }


    private GetRatesDTO getAvailability(String userId, LocalDate startDate, LocalDate endDate) {

        List<Integer> apartmentIds = apartmentService
                .getAllApartments(userId)
                .getApartments()
                .stream()
                .map(SmoobuShortApartmentDTO::getId)
                .toList();

        numberOfApartments = apartmentIds.size();

        return apartmentAvailabilityService.getAllRates(userId, startDate, endDate, apartmentIds);
    }

    private List<Date> getDates(String userId, LocalDate startDate, LocalDate endDate) {
        GetRatesDTO availability = getAvailability(userId, startDate, endDate);

        List<Date> dates = new ArrayList<>();

        availability.getData().forEach((apartmentId, dateDataMap) -> {
            dateDataMap.forEach((date, apartmentData) -> {
                dates.add(apartmentData);
            });
        });

        return dates;
    }

    private Map<String, Double> generateNightsPortalReport(List<ReservationDTO> reservations) {

        Map<String, Integer> channelCountMap = new HashMap<>();

        // Count the occurrences of each channel
        for (ReservationDTO reservation : reservations) {
            String channel = reservation.getChannel().getName();
            channelCountMap.put(channel, channelCountMap.getOrDefault(channel, 0) + 1);
        }

        // Calculate percentages
        int totalReservations = reservations.size();
        Map<String, Double> channelPercentageMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : channelCountMap.entrySet()) {
            String channel = entry.getKey();
            int count = entry.getValue();
            double percentage = (count * 100.0) / totalReservations;
            double roundedPercentage = Math.round(percentage * 100.0) / 100.0;

            channelPercentageMap.put(channel, roundedPercentage);
        }

        return channelPercentageMap;

    }

    private double calculateRevenue(Map<LocalDate, Double> datePriceMap, LocalDate startDate, LocalDate endDate) {
        double revenue = 0.0;

        Map<LocalDate, Double> mergedMap = new HashMap<>();
        datePriceMap.forEach((key, value) -> mergedMap.merge(key, value, Double::sum));

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (mergedMap.containsKey(currentDate)) {
                revenue += mergedMap.get(currentDate);
            }

            currentDate = currentDate.plusDays(1);
        }

        return revenue;
    }

    public Map<LocalDate, Double> createMapFromReservations() {
        Map<LocalDate, Double> datePriceMap = new HashMap<>();

        for (ReservationDTO reservation : reservations) {
            LocalDate arrivalDate = LocalDate.parse(reservation.getArrival());
            LocalDate departureDate = LocalDate.parse(reservation.getDeparture());

            while (!arrivalDate.isAfter(departureDate)) {
                datePriceMap.put(arrivalDate, reservation.getPrice() / getNumberOfNights(reservation));
                arrivalDate = arrivalDate.plusDays(1);
            }
        }

        return datePriceMap;
    }

    private static int getNumberOfNights(ReservationDTO reservation) {
        // Assuming you have a method to calculate the number of nights
        // between arrival and departure dates
        return (int) (LocalDate.parse(reservation.getDeparture()).toEpochDay() -
                LocalDate.parse(reservation.getArrival()).toEpochDay());
    }


    private List<ReservationDTO> getReservations(String userId, LocalDate startDate, LocalDate endDate) {
        String apiUrl = smoobuConfiguration.getApiURI() + "reservations?from=" + startDate + "&to=" + endDate;

        Long parseUserId = Long.valueOf(userId);
        List<ReservationDTO> reservations = apiCallService.getMethod(apiUrl, SmoobuAllReservationsResponseDTO.class, parseUserId).getBookings();

        return reservations;
    }

}
