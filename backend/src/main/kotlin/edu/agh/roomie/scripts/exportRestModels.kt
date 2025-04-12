package edu.agh.roomie.scripts

import dev.adamko.kxstsgen.KxsTsGenerator
import edu.agh.roomie.rest.model.*
import kotlin.io.path.Path

// Generate TypeScript types from Kotlin data classes
fun main(args: Array<String>) {
  //it's ugly, but it works
  val targetDirectory = Path(args.getOrElse(0) { "frontend/src/rest" })

  // directory -> model
  val serializers = mapOf(
    "model" to listOf(
      User.serializer(),
      Preferences.serializer(),
      Info.serializer(),
      Departament.serializer(),
      Hobby.serializer(),
      AdditionalInfoRequest.serializer(),
      LoginRequest.serializer(),
      RegisterRequest.serializer(),
    ),
  )
  val tsGenerator = KxsTsGenerator()

  serializers.forEach { (file, serializers) ->
    val result = tsGenerator.generate(*serializers.toTypedArray())
    val targetFile = targetDirectory.resolve("$file.ts").toFile()
    targetFile.createNewFile()
    targetFile.writeText(result)
  }
  println("Generated TypeScript types in ${targetDirectory.toAbsolutePath()}")
}
