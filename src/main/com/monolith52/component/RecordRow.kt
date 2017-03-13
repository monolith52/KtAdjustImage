package com.monolith52.component

import com.monolith52.component.FileTableRecord
import javafx.scene.control.TableRow
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.paint.Color

class RecordRow : TableRow<FileTableRecord>() {
    private val bgColor = Color(245.0 / 255, 180.0 / 255, 180.0 / 255, 1.0)

    override fun updateItem(item: FileTableRecord?, empty: Boolean) {
        super.updateItem(item, empty)

        if (!empty && item != null && item.getError() != null) {
            background = Background(BackgroundFill(bgColor, null, null))
        } else {
            background = Background.EMPTY
        }
        style = "-fx-border-color: #BBBBBB; -fx-border-width: 0px 0px 1px 0px;"
    }
}