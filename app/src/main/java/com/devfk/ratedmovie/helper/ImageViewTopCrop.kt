package com.devfk.ratedmovie.helper

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView


class ImageViewTopCrop : ShapeableImageView {
    constructor(context: Context?) : super(context!!) {
        scaleType = ScaleType.MATRIX
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        scaleType = ScaleType.MATRIX
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        scaleType = ScaleType.MATRIX
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        computMatrix()
        return super.setFrame(l, t, r, b)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        computMatrix()
    }

    private fun computMatrix() {
        val matrix: Matrix = imageMatrix
        val scaleFactor = width / drawable.intrinsicWidth.toFloat()
        matrix.setScale(scaleFactor, scaleFactor, 0F, 0F)
        setImageMatrix(matrix)
    }
}