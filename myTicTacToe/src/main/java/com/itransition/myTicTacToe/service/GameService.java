package com.itransition.myTicTacToe.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itransition.myTicTacToe.DTO.GameDTO;
import com.itransition.myTicTacToe.domain.Game;
import com.itransition.myTicTacToe.domain.Player;
import com.itransition.myTicTacToe.enums.GameStatus;
import com.itransition.myTicTacToe.enums.GameType;
import com.itransition.myTicTacToe.repository.GameRepository;

@Service
@Transactional
public class GameService {

	private final GameRepository gameRepository;

	@Autowired
	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public Game createNewGame(Player player, GameDTO gameDTO) {
		Game game = new Game();
		game.setFirstPlayer(player);
		game.setGameType(gameDTO.getGameType());
		game.setFirstPlayerPieceCode(gameDTO.getPiece());
		game.setGameName(gameDTO.getGameName());
		game.setTags(gameDTO.getTags());
		game.setGameStatus(
				gameDTO.getGameType() == GameType.COMPUTER ? GameStatus.IN_PROGRESS : GameStatus.WAITS_FOR_PLAYER);
		game.setCreated(new Date());
		gameRepository.save(game);
		return game;
	}

	public Game updateGameStatus(Game game, GameStatus gameStatus) {
		Game g = getGame(game.getId());
		g.setGameStatus(gameStatus);
		return g;
	}

	public List<Game> getGamesToJoin(Player player) {
		return gameRepository.findByGameTypeAndGameStatus(GameType.COMPETITION, GameStatus.WAITS_FOR_PLAYER).stream()
				.filter(game -> game.getFirstPlayer() != player).collect(Collectors.toList());
	}

	public Game joinGame(Player player, GameDTO gameDTO) {
		Game game = getGame((long)gameDTO.getId());
		game.setSecondPlayer(player);
		updateGameStatus(game, GameStatus.IN_PROGRESS);
		gameRepository.save(game);
		return game;
	}

	public List<Game> getPlayerGames(Player player) {
		return gameRepository.findByGameStatus(GameStatus.IN_PROGRESS).stream()
				.filter(game -> game.getFirstPlayer() == player||game.getSecondPlayer() == player).collect(Collectors.toList());
	}

	public Game getGame(Long id) {
		Optional<Game> gameOrNot = gameRepository.findById(id);
		Game game = gameOrNot.orElseThrow();
		return game;
	}
}