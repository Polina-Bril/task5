package com.itransition.myTicTacToe.service;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.itransition.myTicTacToe.domain.Position;

public class GameLogic {

	static boolean isWinner(List<Position> positions) {
		return getWinningPositions().stream().anyMatch(positions::containsAll);
	}

	static List<List<Position>> getWinningPositions() {
		List<List<Position>> winingPositions = new ArrayList<>();
		winingPositions.add(asList(new Position(1, 1), new Position(1, 2), new Position(1, 3)));
		winingPositions.add(asList(new Position(2, 1), new Position(2, 2), new Position(2, 3)));
		winingPositions.add(asList(new Position(3, 1), new Position(3, 2), new Position(3, 3)));
		winingPositions.add(asList(new Position(1, 1), new Position(2, 1), new Position(3, 1)));
		winingPositions.add(asList(new Position(1, 2), new Position(2, 2), new Position(3, 2)));
		winingPositions.add(asList(new Position(1, 3), new Position(2, 3), new Position(3, 3)));
		winingPositions.add(asList(new Position(1, 1), new Position(2, 2), new Position(3, 3)));
		winingPositions.add(asList(new Position(3, 1), new Position(2, 2), new Position(1, 3)));
		return winingPositions;
	}

	static List<Position> getAllPositions() {
		List<Position> positions = new ArrayList<>();
		for (int row = 1; row <= 3; row++) {
			for (int col = 1; col <= 3; col++) {
				positions.add(new Position(row, col));
			}
		}
		return positions;
	}

	static boolean firstPlayerTurn(int numberOfAllMovesInGame) {
		return numberOfAllMovesInGame % 2 == 0 ? true : false;
	}

	static boolean isBoardIsFull(List<Position> takenPositions) {
		return takenPositions.size() == 9;
	}

	static List<Position> getOpenPositions(List<Position> takenPositions) {
		return getAllPositions().stream().filter(p -> !takenPositions.contains(p)).collect(Collectors.toList());
	}

	static Position nextAutoMove(List<Position> takenPositions) {
		int computerMove = (int) (Math.random() * takenPositions.size());
		return getOpenPositions(takenPositions).get(computerMove);
	}
}