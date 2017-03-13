package com.monolith52.view

import com.monolith52.component.*
import com.monolith52.logic.ZipAdjuster
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.DataFormat
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import java.io.File
import java.util.*
import java.util.concurrent.Executors


class ApplicationController {
    @FXML lateinit private var closeMenuItem: MenuItem
    @FXML lateinit private var tableView: TableView<FileTableRecord>
    @FXML lateinit private var messageLabel: Label
    @FXML lateinit private var convertButton: Button
    @FXML lateinit private var columnFile: TableColumn<FileTableRecord, File>
    @FXML lateinit private var columnOriginalSize: TableColumn<FileTableRecord, Long>
    @FXML lateinit private var columnCompressedSize: TableColumn<FileTableRecord, Long>
    @FXML lateinit private var columnProgress: TableColumn<FileTableRecord, Double>

    @FXML
    fun initialize() {
        tableView.setSelectionModel(null)
        tableView.setRowFactory({ tv -> RecordRow() })

        columnFile.setCellValueFactory(PropertyValueFactory<FileTableRecord, File>("file"))
        columnOriginalSize.setCellValueFactory(PropertyValueFactory<FileTableRecord, Long>("originalSize"))
        columnCompressedSize.setCellValueFactory(PropertyValueFactory<FileTableRecord, Long>("compressedSize"))
        columnProgress.setCellValueFactory(PropertyValueFactory<FileTableRecord, Double>("progress"))

        columnFile.setCellFactory({ column -> FileCell() })
        columnOriginalSize.setCellFactory({ column -> FilesizeCell<FileTableRecord>() })
        columnCompressedSize.setCellFactory({ column -> FilesizeCell<FileTableRecord>() })
        columnProgress.setCellFactory({ column -> PercentageCell<FileTableRecord>() })
    }

    @FXML
    fun onConvertButtonAction(event: ActionEvent) {
        convertButton.setDisable(true)
        Thread { convert() }.start()
    }

    fun convert() {
        val threadPool = Executors.newFixedThreadPool(4)
        tableView.getItems().forEach { record ->
            println(String.format("start processing %s", record.getFile().getName()))
            val ia = ZipAdjuster(record.getFile())
            ia.successHandler = success
            ia.errorHandler = failed
            ia.progressHandler = progress
            threadPool.execute { ia.process() }
        }
    }

    fun setScene(scene: Scene) {
        scene.setOnDragDropped(DroppedHandler())
        scene.setOnDragOver(DragOverHandler())
    }

    fun addFiles(files: List<File>) {
        val count = intArrayOf(0)
        tableView.getItems().clear()
        files.forEach { file ->
            val record = FileTableRecord(file, file.length(), 0L, 0.0)
            tableView.getItems().add(record)
            count[0]++
        }
        messageLabel.setText(String.format("%d files detected.", count[0]))
    }

    private fun findRecordByFile(file: File): FileTableRecord? {
        return tableView.getItems().filter({ it.getFile() === file }).first()
    }

    val progress: (file: File, progress: ZipAdjuster.Progress) -> Unit = { file, progress ->
        Platform.runLater({ messageLabel.setText(String.format("Progress %s in %s", file.name, progress.toString())) })
    }

    var success: (inputFile: File, outputFilesize: Long) -> Unit = { inputFile, outputFilesize ->
        Platform.runLater( lambda@{
            val record = findRecordByFile(inputFile)
            if (record == null) return@lambda
            println(String.format("done processing %s", inputFile.name))
            record.setCompressedSize(outputFilesize)
            record.setProgress(outputFilesize.toDouble() / (record.getOriginalSize() ?: 1))
        })
    }

    var failed: (inputFile: File, msg: String) -> Unit = { inputFile, msg ->
        Platform.runLater( lambda@{
            val record = findRecordByFile(inputFile)
            if (record == null) return@lambda
            println(String.format("failed processing %s for %s", inputFile.name, msg))
            record.setError(msg)
            tableView.refresh()
        })
    }

    internal inner class DroppedHandler : EventHandler<DragEvent> {
        override fun handle(event: DragEvent) {
            val content = event.dragboard.getContent(DataFormat.FILES)
            val files = if (content is List<*>) content as List<File> else ArrayList<File>()
            Platform.runLater({
                addFiles(files)
                convertButton.setDisable(false)
            })
        }
    }

    internal inner class DragOverHandler : EventHandler<DragEvent> {
        override fun handle(event: DragEvent) {
            val db = event.dragboard
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY)
            } else {
                event.consume()
            }
        }
    }
}