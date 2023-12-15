package vttp.ssf.assessment.eventmanagement;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vttp.ssf.assessment.eventmanagement.models.Event;
import vttp.ssf.assessment.eventmanagement.repositories.RedisRepository;
import vttp.ssf.assessment.eventmanagement.services.DatabaseService;

@SpringBootApplication
public class EventmanagementApplication implements CommandLineRunner {

	@Autowired
	DatabaseService dbSvc;

	@Autowired
	RedisRepository redisRepo;

	public static void main(String[] args) {
		SpringApplication.run(EventmanagementApplication.class, args);
	}
	
	// TODO: Task 1
	@Override
	public void run(String... arg) throws FileNotFoundException{
		String fileName = "/Users/aliciaong/Downloads/vttp_batch4_assessment-main/vttp2023-batch4-ssf-assessment/events.json";
		List<Event> events = dbSvc.readFile(fileName);
		System.out.println("---events.json file has been read---");
		System.out.println(events);

		System.out.println("---Saving events.json file to redis---");
		for (Event event: events){
            redisRepo.saveRecord(event);
        }
		
		System.out.println("list size: "+redisRepo.getNumberOfEvents());
		System.out.println("1: " + redisRepo.getEvent(1));
	}

}
