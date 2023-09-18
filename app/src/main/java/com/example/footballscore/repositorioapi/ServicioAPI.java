package com.example.footballscore.repositorioapi;

import com.example.footballscore.entidades.apuestas.ApuestasResponse;
import com.example.footballscore.entidades.clasificaciones.StandingsResponse;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.entidades.entrenadores.EntrenadoresResponse;
import com.example.footballscore.entidades.equipos.TeamResponse;
import com.example.footballscore.entidades.fichajes.FichajesResponse;
import com.example.footballscore.entidades.jugadores.TemporadasResponse;
import com.example.footballscore.entidades.lesiones.LesionesResponse;
import com.example.footballscore.entidades.paises.PaisesResponse;
import com.example.footballscore.entidades.partidos.PartidosResponse;
import com.example.footballscore.entidades.partidos.alineaciones.AlineacionesResponse;
import com.example.footballscore.entidades.partidos.estadisticas.EstadisticasResponse;
import com.example.footballscore.entidades.partidos.eventos.EventosResponse;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
import com.example.footballscore.entidades.trofeos.TrofeosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServicioAPI {

    @GET("leagues")
    Call<LeagueResponse> getCompeticionesPorPais(@Query("season") int season, @Query("country") String country);
    @GET("leagues?type=league&current=true")
    Call<LeagueResponse> getLigaPorEquipo(@Query("team") int idTeam);
    @GET("leagues")
    Call<LeagueResponse> getCompeticionesPorEquipoTemporada(@Query("season") int season, @Query("team") int teamId);

    @GET("leagues")
    Call<LeagueResponse> getCompeticionesPorEquipo(@Query("team") int teamId);


    @GET("leagues")
    Call<LeagueResponse> getCompeticionesPorNombre(@Query("season") int season, @Query("name") String leagueName);

    @GET("leagues")
    Call<LeagueResponse> getCompeticionesPorId(@Query("id") int ligaId);

    @GET("teams")
    Call<TeamResponse> getEquiposPorLiga(@Query("league") int leagueId, @Query("season") int seasonYear);

    @GET("teams")
    Call<TeamResponse> getEquipoPorNombre(@Query("name") String nombreEquipo, @Query("season") int seasonYear, @Query("league") int leagueId);

    @GET("teams")
    Call<TeamResponse> getEquipoPorId(@Query("id") int idEquipo);

    @GET("fixtures?timezone=Europe%2FMadrid")
    Call<PartidosResponse> getPartidosPorDia(@Query("date") String date);

    @GET("fixtures?timezone=Europe%2FMadrid")
    Call<PartidosResponse> getPartidosPorLigaJornadaTemporada(@Query("league") int leagueId ,@Query("round") String round ,@Query("season") int seasonYear);

    @GET("fixtures?timezone=Europe%2FMadrid")
    Call<PartidosResponse> getPartidosPorEquipoYTemporada(@Query("team") int IdEquipo ,@Query("season") int season);

    @GET("fixtures?timezone=Europe%2FMadrid")
    Call<PartidosResponse> getPartidosPorLigaTemporada(@Query("league") int leagueId ,@Query("season") int seasonYear);

    @GET("fixtures?live=all")
    Call<PartidosResponse> getPartidosEnJuego();

    @GET("standings")
    Call<StandingsResponse> getClasificacion(@Query("league") int leagueId , @Query("season") int seasonYear);

    @GET("countries")
    Call<PaisesResponse> getPaises();

    @GET("countries")
    Call<PaisesResponse> getPaisPorNombre(@Query("name") String nombrePais);

    @GET("odds?bookmaker=8&bet=1")
    Call<ApuestasResponse> getApuestaGanadorPartido(@Query("fixture")int idPartido);
    @GET("fixtures/lineups")
    Call<AlineacionesResponse> getAlineacionesPartido(@Query("fixture")int idPartido);

    @GET("fixtures/events")
    Call<EventosResponse> getEventosPartido(@Query("fixture")int idPartido);
    @GET("fixtures/statistics")
    Call<EstadisticasResponse> getEstadisticasPartido(@Query("fixture")int idPartido);

    @GET("players/topscorers")
    Call<JugadoresResponse> getMaximoGoleadorPorLigaTemporada(@Query("league")int idLiga, @Query("season")int temporada);

    @GET("players/topassists")
    Call<JugadoresResponse> getMaximoAsistentePorLigaTemporada(@Query("league")int idLiga, @Query("season")int temporada);

    @GET("players/topredcards")
    Call<JugadoresResponse> getMasTarjetasRojasPorLigaTemporada(@Query("league")int idLiga, @Query("season")int temporada);

    @GET("players/topyellowcards")
    Call<JugadoresResponse> getMasTarjetasAmarillasPorLigaTemporada(@Query("league")int idLiga, @Query("season")int temporada);

    @GET("injuries")
    Call<LesionesResponse> getLesionadosPorPartido(@Query("fixture")int idPartido);

    @GET("injuries")
    Call<LesionesResponse> getLesionesPorJugadorYTemporada(@Query("player")int idJugador,@Query("season") int temporada );

    @GET("coachs")
    Call<EntrenadoresResponse> getEntrenadorPorEquipo(@Query("team")int idEquipo);

    @GET("coachs")
    Call<EntrenadoresResponse> getEntrenadorPorId(@Query("id")int idEntrenador);

    @GET("players/squads")
    Call<JugadoresResponse> getPlantillaPorEquipo(@Query("team")int idEquipo);

    @GET("players")
    Call<JugadoresResponse> getEstadisticasPorEquipoTemporada(@Query("team")int idEquipo,@Query("season") int temporada);

    @GET("players")
    Call<JugadoresResponse> getEstadisticasPorJugadorTemporada(@Query("id")int idJugador,@Query("season") int temporada);

    @GET("players/seasons")
    Call<TemporadasResponse> getTemporadasPorJugador(@Query("player")int idJugador);

    @GET("teams/seasons")
    Call<TemporadasResponse> getTemporadasPorEquipo(@Query("team")int idEquipo);


    @GET("transfers")
    Call<FichajesResponse> getFichajesPorEquipo(@Query("team")int idEquipo);

    @GET("transfers")
    Call<FichajesResponse> getFichajesJugador(@Query("player")int idJugador);

    @GET("trophies")
    Call<TrofeosResponse> getTrofeosJugador(@Query("player")int idJugador);

    @GET("trophies")
    Call<TrofeosResponse> getTrofeosEntrenador(@Query("coach")int idEntrenador);

}

