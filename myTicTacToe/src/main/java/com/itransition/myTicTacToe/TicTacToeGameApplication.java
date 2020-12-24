package com.itransition.myTicTacToe;

import com.itransition.myTicTacToe.domain.Player;
import com.itransition.myTicTacToe.repository.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TicTacToeGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicTacToeGameApplication.class, args);
	}

    @Bean
    public CommandLineRunner demo(PlayerRepository playerRepository) {
        return (args) -> {
            playerRepository.save(new Player("ala", "ala@ala.com", new BCryptPasswordEncoder().encode("ala")));
            playerRepository.save(new Player("mary", "mary@mary.com",  new BCryptPasswordEncoder().encode("mary")));
        };
    }
}