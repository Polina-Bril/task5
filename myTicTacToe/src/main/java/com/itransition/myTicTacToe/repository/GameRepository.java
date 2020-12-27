package com.itransition.myTicTacToe.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.itransition.myTicTacToe.domain.Game;
import com.itransition.myTicTacToe.enums.GameStatus;
import com.itransition.myTicTacToe.enums.GameType;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
	List<Game> findByGameTypeAndGameStatus(GameType GameType, GameStatus GameStatus);

	List<Game> findByGameStatus(GameStatus gameStatus);
}
