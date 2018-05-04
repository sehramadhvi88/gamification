package microservice.book.examples.gamification.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import microservices.book.example.gamification.client.dto.MultiplicationResultAttempt;

public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient  {

	private final RestTemplate restTemplate;
	private final String multiplicationHost;
	
	@Autowired
	public MultiplicationResultAttemptClientImpl(final RestTemplate restTemplate,@Value("multiplicationHost") final String multiplicationHost) {
		this.restTemplate = restTemplate;
		this.multiplicationHost=multiplicationHost;
	}
	
	@Override
	public MultiplicationResultAttempt retrieveMultiplicationResultAttemptbyId(Long multiplicationId) {
		// TODO Auto-generated method stub
		return restTemplate.getForObject(multiplicationHost+"/results/"+multiplicationId, MultiplicationResultAttempt.class);
	}

}
