package com.monolith52.component

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TableCell
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle


class PercentageCell<S> : TableCell<S, Double>() {
    private val root = Pane()
    private val fill = Rectangle()
    private val label = Label()
    private val barColor = Color(212.0 / 255, 235.0 / 255, 233.0 / 255, 1.0)

    init {
        root.prefWidthProperty().bind(widthProperty())
        root.prefHeightProperty().bind(heightProperty())
        label.prefWidthProperty().bind(root.widthProperty())
        label.prefHeightProperty().bind(root.heightProperty())
        label.setAlignment(Pos.BASELINE_CENTER)
        fill.setFill(barColor)
        fill.setWidth(30.0)
        fill.heightProperty().bind(root.heightProperty())
        root.children.addAll(fill, label)

        padding = Insets(0.0, 0.0, 0.0, 0.0)
        border = null
    }

    override fun updateItem(item: Double?, empty: Boolean) {
        super.updateItem(item, empty)

        val rate = if (empty) 0.0 else item ?: 0.0
        fill.setWidth(root.width * Math.max(0.0, Math.min(1.0, rate)))
        label.setText(item?.format() ?: "")
        graphic = root
    }

    fun Double.format(): String {
        return String.format("%.2f%%", this * 100)
    }
}