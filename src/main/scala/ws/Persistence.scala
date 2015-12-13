package ws

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.{TableQuery, Tag}
import scala.slick.jdbc.meta.MTable
import scala.util.parsing.json.JSONObject

/**
 * Created by gsm on 12/5/15.
 */

case class Region(name: String, WS_ID: Int, flag: String, region_type: Int, id: Long = 0)

case class Tournament(name: String, WS_ID: Int, url: String, region_id: Long, id: Long = 0)

case class Season(region_id: Long, tournament_id: Long, WS_ID: Int, season: String, id: Long = 0)

case class Stage(region_id: Long, tournament_id: Long, season_id: Long, WS_ID: Int, id: Long = 0)

object Persistence {
  val dbURL: String = "jdbc:postgresql://localhost:5432/WS_DATA?user=gsm&password=0909"
  val dbDriver: String = "org.postgresql.Driver"

  class Regions(tag: Tag) extends Table[Region](tag, "REGIONS") {

    def id = column[Long]("REGION_ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("REGION_NAME")

    def WS_ID = column[Int]("WS_ID")

    def flag = column[String]("FLAG")

    def region_type = column[Int]("REGION_TYPE")

    def * = (name, WS_ID, flag, region_type, id) <> (Region.tupled, Region.unapply)
  }
  val regions = TableQuery[Regions]

  class Tournaments(tag: Tag) extends Table[Tournament](tag, "TOURNAMENTS") {
    def id = column[Long]("TOURNAMENT_ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("TOURNAMENT_NAME")

    def WS_ID = column[Int]("WS_ID")

    def url = column[String]("URL")

    def region_id = column[Long]("REGION_ID")

    def * = (name, WS_ID, url, region_id, id) <>(Tournament.tupled, Tournament.unapply)

    def region = foreignKey("REGION_ID_FK", region_id, regions)(_.id)
  }

  val tournaments = TableQuery[Tournaments]

  class Seasons(tag: Tag) extends Table[Season](tag, "SEASONS") {
    def id = column[Long]("SEASON_ID", O.PrimaryKey, O.AutoInc)

    def region_id = column[Long]("REGION_ID")

    def tournament_id = column[Long]("TOURNAMENT_ID")

    def WS_ID = column[Int]("WS_ID")

    def season = column[String]("SEASON")

    def * = (region_id, tournament_id, WS_ID, season, id) <>(Season.tupled, Season.unapply)

    def region = foreignKey("REGION_ID_FK", region_id, regions)(_.id)

    def tournament = foreignKey("TOURNAMENT_ID_FK", tournament_id, tournaments)(_.id)
  }

  val seasons = TableQuery[Seasons]

  class Stages(tag: Tag) extends Table[Stage](tag, "STAGES") {
    def id = column[Long]("SEASON_ID", O.PrimaryKey, O.AutoInc)

    def region_id = column[Long]("REGION_ID")

    def tournament_id = column[Long]("TOURNAMENT_ID")

    def season_id = column[Long]("SEASON_ID")

    def WS_ID = column[Int]("WS_ID")

    def * = (region_id, tournament_id, season_id, WS_ID, id) <>(Stage.tupled, Stage.unapply)

    def region = foreignKey("REGION_ID_FK", region_id, regions)(_.id)

    def tournament = foreignKey("TOURNAMENT_ID_FK", tournament_id, tournaments)(_.id)

    def season = foreignKey("SEASON_ID_FK", season_id, seasons)(_.id)
  }

  val stages = TableQuery[Stages]

  def createTables(): Unit = {
    Database.forURL(dbURL, driver = dbDriver) withSession { implicit session =>
      if(MTable.getTables("REGIONS").list.isEmpty)
        regions.ddl.create

      if(MTable.getTables("TOURNAMENTS").list.isEmpty)
        tournaments.ddl.create

      if(MTable.getTables("SEASONS").list.isEmpty)
        seasons.ddl.create

      if(MTable.getTables("STAGES").list.isEmpty)
        stages.ddl.create

      session.close
    }
  }

  def addRegion(name: String, WS_ID: Int, flag: String, region_type: Int): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        if(getRegionID(WS_ID) == Int.int2long(-1))
          regions += Region(name, WS_ID, flag, region_type)
        session.close
    }
    return true
  }

  def addTournament(WS_region_id: Int, name: String, WS_ID: Int, url: String): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val region_id = getRegionID(WS_region_id)
        if(region_id == Int.int2long(-1)) {
          println("Region " + WS_region_id + " not found. Error.")
          return false
        }
        if(getTournamentID(region_id, WS_ID) == Int.int2long(-1))
          tournaments += Tournament(name, WS_ID, url, region_id)
        session.close
    }
    return true
  }

  def addSeason(WS_region_id: Int, WS_tournament_id: Int, WS_season_id: Int, season: String): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val region_id = getRegionID(WS_region_id)
        if(region_id == Int.int2long(-1)) {
          println("Region " + WS_region_id + " not found. Error.")
          return false
        }

        val tournament_id = getTournamentID(region_id, WS_tournament_id)
        if(tournament_id == Int.int2long(-1)) {
          println("Tournament " + WS_tournament_id + " not found. Error.")
          return false
        }

        val season_id = getSeasonID(region_id, tournament_id, WS_season_id)
        if(season_id == Int.int2long(-1))
          seasons += Season(region_id, tournament_id, WS_season_id, season)
        session.close
    }
    return true
  }

  def addStage(WS_region_id: Int, WS_tournament_id: Int, WS_season_id: Int, WS_stage_id: Int): Boolean = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val region_id = getRegionID(WS_region_id)
        if(region_id == Int.int2long(-1)) {
          println("Region " + WS_region_id + " not found. Error.")
          return false
        }

        val tournament_id = getTournamentID(region_id, WS_tournament_id)
        if(tournament_id == Int.int2long(-1)) {
          println("Tournament " + WS_tournament_id + " not found. Error.")
          return false
        }

        val season_id = getSeasonID(region_id, tournament_id, WS_season_id)
        if(season_id == Int.int2long(-1)) {
          println("Season " + WS_season_id + " not found. Error.")
          return false
        }

        val stage_id = getStageID(region_id, tournament_id, season_id, WS_stage_id)
        if(stage_id == Int.int2long(-1))
          stages += Stage(region_id, tournament_id, season_id, WS_stage_id)

        session.close
    }

    return true
  }

  def getAllTournaments(): List[(Int, Int)] = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val tournament_row = for {
          t <- tournaments
          r <- regions if t.region_id === r.id
        } yield (r.WS_ID, t.WS_ID)

        session.close()
        return tournament_row.list
    }
  }

  def getAllSeasons(): List[(Int, Int, Int)] = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val season_row = for {
          s <- seasons
          t <- tournaments if s.tournament_id === t.id
          r <- regions if s.region_id === r.id
        } yield(r.WS_ID, t.WS_ID, s.WS_ID)

        session.close
        return season_row.list
    }
  }

  def getAllStages(): List[(Int, Int, Int, Int)] = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val stage_row = for {
          st <- stages
          s <- seasons if st.season_id === s.id
          t <- tournaments if st.tournament_id === t.id
          r <- regions if st.region_id === r.id
        } yield (r.WS_ID, t.WS_ID, s.WS_ID, st.WS_ID)

        session.close()
        return stage_row.list
    }
  }

  def getRegionName(WS_ID: Int): String = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val region_row = for {
          r <- regions if r.WS_ID === WS_ID
        } yield r.name

        if(region_row.length.run != 1)
          return ""
        val region_name: String = region_row.first
        session.close()
        return region_name
    }
  }

  def getTournamentName(WS_ID: Int): String = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val tournament_row = for {
          t <- tournaments if t.WS_ID === WS_ID
        } yield t.name

        if(tournament_row.length.run != 1)
          return ""

        val tournament_name: String = tournament_row.first
        session.close()
        return tournament_name
    }

  }

  def getSeasonName(WS_ID: Int): String = {
    Database.forURL(dbURL, driver = dbDriver) withSession {
      implicit session =>
        val season_row = for {
          s <- seasons if s.WS_ID === WS_ID
        } yield s.season

        if(season_row.length.run != 1)
          return ""

        val season_name = season_row.first
        session.close()
        return season_name
    }
  }

  def getRegionID(WS_ID: Int)(implicit session: Session): Long = {
    val region_row = for {
      r <- regions if r.WS_ID === WS_ID
    } yield r.id

    return if(region_row.length.run == 1) region_row.first else Int.int2long(-1)
  }

  def getTournamentID(region_id: Long, WS_ID: Int)(implicit session: Session): Long = {
    val tournament_row = for {
      t <- tournaments if t.region_id === region_id && t.WS_ID === WS_ID
    } yield t.id

    return if(tournament_row.length.run == 1) tournament_row.first else Int.int2long(-1)
  }

  def getSeasonID(region_id: Long, tournament_id: Long, WS_ID: Int)(implicit session: Session): Long = {
    val season_row = for {
      s <- seasons if s.region_id === region_id && s.tournament_id === tournament_id && s.WS_ID === WS_ID
    } yield s.id

    return if(season_row.length.run == 1) season_row.first else Int.int2long(-1)
  }

  def getStageID(region_id: Long, tournament_id: Long, season_id: Long, WS_ID: Int)(implicit session: Session): Long = {
    val stage_row = for {
      s <- stages if s.region_id === region_id && s.tournament_id === tournament_id && s.season_id === season_id && s.WS_ID === WS_ID
    } yield s.id

    return if(stage_row.length.run == 1) stage_row.first else Int.int2long(-1)
  }
}
