package web

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlinx.serialization.json.decodeFromStream
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
internal data class Engines(val node: String)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
internal data class PackageJson(val engines: Engines, val packageManager: String)

data class Versions(val node: String, val pnpm: String)

@OptIn(ExperimentalSerializationApi::class)
fun versionsFromPackageJson(file: File): Versions {
    file.inputStream().use { inputStream ->
        val packageJson = Json.decodeFromStream<PackageJson>(inputStream)
        return Versions(packageJson.engines.node, packageJson.packageManager.substringAfter("pnpm@"))
    }
}
