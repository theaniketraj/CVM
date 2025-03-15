import java.io.File
import java.util.Properties

object VersionManager {
    private const val versionFile = "version.properties"

    private fun loadProperties(): Properties {
        val props = Properties()
        val file = File(versionFile)
        if (file.exists()) {
            props.load(file.inputStream())
        }
        return props
    }

    private fun saveProperties(props: Properties) {
        val file = File(versionFile)
        file.outputStream().use { props.store(it, null) }
    }

    fun getVersion(): String {
        val props = loadProperties()
        return props.getProperty("version", "1.0.0")
    }

    fun getBuildNumber(): Int {
        val props = loadProperties()
        return props.getProperty("build", "0").toInt()
    }

    fun incrementBuild() {
        val props = loadProperties()
        val build = getBuildNumber() + 1
        props.setProperty("build", build.toString())
        saveProperties(props)
    }

    fun bumpVersion(type: String) {
        val props = loadProperties()
        val versionParts = getVersion().split(".").map { it.toInt() }.toMutableList()

        when (type) {
            "major" -> {
                versionParts[0] += 1
                versionParts[1] = 0
                versionParts[2] = 0
            }
            "minor" -> {
                versionParts[1] += 1
                versionParts[2] = 0
            }
            "patch" -> {
                versionParts[2] += 1
            }
        }

        props.setProperty("version", versionParts.joinToString("."))
        saveProperties(props)
    }
}
