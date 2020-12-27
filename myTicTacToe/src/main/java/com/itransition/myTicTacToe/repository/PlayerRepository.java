package com.itransition.myTicTacToe.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.itransition.myTicTacToe.domain.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
	Player findOneByUserName(String userName);
}
