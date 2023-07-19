package mlb

import mlb.PitcherHomeTeams.PitcherHomeTeam
import zio.json.*
import zio.jdbc.*

import java.time.LocalDate

object HomeTeams {

  opaque type HomeTeam = String

  object HomeTeam {

    def apply(value: String): HomeTeam = value

    def unapply(homeTeam: HomeTeam): String = homeTeam
  }

  given CanEqual[HomeTeam, HomeTeam] = CanEqual.derived
  implicit val homeTeamEncoder: JsonEncoder[HomeTeam] = JsonEncoder.string
  implicit val homeTeamDecoder: JsonDecoder[HomeTeam] = JsonDecoder.string
}

object AwayTeams {

  opaque type AwayTeam = String

  object AwayTeam {

    def apply(value: String): AwayTeam = value

    def unapply(awayTeam: AwayTeam): String = awayTeam
  }

  given CanEqual[AwayTeam, AwayTeam] = CanEqual.derived
  implicit val awayTeamEncoder: JsonEncoder[AwayTeam] = JsonEncoder.string
  implicit val awayTeamDecoder: JsonDecoder[AwayTeam] = JsonDecoder.string
}

object GameDates {

  opaque type GameDate = LocalDate

  object GameDate {

    def apply(value: LocalDate): GameDate = value

    def unapply(gameDate: GameDate): LocalDate = gameDate
  }

  given CanEqual[GameDate, GameDate] = CanEqual.derived
  implicit val gameDateEncoder: JsonEncoder[GameDate] = JsonEncoder.localDate
  implicit val gameDateDecoder: JsonDecoder[GameDate] = JsonDecoder.localDate
}

object SeasonYears {

  opaque type SeasonYear <: Int = Int

  object SeasonYear {

    def apply(year: Int): SeasonYear = year

    def safe(value: Int): Option[SeasonYear] =
      Option.when(value >= 1876 && value <= LocalDate.now.getYear)(value)

    def unapply(seasonYear: SeasonYear): Int = seasonYear
  }

  given CanEqual[SeasonYear, SeasonYear] = CanEqual.derived
  implicit val seasonYearEncoder: JsonEncoder[SeasonYear] = JsonEncoder.int
  implicit val seasonYearDecoder: JsonDecoder[SeasonYear] = JsonDecoder.int
}

object PlayoffRounds {

  opaque type PlayoffRound <: Int = Int

  object PlayoffRound {

    def apply(round: Int): PlayoffRound = round

    def safe(value: Int): Option[PlayoffRound] =
      Option.when(value >= 1 && value <= 4)(value)

    def unapply(playoffRound: PlayoffRound): Int = playoffRound
  }

  given CanEqual[PlayoffRound, PlayoffRound] = CanEqual.derived
  implicit val playoffRoundEncoder: JsonEncoder[PlayoffRound] = JsonEncoder.int
  implicit val playoffRoundDEncoder: JsonDecoder[PlayoffRound] = JsonDecoder.int
}

object EloPreHomeTeams {
  
    opaque type EloPreHomeTeam <: Double = Double
  
    object EloPreHomeTeam {
  
      // Apply method to create an EloPreHomeTeam from a Double
      def apply(eloPreHomeTeam: Double): EloPreHomeTeam = eloPreHomeTeam
  
      // Unapply method to extract the underlying Double from an EloPreHomeTeam
      def unapply(eloPreHomeTeam: EloPreHomeTeam): Double = eloPreHomeTeam
    }
  
    // Equality for EloPreHomeTeam
    given CanEqual[EloPreHomeTeam, EloPreHomeTeam] = CanEqual.derived
    // JSON encoder and decoder for EloPreHomeTeam
    implicit val eloPreHomeTeamEncoder: JsonEncoder[EloPreHomeTeam] = JsonEncoder.double
    implicit val eloPreHomeTeamDencoder: JsonDecoder[EloPreHomeTeam] = JsonDecoder.double
}

object EloPreAwayTeams {
    
      opaque type EloPreAwayTeam <: Double = Double
    
      object EloPreAwayTeam {
    
        // Apply method to create an EloPreAwayTeam from a Double
        def apply(eloPreAwayTeam: Double): EloPreAwayTeam = eloPreAwayTeam
    
        // Unapply method to extract the underlying Double from an EloPreAwayTeam
        def unapply(eloPreAwayTeam: EloPreAwayTeam): Double = eloPreAwayTeam
      }
    
      // Equality for EloPreAwayTeam
      given CanEqual[EloPreAwayTeam, EloPreAwayTeam] = CanEqual.derived
      // JSON encoder and decoder for EloPreAwayTeam
      implicit val eloPreAwayTeamEncoder: JsonEncoder[EloPreAwayTeam] = JsonEncoder.double
      implicit val eloPreAwayTeamDencoder: JsonDecoder[EloPreAwayTeam] = JsonDecoder.double
}

object EloProbHomeTeams {
      
    opaque type EloProbHomeTeam = Double
  
    object EloProbHomeTeam {
  
      def apply(value: Double): EloProbHomeTeam = value
  
      def unapply(eloProbHomeTeam: EloProbHomeTeam): Double = eloProbHomeTeam
    }
  
    given CanEqual[EloProbHomeTeam, EloProbHomeTeam] = CanEqual.derived
    implicit val eloProbHomeTeamEncoder: JsonEncoder[EloProbHomeTeam] = JsonEncoder.double
    implicit val eloProbHomeTeamDecoder: JsonDecoder[EloProbHomeTeam] = JsonDecoder.double
}

object EloProbAwayTeams {
          
      opaque type EloProbAwayTeam = Double
    
      object EloProbAwayTeam {
    
        def apply(value: Double): EloProbAwayTeam = value
    
        def unapply(eloProbAwayTeam: EloProbAwayTeam): Double = eloProbAwayTeam
      }
    
      given CanEqual[EloProbAwayTeam, EloProbAwayTeam] = CanEqual.derived
      implicit val eloProbAwayTeamEncoder: JsonEncoder[EloProbAwayTeam] = JsonEncoder.double
      implicit val eloProbAwayTeamDecoder: JsonDecoder[EloProbAwayTeam] = JsonDecoder.double
}

object EloPostHomeTeams {
    
  opaque type EloPostHomeTeam <: Double = Double

  object EloPostHomeTeam {

    // Apply method to create an EloPostHomeTeam from a Double
    def apply(eloPostHomeTeam: Double): EloPostHomeTeam = eloPostHomeTeam

    // Unapply method to extract the underlying Double from an EloPostHomeTeam
    def unapply(eloPostHomeTeam: EloPostHomeTeam): Double = eloPostHomeTeam
  }

  // Equality for EloPostHomeTeam
  given CanEqual[EloPostHomeTeam, EloPostHomeTeam] = CanEqual.derived
  // JSON encoder and decoder for EloPostHomeTeam
  implicit val eloPostHomeTeamEncoder: JsonEncoder[EloPostHomeTeam] = JsonEncoder.double
  implicit val eloPostHomeTeamDencoder: JsonDecoder[EloPostHomeTeam] = JsonDecoder.double
}

object EloPostAwayTeams {
      
  opaque type EloPostHomeTeam <: Double = Double

  object EloPostHomeTeam {

    // Apply method to create an EloPostHomeTeam from a Double
    def apply(eloPostHomeTeam: Double): EloPostHomeTeam = eloPostHomeTeam

    // Unapply method to extract the underlying Double from an EloPostHomeTeam
    def unapply(eloPostHomeTeam: EloPostHomeTeam): Double = eloPostHomeTeam
  }

  // Equality for EloPostHomeTeam
  given CanEqual[EloPostHomeTeam, EloPostHomeTeam] = CanEqual.derived
  // JSON encoder and decoder for EloPostHomeTeam
  implicit val eloPostHomeTeamEncoder: JsonEncoder[EloPostHomeTeam] = JsonEncoder.double
  implicit val eloPostHomeTeamDencoder: JsonDecoder[EloPostHomeTeam] = JsonDecoder.double
}

object PitcherHomeTeams {
    
  opaque type PitcherHomeTeam = String

  object PitcherHomeTeam {

    def apply(value: String): PitcherHomeTeam = value

    def unapply(pitcherHomeTeam: PitcherHomeTeam): String = pitcherHomeTeam
  }

  given CanEqual[PitcherHomeTeam, PitcherHomeTeam] = CanEqual.derived
  implicit val pitcherHomeTeamEncoder: JsonEncoder[PitcherHomeTeam] = JsonEncoder.string
  implicit val pitcherHomeTeamDecoder: JsonDecoder[PitcherHomeTeam] = JsonDecoder.string
}

object PitcherAwayTeams {
    
  opaque type PitcherAwayTeam = String

  object PitcherAwayTeam {

    def apply(value: String): PitcherAwayTeam = value

    def unapply(pitcherAwayTeam: PitcherAwayTeam): String = pitcherAwayTeam
  }

  given CanEqual[PitcherAwayTeam, PitcherAwayTeam] = CanEqual.derived
  implicit val pitcherAwayTeamEncoder: JsonEncoder[PitcherAwayTeam] = JsonEncoder.string
  implicit val pitcherAwayTeamDecoder: JsonDecoder[PitcherAwayTeam] = JsonDecoder.string
}

import GameDates.*
import PlayoffRounds.*
import SeasonYears.*
import HomeTeams.*
import AwayTeams.*
import EloPreHomeTeams.*
import EloPreAwayTeams.*
import EloProbHomeTeams.*
import EloProbAwayTeams.*
import PitcherHomeTeams.*
import PitcherAwayTeams.*

final case class Game(
    date: GameDate,
    season: SeasonYear,
    homeTeam: HomeTeam,
    awayTeam: AwayTeam,
    eloPreHomeTeam: EloPreHomeTeam,
    eloPreAwayTeam: EloPreAwayTeam,
    eloProbHomeTeam: EloProbHomeTeam,
    eloProbAwayTeam: EloProbAwayTeam,
    pitcherHomeTeam: PitcherHomeTeam,
    pitcherAwayTeam: PitcherAwayTeam
)

object Game {

  given CanEqual[Game, Game] = CanEqual.derived
  implicit val gameEncoder: JsonEncoder[Game] = DeriveJsonEncoder.gen[Game]
  implicit val gameDecoder: JsonDecoder[Game] = DeriveJsonDecoder.gen[Game]

  def unapply(game: Game): (GameDate, SeasonYear, HomeTeam, AwayTeam, EloPreHomeTeam, EloPreAwayTeam, EloProbHomeTeam, EloProbAwayTeam, PitcherHomeTeam, PitcherAwayTeam) =
    (game.date, game.season, game.homeTeam, game.awayTeam, game.eloPreHomeTeam, game.eloPreAwayTeam, game.eloProbHomeTeam, game.eloProbAwayTeam, game.pitcherHomeTeam, game.pitcherAwayTeam)

  // a custom decoder from a tuple
  type Row = (String, Int, String, String, Double, Double, Double, Double, String, String)

  extension (g:Game)
    def toRow: Row =
      val (d, y, h, a, eloPreHomeTeam, eloPreAwayTeam, eloProbHomeTeam, eloProbAwayTeam, pitcherHomeTeam, pitcherAwayTeam) = Game.unapply(g)
      (
        GameDate.unapply(d).toString,
        SeasonYear.unapply(y),
        HomeTeam.unapply(h),
        AwayTeam.unapply(a),
        EloPreHomeTeam.unapply(eloPreHomeTeam), 
        EloPreAwayTeam.unapply(eloPreAwayTeam), 
        EloProbHomeTeam.unapply(eloProbHomeTeam), 
        EloProbAwayTeam.unapply(eloProbAwayTeam), 
        PitcherHomeTeam.unapply(pitcherHomeTeam), 
        PitcherAwayTeam.unapply(pitcherAwayTeam)
      )

  implicit val jdbcDecoder: JdbcDecoder[Game] = JdbcDecoder[Row]().map[Game] { t =>
      val (date, season, home, away, eloPreHomeTeam, eloPreAwayTeam, eloProbHomeTeam, eloProbAwayTeam, pitcherHomeTeam, pitcherAwayTeam) = t
      Game(
        GameDate(LocalDate.parse(date)),
        SeasonYear(season),
        HomeTeam(home),
        AwayTeam(away),
        EloPreHomeTeam(eloPreHomeTeam),
        EloPreAwayTeam(eloPreAwayTeam), 
        EloProbHomeTeam(eloProbHomeTeam), 
        EloProbAwayTeam(eloProbAwayTeam), 
        PitcherHomeTeam(pitcherHomeTeam), 
        PitcherAwayTeam(pitcherAwayTeam)
      )
    }
}
