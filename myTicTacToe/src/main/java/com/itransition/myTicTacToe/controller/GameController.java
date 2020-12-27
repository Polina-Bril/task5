package com.itransition.myTicTacToe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itransition.myTicTacToe.DTO.GameDTO;
import com.itransition.myTicTacToe.domain.Game;
import com.itransition.myTicTacToe.service.GameService;
import com.itransition.myTicTacToe.service.PlayerService;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	GameService gameService;

	@Autowired
	PlayerService playerService;

	@Autowired
	HttpSession httpSession;

	Logger logger = LoggerFactory.getLogger(GameController.class);

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Game createNewGame(@RequestBody GameDTO gameDTO) {
		Game game = gameService.createNewGame(playerService.getLoggedUser(), gameDTO);
		httpSession.setAttribute("gameId", game.getId());
		logger.info("new game id: " + httpSession.getAttribute("gameId") + " stored in session");
		return game;
	}

	@RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Game> getGamesToJoin() {
		return gameService.getGamesToJoin(playerService.getLoggedUser());
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public Game joinGame(@RequestBody GameDTO gameDTO) {
		Game game = gameService.joinGame(playerService.getLoggedUser(), gameDTO);
		return game;
	}

	@RequestMapping(value = "/player/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Game> getPlayerGames() {
		return gameService.getPlayerGames(playerService.getLoggedUser());
	}

	@RequestMapping(value = "/{id}")
	public Game getGameProperties(@PathVariable Long id) {
		httpSession.setAttribute("gameId", id);
		return gameService.getGame(id);
	}
}