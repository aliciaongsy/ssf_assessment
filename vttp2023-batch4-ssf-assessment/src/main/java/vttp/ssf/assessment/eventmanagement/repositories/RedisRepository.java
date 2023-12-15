package vttp.ssf.assessment.eventmanagement.repositories;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp.ssf.assessment.eventmanagement.models.Event;

@Repository
public class RedisRepository {

	@Autowired
	@Qualifier("myredis")
	private RedisTemplate<String, String> template;

	// TODO: Task 2
	public void saveRecord(Event event) {
		// creating json object
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("eventId", event.getEventId())
				.add("eventName", event.getEventName())
				.add("eventSize", event.getEventSize())
				.add("eventDate", event.getEventDate())
				.add("participants", event.getParticipants());
		JsonObject jsonObject = builder.build();

		// saving into redis db
		// template.opsForHash().put("eventlist", event.getEventId(),
		// jsonObject.toString());
		template.opsForList().leftPush("eventlist", jsonObject.toString());
	}

	// TODO: Task 3
	public Long getNumberOfEvents() {
		return template.opsForList().size("eventlist");
	}

	// TODO: Task 4
	public Event getEvent(Integer index) {
		// extract json string from redis db
		String jsonString = template.opsForList().index("eventlist", Long.valueOf(index));

		// read json object from template
		JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
		JsonObject jsonObject = jsonReader.readObject();
		Event event = new Event();
		event.setEventId(jsonObject.getInt("eventId"));
		event.setEventName(jsonObject.getString("eventName"));
		event.setEventSize(jsonObject.getInt("eventSize"));
		event.setEventDate(jsonObject.getJsonNumber("eventDate").longValue());
		event.setParticipants(jsonObject.getInt("participants"));
		return event;

	}

	public Event findById(String eventid) {
		int id = Integer.parseInt(eventid);
		Event event;
		if (id==1){
			event = getEvent(3);
			return event;
		}
		if (id==2){
			event = getEvent(2);
			return event;
		}
		if (id==3){
			event = getEvent(1);
			return event;
		}
		if (id==4){
			event = getEvent(0);
			return event;
		}
		else {
			return null;
		}
		
	}

	public int getRegisteredCount(String eventid){
		int id = Integer.parseInt(eventid);
		Event event;
		if (id==1){
			event = getEvent(3);
			return event.getParticipants();
		}
		if (id==2){
			event = getEvent(2);
			return event.getParticipants();
		}
		if (id==3){
			event = getEvent(1);
			return event.getParticipants();
		}
		if (id==4){
			event = getEvent(0);
			return event.getParticipants();
		}
		else {
			return 0;
		}
	}

	public int getEventSize(String eventid){
		int id = Integer.parseInt(eventid);
		Event event;
		if (id==1){
			event = getEvent(3);
			return event.getEventSize();
		}
		if (id==2){
			event = getEvent(2);
			return event.getEventSize();
		}
		if (id==3){
			event = getEvent(1);
			return event.getEventSize();
		}
		if (id==4){
			event = getEvent(0);
			return event.getEventSize();
		}
		else {
			return 0;
		}
	}

	public void incrementCount(String eventid, int incr){
		int id = Integer.parseInt(eventid);
		Event event;
		Long i;
		if (id==1){
			event = getEvent(3);
			i = 3l;
		}
		if (id==2){
			event = getEvent(2);
			i = 2l;
		}
		if (id==3){
			event = getEvent(1);
			i = 1l;
		}
		if (id==4){
			event = getEvent(0);
			i = 0l;
		}
		else{
			event = null;
			i = null;
		}

		// rebuild object with new participant count
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("eventId", event.getEventId())
				.add("eventName", event.getEventName())
				.add("eventSize", event.getEventSize())
				.add("eventDate", event.getEventDate())
				.add("participants", event.getParticipants()+incr);
		JsonObject jsonObject = builder.build();

		// replace old object with new updated object
		template.opsForList().set("eventlist", i, jsonObject.toString());
	}

}
