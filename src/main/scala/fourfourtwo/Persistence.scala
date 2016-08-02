package fourfourtwo

import java.sql.Timestamp
import java.util
import java.util.StringTokenizer

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.meta.MTable
import scala.slick.lifted.{TableQuery, Tag}

case class League(name: String, FFT_ID: Long, id: Long = 0)

case class Team(name: String, league_id: Long, id: Long = 0)

case class Player(FFT_name: String, FFT_id: Long, id: Long = 0)

case class Game(stadium: String, date: java.sql.Timestamp, home_team_id: Long, away_team_id: Long, league_id: Long, season: Int, full_time_goals: String, possession: String, FFT_ID: Long, saved: Boolean, id: Long = 0)

case class StartingXI(game_id: Long, team_id: Long, player_id: Long)

case class Sub(game_id: Long, team_id: Long, player_id: Long)

case class Penalty(game_id: Long, team_id: Long, player_id: Long, time: String, result: Int)
	/* result: On-Target=1 ; Off-Target=2 ; Goal=3 ; Block=4 */

case class Substitution(game_id: Long, sub_in_player_id: Long, sub_out_player_id: Long, time: String)

case class ReceivedPass(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String, result: Int)
	/* result: Success=1 ; Fail=2 ; Assist=3 ; Chance Created=4 */

case class Pass(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String, result: Int, third: Int, free_kick: Boolean)
	/* result: Success=1 ; Fail=0 ; */

case class Assist(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String, from: Boolean)
	/* from: open_play=1 ; set_play=0 ; */

case class FormationChange(game_id: Long, team_id: Long, time: String, formation: String)

case class TakeOn(game_id: Long, team_id: Long, player_id: Long, time: String, location: String, result: Boolean)
	/* result: Success=1 ; Fail=0 */

case class ChanceCreated(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String, from: Boolean)
	/* from: open_play=1 ; set_play=0 */

case class Shot(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String, from: Int, result: Int, set_play: Boolean)
	/* from: right-foot=1 ; left-foot=2 ; header=3 ; other=4
	   result: on-target=1 ; off-target=2 ; goal=3 ; blocked=4 */

case class BallRecovery(game_id: Long, team_id: Long, player_id: Long, time: String, location: String)
	/* result: Success=1 ; Fail=0 */

case class FreekickShot(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String, result: Int)
	/* result: on-target=1 ; off-target=2 ; goal=3 ; blocked=4 */

case class Corner(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String, result: Int)
	/* result: success=1 ; fail=2 ; assist=3 ; chance created=4 */

case class Tackle(game_id: Long, team_id: Long, player_id: Long, time: String, location: String, result: Boolean)
	/* result: success=1 ; fail=0  */

case class Cross(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String, result: Int)
	/* result: success=1 ; fail=2 ; assist=3 ; chance created=4 ; blocked=5  */

case class AerialDuel(game_id: Long, team_id: Long, player_id: Long, time: String, location: String, result: Boolean)
	/* result: success=1 ; fail=0  */

case class Clearance(game_id: Long, team_id: Long, player_id: Long, time: String, location: String, result: Boolean)

case class Interception(game_id: Long, team_id: Long, player_id: Long, time: String, location: String)

case class Block(game_id: Long, team_id: Long, player_id: Long, time: String, location: String)

case class DefensiveError(game_id: Long, team_id: Long, player_id: Long, time: String, location: String, leadingTo: Boolean)
	/* leadingTo: Goal=0 ; Shot=1 */

case class Foul(game_id: Long, team_id: Long, player_id: Long, time: String, location: String, how: Boolean)
	/* how: Commit=1 ; Suffer=0 */

case class BlockedCross(game_id: Long, team_id: Long, player_id: Long, time: String, location: String)

case class RedCard(game_id: Long, team_id: Long, player_id: Long, time: String)

case class OffsidePass(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String)

object Persistence {

  class Leagues(tag: Tag) extends Table[League](tag, "LEAGUES") {

    def id = column[Long]("LEAGUE_ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("LEAGUE_NAME")

    def FFT_ID = column[Long]("FFT_ID")

    def * = (name, FFT_ID, id) <>(League.tupled, League.unapply)
  }

  val leagues = TableQuery[Leagues]

  class Teams(tag: Tag) extends Table[Team](tag, "TEAMS") {
    def id = column[Long]("TEAM_ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("TEAM_NAME")

    def league_id = column[Long]("LEAGUE_ID")

    def * = (name, league_id, id) <>(Team.tupled, Team.unapply)

    def league = foreignKey("LEAGUE_ID_FK", league_id, leagues)(_.id)
  }

  val teams = TableQuery[Teams]

  class Players(tag: Tag) extends Table[Player](tag, "PLAYERS") {
    def id = column[Long]("PLAYER_ID", O.PrimaryKey, O.AutoInc)

    def FFT_name = column[String]("FFT_NAME")

    def FFT_id = column[Long]("FFT_ID")

    def * = (FFT_name, FFT_id, id) <>(Player.tupled, Player.unapply)

  }

  val players = TableQuery[Players]

  class Games(tag: Tag) extends Table[Game](tag, "GAMES") {
    def id = column[Long]("GAME_ID", O.PrimaryKey, O.AutoInc)

    def stadium = column[String]("STADIUM")

    def date = column[java.sql.Timestamp]("DATE")

    def home_team_id = column[Long]("HOME_TEAM_ID")

    def away_team_id = column[Long]("AWAY_TEAM_ID")

    def league_id = column[Long]("LEAGUE_ID")

    def season = column[Int]("SEASON")

    def full_time_goals = column[String]("FULL_TIME_GOALS")

    def possession = column[String]("POSSESSION")

    def FFT_ID = column[Long]("FFT_ID")

    def saved = column[Boolean]("GAME_SAVED")

    def * = (stadium, date, home_team_id, away_team_id, league_id, season, full_time_goals, possession, FFT_ID, saved, id) <>(Game.tupled, Game.unapply)

    def home_team = foreignKey("HOME_TEAM_ID_FK", home_team_id, teams)(_.id)

    def away_team = foreignKey("AWAY_TEAM_ID_FK", away_team_id, teams)(_.id)

    def league = foreignKey("LEAGUE_ID_FK", league_id, leagues)(_.id)
  }

  val games = TableQuery[Games]

  class StartingXIs(tag: Tag) extends Table[StartingXI](tag, "STARTINGXIS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def * = (game_id, team_id, player_id) <>(StartingXI.tupled, StartingXI.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val startingXIs = TableQuery[StartingXIs]

  class Subs(tag: Tag) extends Table[Sub](tag, "SUBS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def * = (game_id, team_id, player_id) <>(Sub.tupled, Sub.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val subs = TableQuery[Subs]

  class Penalties(tag: Tag) extends Table[Penalty](tag, "PENALTIES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def result = column[Int]("RESULT")

    def * = (game_id, team_id, player_id, time, result) <>(Penalty.tupled, Penalty.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val penalties = TableQuery[Penalties]

  class Substitutions(tag: Tag) extends Table[Substitution](tag, "SUBSTITUTIONS") {
    def game_id = column[Long]("GAME_ID")

    def sub_in_player_id = column[Long]("SUB_IN_PLAYER_ID")

    def sub_out_player_id = column[Long]("SUB_OUT_PLAYER_ID")

    def time = column[String]("TIME")

    def * = (game_id, sub_in_player_id, sub_out_player_id, time) <>(Substitution.tupled, Substitution.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def sub_in_player = foreignKey("SUB_IN_PLAYER_ID_FK", sub_in_player_id, players)(_.id)

    def sub_out_player = foreignKey("SUB_OUT_PLAYER_ID_FK", sub_out_player_id, players)(_.id)
  }

  val substitutions = TableQuery[Substitutions]

  class ReceivedPasses(tag: Tag) extends Table[ReceivedPass](tag, "RECEIVEDPASSES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCATION_END")

    def result = column[Int]("RESULT")

    def * = (game_id, team_id, player_id, time, location_start, location_end, result) <>(ReceivedPass.tupled, ReceivedPass.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val receivedpasses = TableQuery[ReceivedPasses]

  class Passes(tag: Tag) extends Table[Pass](tag, "PASSES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCATION_END")

    def result = column[Int]("RESULT")

    def third = column[Int]("THIRD")

    def free_kick = column[Boolean]("FREE_KICK")

    def * = (game_id, team_id, player_id, time, location_start, location_end, result, third, free_kick) <>(Pass.tupled, Pass.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val passes = TableQuery[Passes]

  class Assists(tag: Tag) extends Table[Assist](tag, "ASSISTS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCATION_END")

    def from = column[Boolean]("TYPE")

    def * = (game_id, team_id, player_id, time, location_start, location_end, from) <>(Assist.tupled, Assist.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val assists = TableQuery[Assists]

  class FormationChanges(tag: Tag) extends Table[FormationChange](tag, "FORMATIONCHANGES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def time = column[String]("TIME")

    def formation = column[String]("FORMATION")

    def * = (game_id, team_id, time, formation) <>(FormationChange.tupled, FormationChange.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)
  }

  val formationchanges = TableQuery[FormationChanges]

  class TakeOns(tag: Tag) extends Table[TakeOn](tag, "TAKEONS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def result = column[Boolean]("RESULT")

    def * = (game_id, team_id, player_id, time, location, result) <>(TakeOn.tupled, TakeOn.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val takeons = TableQuery[TakeOns]

  class ChancesCreated(tag: Tag) extends Table[ChanceCreated](tag, "CHANCESCREATED") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCATION_END")

    def from = column[Boolean]("FROM")

    def * = (game_id, team_id, player_id, time, location_start, location_end, from) <>(ChanceCreated.tupled, ChanceCreated.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val chancescreated = TableQuery[ChancesCreated]

  class Shots(tag: Tag) extends Table[Shot](tag, "SHOTS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCATION_END")

    def from = column[Int]("FROM")

    def result = column[Int]("RESULT")

    def set_play = column[Boolean]("FROM_SET_PLAY")

    def * = (game_id, team_id, player_id, time, location_start, location_end, from, result, set_play) <>(Shot.tupled, Shot.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val shots = TableQuery[Shots]

  class BallRecoveries(tag: Tag) extends Table[BallRecovery](tag, "BALLRECOVERIES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def * = (game_id, team_id, player_id, time, location) <>(BallRecovery.tupled, BallRecovery.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val ballrecoveries = TableQuery[BallRecoveries]

  class FreekickShots(tag: Tag) extends Table[FreekickShot](tag, "FREEKICK_SHOTS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCAION_END")

    def result = column[Int]("RESULT")

    def * = (game_id, team_id, player_id, time, location_start, location_end, result) <>(FreekickShot.tupled, FreekickShot.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val freekickshots = TableQuery[FreekickShots]

  class Corners(tag: Tag) extends Table[Corner](tag, "CORNERS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCAION_END")

    def result = column[Int]("RESULT")

    def * = (game_id, team_id, player_id, time, location_start, location_end, result) <>(Corner.tupled, Corner.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val corners = TableQuery[Corners]

  class Tackles(tag: Tag) extends Table[Tackle](tag, "TACKLES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def result = column[Boolean]("RESULT")

    def * = (game_id, team_id, player_id, time, location, result) <>(Tackle.tupled, Tackle.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val tackles = TableQuery[Tackles]

  class Crosses(tag: Tag) extends Table[Cross](tag, "CROSSES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCAION_END")

    def result = column[Int]("RESULT")

    def * = (game_id, team_id, player_id, time, location_start, location_end, result) <>(Cross.tupled, Cross.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val crosses = TableQuery[Crosses]

  class AerialDuels(tag: Tag) extends Table[AerialDuel](tag, "AERIAL_DUELS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def result = column[Boolean]("RESULT")

    def * = (game_id, team_id, player_id, time, location, result) <>(AerialDuel.tupled, AerialDuel.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val aerialduels = TableQuery[AerialDuels]

  class Clearances(tag: Tag) extends Table[Clearance](tag, "CLEARANCES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def result = column[Boolean]("RESULT")

    def * = (game_id, team_id, player_id, time, location, result) <>(Clearance.tupled, Clearance.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val clearances = TableQuery[Clearances]

  class Interceptions(tag: Tag) extends Table[Interception](tag, "INTERCEPTIONS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def * = (game_id, team_id, player_id, time, location) <>(Interception.tupled, Interception.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val interceptions = TableQuery[Interceptions]

  class Blocks(tag: Tag) extends Table[Block](tag, "BLOCKS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def * = (game_id, team_id, player_id, time, location) <>(Block.tupled, Block.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val blocks = TableQuery[Blocks]

  class DefensiveErrors(tag: Tag) extends Table[DefensiveError](tag, "DEFENSIVE_ERRORS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def leadingTo = column[Boolean]("LEADING_TO")

    def * = (game_id, team_id, player_id, time, location, leadingTo) <>(DefensiveError.tupled, DefensiveError.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val defensiveerrors = TableQuery[DefensiveErrors]

  class Fouls(tag: Tag) extends Table[Foul](tag, "FOULS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def how = column[Boolean]("LEADING_TO")

    def * = (game_id, team_id, player_id, time, location, how) <>(Foul.tupled, Foul.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val fouls = TableQuery[Fouls]

  class BlockedCrosses(tag: Tag) extends Table[BlockedCross](tag, "BLOCKED_CROSSES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location = column[String]("LOCATION")

    def * = (game_id, team_id, player_id, time, location) <>(BlockedCross.tupled, BlockedCross.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val blockedcrosses = TableQuery[BlockedCrosses]

  class RedCards(tag: Tag) extends Table[RedCard](tag, "RED_CARDS") {

    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def * = (game_id, team_id, player_id, time) <> (RedCard.tupled, RedCard.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)

  }

  val redcards = TableQuery[RedCards]

  class OffsidePasses(tag: Tag) extends Table[OffsidePass](tag, "OFFSIDE_PASSES") {

    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCATION_END")

    def * = (game_id, team_id, player_id, time, location_start, location_end) <> (OffsidePass.tupled, OffsidePass.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)

  }

  val offsidePasses = TableQuery[OffsidePasses]

  def createTables(): Unit = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession { implicit session =>
      if(!MTable.getTables("LEAGUES").list.isEmpty) {
        //println("Tables already Created.")
        return
      }

      //println("Creating Tables");

      (leagues.ddl ++ teams.ddl ++ players.ddl ++ games.ddl ++ startingXIs.ddl ++ subs.ddl ++ penalties.ddl ++ substitutions.ddl ++ receivedpasses.ddl ++ passes.ddl ++ assists.ddl ++ formationchanges.ddl ++ takeons.ddl ++ chancescreated.ddl ++ shots.ddl ++ ballrecoveries.ddl ++ freekickshots.ddl ++ corners.ddl ++ tackles.ddl ++ crosses.ddl ++ aerialduels.ddl ++ clearances.ddl ++ interceptions.ddl ++ blocks.ddl ++ defensiveerrors.ddl ++ fouls.ddl ++ blockedcrosses.ddl ++ redcards.ddl ++ offsidePasses.ddl).create
      leagues += League("Dummy", Helper.DUMMY_LEAGUE_ID) // Dummy League to accomodate special use-case
      //println("Tables Created");
      session.close
    }
  }

  def addLeague(FFTLeagueID: Long) = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        if(getLeagueID(FFTLeagueID) == Int.int2long(-1))
          leagues += League(Helper.getLeagueName(FFTLeagueID), FFTLeagueID)
        session.close
    }
  }

  def addTeam(teamName: String, FFTLeagueID: Long, category: Int): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        var league_id = getLeagueID(FFTLeagueID)
        if (league_id == Int.int2long(-1)) {
          println("League " + Helper.getLeagueName(FFTLeagueID) + " not found. Error.")
          session.close
          return false
        }
        if(category != Helper.LEAGUE)
          league_id = getLeagueID(Helper.DUMMY_LEAGUE_ID)

        val team_row = for {
          t <- teams if t.name === teamName.toLowerCase
        } yield(t.league_id)

        if (team_row.length.run == 0) {
          teams += Team(teamName.toLowerCase, league_id)
          println("Team " + teamName + " added. ")
        }
        else if(team_row.first == getLeagueID(Helper.DUMMY_LEAGUE_ID))
          team_row.update(league_id)

        session.close
    }
    return true
  }

  def addPlayer(teamName: String, playerName: String, playerID: String, FFTmatchID: String, date: java.util.Date): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val timestamp = new Timestamp(date.getTime)
        val team_id = getTeamID(teamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + teamName + " not Found. Error.")
          session.close; return false;
        }

        val player_row = for {
          p <- players if p.FFT_id === playerID.toLong
        } yield(p.id)

        if (player_row.length.run == 0) {
          val player_id = (players returning players.map(_.id)) += Player(playerName.toLowerCase, playerID.toLong)
          //println("Player " + playerName + " added.")
        }
        session.close
    }
    return true
  }

  def addMatch(stadium: String, date: java.util.Date, FFTLeagueID: Long, home_team_name: String, away_team_name: String, full_time_score: String, FFT_match_id: String, season: String, home_possession: Double, away_possession: Double): Boolean = {
    val category = Helper.category(FFTLeagueID)

    if(!addTeam(home_team_name, FFTLeagueID, category))
      return false
    if(!addTeam(away_team_name, FFTLeagueID, category))
      return false
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val home_team_id = getTeamID(home_team_name)
        val away_team_id = getTeamID(away_team_name)
        if(home_team_id == Int.int2long(-1) || away_team_id == Int.int2long(-1)) {
          println("Home Team " + home_team_name + " or Away Team " + away_team_name + " not Found. Error.")
          session.close; return false;
        }

        val league_id = getLeagueID(FFTLeagueID)
        if(league_id == Int.int2long(-1)) {
          println("League " + FFTLeagueID + " not Found. Error.")
          session.close; return false;
        }

        val game_id = getMatchID(FFT_match_id.toLong)
        if (game_id != Int.int2long(-1)) {
          deleteMatch(FFT_match_id)
        }

        games += Game(stadium, new Timestamp(date.getTime), home_team_id, away_team_id, league_id, season.toInt, full_time_score, home_possession + "-" + away_possession, FFT_match_id.toLong, false)
        session.close
    }
    return true
  }



  def addStartingXIs(FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        startingXIs += StartingXI(game_id, team_id, player_id)
        session.close
    }
    return true
  }

  def addSUBs(FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close
          return false
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        subs += Sub(game_id, team_id, player_id)
        session.close
    }
    return true
  }

  def addRedCard(FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String, time: String): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close;
          return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close;
          return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close;
          return false;
        }

        redcards += RedCard(game_id, team_id, player_id, time)

        session.close

    }
    return true
  }

  def addPenalties(penaltys: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to penaltys.size()) {
          var temp: String = penaltys.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()

          penalties += Penalty(game_id, team_id, player_id, getTime(time), result.toInt)
        }

        session.close
    }
    return true
  }

  def addFKShots(fkShots: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        var count: Int = 0

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to fkShots.size()) {
          count = count + 1
          var temp: String = fkShots.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()

          freekickshots += FreekickShot(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt)

        }
        session.close
    }
    return true
  }

  def addShots(shotss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {
    //println("Inside addShots")
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val game_id = getMatchID(FFTmatchID.toLong)
        if(game_id == Int.int2long(-1)) {
          println("Error retrieving Game Details for Game#" + FFTmatchID)
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to shotss.size()) {

          var temp: String = shotss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")

          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()
          val part: String = st.nextToken()

          if(part.toInt == 5) {
            shots += Shot(game_id, team_id, player_id, getTime(time), location_start, location_end, -1, result.toInt, true)
          }
          else {
            shots += Shot(game_id, team_id, player_id, getTime(time), location_start, location_end, part.toInt, result.toInt, false)
          }
        }

        session.close
    }
    return true
  }

  def addPasses(passs: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to passs.size()) {
          var temp: String = passs.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()
          val third: String = st.nextToken()

          if(third.toInt == 4) {
            passes += Pass(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt, -1, true)
          }
          else {
            passes += Pass(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt, third.toInt, false)
          }
        }

        session.close
    }
    return true
  }

  def addReceivedPasses(passs: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to passs.size()) {
          var temp: String = passs.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()

          receivedpasses += ReceivedPass(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt)

        }

        session.close
    }
    return true
  }

  def addAssists(assistss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to assistss.size()) {
          var temp: String = assistss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val from: String = (st.nextToken().toInt - 1).toString

          assists += Assist(game_id, team_id, player_id, getTime(time), location_start, location_end, getBoolean(from))
        }

        session.close
    }
    return true
  }

  def addChancesCreated(chances: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to chances.size()) {
          var temp: String = chances.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val from: String = (st.nextToken().toInt - 1).toString

          chancescreated += ChanceCreated(game_id, team_id, player_id, getTime(time), location_start, location_end, getBoolean(from))
        }

        session.close
    }
    return true
  }

  def addCrosses(crossess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to crossess.size()) {
          var temp: String = crossess.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()

          crosses += Cross(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt)
        }

        session.close
    }
    return true
  }

  def addTakeOns(takeonss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to takeonss.size()) {
          var temp: String = takeonss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()
          val result: String = st.nextToken()
          takeons += TakeOn(game_id, team_id, player_id, getTime(time), location, getBoolean(result))
        }

        session.close
    }
    return true
  }

  def addCorners(cornerss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to cornerss.size()) {
          val temp: String = cornerss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()

          corners += Corner(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt)
        }

        session.close
    }
    return true
  }

  def addOffsidePasses(offsidePassess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to offsidePassess.size()) {
          var temp: String = offsidePassess.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()

          offsidePasses += OffsidePass(game_id, team_id, player_id, time, location_start, location_end)
        }

        session.close
    }
    return true
  }

  def addBallRecoveries(ballRecoveriess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to ballRecoveriess.size()) {
          var temp: String = ballRecoveriess.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()

          ballrecoveries += BallRecovery(game_id, team_id, player_id, getTime(time), location)
        }
        session.close
    }
    return true
  }

  def addTackles(tackless: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to tackless.size()) {

          var temp: String = tackless.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()
          val result: String = st.nextToken()
          tackles += Tackle(game_id, team_id, player_id, getTime(time), location, getBoolean(result))
        }

        session.close
    }
    return true
  }

  def addInterceptions(interceptionss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to interceptionss.size()) {
          var temp: String = interceptionss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()

          interceptions += Interception(game_id, team_id, player_id, getTime(time), location)
        }

        session.close
    }
    return true
  }

  def addBlocks(blockss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to blockss.size()) {
          var temp: String = blockss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()

          blocks += Block(game_id, team_id, player_id, getTime(time), location)
        }

        session.close
    }
    return true
  }

  def addClearances(clearancess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to clearancess.size()) {
          var temp: String = clearancess.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()
          val result: String = st.nextToken()

          clearances += Clearance(game_id, team_id, player_id, getTime(time), location, getBoolean(result))
        }

        session.close
    }
    return true
  }

  def addAerialDuels(aerialDuelss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to aerialDuelss.size()) {

          var temp: String = aerialDuelss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()
          val result: String = st.nextToken()
          aerialduels += AerialDuel(game_id, team_id, player_id, getTime(time), location, getBoolean(result))
        }

        session.close
    }
    return true
  }

  def addBlockedCrosses(blockedCrossess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to blockedCrossess.size()) {
          var temp: String = blockedCrossess.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()

          blockedcrosses += BlockedCross(game_id, team_id, player_id, getTime(time), location)
        }

        session.close
    }
    return true
  }

  def addDefensiveErrors(defensiveErrorss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to defensiveErrorss.size()) {

          var temp: String = defensiveErrorss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()
          val leadingTo: String = (st.nextToken().toInt - 1).toString
          defensiveerrors += DefensiveError(game_id, team_id, player_id, getTime(time), location, getBoolean(leadingTo))
        }

        session.close
    }
    return true
  }

  def addFouls(foulss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>

        val game_id = getMatchID(FFTmatchID.toLong)
        if (game_id == Int.int2long(-1)) {
          println("Game " + FFTmatchID + " not found. Error.")
          session.close; return false;
        }

        val team_id = getTeamID(FFTteamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + FFTteamName + " not Found. Error.")
          session.close; return false;
        }

        val player_id = getPlayerID(FFTplayerID.toLong)
        if (player_id == Int.int2long(-1)) {
          println("Player " + FFTplayerID + " not found. Error.")
          session.close; return false;
        }

        for (i <- 1 to foulss.size()) {

          var temp: String = foulss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()
          val who: String = (st.nextToken().toInt - 1).toString
          fouls += Foul(game_id, team_id, player_id, getTime(time), location, getBoolean(who))
        }

        session.close
    }
    return true
  }

  def addSubstitutions(FFT_match_id: String, sub_in_id: String, sub_out_id: String): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val game_id = getMatchID(FFT_match_id.toLong)
        val sub_in = getPlayerID(sub_in_id.toLong)
        val sub_out = getPlayerID(sub_out_id.toLong)
        if(game_id == Int.int2long(-1) || sub_in == Int.int2long(-1) || sub_out == Int.int2long(-1)) {
          println("Game or Team or Player not found for Adding substitutions.")
          session.close
          return false
        }

        substitutions += Substitution(game_id, sub_in, sub_out, "")
        session.close
    }
    return true
  }

  def getTime(time: String): String = {
    return time
    //return time.split("-")(1).trim.toInt
  }

  def getBoolean(result: String): Boolean = {
    return (if (result.toInt == 0) false else true)
  }

  def getLeagueID(FFT_League_ID: Long)(implicit session: Session): Long = {
    val league_row = for {
      l <- leagues if l.FFT_ID === FFT_League_ID && l.name === Helper.getLeagueName(FFT_League_ID)
    } yield(l.id)

    return if(league_row.length.run == 1) league_row.first else Int.int2long(-1)
  }

  def getPlayerID(FFTplayerID: Long)(implicit session: Session): Long = {
    val player_row = for {
      p <- players if p.FFT_id === FFTplayerID
    } yield(p.id)

    return if(player_row.length.run == 1) player_row.first else Int.int2long(-1)
  }

  def getTeamID(FFTteamName: String)(implicit session: Session): Long = {
    val team_row = for {
      t <- teams if t.name === FFTteamName.toLowerCase
    } yield(t.id)

    return if(team_row.length.run == 1) team_row.first else Int.int2long(-1)
  }

  def getMatchID(FFTmatchID: Long)(implicit session: Session): Long = {
    val game_row = for {
      g <- games if g.FFT_ID === FFTmatchID
    } yield(g.id)

    return if(game_row.length.run == 1) game_row.first else Int.int2long(-1)
  }

  def gameExists(FFT_match_id: String): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val game_row = for {
          g <- games if g.FFT_ID === FFT_match_id.toLong
        } yield(g.saved)
        session.close
        if(game_row.length.run != 1 || game_row.first == false) {
          return false
        }
    }
    return true
  }

  def gameSaved(FFT_match_id: String): Boolean = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val game_row = for {
          g <- games if g.FFT_ID === FFT_match_id.toLong
        } yield(g.saved)

        if(game_row.length.run != 1) {
          println("Game " + FFT_match_id + " details not found. Error. Exiting.")
          session.close
          return false
        }

        game_row.update(true)
        session.close
    }
    return true
  }

  def deleteMatch(FFTmatchID: String): Unit = {
    Database.forURL(Helper.dbURL, driver = Helper.dbDriver) withSession {
      implicit session =>
        val matchID = getMatchID(FFTmatchID.toLong)
        if(matchID == Int.int2long(-1)) {
          println("Match " + FFTmatchID + " could not be retrieved. No match record exists. Nothing to delete.")
          return
        }
        val aerial_duelRow = for {
          a <- aerialduels if a.game_id === matchID
        } yield (a)
        aerial_duelRow.delete

        val assistsRow = for {
          a <- assists if a.game_id === matchID
        } yield (a)
        assistsRow.delete
        val brRow = for {
          a <- ballrecoveries if a.game_id === matchID
        } yield (a)
        brRow.delete
        val bcRow = for {
          a <- blockedcrosses if a.game_id === matchID
        } yield (a)
        bcRow.delete
        val bRow = for {
          a <- blocks if a.game_id === matchID
        } yield (a)
        bRow.delete
        val ccRow = for {
          a <- chancescreated if a.game_id === matchID
        } yield (a)
        ccRow.delete
        val cRow = for {
          a <- clearances if a.game_id === matchID
        } yield (a)
        cRow.delete
        val coRow = for {
          a <- corners if a.game_id === matchID
        } yield (a)
        coRow.delete
        val crRow = for {
          a <- crosses if a.game_id === matchID
        } yield (a)
        crRow.delete
        val deRow = for {
          a <- defensiveerrors if a.game_id === matchID
        } yield (a)
        deRow.delete
        val fcRow = for {
          a <- formationchanges if a.game_id === matchID
        } yield (a)
        fcRow.delete
        val fRow = for {
          a <- fouls if a.game_id === matchID
        } yield (a)
        fRow.delete
        val fksRow = for {
          a <- freekickshots if a.game_id === matchID
        } yield (a)
        fksRow.delete
        val iRow = for {
          a <- interceptions if a.game_id === matchID
        } yield (a)
        iRow.delete
        val pRow = for {
          a <- passes if a.game_id === matchID
        } yield (a)
        pRow.delete
        val peRow = for {
          a <- penalties if a.game_id === matchID
        } yield (a)
        peRow.delete
        val rpRow = for {
          a <- receivedpasses if a.game_id === matchID
        } yield (a)
        rpRow.delete
        val sRow = for {
          a <- shots if a.game_id === matchID
        } yield (a)
        sRow.delete
        val sxRow = for {
          a <- startingXIs if a.game_id === matchID
        } yield (a)
        sxRow.delete
        val suRow = for {
          a <- subs if a.game_id === matchID
        } yield (a)
        suRow.delete
        val subRow = for {
          a <- substitutions if a.game_id === matchID
        } yield (a)
        subRow.delete
        val tRow = for {
          a <- tackles if a.game_id === matchID
        } yield (a)
        tRow.delete
        val toRow = for {
          a <- takeons if a.game_id === matchID
        } yield (a)
        toRow.delete
        val rcRow = for {
          r <- redcards if r.game_id === matchID
        } yield(r)
        rcRow.delete
        val opRow = for {
          o <- offsidePasses if o.game_id === matchID
        } yield(o)
        opRow.delete
        val gaRow = for {
          a <- games if a.id === matchID
        } yield (a)
        gaRow.delete
        session.close
    }
  }
}