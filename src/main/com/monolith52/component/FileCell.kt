package com.monolith52.component

import com.monolith52.component.FileTableRecord
import javafx.scene.control.TableCell
import java.io.File


class FileCell : TableCell<FileTableRecord, File>() {
    override fun updateItem(item: File?, empty: Boolean) {
        super.updateItem(item, empty)

        if (empty) {
            text = ""
            return
        }
        val record = tableView.items[index]
        if (record.getError() != null) {
            text = "${record.getFile().name} ${record.getError()}"
        } else {
            text = record.getFile().name
        }
    }
}