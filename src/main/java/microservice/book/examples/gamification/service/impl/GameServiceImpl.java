package microservice.book.examples.gamification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservice.book.examples.gamification.client.MultiplicationResultAttemptClient;
import microservice.book.examples.gamification.domain.Badge;
import microservice.book.examples.gamification.domain.BadgeCard;
import microservice.book.examples.gamification.domain.GameStats;
import microservice.book.examples.gamification.domain.ScoreCard;
import microservice.book.examples.gamification.repository.BadgeCardRepository;
import microservice.book.examples.gamification.repository.ScoreCardRepository;
import microservice.book.examples.gamification.service.GameService;
import microservices.book.example.gamification.client.dto.MultiplicationResultAttempt;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
	
	public static final int LUCKY_NUMBER = 42;
	
	private ScoreCardRepository scoreCardRepository;
	private BadgeCardRepository badgeCardRepository;
	private MultiplicationResultAttemptClient attemptClient;
	
	public GameServiceImpl(ScoreCardRepository scoreCardRepository,BadgeCardRepository badgeCardRepository,MultiplicationResultAttemptClient attemptClient) {
		this.scoreCardRepository=scoreCardRepository;
		this.badgeCardRepository=badgeCardRepository;
		this.attemptClient = attemptClient; 
	}
	
	@Override
	public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
		// TODO Auto-generated method stub
		
		if(correct){
			ScoreCard scoreCard = new ScoreCard(userId,attemptId);
			scoreCardRepository.save(scoreCard);
			log.info("User with id {} scored {} points for attempt id {}",userId, scoreCard.getScore(), attemptId);
			List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
			return new GameStats(userId, scoreCard.getScore(),badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
			
		}
		
		return GameStats.emptyStats(userId);
	}
	
	/**
	 * Checks the total score and the different score cards	obtained
	 * to give new badges in case their conditions are met.
	 */
	private List<BadgeCard> processForBadges(Long userId, Long attemptId) {
		// TODO Auto-generated method stub 
		
		List<BadgeCard> badgeCards = new ArrayList<>();
		
		int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
		log.info("New score for user {} is {}", userId,totalScore);
		
		List<ScoreCard> scoreCardList = scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
		List<BadgeCard> badgeCardList = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId); 
		
		checkAndGiveBadgeBasedOnScore(badgeCardList,Badge.BRONZE_MULTIPLICATOR, totalScore, 100,userId).ifPresent(badgeCards::add);
		checkAndGiveBadgeBasedOnScore(badgeCardList,Badge.SILVER_MULTIPLICATOR, totalScore, 500,userId).ifPresent(badgeCards::add);
		checkAndGiveBadgeBasedOnScore(badgeCardList,Badge.GOLD_MULTIPLICATOR, totalScore, 999,userId).ifPresent(badgeCards::add);
		
		// First won badge
		 if(scoreCardList.size() == 1 && !containsBadge(badgeCardList, Badge.FIRST_WON)) {
			 BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
			 badgeCards.add(firstWonBadge);
		 }
		 
		// Lucky number badge
		MultiplicationResultAttempt attempt = attemptClient.retrieveMultiplicationResultAttemptbyId(attemptId);
		 
		if(!containsBadge(badgeCardList, Badge.LUCKY_NUMBER) && (LUCKY_NUMBER == attempt.getMultiplicationFactorA() || LUCKY_NUMBER == attempt.getMultiplicationFactorB())) {
			BadgeCard luckyNumberBadge = giveBadgeToUser(Badge.LUCKY_NUMBER, userId);
			badgeCards.add(luckyNumberBadge);
		 }
		 
		 return badgeCards;
	}

	@Override
	public GameStats retrieveStatsForUser(Long userId) {
		// TODO Auto-generated method stub
		int score = scoreCardRepository.
		getTotalScoreForUser(userId);
		List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
		return new GameStats(userId, score, badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
	}
	
	private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(final List<BadgeCard> badgeCards, final Badge badge,final int score, final int scoreThreshold, final Long userId){
		
		if(score >= scoreThreshold && !containsBadge(badgeCards, badge)) {
				 return Optional.of(giveBadgeToUser(badge, userId));
		}
		return Optional.empty();
		
	}
	/**
	 * Checks if the passed list of badges includes the one
	being checked
	 */
	private boolean containsBadge(final List<BadgeCard>	badgeCards,final Badge badge) {
		return badgeCards.stream().anyMatch(b -> b.getBadge().equals(badge));
	}
	
	/**
	 * Assigns a new badge to the given user
	 */
	 private BadgeCard giveBadgeToUser(final Badge badge, final	Long userId) {
		 BadgeCard badgeCard = new BadgeCard(userId, badge);	
		 badgeCardRepository.save(badgeCard);
		 log.info("User with id {} won a new badge: {}", userId,badge);
		 return badgeCard;
	 }
}
