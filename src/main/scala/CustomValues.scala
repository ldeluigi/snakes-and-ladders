import untitled.goose.framework.model.entities.definitions.TileDefinition
import untitled.goose.framework.model.entities.runtime.GameState
import untitled.goose.framework.model.events.persistent.TurnEndedEvent
import untitled.goose.framework.model.entities.runtime.functional.HistoryExtensions.PimpedHistory

trait CustomValues {
  val gooseGroup = "GooseTile"
  val bottomLadder = "Bottom Ladder"
  val topLadder = "Top Ladder"
  val snakeHead = "Snake Head"
  val snakeTail = "Snake Tail"
  val theEnd = "the End"


  def tileIs(name: String): TileDefinition => Boolean = _.name.contains(name)

  //TODO why not autoimport of extensions?
  def isPlayerFirstTurn: GameState => Boolean = s => s.players(s.currentPlayer).history.only[TurnEndedEvent].isEmpty
}