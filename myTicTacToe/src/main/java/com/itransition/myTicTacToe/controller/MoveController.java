package com.itransition.myTicTacToe.controller;

import com.itransition.myTicTacToe.DTO.CreateMoveDTO;
import com.itransition.myTicTacToe.DTO.MoveDTO;
import com.itransition.myTicTacToe.domain.Game;
import com.itransition.myTicTacToe.domain.Move;
import com.itransition.myTicTacToe.service.GameService;
import com.itransition.myTicTacToe.service.MoveService;
import com.itransition.myTicTacToe.service.PlayerService;
import com.itransition.myTicTacToe.domain.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/move")
public class MoveController {

	@Autowired
	private MoveService moveService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private GameService gameService;

	@Autowired
	private HttpSession httpSession;

	Logger logger = LoggerFactory.getLogger(MoveController.class);

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Move createMove(@RequestBody CreateMoveDTO createMoveDTO) {
		Long gameId = (Long) httpSession.getAttribute("gameId");
		logger.info("move to insert:" + createMoveDTO.getBoardColumn() + createMoveDTO.getBoardRow());
		Game game = gameService.getGame(gameId);
		Move move = moveService.createMove(game, playerService.getLoggedUser(), createMoveDTO);
		gameService.updateGameStatus(game, moveService.checkCurrentGameStatus(game));
		return move;
	}

	@RequestMapping(value = "/autocreate", method = RequestMethod.GET)
	public Move autoCreateMove() {
		Long gameId = (Long) httpSession.getAttribute("gameId");
		logger.info("AUTO move to insert:");
		Game game = gameService.getGame(gameId);
		Move move = moveService.autoCreateMove(game);
		gameService.updateGameStatus(game, moveService.checkCurrentGameStatus(game));
		return move;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<MoveDTO> getMovesInGame() {
		Long gameId = (Long) httpSession.getAttribute("gameId");
		return moveService.getMovesInGame(gameService.getGame(gameId));
	}

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public List<Position> validateMoves() {
		Long gameId = (Long) httpSession.getAttribute("gameId");
		return moveService.getPlayerMovePositionsInGame(gameService.getGame(gameId), playerService.getLoggedUser());
	}

	@RequestMapping(value = "/turn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean isPlayerTurn() {
		Long gameId = (Long) httpSession.getAttribute("gameId");
		boolean isFirstPlayerTurn = moveService.isFirstPlayerTurn(gameService.getGame(gameId));
		return playerService.getLoggedUser() == gameService.getGame(gameId).getFirstPlayer() ? isFirstPlayerTurn
				: !isFirstPlayerTurn;
	}
}