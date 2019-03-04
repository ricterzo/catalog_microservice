package com.umana.corso.catalogue.domain.usecase.message

import com.umana.corso.catalogue.domain.model.Movie

/**
  * Classe contenente i messaggi che sa gestire lo UserActor
  */
object MovieMessages {



  case class GetMovieList()

  case class GetMovieListResponse(result: Seq[Movie])

  //endregion

}
