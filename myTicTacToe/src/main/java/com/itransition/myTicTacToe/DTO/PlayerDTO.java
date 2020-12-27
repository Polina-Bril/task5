package com.itransition.myTicTacToe.DTO;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {

	@NotNull
	private String userName;
	@NotNull
	private String password;
	@NotNull
	private String email;
}
