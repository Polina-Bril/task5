var gameModule = angular.module('gameModule', []);

gameModule.controller('newGameController', ['$rootScope', '$scope', '$http', '$location',

    function (rootScope, scope, http, location) {

        rootScope.gameId = null;
        scope.newGameData = null;

        scope.newGameOptions = {
            availablePieces: [
                {name: 'X'},
                {name: 'O'}
            ],
            selectedPiece: {name: 'O'},
            availableGameTypes: [
                {name: 'COMPETITION'},
                {name: 'COMPUTER'}
            ],
            selectedBoardDimension: {name: 'COMPUTER'}
        };
        var input = document.querySelector('input[name="tags"]'), 
		tagify = new Tagify(input, {
				whitelist : [ "Ala", "Mary", "100$", "50$", "5$", "10$",
						"Minsk", "Grodno", "Brest", "Moscow", "Piter", "Sasha",
						"Pasha", "Vitebsk", "Homel", "Magilev", "Baranovichi",
						"Ada" ],
				maxTags : 10,
				dropdown : {
					maxItems : 20,
					classname : "tags-look",
					enabled : 0,
					closeOnSelect : false
				}
			});
			scope.tags={tags: '$input'};
        scope.createNewGame = function () {

            var data = scope.newGameData;
            var params = JSON.stringify(data);

            http.post("/game/create", params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                rootScope.gameId = data.id;
                location.path('/game/' + rootScope.gameId);
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });
        }

    }
]);


gameModule.controller('gamesToJoinController', ['$scope', '$http', '$location',
    function (scope, http, location) {

        scope.gamesToJoin = [];
          var input = document.querySelector('input[name="taga"]'), 
		tagify = new Tagify(input, {
				whitelist : [ "Ala", "Mary", "100$", "50$", "5$", "10$",
						"Minsk", "Grodno", "Brest", "Moscow", "Piter", "Sasha",
						"Pasha", "Vitebsk", "Homel", "Magilev", "Baranovichi",
						"Ada" ],
				maxTags : 10,
				dropdown : {
					maxItems : 20,
					classname : "tags-look",
					enabled : 0,
					closeOnSelect : false
				}
			});

        http.get('/game/list').success(function (data) {
            scope.gamesToJoin = data;
        }).error(function (data, status, headers, config) {
            location.path('/player/panel');
        });
//		scope.filterNewGame=function(input){
//			forEach(let g:scope.gamesToJoin){
//				let count=0;
//				forEach(let t:input){
//					if(g.tags.contains(t)){
//						count++;
//					}
//				}
//				if(count==0) delete(g);
//		}};

        scope.joinGame = function (id) {

            var params = {"id" : id}

            http.post('/game/join', params, {
                headers: {
                 'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data) {
                location.path('/game/' + data.id);
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });
        }

    }]);


gameModule.controller('playerGamesController', ['$scope', '$http', '$location', '$routeParams',
    function (scope, http, location, routeParams) {

        scope.playerGames = [];

        http.get('/game/player/list').success(function (data) {
            scope.playerGames = data;
        }).error(function (data, status, headers, config) {
            location.path('/player/panel');
        });

        scope.loadGame = function (id) {
            console.log(id);
            location.path('/game/' + id);
        }

    }]);


gameModule.controller('gameController', ['$rootScope', '$routeParams', '$scope', '$http',
    function (rootScope, routeParams, scope, http) {
       var gameStatus;
       getInitialData()

        function getInitialData() {

           http.get('/game/' + routeParams.id).success(function (data) {
                scope.gameProperties = data;
                gameStatus = scope.gameProperties.gameStatus;
                getMoveHistory();
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
        }

        function getMoveHistory() {
            scope.movesInGame = [];

          return  http.get('/move/list').success(function (data) {
                scope.movesInGame = data;
                scope.playerMoves = [];

                angular.forEach(scope.movesInGame, function(move) {
                    scope.rows[move.boardRow - 1][move.boardColumn - 1].letter = move.playerPieceCode;
                });
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed to load moves in game"
            });
        }

        function checkPlayerTurn() {
            return http.get('/move/turn').success(function (data) {
                scope.playerTurn = data;
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed to get the player turn"
            });
        }

        function getNextMove() {

        scope.nextMoveData = []

        if(!scope.gameProperties.secondPlayer) {
            http.get("/move/autocreate").success(function (data, status, headers, config) {
                scope.nextMoveData = data;
                getMoveHistory().success(function () {
                    var gameStatus = scope.movesInGame[scope.movesInGame.length - 1].gameStatus;
                    if (gameStatus != 'IN_PROGRESS') {
                        alert(gameStatus);
                        
                    }
                });
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Can't send the move"
            });

        } else {
          console.log(' another player move');
        }
        }

        function checkIfBoardCellAvailable(boardRow, boardColumn) {

            for (var i = 0; i < scope.movesInGame.length; i++) {
                var move = scope.movesInGame[i];
                if (move.boardColumn == boardColumn && move.boardRow == boardRow) {
                    return false;
                }
            }
            return true;
        }

        scope.rows = [
            [
                {'id': '11', 'letter': '', 'class': 'box'},
                {'id': '12', 'letter': '', 'class': 'box'},
                {'id': '13', 'letter': '', 'class': 'box'}
            ],
            [
                {'id': '21', 'letter': '', 'class': 'box'},
                {'id': '22', 'letter': '', 'class': 'box'},
                {'id': '23', 'letter': '', 'class': 'box'}
            ],
            [
                {'id': '31', 'letter': '', 'class': 'box'},
                {'id': '32', 'letter': '', 'class': 'box'},
                {'id': '33', 'letter': '', 'class': 'box'}
            ]
        ];

        angular.forEach(scope.rows, function (row) {
            row[0].letter = row[1].letter = row[2].letter = '';
            row[0].class = row[1].class = row[2].class = 'box';
        });

        scope.markPlayerMove = function (column) {
            checkPlayerTurn().success(function () {

                var boardRow = parseInt(column.id.charAt(0));
                var boardColumn = parseInt(column.id.charAt(1));
                var params = {'boardRow': boardRow, 'boardColumn': boardColumn}

                if (checkIfBoardCellAvailable(boardRow, boardColumn) == true) {
                    if (scope.playerTurn == true) {

                        http.post("/move/create", params, {
                            headers: {
                                'Content-Type': 'application/json; charset=UTF-8'
                            }
                        }).success(function () {
                            getMoveHistory().success(function () {

                                var gameStatus = scope.movesInGame[scope.movesInGame.length - 1].gameStatus;
                                if (gameStatus == 'IN_PROGRESS') {
                                    getNextMove();
                                } else if(gameStatus!="WAITS_FOR_PLAYER"){
                                    alert(gameStatus);
                                    window.location = '/#/player/panel';
                                    
                                }
                            });

                        }).error(function (data, status, headers, config) {
                            scope.errorMessage = "Can't send the move"
                        });

                    }
                }
            });
        };


    }
]);
