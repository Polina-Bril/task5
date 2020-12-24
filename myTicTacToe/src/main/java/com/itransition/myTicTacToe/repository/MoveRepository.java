package com.itransition.myTicTacToe.repository;

import com.itransition.myTicTacToe.domain.Game;
import com.itransition.myTicTacToe.domain.Move;
import com.itransition.myTicTacToe.domain.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends CrudRepository<Move, Long> {

    List<Move> findByGame(Game game);
    List<Move> findByGameAndPlayer(Game game, Player player);
    int countByGameAndPlayer(Game game, Player player);
}
