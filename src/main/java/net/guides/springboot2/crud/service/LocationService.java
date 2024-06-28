/*
package net.guides.springboot2.crud.service;

import net.guides.springboot2.crud.model.Applicant;
import net.guides.springboot2.crud.repository.AddressRepository;
import net.guides.springboot2.crud.repository.ApplicantRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LocationService {

    private final AddressRepository addressRepository;
    private final JavaMailSender mailSender;
    private final ApplicantRepository applicantRepository;

    private static final double EARTH_RADIUS = 6371e3; // meters

    @Value("${ipinfo.token}")
    private String ipinfoToken;

    private static final String IP_API_URL = "http://ip-api.com/json/";

    @Autowired
    public LocationService(AddressRepository addressRepository, JavaMailSender mailSender, ApplicantRepository applicantRepository) {
        this.addressRepository = addressRepository;
        this.mailSender = mailSender;
        this.applicantRepository = applicantRepository;
    }

    public boolean isWithinProximity(double userLat, double userLng, double fixedLat, double fixedLng) {
        double dLat = Math.toRadians(fixedLat - userLat);
        double dLng = Math.toRadians(fixedLng - userLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(fixedLat))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance <= 100; // 100 meters
    }

    public void sendAlert(String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("chhaganp2808@gmail.com"); // Replace with recipient's email
        mailMessage.setSubject("Proximity Alert");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

    public String getLocationFromIp(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String url = IP_API_URL + ip;
        String response = restTemplate.getForObject(url, String.class);

        JSONObject jsonObject = new JSONObject(response);
        String city = jsonObject.optString("city");
        String region = jsonObject.optString("region");
        String country = jsonObject.optString("country");

        return String.format("%s, %s, %s", city, region, country);
    }

    public String getDistanceAndStatus(String applicant, double reqLat, double reqLon) {
        Applicant applicantData = applicantRepository.findByApplicant(applicant);
        if (applicantData == null) {
            return "Applicant not found";
        }

        double dbLat = applicantData.getLatitude();
        double dbLon = applicantData.getLongitude();

        double distance = calculateDistance(reqLat, reqLon, dbLat, dbLon);
        String status = (distance <= 0.1) ? "match" : "No match"; // 0.1 km = 100 meters

        if ("No match".equals(status)) {
            sendAlertToAdmin(applicant, reqLat, reqLon, dbLat, dbLon, distance);
        }

        return status;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Convert to km
        return distance;
    }

    private void sendAlertToAdmin(String applicant, double reqLat, double reqLon, double dbLat, double dbLon, double distance) {
        // Implement the alerting mechanism, e.g., sending an email or logging
        System.out.println("Alert: Applicant " + applicant + " location mismatch.");
    }
}
*/
package net.guides.springboot2.crud.service;

import net.guides.springboot2.crud.model.Applicant;
import net.guides.springboot2.crud.repository.AddressRepository;
import net.guides.springboot2.crud.repository.ApplicantRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LocationService {

    private final AddressRepository addressRepository;
    private final ApplicantRepository applicantRepository;

    private static final double EARTH_RADIUS = 6371e3; // meters

    @Autowired
    public LocationService(AddressRepository addressRepository, ApplicantRepository applicantRepository) {
        this.addressRepository = addressRepository;
        this.applicantRepository = applicantRepository;
    }

    public boolean isWithinProximity(double userLat, double userLng, double fixedLat, double fixedLng) {
        double dLat = Math.toRadians(fixedLat - userLat);
        double dLng = Math.toRadians(fixedLng - userLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(fixedLat))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance <= 100; // 100 meters
    }

    public String getLocationFromIp(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://ip-api.com/json/" + ip;
        String response = restTemplate.getForObject(url, String.class);

        JSONObject jsonObject = new JSONObject(response);
        String city = jsonObject.optString("city");
        String region = jsonObject.optString("region");
        String country = jsonObject.optString("country");

        return String.format("%s, %s, %s", city, region, country);
    }

    public String getDistanceAndStatus(String applicant, double reqLat, double reqLon) {
        Applicant applicantData = applicantRepository.findByApplicant(applicant);
        if (applicantData == null) {
            return "Applicant not found";
        }

        double dbLat = applicantData.getLatitude();
        double dbLon = applicantData.getLongitude();

        double distance = calculateDistance(reqLat, reqLon, dbLat, dbLon);
        String status = (distance <= 0.1) ? "match" : "No match"; // 0.1 km = 100 meters

        if ("No match".equals(status)) {
            // Optionally log the mismatch
            System.out.println("Alert: Applicant " + applicant + " location mismatch.");
        }

        return status;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Convert to km
        return distance;
    }

    //latest
    public boolean isWithinProximity1(double userLat, double userLng, double fixedLat, double fixedLng) {
        double dLat = Math.toRadians(fixedLat - userLat);
        double dLng = Math.toRadians(fixedLng - userLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(fixedLat))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance <= 100; // 100 meters
    }

    public String checkProximity(double userLat, double userLng) {
        // Coordinates for Ackruti Star (Andheri East)
        double ackrutiLat = 19.118469599258226;
        double ackrutiLng = 72.87034380917989;

        // Coordinates for Airport
        double airportLat = 19.08861111111111;
        double airportLng = 72.86805555555554;

       /* if (isWithinProximity1(userLat, userLng, ackrutiLat, ackrutiLng)) {
           String result= "Within proximity of Ackruti Star (Andheri East)";
           return result;
        } else if (isWithinProximity1(userLat, userLng, airportLat, airportLng)) {
            return "Within proximity of Airport";
        } else {
            return "Not within proximity of any known locations";
        }}*/
        //}
        if (isWithinProximity1(userLat, userLng, ackrutiLat, ackrutiLng)) {
            return "Within proximity of Ackruti Star (Andheri East)";
        } else if (isWithinProximity1(userLat, userLng, airportLat, airportLng)) {
            return "Within proximity of Airport";
        } else {
            return "Not within proximity of any known locations";
        }}

    }

