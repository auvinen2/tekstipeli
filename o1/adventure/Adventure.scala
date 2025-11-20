package o1.adventure

/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of “hard-coded” information that pertains to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure:

  /** the name of the game */
  val title = "A Forest Adventure"

  /* tulee nyt vähän suomi - englanti - sekasikiö ihsm tuntuis tyhmält kirjottaa suomeks
  * - muuttujat suomeks kartan luettavuuden takia*/
  private val kaverinKoti = Area("Your friend's place", "Oh no! You just missed the last bus. \nDo you take the long walk home (south) or the shortcut through the woods (east)?")
  private val polku       = Area("Path", "Ooh. it's getting darker in here. Well, can't turn back now.")
  private val kallio      = Area("Cliff", "That's a steep drop right there! \nYou have to walk carefully, in case it's slippery.")
  private val luola       = Area("Dark Cave", "This is kind of spooky... I don't remember this being here? \nWait, what- who's that?!?!")
  private val tiheikko    = Area("Thick Brushwood of Spruces", "Ouch, the spruce needles really hurt against your face! \nYou struggle to keep your eyes open.")
  private val kelokko     = Area("Dead Tree Forest", "The mighty dead pines stand before you, swaying lightly with the wind. \nYou feel you're getting closer to home.")
  private val aukio       = Area("Forest Clearing", "At least the starry sky looks pretty. \nJust like the one you programmed during O1.")
  private val lampi       = Area("Duck Pond", "You can't see any ducks. What could've scared them away?")
  private val kaivo       = Area("The Old Well", "The silhouette of the well is almost visible in the darkness.")
  private val metsa       = Area("Forest", "Erm... where did the path go? All you can see now are trees!")
  private val pelto       = Area("Field", "How did you get here? And what's that dent in the grain?")
  private val silta       = Area("The Creaky Bridge", "You sure hope this doesn't collapse! Wet socks are a pain.")
  private val home        = Area("Home", "What a journey. Maybe next time you'll bring a flashlight.")
  private val destination = home

/* sijaintien suhteet siten, kuin kartassa olisi kompassiruusu 
(pohjoinen ylhäällä, itä oikealla jne)*/
  kaverinKoti.setNeighbors(Vector(                        "east" -> polku,    "south" -> home                            ))
  polku      .setNeighbors(Vector("north" -> metsa,       "east" -> silta,    "south" -> kallio                          ))
  kallio     .setNeighbors(Vector("north" -> polku,                           "south" -> luola                           ))
  luola      .setNeighbors(Vector("north" -> kallio,      "east" -> tiheikko                                             ))
  tiheikko   .setNeighbors(Vector("north" -> kelokko,                                               "west" -> luola      ))
  kelokko    .setNeighbors(Vector("north" -> aukio,       "east" -> home,     "south" -> tiheikko,  "west" -> silta      ))
  aukio      .setNeighbors(Vector("north" -> lampi,                           "south" -> kelokko                         ))
  lampi      .setNeighbors(Vector(                                            "south" -> aukio,      "west" -> kaivo     ))
  kaivo      .setNeighbors(Vector("north" -> pelto,       "east" -> lampi,    "south" -> metsa                           ))
  metsa      .setNeighbors(Vector("north" -> kaivo,                           "south" -> polku                           ))
  pelto      .setNeighbors(Vector(                                            "south" -> kaivo                           ))
  silta      .setNeighbors(Vector(                        "east" -> kelokko,                         "west" -> polku     ))
  home       .setNeighbors(Vector(                                                                   "west" -> kelokko   ))

  // TODO: Uncomment the two lines below. Improve the code so that it places the items in clearing and southForest, respectively.
  //clearing.addItem(Item("battery", "It's a small battery cell. Looks new."))
  //southForest.addItem(Item("remote", "It's the remote control for your TV.\nWhat it was doing in the forest, you have no idea.\nProblem is, there's no battery."))

  /** The character that the player controls in the game. */
  val player = Player(kaverinKoti)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 40


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.destination && player.has("remote") && player.has("battery")

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You are lost in the woods. Find your way back home.\n\nBetter hurry, 'cause Scalatut elämät is on real soon now. And you can't miss Scalkkarit, right?"


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether the player has completed their quest. */
  def goodbyeMessage =
    if this.isComplete then
      "Home at last... and phew, just in time! Well done!"
    else if this.turnCount == this.timeLimit then
      "Oh no! Time's up. Starved of entertainment, you collapse and weep like a child.\nGame over!"
    else  // game over due to player quitting
      "Quitter!"


  /** Plays a turn by executing the given in-game command, such as “go west”. Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String): String =
    val action = Action(command)
    val outcomeReport = action.execute(this.player)
    if outcomeReport.isDefined then
      this.turnCount += 1
    outcomeReport.getOrElse(s"""Unknown command: "$command".""")

// testi moikka

end Adventure

