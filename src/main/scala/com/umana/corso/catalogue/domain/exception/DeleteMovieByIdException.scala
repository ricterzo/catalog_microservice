package com.umana.corso.catalogue.domain.exception

sealed trait DeleteMovieByIdException extends RuntimeException

class MissingIdException extends DeleteMovieByIdException