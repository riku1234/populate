package fourfourtwo

import java.util.Locale

object Helper {

  val LEAGUE = 0
  val CHAMPIONS_LEAGUE = 1
  val DOMESTIC_CUP = 2
  val DUMMY_LEAGUE_ID: Long = -99

  def getLocale(leagueID: Long): Locale = {
    leagueID match {
      case 5 => return Locale.ENGLISH
      case 8 => return Locale.ENGLISH
			case 21 => return Locale.ENGLISH
      case 22 => return Locale.ENGLISH
      case 23 => return Locale.ENGLISH
      case 24 => return Locale.ENGLISH
      case default => println(default + " Bad parameters for getLocale")
        return Locale.ENGLISH
    }
  }

  def getLeagueName(leagueID: Long): String = {
		leagueID match {
      case 5 => return "UEFA Champions League"
			case 8 => return "Premier League"
			case 21 => return "Serie A"
      case 22 => return "Bundesliga"
      case 23 => return "La Liga"
      case 24 => return "Ligue 1"
      case -99 => return "Dummy"
      case default => println(default + " Bad parameter for getLeagueName")
        return ""
		}
	}

  def category(leagueID: Long): Int = {
    leagueID match {
      case 5 => return CHAMPIONS_LEAGUE
      case 8 => return LEAGUE
      case 21 => return LEAGUE
      case 22 => return LEAGUE
      case 23 => return LEAGUE
      case 24 => return LEAGUE
      case 99 => return DUMMY_LEAGUE_ID.toInt
    }
  }

}
