package com.itransition.myTicTacToe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itransition.myTicTacToe.DTO.CreateMoveDTO;
import com.itransition.myTicTacToe.DTO.MoveDTO;
import com.itransition.myTicTacToe.domain.Game;
import com.itransition.myTicTacToe.domain.Move;
import com.itransition.myTicTacToe.domain.Player;
import com.itransition.myTicTacToe.domain.Position;
import com.itransition.myTicTacToe.enums.GameStatus;
import com.itransition.myTicTacToe.enums.GameType;
import com.itransition.myTicTacToe.enums.Piece;
import com.itransition.myTicTacToe.repository.MoveRepository;

@Service
@Transactional
public class MoveService {

	private final MoveRepository moveRepository;

	@Autowired
	public MoveService(MoveRepository moveRepository) {
		this.moveRepository = moveRepository;
	}

	public Move createMove(Game game, Player player, CreateMoveDTO createMoveDTO) {
		Move move = new Move();
		move.setBoardColumn(createMoveDTO.getBoardColumn());
		move.setBoardRow(createMoveDTO.getBoardRow());
		move.setCreated(new Date());
		move.setPlayer(player);
		move.setGame(game);
		moveRepository.save(move);
		return move;
	}

	public Move autoCreateMove(Game game) {
		Move move = new Move();
		Position autoMove = GameLogic.nextAutoMove(getTakenMovePositionsInGame(game));
		move.setBoardColumn(autoMove.getBoardColumn());
		move.setBoardRow(autoMove.getBoardRow());
		move.setCreated(new Date());
		move.setPlayer(null);
		move.setGame(game);
		moveRepository.save(move);
		return move;
	}

	public GameStatus checkCurrentGameStatus(Game game) {
		if (GameLogic.isWinner(getPlayerMovePositionsInGame(game, game.getFirstPlayer()))) {
			return GameStatus.FIRST_PLAYER_WON;
		} else if (GameLogic.isWinner(getPlayerMovePositionsInGame(game, game.getSecondPlayer()))) {
			return GameStatus.SECOND_PLAYER_WON;
		} else if (GameLogic.isBoardIsFull(getTakenMovePositionsInGame(game))) {
			return GameStatus.TIE;
		} else if (game.getGameType() == GameType.COMPETITION && game.getSecondPlayer() == null) {
			return GameStatus.WAITS_FOR_PLAYER;
		} else {
			return GameStatus.IN_PROGRESS;
		}
	}

	public List<MoveDTO> getMovesInGame(Game game) {
		List<Move> movesInGame = moveRepository.findByGame(game);
		List<MoveDTO> moves = new ArrayList<>();
		for (Move move : movesInGame) {
			MoveDTO moveDTO = new MoveDTO();
			moveDTO.setBoardColumn(move.getBoardColumn());
			moveDTO.setBoardRow(move.getBoardRow());
			moveDTO.setCreated(move.getCreated());
			moveDTO.setGameStatus(move.getGame().getGameStatus());
			moveDTO.setUserName(
					move.getPlayer() == null ? GameType.COMPUTER.toString() : move.getPlayer().getUserName());
			moveDTO.setPlayerPieceCode(move.getPlayer() == game.getFirstPlayer() ? game.getFirstPlayerPieceCode()
					: (game.getFirstPlayerPieceCode() == Piece.X ? Piece.O : Piece.X));
			moves.add(moveDTO);
		}
		return moves;
	}

	public List<Position> getTakenMovePositionsInGame(Game game) {
		return moveRepository.findByGame(game).stream()
				.map(move -> new Position(move.getBoardRow(), move.getBoardColumn())).collect(Collectors.toList());
	}

	public List<Position> getPlayerMovePositionsInGame(Game game, Player player) {
		return moveRepository.findByGameAndPlayer(game, player).stream()
				.map(move -> new Position(move.getBoardRow(), move.getBoardColumn())).collect(Collectors.toList());
	}

	public int getTheNumberOfPlayerMovesInGame(Game game, Player player) {
		return moveRepository.countByGameAndPlayer(game, player);
	}

	public boolean isFirstPlayerTurn(Game game) {
		return GameLogic.firstPlayerTurn(getTakenMovePositionsInGame(game).size());
	}
}