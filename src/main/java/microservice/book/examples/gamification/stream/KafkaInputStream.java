package microservice.book.examples.gamification.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaInputStream {

	public static final String MULTIPLY_INPUT = "multiplication_exchange";
	
	@Input(MULTIPLY_INPUT)
	SubscribableChannel getMultiplicationEvent(); 
	
}
