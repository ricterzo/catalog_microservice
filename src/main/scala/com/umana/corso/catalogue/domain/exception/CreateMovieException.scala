package com.umana.corso.catalogue.domain.exception

sealed trait CreateMovieException extends RuntimeException

class MovieAlreadyExistsException extends CreateMovieException