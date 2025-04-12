package edu.agh.roomie.rest

import edu.agh.roomie.service.AuthService
import edu.agh.roomie.service.UserService
import org.jetbrains.exposed.sql.Database

class Dependencies(
  val database: Database,
  val userService: UserService,
  val authService: AuthService,
)