package microservice.book.examples.gamification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservice.book.examples.gamification.domain.LeaderBoardRow;
import microservice.book.examples.gamification.service.LeaderBoardService;

@RestController
@RequestMapping("/leaders")
public class LeaderBoardController {

	private final LeaderBoardService leaderBoardService;
	
	public LeaderBoardController(final LeaderBoardService leaderBoardService) {
			 this.leaderBoardService = leaderBoardService;
	}
	
	@GetMapping
	public List<LeaderBoardRow> getLeaderBoard() {
		 return leaderBoardService.getCurrentLeaderBoard();
	}
}
