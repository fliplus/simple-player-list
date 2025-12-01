plugins {
    id("dev.isxander.modstitch.base") version "0.7.1-unstable"
}

val loader: String = stonecutter.current.project.substringAfter("-", "vanilla")
fun getProperty(propertyName: String) = findProperty(propertyName) as? String

modstitch {
    minecraftVersion = stonecutter.current.version
    javaVersion = 21

    metadata {
        modId = getProperty("mod.id")
        modVersion = "${getProperty("mod.version")}+${stonecutter.current.version}-${loader}"
        modGroup = getProperty("mod.group")
        modName = getProperty("mod.name")
        modDescription = getProperty("mod.description")
        modAuthor = getProperty("mod.author")
        modLicense = getProperty("mod.license")

        fun MapProperty<String, String>.populate(block: MapProperty<String, String>.() -> Unit) = block()
        replacementProperties.populate {
            put("minecraft", getProperty("meta.minecraft").orEmpty())
            put("loader", getProperty("meta.loader").orEmpty())
        }
    }

    parchment {
        mappingsVersion = getProperty("deps.parchment")
    }

    fun TaskContainer.registerCleanTasks(groupName: String) {
        register<Delete>("cleanClient") {
            group = groupName
            description = "Deletes the run/client directory."
            delete("run/client")
        }
        register<Delete>("cleanServer") {
            group = groupName
            description = "Deletes the run/server directory."
            delete("run/server")
        }
    }

    loom {
        fabricLoaderVersion = getProperty("deps.fabric-loader")

        configureLoom {
            runs {
                named("client") { runDir("run/client") }
                named("server") { runDir("run/server") }
            }
            tasks { registerCleanTasks("fabric") }
        }
    }

    moddevgradle {
        neoForgeVersion = getProperty("deps.neoforge")

        defaultRuns()
        configureNeoForge {
            runs {
                named("client") { gameDirectory = layout.projectDirectory.dir("run/client") }
                named("server") { gameDirectory = layout.projectDirectory.dir("run/server") }
            }
            tasks { registerCleanTasks("mod development") }
        }
    }

    mixin {
        addMixinsToModManifest = true
        configs.register(getProperty("mod.id").toString())
    }

    if (loader == "vanilla") modLoaderManifest = "fabric.vanilla.json"
}

stonecutter {
    constants.match(
        loader,
        "fabric",
        "neoforge"
    )
}

dependencies {
    if (loader == "fabric") {
        modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:${getProperty("deps.fabric-api")}")
        getProperty("deps.modmenu")?.takeIf { it.isNotBlank() }?.let {
            modstitchModImplementation("com.terraformersmc:modmenu:$it")
        }
    }

    val devAuthLoader = loader.takeIf { it != "vanilla" } ?: "fabric"
    modstitchRuntimeOnly("me.djtheredstoner:DevAuth-$devAuthLoader:${getProperty("deps.devauth")}")
}

afterEvaluate {
    if (loader == "vanilla") {
        tasks.findByName("compileJava")?.enabled = false
    }
}