package o1.adventure

import scala.collection.mutable.Map
import scala.io.StdIn.readLine

/** A `Player` object represents a player character controlled by the real-life user
  * of the program.
  *
  * A player object’s state is mutable: the player’s location and possessions can change,
  * for instance.
  *
  * @param startingArea  the player’s initial location */
class Player(startingArea: Area):

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag

  private var isHidden = false                      //kertoo, onko pelaaja "piilossa"

  private var letters = Vector[String]()            //kerättyjen kirajimien säilyttäjä

  var correctWord = false

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the player’s current location. */
  def location = this.currentLocation

  private var items = Map[String, Item]()

  def drop(itemName: String) =

      this.items.get(itemName) match {
      case Some(item) =>
        this.items -= itemName
        location.addItem(item)
        s"You drop the ${itemName}."
      case None => "You don't have that!"
    }



  def has(itemName: String) = this.items.contains(itemName)

  def get(itemName: String) =

      location.removeItem(itemName) match {
        case Some(item) =>
          this.items += itemName -> item
          s"You pick up the ${itemName}."
        case None => s"There is no${itemName} here to pick up."
      }

  def remember(letter: String) =
    this.letters = this.letters :+ letter
    this.location.removeLetter(letter)
    s"You remember the letter ${letter.toUpperCase} oddly well."

  def check =
    if this.letters.nonEmpty then
      s"You have found these letters: \n${this.letters.flatMap(_.toUpperCase).mkString(", ")}"
    else
      "Looks like you haven't found any letters yet."


  def examine(itemName: String) =
    if this.items.contains(itemName) then
      s"You look closely at the ${itemName}.\n${this.items(itemName).description}"
    else
      "If you want to examine something, you need to pick it up first."

  def inventory =
    if this.items.nonEmpty then
      s"You are carrying:\n${items.keys.mkString("\n")}"
    else
      "You are empty-handed."

  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player’s current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  //pelaaja liikkuu vain, jos ei ole piilossa
  def go(direction: String): String =
    val destination = this.location.neighbor(direction)
    if this.isHidden then
      "You can't move while you are hidden! You have to 'unhide' yourself first."
    else
      this.currentLocation = destination.getOrElse(this.currentLocation)
      if destination.isDefined then
        s"You go $direction."
      else s"You can't go $direction."

  //piiloutuminen ja esiintulo
  def hide() =
    this.isHidden = true
    "You are now hidden. Try to stay quiet."

  def unhide() =
    this.isHidden = false
    "You are no longer hiding. Remember to watch out."

    destination match {
      case Some(area) =>
        if area.name == "Home" then
          def tonttu() =
            val art =
              """
                |    /\
                |   /  \
                |  /____\
                |   (o_o)
                |   <| |>
                |    / \
                |""".stripMargin
            println(art)
          tonttu()
          val word = readLine("You see a little gnome. The little gnome asks: 'What do you feel?' ").toUpperCase

          if word == "FEAR" then
            correctWord = true
            this.currentLocation = area
            "The fear starts to settle. A warm feeling passes over you... You go home."
          else
            "You can't go home yet. There are still secrets you have to unravel."
        else
          this.currentLocation = area
          s"You go $direction."

      case None =>
        s"You can't go $direction."
    }




  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() = "You rest for a while. Better get a move on, though."


  /** Signals that the player wants to quit the game. Returns a description of what happened
    * within the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    ""


  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

end Player

