package fourfourtwo

import java.util.Locale

object Helper {
	var leagueID: Long = -1

	def getPlayerStatsPage(gamePage: String): String = {
		val playerStatsPageAppend: String = "/player-stats#tabs-wrapper-anchor"
		return (gamePage + playerStatsPageAppend)	
	}

  def getLocale(leagueID: Long): Locale = {
    leagueID match {
      case 8 => return Locale.ENGLISH
			case 21 => return Locale.ENGLISH
      case 22 => return Locale.ENGLISH
      case 23 => return Locale.ENGLISH
      case 24 => return Locale.ENGLISH
      case default => println(default + " Bad parameters for getLocale")
        return Locale.getDefault()
    }
  }

  def setLeagueID(id: Long): Unit = {
    leagueID = id
  }

	def getLeagueName(leagueID: Long): String = {
		leagueID match {
			case 8 => return "Premier League"
			case 21 => return "Serie A"
      case 22 => return "Bundesliga"
      case 23 => return "La Liga"
      case 24 => return "Ligue 1"
      case default => println(default + " Bad parameter for getLeagueName")
        return ""
		}
	}

}
