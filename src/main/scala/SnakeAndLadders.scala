import scalafx.scene.paint.Color._
import untitled.goose.framework.dsl.GooseDSL
import untitled.goose.framework.dsl.board.words.DispositionType.Snake
import untitled.goose.framework.model.entities.runtime.GameStateExtensions._
import untitled.goose.framework.model.events.consumable._
import untitled.goose.framework.model.events.persistent.TileActivatedEvent
import untitled.goose.framework.model.rules.ruleset.PlayerOrderingType.Fixed

object SnakeAndLadders extends GooseDSL with CustomValues {

  Rules of "Snake and Ladders"
  2 to 4 players

  Players have order(Fixed)

  The game board has size(30)
  the game board has disposition(Snake)

  The tiles(1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29) have color(Red)
  The tiles(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30) have color(White)

  The tile 1 has name(firstSnakeTailEnd)
  The tile 4 has name(secondSnakeTailEnd)
  The tile 7 has name(thirdSnakeTailEnd)
  The tile 27 has name(firstSnakeHead)
  The tile 17 has name(secondSnakeHead)
  The tile 19 has name(thirdSnakeHead)

  The tiles(11, 14, 23) have background("ladder.png")
  The tiles(4, 9, 16) have background("ladder.png")
  The tiles(18, 19) have background("ladder.png")

  The tile 30 has(
    name(theEnd),
    //background("victory.png"),
    color(Gold)
  )

  val snakeMap: Map[Int, Int] = Map(17 -> 1, 19 -> 4, 27 -> 7)
  val ladderMap: Map[Int, Int] = Map(4 -> 13, 12 -> 20, 6 -> 24)

  The tiles (snakeMap.keys.toList: _*) have group(snakeHead)
  The tiles (snakeMap.values.toList: _*) have group(snakeTail)
  The tiles (ladderMap.keys.toList: _*) have group(bottomLadder)
  The tiles (ladderMap.values.toList: _*) have group(topLadder)

  Players start on tile 1

  Create movementDice "six-faced" having totalSides(6)

  Players loseTurn priority is 10

  Each turn players are (always allowed to roll 2 movementDice "six-faced" as "roll a dice" priority 5)

  //To win you must reach tile 30 exactly.
  // If your dice roll is more than you need then you move in to tile 30 bounce back out again,
  // each spot on the dice is still one step in this move.
  // If you land on any of the special tiles while you are doing this then you must follow the normal instructions.
  always when numberOf(events[TileEnteredEvent] matching (e => tileIs(theEnd)(e.tile))) is (_ > 0) resolve (
    forEach trigger ((e, s) => InvertMovementEvent(e.player, s.currentTurn, s.currentCycle))
    ) andThen consume

  //When you land on square 30 exactly you are the winner!
  always when numberOf(events[StopOnTileEvent] matching (e => tileIs(theEnd)(e.tile))) is (_ > 0) resolve(
    forEach trigger ((e, s) => VictoryEvent(e.player, s.currentTurn, s.currentCycle)),
    forEach trigger ((e, s) => TileActivatedEvent(e.tile, s.currentTurn, s.currentCycle))
  ) andThen consume

  //When you land on a snake head tile you must slide down the snake body!
  always when numberOf(events[StopOnTileEvent] matching (e => e.tile.definition.belongsTo(snakeHead))) is (_ > 0) resolve(
    displayMessage("You ended up on a snake's head!", "You will slide back down to the snake's tail."),
    trigger((e, s) => TeleportEvent(s.getTile(snakeMap(e.tile.definition.number.get)).get, e.player, s.currentTurn, s.currentCycle))
  ) andThen consume && save

  //When you land on a ladder bottom tile you climb up to its top!
  always when numberOf(events[StopOnTileEvent] matching (e => e.tile.definition.belongsTo(bottomLadder))) is (_ > 0) resolve(
    displayMessage("You ended up on a ladder!", "You will climb up to its top!"),
    trigger((e, s) => TeleportEvent(s.getTile(ladderMap(e.tile.definition.number.get)).get, e.player, s.currentTurn, s.currentCycle))
  ) andThen consume && save


}
