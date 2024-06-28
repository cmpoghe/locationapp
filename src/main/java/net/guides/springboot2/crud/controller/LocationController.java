package net.guides.springboot2.crud.controller;

import net.guides.springboot2.crud.model.Address;
import net.guides.springboot2.crud.repository.AddressRepository;
import net.guides.springboot2.crud.request.LocationRequest;
import net.guides.springboot2.crud.request.LocationRequests;
import net.guides.springboot2.crud.service.LocationService;
import net.guides.springboot2.crud.util.ProximityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/location")
@CrossOrigin("*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private AddressRepository addressRepository;

    @PostMapping("/check")
    public String checkLocation(@RequestBody LocationRequest locationRequest) {
        Address fixedAddress = addressRepository.findById(1L).orElseThrow(() -> new RuntimeException("Fixed address not found"));
        boolean isWithinProximity = locationService.isWithinProximity(locationRequest.getUserLat(), locationRequest.getUserLng(), fixedAddress.getLatitude(), fixedAddress.getLongitude());

        if (isWithinProximity) {
            return "Location is within proximity";
        } else {
           // locationService.sendAlert("User location is not within the 100-meter proximity.");
            return "Location is not within proximity";
        }
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Endpoint is working!";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String location = locationService.getLocationFromIp(ipAddress);
        return String.format("IP Address: %s, Location: %s", ipAddress, location);
    }

    @GetMapping("/loginbyip")
    public String login(@RequestParam(required = false) String ip, HttpServletRequest request) {
        String ipAddress = (ip != null) ? ip : request.getRemoteAddr();
        String location = locationService.getLocationFromIp(ipAddress);
        return String.format("IP Address: %s, Location: %s", ipAddress, location);
    }

    @GetMapping("/url/{applicant}")
    public String checkLocation(@PathVariable String applicant, @RequestParam double lat, @RequestParam double lon) {
        return locationService.getDistanceAndStatus(applicant, lat, lon);
    }
  /*  @GetMapping("/check-proximity")
    public ResponseEntity<String> checkProximity(@RequestBody LocationRequests locationRequest) {
        double lat = locationRequest.getLat();
        double lon = locationRequest.getLon();
        String result = locationService.checkProximity(lat, lon);
        return ResponseEntity.ok(result);
    }*/
  @GetMapping("/check-proximity")
  public ResponseEntity<ProximityResponse> checkProximity(@RequestParam double lat, @RequestParam double lon) {
        //String result = locationService.checkProximity(lat, lon);
      //return ResponseEntity.ok(result);
      try {
          String message = locationService.checkProximity(lat, lon);
          return ResponseEntity.ok(new ProximityResponse(message));
      } catch (Exception e) {
          e.printStackTrace();
          ProximityResponse errorResponse = new ProximityResponse("Error checking proximity: " + e.getMessage());
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }

  }
}
