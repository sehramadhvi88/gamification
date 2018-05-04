package microservice.book.examples.gamification.service;

import java.util.List;

import microservice.book.examples.gamification.domain.LeaderBoardRow;

public interface LeaderBoardService {
	
	/**
	 * Retrieves the current leader board with the top score users
	 * @return the users with the highest score
	 */
	 List<LeaderBoardRow> getCurrentLeaderBoard();

}
