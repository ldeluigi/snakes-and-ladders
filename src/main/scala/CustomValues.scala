import untitled.goose.framework.model.entities.runtime.GameStateExtensions._
import untitled.goose.framework.model.entities.runtime.{GameState, Tile}
import untitled.goose.framework.model.events.persistent.TurnEndedEvent

trait CustomValues {
  val gooseGroup = "GooseTile"
  val bottomLadder = "Bottom Ladder"
  val topLadder = "Top Ladder"
  val snakeHead = "Snake Head"
  val snakeTail = "Snake Tail"
  val theEnd = "the End"


  def tileIs(name: String): Tile => Boolean = _.definition.name.contains(name)

  //TODO why not autoimport of extensions?
  def isPlayerFirstTurn: GameState => Boolean = _.currentPlayer.history.only[TurnEndedEvent].isEmpty
}