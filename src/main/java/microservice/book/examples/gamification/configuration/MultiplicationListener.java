/*package microservice.book.examples.gamification.configuration;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import microservice.book.examples.gamification.stream.KafkaInputStream;
import microservices.book.example.gamification.client.dto.MultiplicationResultAttempt;

@Component
@Slf4j
@EnableBinding(KafkaInputStream.class)
public class MultiplicationListener{

	@StreamListener(KafkaInputStream.MULTIPLY_INPUT)
	public void handleMultiplication(@Payload MultiplicationResultAttempt multiplicationResultAttempt){
		log.info("Received multiplicationResultAttempt: {}", multiplicationResultAttempt);
	}
	
}
*/