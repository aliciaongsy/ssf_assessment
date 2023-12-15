package vttp.ssf.assessment.eventmanagement.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import vttp.ssf.assessment.eventmanagement.models.Event;
import vttp.ssf.assessment.eventmanagement.repositories.RedisRepository;

@Service
public class DatabaseService {

    @Autowired
    RedisRepository redisRepo;

    // TODO: Task 1
    public List<Event> readFile(String fileName) throws FileNotFoundException {

        File file = new File(fileName);

        InputStream is = new FileInputStream(file);

        JsonReader jsonReader = Json.createReader(is);
        JsonArray jsonArray = jsonReader.readArray();

        List<Event> events = jsonArray.stream()
                .map(j -> j.asJsonObject())
                .map(o -> {
                    Event event = new Event();
                    event.setEventId(o.getInt("eventId"));
                    event.setEventName(o.getString("eventName"));
                    event.setEventSize(o.getInt("eventSize"));
                    event.setEventDate(o.getJsonNumber("eventDate").longValue());
                    System.out.println("date in long:" + event.getEventDate());
                    event.setParticipants(o.getInt("participants"));
                    return event;
                })
                .toList();

        return events;
    }

    public List<Event> retrieveList() {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < redisRepo.getNumberOfEvents(); i++) {
            // System.out.println(i + ":" + redisRepo.getEvent(i));
            events.add(redisRepo.getEvent(i));
        }
        return events;
    }

    public String convertDate(Long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(date);
    }

    public boolean countCheck(String id, int numberToReg){
        int eventSize = redisRepo.getEventSize(id);
        int participantsReg = redisRepo.getRegisteredCount(id);
        if (eventSize >= (participantsReg + numberToReg)){
            return true;
        }
        else {
            return false;
        }
    }

    public int calculateAge(Date birthDate, Date currentDate) {
        LocalDate dob = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate curr = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if ((dob != null) && (curr != null)) {
            return Period.between(dob, curr).getYears();
        } else {
            return 0;
        }
    }
}
