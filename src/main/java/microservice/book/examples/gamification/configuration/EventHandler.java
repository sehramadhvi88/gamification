package microservice.book.examples.gamification.configuration;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import microservice.book.examples.gamification.event.MultiplicationSolvedEvent;
import microservice.book.examples.gamification.service.GameService;
import microservice.book.examples.gamification.stream.KafkaInputStream;

@Slf4j
@Component
@EnableBinding(KafkaInputStream.class)
public class EventHandler {
	
	private GameService gameService;
		
	EventHandler(final GameService gameService) {
		this.gameService = gameService;		
	}	
	
	@StreamListener(KafkaInputStream.MULTIPLY_INPUT)
	void handleMultiplicationSolved(@Payload final MultiplicationSolvedEvent event) {
	 log.info("Multiplication Solved Event received: {}",event.getMultiplicationResultAttemptId());
	 	try {
	 		gameService.newAttemptForUser(event.getUserId(),event.getMultiplicationResultAttemptId(),event.isCorrect());
	 } catch (final Exception e) {
		 log.error("Error when trying to process MultiplicationSolvedEvent", e);
	 		 
	 	}
	 }			
	
}
