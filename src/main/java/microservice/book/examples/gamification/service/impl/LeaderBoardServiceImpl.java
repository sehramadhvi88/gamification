package microservice.book.examples.gamification.service.impl;

import java.util.List;

import microservice.book.examples.gamification.domain.LeaderBoardRow;
import microservice.book.examples.gamification.repository.ScoreCardRepository;
import microservice.book.examples.gamification.service.LeaderBoardService;

public class LeaderBoardServiceImpl implements LeaderBoardService {

	private ScoreCardRepository scoreCardRepository;
	
	public LeaderBoardServiceImpl(ScoreCardRepository scoreCardRepository) {
		this.scoreCardRepository = scoreCardRepository;
	}
	
	@Override
	public List<LeaderBoardRow> getCurrentLeaderBoard() {
		// TODO Auto-generated method stub
		return scoreCardRepository.findFirst10();
	}

}
