package com.example.cvm
import java.io.File
import java.util.Properties

object VersionManager {

    private val versionFile=File("version.properties")
    private val properties=Properties().apply {
        load(versionFile.inputStream())
    }

    fun getVersion():String = properties.getProperty("version")
    fun getBuildNumber():Int = properties.getProperty("build").toInt()

    fun incrementBuild() {
        properties.setProperty("build", (getBuildNumber()+1).toString())
        save()
    }

    fun bumpVersion(type:String) {
        val(major,minor,patch)=getVersion().split(".").map{it.toInt()}
        val newVersion=when(type) {
            "major"->"$major+1.0.0"
            "minor"->"$major.$minor+1.0"
            "patch"->"$major.$minor.$patch+1"
            else->throw IllegalArgumentException("Invalid bump type")
        }
        properties.setProperty("version", newVersion)
        properties.setProperty("build", "1")
        save()
    }

    private fun save() {
        versionFile.outputStream().use {
            properties.store(it, null)
        }
    }


}