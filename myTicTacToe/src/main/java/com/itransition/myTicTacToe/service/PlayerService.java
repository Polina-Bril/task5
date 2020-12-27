package com.itransition.myTicTacToe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itransition.myTicTacToe.DTO.PlayerDTO;
import com.itransition.myTicTacToe.domain.Player;
import com.itransition.myTicTacToe.repository.PlayerRepository;
import com.itransition.myTicTacToe.security.ContextUser;

@Service
@Transactional
public class PlayerService {

	private final PlayerRepository playerRepository;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	public PlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public Player createNewPlayer(PlayerDTO playerDTO) {
		Player newPlayer = new Player();
		newPlayer.setUserName(playerDTO.getUserName());
		newPlayer.setPassword(passwordEncoder.encode(playerDTO.getPassword()));
		newPlayer.setEmail(playerDTO.getEmail());
		playerRepository.save(newPlayer);
		return newPlayer;
	}

	public Player getLoggedUser() {
		ContextUser principal = (ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return playerRepository.findOneByUserName(principal.getPlayer().getUserName());
	}

	public List<Player> listPlayers() {
		List<Player> players = (List<Player>) playerRepository.findAll();
		return players;
	}
}