package models

import java.time.Instant


case class Release(version: Int, commit: String, buildpack: String, createdAt: Instant)