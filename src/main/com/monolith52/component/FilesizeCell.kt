package com.monolith52.component

import javafx.geometry.Pos
import javafx.scene.control.TableCell

class FilesizeCell<S> : TableCell<S, Long>() {
    override fun updateItem(item: Long?, empty: Boolean) {
        super.updateItem(item, empty)

        text = item?.format() ?: ""
        alignment = Pos.BASELINE_RIGHT
    }

    protected fun Long.format(): String {
        return String.format("%1$,3d KB", Long@this / 1024)
    }
}