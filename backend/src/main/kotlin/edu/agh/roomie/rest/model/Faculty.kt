package edu.agh.roomie.rest.model

import kotlinx.serialization.Serializable

@Serializable
enum class Faculty(val fullname: String) {
  WILiGZ("Wydział Inżynierii Lądowej i Gospodarki Zasobami"),
  WIMiIP("Wydział Inżynierii Metali i Informatyki Przemysłowej"),
  WEAIIB("Wydział Elektrotechniki, Automatyki, Informatyki i Inżynierii Biomedycznej"),
  WIET("Wydział Informatyki, Elektroniki i Telekomunikacji"),
  WIMiR("Wydział Inżynierii Mechanicznej i Robotyki"),
  WGGiOS("Wydział Geologii, Geofizyki i Ochrony Środowiska"),
  WGGiIS("Wydział Geodezji Górniczej i Inżynierii Środowiska"),
  WIMiC("Wydział Inżynierii Materiałowej i Ceramiki"),
  WO("Wydział Odlewnictwa"),
  WMN("Wydział Metali Nieżelaznych"),
  WWNiG("Wydział Wiertnictwa, Nafty i Gazu"),
  WZ("Wydział Zarządzania"),
  WEiP("Wydział Energetyki i Paliw"),
  WFiIS("Wydział Fizyki i Informatyki Stosowanej"),
  WMS("Wydział Matematyki Stosowanej"),
  WH("Wydział Humanistyczny"),
  WI("Wydział Informatyki"),
  WTK("Wydział Technologii Kosmicznych"),
}

data object Faculties {
  val all = Faculty.entries.associate { it to it.fullname }
}