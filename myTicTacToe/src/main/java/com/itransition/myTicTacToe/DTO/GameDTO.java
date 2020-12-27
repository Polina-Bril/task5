package com.itransition.myTicTacToe.DTO;

import com.itransition.myTicTacToe.enums.GameType;
import com.itransition.myTicTacToe.enums.Piece;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {

	private int id;
	private GameType gameType;
	private Piece piece;
	private String gameName;
	private String tags;

}
