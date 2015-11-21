package fourfourtwo

import java.sql.Timestamp
import java.util
import java.util.StringTokenizer

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.meta.MTable
import scala.slick.lifted.{TableQuery, Tag}

case class League(name: String, FFT_ID: Long, id: Long = 0)

case class Team(name: String, league_id: Long, id: Long = 0)

case class Player(team_id: Long, FFT_name: String, FFT_id: Long, id: Long = 0)

case class Transfer(player_id: Long, team_from_id: Long, team_to_id: Long, date_from: Timestamp, date_to: Timestamp, id: Long = 0)

case class Game(stadium: String, date: java.sql.Timestamp, home_team_id: Long, away_team_id: Long, league_id: Long, season: Int, full_time_goals: String, half_time_goals: String, starting_formation: String, shots: String, possession: String, dribbles: String, aerial_duels: String, tackles: String, offsides: String, corners: String, throw_ins: String, fouls: String, FFT_ID: Long, saved: Boolean, id: Long = 0)

case class StartingXI(game_id: Long, team_id: Long, player_id: Long)

case class Sub(game_id: Long, team_id: Long, player_id: Long)

case class Penalty(game_id: Long, team_id: Long, player_id: Long, time: String, result: Int)
	/* result: On-Target=1 ; Off-Target=2 ; Goal=3 ; Block=4 */

case class Substitution(game_id: Long, team_id: Long, sub_in_player_id: Long, sub_out_player_id: Long, time: String)

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

case class LongPass(game_id: Long, team_id: Long, player_id: Long, count: Int, success_count: Int, fail_count: Int, assist_count: Int, chances_created_count: Int)

case class ShortPass(game_id: Long, team_id: Long, player_id: Long, count: Int, success_count: Int, fail_count: Int, assist_count: Int, chances_created_count: Int)

case class Shot(game_id: Long, team_id: Long, player_id: Long, time: String, location_start: String, location_end: String, from: Int, result: Int, from_set_play: Boolean, from_free_kick: Boolean, from_penalty: Boolean)
	/* from: right-foot=1 ; left-foot=2 ; header=3 ; other=4
	   result: on-target=1 ; off-target=2 ; goal=3 ; blocked=4 */

case class BallRecovery(game_id: Long, team_id: Long, player_id: Long, time: String, location: String)
	/* result: Success=1 ; Fail=0 */

case class PassSummary(game_id: Long, team_id: Long, player_id: Long, received_count: Int, success_count: Int, fail_count: Int, assist_count: Int, chances_created_count: Int, free_kick_count: Int, long_count: Int, short_count: Int, offside_passes_count: Int, throw_ins_count: Int)

case class ShotSummary(game_id: Long, team_id: Long, player_id: Long, free_kick_count: Int, own_goal_count: Int, penalty_scored_count: Int, penalty_missed_count: Int, woodwork_count: Int, goal_count: Int, shots_count: Int, shots_on_target_count: Int, shots_blocked_count: Int)

case class OtherSummary(game_id: Long, player_id: Long, team_id: Long, corners_count: Int, crosses_count: Int, tackle_success_count: Int, tackle_fail_count: Int, interception_count: Int, block_count: Int, clearance_count: Int, headed_clearance_count: Int, aerial_duels_won_count: Int, aerial_duels_lost_count: Int, blocked_crosses_count: Int, error_leading_to_shot_count: Int, error_leading_to_goal_count: Int, fouls_commit_count: Int, fouls_suffer_count: Int, yellow_card_count: Int, red_card_count: Int, take_on_success_count: Int, take_on_fail_count: Int)

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
  val dbURL: String = "jdbc:postgresql://localhost:5432/FFT_DATA?user=gsm&password=0909"
  val dbDriver: String = "org.postgresql.Driver"

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

    def team_id = column[Long]("TEAM_ID")

    def FFT_name = column[String]("FFT_NAME")

    def FFT_id = column[Long]("FFT_ID")

    def * = (team_id, FFT_name, FFT_id, id) <>(Player.tupled, Player.unapply)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)
  }

  val players = TableQuery[Players]

  class Transfers(tag: Tag) extends Table[Transfer](tag, "TRANSFERS") {
    def id = column[Long]("TRANSFER_ID", O.PrimaryKey, O.AutoInc)

    def player_id = column[Long]("PLAYER_ID")

    def team_from_id = column[Long]("TEAM_FROM_ID")

    def team_to_id = column[Long]("TEAM_TO_ID")

    def date_from = column[Timestamp]("START_DATE")

    def date_to = column[Timestamp]("LAST_DATE")

    def * = (player_id, team_from_id, team_to_id, date_from, date_to, id) <> (Transfer.tupled, Transfer.unapply)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)

    def team_from = foreignKey("TEAM_FROM_FK", team_from_id, teams)(_.id)

    def team_to = foreignKey("TEAM_TO_FK", team_to_id, teams)(_.id)
  }

  val transfers = TableQuery[Transfers]

  class Games(tag: Tag) extends Table[Game](tag, "GAMES") {
    def id = column[Long]("GAME_ID", O.PrimaryKey, O.AutoInc)

    def stadium = column[String]("STADIUM")

    def date = column[java.sql.Timestamp]("DATE")

    def home_team_id = column[Long]("HOME_TEAM_ID")

    def away_team_id = column[Long]("AWAY_TEAM_ID")

    def league_id = column[Long]("LEAGUE_ID")

    def season = column[Int]("SEASON")

    def full_time_goals = column[String]("FULL_TIME_GOALS")

    def half_time_goals = column[String]("HALF_TIME_GOALS")

    def starting_formation = column[String]("STARTING_FORMATION")

    def shots = column[String]("SHOTS")

    def possession = column[String]("POSSESSION")

    def dribbles = column[String]("DRIBBLES")

    def aerial_duels = column[String]("AERIAL_DUELS")

    def tackles = column[String]("TACKLES")

    def offsides = column[String]("OFFSIDES")

    def corners = column[String]("CORNERS")

    def throw_ins = column[String]("THROW_INS")

    def fouls = column[String]("FOULS")

    def FFT_ID = column[Long]("FFT_ID")

    def saved = column[Boolean]("GAME_SAVED")

    def * = (stadium, date, home_team_id, away_team_id, league_id, season, full_time_goals, half_time_goals, starting_formation, shots, possession, dribbles, aerial_duels, tackles, offsides, corners, throw_ins, fouls, FFT_ID, saved, id) <>(Game.tupled, Game.unapply)

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

    def team_id = column[Long]("TEAM_ID")

    def sub_in_player_id = column[Long]("SUB_IN_PLAYER_ID")

    def sub_out_player_id = column[Long]("SUB_OUT_PLAYER_ID")

    def time = column[String]("TIME")

    def * = (game_id, team_id, sub_in_player_id, sub_out_player_id, time) <>(Substitution.tupled, Substitution.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

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

  class LongPasses(tag: Tag) extends Table[LongPass](tag, "LONGPASSES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def count = column[Int]("COUNT")

    def success_count = column[Int]("SUCCESS_COUNT")

    def fail_count = column[Int]("FAIL_COUNT")

    def assist_count = column[Int]("ASSIST_COUNT")

    def chances_created_count = column[Int]("CHANCES_CREATED")

    def * = (game_id, team_id, player_id, count, success_count, fail_count, assist_count, chances_created_count) <>(LongPass.tupled, LongPass.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val longpasses = TableQuery[LongPasses]

  class ShortPasses(tag: Tag) extends Table[ShortPass](tag, "SHORTPASSES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def count = column[Int]("COUNT")

    def success_count = column[Int]("SUCCESS_COUNT")

    def fail_count = column[Int]("FAIL_COUNT")

    def assist_count = column[Int]("ASSIST_COUNT")

    def chances_created_count = column[Int]("CHANCES_CREATED")

    def * = (game_id, team_id, player_id, count, success_count, fail_count, assist_count, chances_created_count) <>(ShortPass.tupled, ShortPass.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val shortpasses = TableQuery[ShortPasses]

  class Shots(tag: Tag) extends Table[Shot](tag, "SHOTS") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def time = column[String]("TIME")

    def location_start = column[String]("LOCATION_START")

    def location_end = column[String]("LOCATION_END")

    def from = column[Int]("FROM")

    def result = column[Int]("RESULT")

    def from_set_play = column[Boolean]("FROM_SET_PLAY")

    def from_free_kick = column[Boolean]("FROM_FREE_KICK")

    def from_penalty = column[Boolean]("FROM_PENALTY")

    def * = (game_id, team_id, player_id, time, location_start, location_end, from, result, from_set_play, from_free_kick, from_penalty) <>(Shot.tupled, Shot.unapply)

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

  class PassSummaries(tag: Tag) extends Table[PassSummary](tag, "PASS_SUMMARIES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def received_count = column[Int]("PASSES_RECEIVED_COUNT")

    def success_count = column[Int]("PASSES_SUCCESS_COUNT")

    def fail_count = column[Int]("PASSES_FAIL_COUNT")

    def assist_count = column[Int]("ASSIST_COUNT")

    def chances_created_count = column[Int]("CHANCES_CREATED_COUNT")

    def free_kick_count = column[Int]("FREE_KICK_PASSES_COUNT")

    def long_count = column[Int]("LONG_PASSES_COUNT")

    def short_count = column[Int]("SHORT_PASSES_COUNT")

    def offside_passes_count = column[Int]("OFFSIDE_PASSES_COUNT")

    def throw_ins_count = column[Int]("THROW_INS_COUNT")

    def * = (game_id, team_id, player_id, received_count, success_count, fail_count, assist_count, chances_created_count, free_kick_count, long_count, short_count, offside_passes_count, throw_ins_count) <>(PassSummary.tupled, PassSummary.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val passsummaries = TableQuery[PassSummaries]

  class ShotSummaries(tag: Tag) extends Table[ShotSummary](tag, "SHOT_SUMMARIES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def free_kick_count = column[Int]("FREE_KICK_SHOTS_COUNT")

    def own_goal_count = column[Int]("OWN_GOAL_COUNT")

    def penalty_scored_count = column[Int]("PENALTY_SCORED_COUNT")

    def penalty_missed_count = column[Int]("PENALTY_MISSED_COUNT")

    def woodwork_count = column[Int]("WOODWORK_COUNT")

    def goal_count = column[Int]("GOAL_COUNT")

    def shots_count = column[Int]("SHOTS_COUNT")

    def shots_on_target_count = column[Int]("SHOTS_ON_TARGET_COUNT")

    def shots_blocked_count = column[Int]("SHOTS_BLOCKED_COUNT")

    def * = (game_id, team_id, player_id, free_kick_count, own_goal_count, penalty_scored_count, penalty_missed_count, woodwork_count, goal_count, shots_count, shots_on_target_count, shots_blocked_count) <>(ShotSummary.tupled, ShotSummary.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val shotsummaries = TableQuery[ShotSummaries]

  class OtherSummaries(tag: Tag) extends Table[OtherSummary](tag, "OTHER_SUMMARIES") {
    def game_id = column[Long]("GAME_ID")

    def team_id = column[Long]("TEAM_ID")

    def player_id = column[Long]("PLAYER_ID")

    def corners_count = column[Int]("CORNERS_COUNT")

    def crosses_count = column[Int]("CROSSES_COUNT")

    def tackle_success_count = column[Int]("TACKLE_SUCCESS_COUNT")

    def tackle_fail_count = column[Int]("TACKLE_FAIL_COUNT")

    def interception_count = column[Int]("INTERCEPTION_COUNT")

    def block_count = column[Int]("BLOCK_COUNT")

    def clearance_count = column[Int]("CLEARANCE_COUNT")

    def headed_clearance_count = column[Int]("HEADED_CLEARANCE_COUNT")

    def aerial_duels_won_count = column[Int]("AERIAL_DUELS_WON_COUNT")

    def aerial_duels_lost_count = column[Int]("AERIAL_DUELS_LOST_COUNT")

    def blocked_crosses_count = column[Int]("BLOCKED_CROSSES_COUNT")

    def error_leading_to_shot_count = column[Int]("ERROR_LEADING_TO_SHOT_COUNT")

    def error_leading_to_goal_count = column[Int]("ERROR_LEADING_TO_GOAL_COUNT")

    def fouls_commit_count = column[Int]("FOULS_COMMIT_COUNT")

    def fouls_suffer_count = column[Int]("FOULS_SUFFER_COUNT")

    def yellow_card_count = column[Int]("YELLOW_CARD_COUNT")

    def red_card_count = column[Int]("RED_CARD_COUNT")

    def take_on_success_count = column[Int]("DRIBBLE_SUCCESS")

    def take_on_fail_count = column[Int]("DRIBBLE_FAIL")

    def * = (game_id, team_id, player_id, corners_count, crosses_count, tackle_success_count, tackle_fail_count, interception_count, block_count, clearance_count, headed_clearance_count, aerial_duels_won_count, aerial_duels_lost_count, blocked_crosses_count, error_leading_to_shot_count, error_leading_to_goal_count, fouls_commit_count, fouls_suffer_count, yellow_card_count, red_card_count, take_on_success_count, take_on_fail_count) <>(OtherSummary.tupled, OtherSummary.unapply)

    def game = foreignKey("GAME_ID_FK", game_id, games)(_.id)

    def team = foreignKey("TEAM_ID_FK", team_id, teams)(_.id)

    def player = foreignKey("PLAYER_ID_FK", player_id, players)(_.id)
  }

  val othersummaries = TableQuery[OtherSummaries]

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
    Database.forURL(dbURL, driver = dbDriver) withSession { implicit session =>
      if(!MTable.getTables("LEAGUES").list.isEmpty) {
        //println("Tables already Created.")
        return
      }

      //println("Creating Tables");

      (leagues.ddl ++ teams.ddl ++ players.ddl ++ transfers.ddl ++ games.ddl ++ startingXIs.ddl ++ subs.ddl ++ penalties.ddl ++ substitutions.ddl ++ receivedpasses.ddl ++ passes.ddl ++ assists.ddl ++ formationchanges.ddl ++ takeons.ddl ++ chancescreated.ddl ++ longpasses.ddl ++ shortpasses.ddl ++ shots.ddl ++ ballrecoveries.ddl ++ passsummaries.ddl ++ shotsummaries.ddl ++ othersummaries.ddl ++ freekickshots.ddl ++ corners.ddl ++ tackles.ddl ++ crosses.ddl ++ aerialduels.ddl ++ clearances.ddl ++ interceptions.ddl ++ blocks.ddl ++ defensiveerrors.ddl ++ fouls.ddl ++ blockedcrosses.ddl ++ redcards.ddl ++ offsidePasses.ddl).create

      //println("Tables Created");
      session.close
    }
  }

  def addLeague(FFTLeagueID: Long) = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        if(getLeagueID(FFTLeagueID) == Int.int2long(-1))
          leagues += League(Helper.getLeagueName(FFTLeagueID), FFTLeagueID)
        session.close
    }
  }

  def addTeam(teamName: String, FFTLeagueID: Long, category: Int): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var league_id = getLeagueID(FFTLeagueID)
        if (league_id == Int.int2long(-1)) {
          println("League " + Helper.getLeagueName(FFTLeagueID) + " not found. Error.")
          session.close
          return false
        }
        if(category != Helper.LEAGUE)
          league_id = -1

        val team_row = for {
          t <- teams if t.name === teamName.toLowerCase
        } yield(t.league_id)

        if (team_row.length.run == 0) {
          teams += Team(teamName.toLowerCase, league_id)
          println("Team " + teamName + " added. ")
        }
        else if(team_row.first == Int.int2long(-1))
          team_row.update(league_id)

        session.close
    }
    return true
  }

  def addPlayer(teamName: String, playerName: String, playerID: String, FFTmatchID: String, date: java.util.Date): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>

        val timestamp = new Timestamp(date.getTime)
        val team_id = getTeamID(teamName)
        if (team_id == Int.int2long(-1)) {
          println("Team " + teamName + " not Found. Error.")
          session.close; return false;
        }

        val player_row = for {
          p <- players if p.FFT_id === playerID.toLong
        } yield(p.id, p.team_id)

        if (player_row.length.run == 0) {
          val player_id = (players returning players.map(_.id)) += Player(team_id, playerName.toLowerCase, playerID.toLong)
          transfers += Transfer(player_id, team_id, team_id, new Timestamp(date.getTime), new Timestamp(date.getTime))
          //println("Player " + playerName + " added.")
        }
        else {
          if (player_row.first._2 != team_id) {
            val transfer_row_check = for {
              t <- transfers if t.player_id === player_row.first._1 && t.team_from_id === team_id && t.team_to_id === player_row.first._2
            } yield(t.date_to)

            if(transfer_row_check.length.run != 0) {
              transfer_row_check.foreach((time) => if(time.after(timestamp)) return true)
            }

            //println("Player " + playerName + " team updated from " + player_row.first._2 + " to " + team_id)
            player_row.update(player_row.first._1, team_id)

            val transfer_row = for {
              t <- transfers if t.player_id === player_row.first._1 && t.team_to_id === t.team_from_id
            } yield(t.team_to_id, t.date_to)
            if(transfer_row.length.run != 1) {
              println("Transfer Row Length not equal to 1. Error.")
              session.close; return false;
            }
            transfer_row.update(team_id, new Timestamp(date.getTime))
            transfers += Transfer(player_row.first._1, team_id, team_id, timestamp, new Timestamp(date.getTime))
          }
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
    Database.forURL(dbURL, driver = dbDriver) withSession {
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

        games += Game(stadium, new Timestamp(date.getTime), home_team_id, away_team_id, league_id, season.toInt, full_time_score, "", "", "0-0-0-0", home_possession + "-" + away_possession, "0-0-0-0", "0-0-0-0", "0-0-0-0", "0-0", "0-0", "0-0", "0-0", FFT_match_id.toLong, false)
        session.close
    }
    return true
  }



  def addStartingXIs(FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
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
    Database.forURL(dbURL, driver = dbDriver) withSession {
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
    Database.forURL(dbURL, driver = dbDriver) withSession {
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

        val othersummaryRow = for {
          o <- othersummaries if o.game_id === game_id && o.team_id === team_id && o.player_id === player_id
        } yield (o.red_card_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close
          return false
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(1)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(game_id, team_id, player_id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0)
        }

        session.close

    }
    return true
  }

  def addPenalties(penaltys: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var penalty_missed: Int = 0
        var penalty_scored: Int = 0

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

          val shotsRow = for {
            s <- shots if s.game_id === game_id && s.team_id === team_id && s.player_id === player_id && s.time === getTime(time) && s.location_start === location_start && s.location_end === location_end && s.result === result.toInt
          } yield(s.from_penalty)

          if(shotsRow.length.run > 1) {
            println("More than 1 Shots Row found in addPenalties. Some Error.")
            session.close
            return false
          }
          else if(shotsRow.length.run < 1) {
            //println("Shot not found to add Penalty information. Adding Shot Information.")
            //println("GameID - " + game_id + " TeamID = " + team_id + " Player ID = " + player_id + " Time = " + time + " Location: " + location_start + " - " + location_end + " Result = " + result)
            shots += Shot(game_id, team_id, player_id, getTime(time), location_start, location_end, -1, result.toInt, false, false, true)
          }
          else
            shotsRow.update(true)

          penalties += Penalty(game_id, team_id, player_id, getTime(time), result.toInt)
          if (result.toInt == 3)
            penalty_scored = penalty_scored + 1
          else
            penalty_missed = penalty_missed + 1
        }

        val shotsummaryRow = for {
          s <- shotsummaries if s.game_id === game_id && s.team_id === team_id && s.player_id === player_id
        } yield (s.penalty_missed_count, s.penalty_scored_count)
        if (shotsummaryRow.length.run > 1) {
          println("More than 1 Shot Summary Found. Error.")
          session.close
          return false
        }
        else if (shotsummaryRow.length.run == 1) {
          shotsummaryRow.update(penalty_missed, penalty_scored)
        }
        else if (shotsummaryRow.length.run == 0) {
          shotsummaries += ShotSummary(game_id, team_id, player_id, 0, 0, penalty_scored, penalty_missed, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addFKShots(fkShots: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
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

          val shotsRow = for {
            s <- shots if s.game_id === game_id && s.team_id === team_id && s.player_id === player_id && s.time === getTime(time) && s.location_start === location_start && s.location_end === location_end && s.result === result.toInt
          } yield(s.from_free_kick)

          if(shotsRow.length.run > 1) {
            println("Inside addFKSHots. " + shotsRow.length.run + " Shots Row found. Some error. Details.")
            println("Game ID = " + game_id + " Team ID = " + team_id + " Player ID = " + player_id + " Time = " + time + " Location Start = " + location_start + " End = " + location_end + " Result = " + result)
            session.close; return false;
          }
          else if(shotsRow.length.run < 1) {
            //println("Shot not found to add Free Kick information. Adding Shot.")
            //println("GameID - " + game_id + " TeamID = " + team_id + " Player ID = " + player_id + " Time = " + time + " Location: " + location_start + " - " + location_end + " Result = " + result)
            shots += Shot(game_id, team_id, player_id, getTime(time), location_start, location_end, -1, result.toInt, false, true, false)
          }
          else
            shotsRow.update(true)

          freekickshots += FreekickShot(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt)

        }
        val shotsummaryRow = for {
          s <- shotsummaries if s.game_id === game_id && s.team_id === team_id && s.player_id === player_id
        } yield (s.free_kick_count)
        if (shotsummaryRow.length.run > 1) {
          println("More than 1 Shot Summary Found. Error.")
          session.close; return false;
        }
        else if (shotsummaryRow.length.run == 1) {
          shotsummaryRow.update(count)
        }
        else if (shotsummaryRow.length.run == 0) {
          shotsummaries += ShotSummary(game_id, team_id, player_id, count, 0, 0, 0, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addShots(shotss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {
    //println("Inside addShots")
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var shots_total: Int = 0
        var shots_on_target: Int = 0
        var goals_count: Int = 0
        var shots_blocked_count: Int = 0

        val gameRow = for {
          g <- games if g.FFT_ID === FFTmatchID.toLong && g.season === season.toInt
        } yield (g.id, g.shots, g.home_team_id, g.away_team_id)
        if (gameRow.length.run != 1) {
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
            val shotsRow = for {
              s <- shots if s.game_id === gameRow.first._1 && s.team_id === team_id && s.player_id === player_id && s.time === getTime(time) && s.location_start === location_start && s.location_end === location_end && s.result === result.toInt
            } yield(s.from_set_play)

            if(shotsRow.length.run > 1) {
              println("Inside addShots. More than 1 ShotsRow found. Error.")
              session.close; return false;
            }
            else if(shotsRow.length.run < 1) {
              shots_total = shots_total + 1
              if (result.toInt == 3) {
                goals_count = goals_count + 1
                shots_on_target = shots_on_target + 1
              }
              else if (result.toInt == 1)
                shots_on_target = shots_on_target + 1
              else if (result.toInt == 4)
                shots_blocked_count = shots_blocked_count + 1

              //println("Shot Details not found to add set_play information to. Adding Shots Row. Details - ")
              //println("GameID = " + gameRow.first._1 + " TeamID = " + team_id + " PlayerID = " + player_id + " Time - " + time + " Location From-To = " + location_start + " - " + location_end + " Result = " + result)
              shots += Shot(gameRow.first._1, team_id, player_id, getTime(time), location_start, location_end, -1, result.toInt, true, false, false)
              //return false
            }
            else
              shotsRow.update(true)
          }
          else {
            shots_total = shots_total + 1
            shots += Shot(gameRow.first._1, team_id, player_id, getTime(time), location_start, location_end, part.toInt, result.toInt, false, false, false)
            if (result.toInt == 3) {
              goals_count = goals_count + 1
              shots_on_target = shots_on_target + 1
            }
            else if (result.toInt == 1)
              shots_on_target = shots_on_target + 1
            else if (result.toInt == 4)
              shots_blocked_count = shots_blocked_count + 1
          }
        }
        val shotsummaryRow = for {
          s <- shotsummaries if s.game_id === gameRow.first._1 && s.team_id === team_id && s.player_id === player_id
        } yield (s.goal_count, s.shots_count, s.shots_on_target_count, s.shots_blocked_count)
        if (shotsummaryRow.length.run > 1) {
          println("More than 1 Shot Summary Found. Error.")
          session.close; return false;
        }
        else if (shotsummaryRow.length.run == 1) {
          shotsummaryRow.update(shotsummaryRow.first._1 + goals_count, shotsummaryRow.first._2 + shots_total, shotsummaryRow.first._3 + shots_on_target, shotsummaryRow.first._4 + shots_blocked_count)
        }
        else if (shotsummaryRow.length.run == 0) {
          shotsummaries += ShotSummary(gameRow.first._1, team_id, player_id, 0, 0, 0, 0, 0, goals_count, shots_total, shots_on_target, shots_blocked_count)
        }

        val temp: StringTokenizer = new StringTokenizer(gameRow.first._2, "-");
        if (team_id == gameRow.first._3) {
          val home_shots: Int = temp.nextToken().toInt + shots_total
          val home_shots_on_target: Int = temp.nextToken().toInt + shots_on_target
          val away_shots: Int = temp.nextToken().toInt
          val away_shots_on_target: Int = temp.nextToken().toInt
          gameRow.update(gameRow.first._1, home_shots + "-" + home_shots_on_target + "-" + away_shots + "-" + away_shots_on_target, gameRow.first._3, gameRow.first._4)
        }
        else if (team_id == gameRow.first._4) {
          val home_shots: Int = temp.nextToken().toInt
          val home_shots_on_target: Int = temp.nextToken().toInt
          val away_shots: Int = temp.nextToken().toInt + shots_total
          val away_shots_on_target: Int = temp.nextToken().toInt + shots_on_target
          gameRow.update(gameRow.first._1, home_shots + "-" + home_shots_on_target + "-" + away_shots + "-" + away_shots_on_target, gameRow.first._3, gameRow.first._4)
        }
        else {
          println("Strange Error - 1")
          session.close; return false;
        }
        session.close
    }
    return true
  }

  def addPasses(passs: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var passes_total: Int = 0
        var passes_success: Int = 0
        var passes_fail: Int = 0
        var fk_passes: Int = 0

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
          passes_total = passes_total + 1
          var temp: String = passs.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()
          val third: String = st.nextToken()

          if(third.toInt == 4) {
            fk_passes = fk_passes + 1
            val passRow = for {
              p <- passes if p.game_id === game_id && p.team_id === team_id && p.player_id === player_id && p.time === getTime(time) && p.location_start === location_start && p.location_end === location_end && p.result === result.toInt
            } yield(p.free_kick)

            if(passRow.length.run > 1) {
              println("Inside addPasses. More than 1 Passrow found. Error.")
              session.close; return false;
            }
            else if(passRow.length.run < 1) {
              if (result.toInt == 2)
                passes_fail = passes_fail + 1
              else if (result.toInt == 1 || result.toInt == 3 || result.toInt == 4)
                passes_success = passes_success + 1
              //println("Free Kick Pass not found. Adding pass. Game Details - ")
              //println("Game ID - " + game_id + " Team_ID = " + team_id + " Player_ID = " + player_id + " Time = " + time + " From-To = " + location_start + " - " + location_end + " Result - " + result)
              passes += Pass(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt, -1, true)
            }
            else
              passRow.update(true)
          }
          else {
            passes += Pass(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt, third.toInt, false)
            if (result.toInt == 2)
              passes_fail = passes_fail + 1
            else if (result.toInt == 1 || result.toInt == 3 || result.toInt == 4)
              passes_success = passes_success + 1
          }
        }

        val passsummaryRow = for {
          p <- passsummaries if p.game_id === game_id && p.team_id === team_id && p.player_id === player_id
        } yield (p.success_count, p.fail_count, p.free_kick_count)
        if (passsummaryRow.length.run > 1) {
          println("More than 1 Pass Summary Found. Error.")
          session.close; return false;
        }
        else if (passsummaryRow.length.run == 1) {
          passsummaryRow.update(passsummaryRow.first._1 + passes_success, passsummaryRow.first._2 + passes_fail, passsummaryRow.first._3 + fk_passes)
        }
        else if (passsummaryRow.length.run == 0) {
          passsummaries += PassSummary(game_id, team_id, player_id, 0, passes_success, passes_fail, 0, 0, fk_passes, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addReceivedPasses(passs: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var received_total: Int = 0

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
          received_total = received_total + 1
          var temp: String = passs.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()

          receivedpasses += ReceivedPass(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt)

        }
        val passsummaryRow = for {
          p <- passsummaries if p.game_id === game_id && p.team_id === team_id && p.player_id === player_id
        } yield (p.received_count)
        if (passsummaryRow.length.run > 1) {
          println("More than 1 Pass Summary Found. Error.")
          session.close; return false;
        }
        else if (passsummaryRow.length.run == 1) {
          passsummaryRow.update(received_total)
        }
        else if (passsummaryRow.length.run == 0) {
          passsummaries += PassSummary(game_id, team_id, player_id, received_total, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addAssists(assistss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var assists_total: Int = 0

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
          assists_total = assists_total + 1
          var temp: String = assistss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val from: String = (st.nextToken().toInt - 1).toString

          assists += Assist(game_id, team_id, player_id, getTime(time), location_start, location_end, getBoolean(from))
        }
        val passsummaryRow = for {
          p <- passsummaries if p.game_id === game_id && p.team_id === team_id && p.player_id === player_id
        } yield (p.assist_count)
        if (passsummaryRow.length.run > 1) {
          println("More than 1 Pass Summary Found. Error.")
          session.close; return false;
        }
        else if (passsummaryRow.length.run == 1) {
          passsummaryRow.update(passsummaryRow.first + assists_total)
        }
        else if (passsummaryRow.length.run == 0) {
          passsummaries += PassSummary(game_id, team_id, player_id, 0, 0, 0, assists_total, 0, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addChancesCreated(chances: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var chances_total: Int = 0

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
          chances_total = chances_total + 1
          var temp: String = chances.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val from: String = (st.nextToken().toInt - 1).toString

          chancescreated += ChanceCreated(game_id, team_id, player_id, getTime(time), location_start, location_end, getBoolean(from))
        }
        val passsummaryRow = for {
          p <- passsummaries if p.game_id === game_id && p.team_id === team_id && p.player_id === player_id
        } yield (p.chances_created_count)
        if (passsummaryRow.length.run > 1) {
          println("More than 1 Pass Summary Found. Error.")
          session.close; return false;
        }
        else if (passsummaryRow.length.run == 1) {
          passsummaryRow.update(passsummaryRow.first + chances_total)
        }
        else if (passsummaryRow.length.run == 0) {
          passsummaries += PassSummary(game_id, team_id, player_id, 0, 0, 0, 0, chances_total, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addLongPasses(longpassess: String, FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
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

        val st: StringTokenizer = new StringTokenizer(longpassess, ";;")
        val count: Int = st.nextToken().toInt
        val success_count: Int = st.nextToken().toInt
        val fail_count: Int = st.nextToken().toInt
        val assists_count: Int = st.nextToken().toInt
        val chances_count: Int = st.nextToken().toInt

        longpasses += LongPass(game_id, team_id, player_id, count, success_count, fail_count, assists_count, chances_count)

        val passsummaryRow = for {
          p <- passsummaries if p.game_id === game_id && p.team_id === team_id && p.player_id === player_id
        } yield (p.long_count)
        if (passsummaryRow.length.run > 1) {
          println("More than 1 Pass Summary Found. Error.")
          session.close; return false;
        }
        else if (passsummaryRow.length.run == 1) {
          passsummaryRow.update(count)
        }
        else if (passsummaryRow.length.run == 0) {
          passsummaries += PassSummary(game_id, team_id, player_id, 0, 0, 0, 0, 0, 0, count, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addShortPasses(shortpassess: String, FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var chances_total: Int = 0

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

        val st: StringTokenizer = new StringTokenizer(shortpassess, ";;")
        val count: Int = st.nextToken().toInt
        val success_count: Int = st.nextToken().toInt
        val fail_count: Int = st.nextToken().toInt
        val assists_count: Int = st.nextToken().toInt
        val chances_count: Int = st.nextToken().toInt

        shortpasses += ShortPass(game_id, team_id, player_id, count, success_count, fail_count, assists_count, chances_count)

        val passsummaryRow = for {
          p <- passsummaries if p.game_id === game_id && p.team_id === team_id && p.player_id === player_id
        } yield (p.short_count)
        if (passsummaryRow.length.run > 1) {
          println("More than 1 Pass Summary Found. Error.")
          session.close; return false;
        }
        else if (passsummaryRow.length.run == 1) {
          passsummaryRow.update(count)
        }
        else if (passsummaryRow.length.run == 0) {
          passsummaries += PassSummary(game_id, team_id, player_id, 0, 0, 0, 0, 0, 0, 0, count, 0, 0)
        }
        session.close
    }
    return true
  }

  def addCrosses(crossess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var crosses_total: Int = 0

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
          crosses_total = crosses_total + 1
          var temp: String = crossess.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()

          crosses += Cross(game_id, team_id, player_id, getTime(time), location_start, location_end, result.toInt)
        }
        val othersummaryRow = for {
          o <- othersummaries if o.game_id === game_id && o.team_id === team_id && o.player_id === player_id
        } yield (o.crosses_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(crosses_total)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(game_id, team_id, player_id, 0, crosses_total, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addTakeOns(takeonss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var success_count: Int = 0
        var fail_count: Int = 0

        val gameRow = for {
          g <- games if g.FFT_ID === FFTmatchID.toLong && g.season === season.toInt
        } yield (g.id, g.dribbles, g.home_team_id, g.away_team_id)
        if (gameRow.length.run != 1) {
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
          if (result.toInt == 1)
            success_count = success_count + 1
          else if (result.toInt == 0)
            fail_count = fail_count + 1
          takeons += TakeOn(gameRow.first._1, team_id, player_id, getTime(time), location, getBoolean(result))
        }
        val othersummaryRow = for {
          o <- othersummaries if o.game_id === gameRow.first._1 && o.team_id === team_id && o.player_id === player_id
        } yield (o.take_on_success_count, o.take_on_fail_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(success_count, fail_count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(gameRow.first._1, team_id, player_id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, success_count, fail_count)
        }
        val temp: StringTokenizer = new StringTokenizer(gameRow.first._2, "-");
        if (team_id == gameRow.first._3) {
          val home_dribbles_success: Int = temp.nextToken().toInt + success_count
          val home_dribbles_fail: Int = temp.nextToken().toInt + fail_count
          val away_dribbles_success: Int = temp.nextToken().toInt
          val away_dribbles_fail: Int = temp.nextToken().toInt
          gameRow.update(gameRow.first._1, home_dribbles_success + "-" + home_dribbles_fail + "-" + away_dribbles_success + "-" + away_dribbles_fail, gameRow.first._3, gameRow.first._4)
        }
        else if (team_id == gameRow.first._4) {
          val home_dribbles_success: Int = temp.nextToken().toInt
          val home_dribbles_fail: Int = temp.nextToken().toInt
          val away_dribbles_success: Int = temp.nextToken().toInt + success_count
          val away_dribbles_fail: Int = temp.nextToken().toInt + fail_count
          gameRow.update(gameRow.first._1, home_dribbles_success + "-" + home_dribbles_fail + "-" + away_dribbles_success + "-" + away_dribbles_fail, gameRow.first._3, gameRow.first._4)
        }
        else {
          println("Strange Error - 2")
          session.close; return false;
        }
        session.close
    }
    return true
  }

  def addCorners(cornerss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var count: Int = 0

        val gameRow = for {
          g <- games if g.FFT_ID === FFTmatchID.toLong && g.season === season.toInt
        } yield (g.id, g.corners, g.home_team_id, g.away_team_id)
        if (gameRow.length.run != 1) {
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
          count = count + 1
          val temp: String = cornerss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location_start: String = st.nextToken()
          val location_end: String = st.nextToken()
          val result: String = st.nextToken()

          corners += Corner(gameRow.first._1, team_id, player_id, getTime(time), location_start, location_end, result.toInt)
        }
        val othersummaryRow = for {
          o <- othersummaries if o.game_id === gameRow.first._1 && o.team_id === team_id && o.player_id === player_id
        } yield (o.corners_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(gameRow.first._1, team_id, player_id, count, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }

        val temp: StringTokenizer = new StringTokenizer(gameRow.first._2, "-");
        if (team_id == gameRow.first._3) {
          val home_corners: Int = temp.nextToken().toInt + count
          val away_corners: Int = temp.nextToken().toInt
          gameRow.update(gameRow.first._1, home_corners + "-" + away_corners, gameRow.first._3, gameRow.first._4)
        }
        else if (team_id == gameRow.first._4) {
          val home_corners: Int = temp.nextToken().toInt
          val away_corners: Int = temp.nextToken().toInt + count
          gameRow.update(gameRow.first._1, home_corners + "-" + away_corners, gameRow.first._3, gameRow.first._4)
        }
        else {
          println("Strange Error - 3")
          session.close; return false;
        }
        session.close
    }
    return true
  }

  def addOffsidePasses(offsidePassess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val count: Int = offsidePassess.size

        val gameRow = for {
          g <- games if g.FFT_ID === FFTmatchID.toLong && g.season === season.toInt
        } yield (g.id, g.offsides, g.home_team_id, g.away_team_id)
        if (gameRow.length.run != 1) {
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

          offsidePasses += OffsidePass(gameRow.first._1, team_id, player_id, time, location_start, location_end)
        }

        val passsummaryRow = for {
          p <- passsummaries if p.game_id === gameRow.first._1 && p.team_id === team_id && p.player_id === player_id
        } yield (p.offside_passes_count)
        if (passsummaryRow.length.run > 1) {
          println("More than 1 Pass Summary Found. Error.")
          session.close; return false;
        }
        else if (passsummaryRow.length.run == 1) {
          passsummaryRow.update(count)
        }
        else if (passsummaryRow.length.run == 0) {
          passsummaries += PassSummary(gameRow.first._1, team_id, player_id, 0, 0, 0, 0, 0, 0, 0, 0, count, 0)
        }

        val temp: StringTokenizer = new StringTokenizer(gameRow.first._2, "-");
        if (team_id == gameRow.first._3) {
          val home_offsides: Int = temp.nextToken().toInt + count
          val away_offsides: Int = temp.nextToken().toInt
          gameRow.update(gameRow.first._1, home_offsides + "-" + away_offsides, gameRow.first._3, gameRow.first._4)
        }
        else if (team_id == gameRow.first._4) {
          val home_offsides: Int = temp.nextToken().toInt
          val away_offsides: Int = temp.nextToken().toInt + count
          gameRow.update(gameRow.first._1, home_offsides + "-" + away_offsides, gameRow.first._3, gameRow.first._4)
        }
        else {
          println("Strange Error - 4")
          session.close; return false;
        }
        session.close
    }
    return true
  }

  def addBallRecoveries(ballRecoveriess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
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

        for (i <- 1 to ballRecoveriess.size()) {
          count = count + 1
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

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var success_count: Int = 0
        var fail_count: Int = 0

        val gameRow = for {
          g <- games if g.FFT_ID === FFTmatchID.toLong && g.season === season.toInt
        } yield (g.id, g.tackles, g.home_team_id, g.away_team_id)
        if (gameRow.length.run != 1) {
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
          if (result.toInt == 1)
            success_count = success_count + 1
          else if (result.toInt == 0)
            fail_count = fail_count + 1
          tackles += Tackle(gameRow.first._1, team_id, player_id, getTime(time), location, getBoolean(result))
        }

        val othersummaryRow = for {
          o <- othersummaries if o.game_id === gameRow.first._1 && o.team_id === team_id && o.player_id === player_id
        } yield (o.tackle_success_count, o.tackle_fail_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(success_count, fail_count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(gameRow.first._1, team_id, player_id, 0, 0, success_count, fail_count, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }

        val temp: StringTokenizer = new StringTokenizer(gameRow.first._2, "-");
        if (team_id == gameRow.first._3) {
          val home_tackles_success: Int = temp.nextToken().toInt + success_count
          val home_tackles_fail: Int = temp.nextToken().toInt + fail_count
          val away_tackles_success: Int = temp.nextToken().toInt
          val away_tackles_fail: Int = temp.nextToken().toInt
          gameRow.update(gameRow.first._1, home_tackles_success + "-" + home_tackles_fail + "-" + away_tackles_success + "-" + away_tackles_fail, gameRow.first._3, gameRow.first._4)
        }
        else if (team_id == gameRow.first._4) {
          val home_tackles_success: Int = temp.nextToken().toInt
          val home_tackles_fail: Int = temp.nextToken().toInt
          val away_tackles_success: Int = temp.nextToken().toInt + success_count
          val away_tackles_fail: Int = temp.nextToken().toInt + fail_count
          gameRow.update(gameRow.first._1, home_tackles_success + "-" + home_tackles_fail + "-" + away_tackles_success + "-" + away_tackles_fail, gameRow.first._3, gameRow.first._4)
        }
        else {
          println("Strange Error - 5")
          session.close; return false;
        }
        session.close
    }
    return true
  }

  def addInterceptions(interceptionss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
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

        for (i <- 1 to interceptionss.size()) {
          count = count + 1
          var temp: String = interceptionss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()

          interceptions += Interception(game_id, team_id, player_id, getTime(time), location)
        }

        val othersummaryRow = for {
          o <- othersummaries if o.game_id === game_id && o.team_id === team_id && o.player_id === player_id
        } yield (o.interception_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(game_id, team_id, player_id, 0, 0, 0, 0, count, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addBlocks(blockss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
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

        for (i <- 1 to blockss.size()) {
          count = count + 1
          var temp: String = blockss.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()

          blocks += Block(game_id, team_id, player_id, getTime(time), location)
        }

        val othersummaryRow = for {
          o <- othersummaries if o.game_id === game_id && o.team_id === team_id && o.player_id === player_id
        } yield (o.block_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(game_id, team_id, player_id, 0, 0, 0, 0, 0, count, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addClearances(clearancess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
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

        for (i <- 1 to clearancess.size()) {
          count = count + 1
          var temp: String = clearancess.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()
          val result: String = st.nextToken()

          clearances += Clearance(game_id, team_id, player_id, getTime(time), location, getBoolean(result))
        }

        val othersummaryRow = for {
          o <- othersummaries if o.game_id === game_id && o.team_id === team_id && o.player_id === player_id
        } yield (o.clearance_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(game_id, team_id, player_id, 0, 0, 0, 0, 0, 0, count, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addAerialDuels(aerialDuelss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var won_count: Int = 0
        var lost_count: Int = 0

        val gameRow = for {
          g <- games if g.FFT_ID === FFTmatchID.toLong && g.season === season.toInt
        } yield (g.id, g.aerial_duels, g.home_team_id, g.away_team_id)
        if (gameRow.length.run != 1) {
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
          if (result.toInt == 1)
            won_count = won_count + 1
          else if (result.toInt == 0)
            lost_count = lost_count + 1
          aerialduels += AerialDuel(gameRow.first._1, team_id, player_id, getTime(time), location, getBoolean(result))
        }

        val othersummaryRow = for {
          o <- othersummaries if o.game_id === gameRow.first._1 && o.team_id === team_id && o.player_id === player_id
        } yield (o.aerial_duels_won_count, o.aerial_duels_lost_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(won_count, lost_count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(gameRow.first._1, team_id, player_id, 0, 0, 0, 0, 0, 0, 0, 0, won_count, lost_count, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }

        val temp: StringTokenizer = new StringTokenizer(gameRow.first._2, "-");
        if (team_id == gameRow.first._3) {
          val home_aerial_duels_won: Int = temp.nextToken().toInt + won_count
          val home_aerial_duels_lost: Int = temp.nextToken().toInt + lost_count
          val away_aerial_duels_won: Int = temp.nextToken().toInt
          val away_aerial_duels_lost: Int = temp.nextToken().toInt
          gameRow.update(gameRow.first._1, home_aerial_duels_won + "-" + home_aerial_duels_lost + "-" + away_aerial_duels_won + "-" + away_aerial_duels_lost, gameRow.first._3, gameRow.first._4)
        }
        else if (team_id == gameRow.first._4) {
          val home_aerial_duels_won: Int = temp.nextToken().toInt
          val home_aerial_duels_lost: Int = temp.nextToken().toInt
          val away_aerial_duels_won: Int = temp.nextToken().toInt + won_count
          val away_aerial_duels_lost: Int = temp.nextToken().toInt + lost_count
          gameRow.update(gameRow.first._1, home_aerial_duels_won + "-" + home_aerial_duels_lost + "-" + away_aerial_duels_won + "-" + away_aerial_duels_lost, gameRow.first._3, gameRow.first._4)
        }
        else {
          println("Strange Error - 6")
          session.close; return false;
        }
        session.close
    }
    return true
  }

  def addBlockedCrosses(blockedCrossess: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
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

        for (i <- 1 to blockedCrossess.size()) {
          count = count + 1
          var temp: String = blockedCrossess.get(i - 1)
          val st: StringTokenizer = new StringTokenizer(temp, ";;")
          val time: String = st.nextToken()
          val location: String = st.nextToken()

          blockedcrosses += BlockedCross(game_id, team_id, player_id, getTime(time), location)
        }

        val othersummaryRow = for {
          o <- othersummaries if o.game_id === game_id && o.team_id === team_id && o.player_id === player_id
        } yield (o.blocked_crosses_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(game_id, team_id, player_id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, count, 0, 0, 0, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addDefensiveErrors(defensiveErrorss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var shot_count: Int = 0
        var goal_count: Int = 0

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
          if (leadingTo.toInt == 0)
            goal_count = goal_count + 1
          else if (leadingTo.toInt == 1)
            shot_count = shot_count + 1
          defensiveerrors += DefensiveError(game_id, team_id, player_id, getTime(time), location, getBoolean(leadingTo))
        }

        val othersummaryRow = for {
          o <- othersummaries if o.game_id === game_id && o.team_id === team_id && o.player_id === player_id
        } yield (o.error_leading_to_shot_count, o.error_leading_to_goal_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(othersummaryRow.first._1 + shot_count, othersummaryRow.first._2 + goal_count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(game_id, team_id, player_id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, shot_count, goal_count, 0, 0, 0, 0, 0, 0)
        }
        session.close
    }
    return true
  }

  def addFouls(foulss: util.ArrayList[String], FFTmatchID: String, FFTteamName: String, FFTplayerID: String, season: String): Boolean = {

    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        var commit_count: Int = 0
        var suffer_count: Int = 0

        val gameRow = for {
          g <- games if g.FFT_ID === FFTmatchID.toLong && g.season === season.toInt
        } yield (g.id, g.fouls, g.home_team_id, g.away_team_id)
        if (gameRow.length.run != 1) {
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
          if (who.toInt == 0)
            commit_count = commit_count + 1
          else if (who.toInt == 1)
            suffer_count = suffer_count + 1
          fouls += Foul(gameRow.first._1, team_id, player_id, getTime(time), location, getBoolean(who))
        }

        val othersummaryRow = for {
          o <- othersummaries if o.game_id === gameRow.first._1 && o.team_id === team_id && o.player_id === player_id
        } yield (o.fouls_commit_count, o.fouls_suffer_count)
        if (othersummaryRow.length.run > 1) {
          println("More than 1 Other Summary Found. Error.")
          session.close; return false;
        }
        else if (othersummaryRow.length.run == 1) {
          othersummaryRow.update(othersummaryRow.first._1 + commit_count, othersummaryRow.first._2 + suffer_count)
        }
        else if (othersummaryRow.length.run == 0) {
          othersummaries += OtherSummary(gameRow.first._1, team_id, player_id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, commit_count, suffer_count, 0, 0, 0, 0)
        }

        val temp: StringTokenizer = new StringTokenizer(gameRow.first._2, "-");
        if (team_id == gameRow.first._3) {
          val home_fouls: Int = temp.nextToken().toInt + commit_count
          val away_fouls: Int = temp.nextToken().toInt
          gameRow.update(gameRow.first._1, home_fouls + "-" + away_fouls, gameRow.first._3, gameRow.first._4)
        }
        else if (team_id == gameRow.first._4) {
          val home_fouls: Int = temp.nextToken().toInt
          val away_fouls: Int = temp.nextToken().toInt + commit_count
          gameRow.update(gameRow.first._1, home_fouls + "-" + away_fouls, gameRow.first._3, gameRow.first._4)
        }
        else {
          println("Strange Error - 7")
          session.close; return false;
        }
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
    Database.forURL(dbURL, driver = dbDriver) withSession {
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
    Database.forURL(dbURL, driver = dbDriver) withSession {
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

  def addSubstitutions(FFT_match_id: String, sub_in_id: String, sub_out_id: String): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val game_id = getMatchID(FFT_match_id.toLong)
        val player_row = for {
          p <- players if p.FFT_id === sub_in_id.toLong
        } yield(p.id, p.team_id)
        val team_id = player_row.first._2
        val sub_in = player_row.first._1
        val sub_out = getPlayerID(sub_out_id.toLong)
        if(game_id == Int.int2long(-1) || team_id == Int.int2long(-1) || sub_in == Int.int2long(-1) || sub_out == Int.int2long(-1)) {
          println("Game or Team or Player not found for Adding substitutions.")
          session.close
          return false
        }

        substitutions += Substitution(game_id, team_id, sub_in, sub_out, "")
        session.close
    }
    return true
  }

  def deleteMatch(FFTmatchID: String): Unit = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
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
        val lpRow = for {
          a <- longpasses if a.game_id === matchID
        } yield (a)
        lpRow.delete
        val osRow = for {
          a <- othersummaries if a.game_id === matchID
        } yield (a)
        osRow.delete
        val pRow = for {
          a <- passes if a.game_id === matchID
        } yield (a)
        pRow.delete
        val psRow = for {
          a <- passsummaries if a.game_id === matchID
        } yield (a)
        psRow.delete
        val peRow = for {
          a <- penalties if a.game_id === matchID
        } yield (a)
        peRow.delete
        val rpRow = for {
          a <- receivedpasses if a.game_id === matchID
        } yield (a)
        rpRow.delete
        val spRow = for {
          a <- shortpasses if a.game_id === matchID
        } yield (a)
        spRow.delete
        val sRow = for {
          a <- shots if a.game_id === matchID
        } yield (a)
        sRow.delete
        val ssRow = for {
          a <- shotsummaries if a.game_id === matchID
        } yield (a)
        ssRow.delete
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