package com.monolith52

import com.monolith52.view.ApplicationController
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class Main : Application() {
    override fun start(primaryStage: Stage?) {
        if (primaryStage == null) return
        val loader = FXMLLoader(javaClass.getResource("view/Application.fxml"))
        val root: BorderPane = loader.load()
        val scene = Scene(root, 600.0, 400.0)
        scene.stylesheets.add(javaClass.getResource("view/Application.css").toExternalForm())
        primaryStage.scene = scene
        primaryStage.onCloseRequest = EventHandler {
            Platform.exit()
            System.exit(0)
        }
        loader.getController<ApplicationController>().setScene(scene)
        primaryStage.show()
    }

}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}
