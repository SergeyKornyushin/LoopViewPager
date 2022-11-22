package com.github.loopviewpager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import kotlin.math.min

/**
 * View для отображения индикаторов пагинации
 */
class PageIndicatorsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val inflater = LayoutInflater.from(context)

    private var totalPageCount = 0
    private var maxPagePosition = 0
    private var currentIndicatorPosition = 0
    private var currentPagePosition = 0
    private var maxIndicatorPosition = 0

    init {
        gravity = Gravity.CENTER
    }

    /**
     * Задаёт количество страниц
     */
    fun setPageCount(pageCount: Int) {
        totalPageCount = pageCount
        initPageIndicators()
    }

    /**
     * Выбирает страницу
     *
     * @param selectedPageNumber выбранный номер страницы
     */
    fun selectPage(selectedPageNumber: Int) {
        updateIndicatorsByPagePosition(currentPagePosition, selectedPageNumber)
        currentPagePosition = selectedPageNumber
    }

    /**
     * Обновляет индикаторы на основе изменения позиции страницы
     * Логика обновления позиции следующая:
     *
     *
     * Так как количество страниц может достигать несколько десятков
     * было решено сделать индикаторы позиции слайда
     * с фиксированным значением [MAX_DISPLAYED_INDICATORS]
     *
     *
     * Крайние позиции индикаторов выделяются ТОЛЬКО на первой и последней страницы.
     * Остальные позиции индикаторов между крайними их позициями изменяются в зависимости
     * от изменения позиции страницы. Т.е. если позиция страницы уменьшилась то позиция
     * активного индикатора тоже уменьшилась, и наоборот.
     *
     * @param positionBefore позиция страницы до
     * @param positionAfter  позиция страницы после
     */
    private fun updateIndicatorsByPagePosition(positionBefore: Int, positionAfter: Int) {
        when {
            positionAfter == 0 -> {
                setMinIndicatorPosition()
            }
            positionAfter == maxPagePosition -> {
                setMaxIndicatorPosition()
            }
            positionAfter > positionBefore -> {
                increaseIndicatorPosition()
            }
            positionAfter < positionBefore -> {
                decreaseIndicatorPosition()
            }
        }
        updateIndicatorsByIndicatorPosition()
    }

    /**
     * Инициализирует индикаторы страниц
     */
    private fun initPageIndicators() {
        val displayedIndicatorCount = min(totalPageCount, MAX_DISPLAYED_INDICATORS)

        maxPagePosition = totalPageCount - 1
        maxIndicatorPosition = displayedIndicatorCount - 1

        removeAllViews()

        for (i in 0 until displayedIndicatorCount) {
            Log.i("test4", "for $i")
            addPageIndicator()
        }

        updateVisibility(displayedIndicatorCount)

        updateIndicatorsByIndicatorPosition()
    }

    /**
     * Добавляет индикатор страницы
     */
    private fun addPageIndicator() {
        val pageIndicatorView = inflater.inflate(R.layout.view_page_indicator, this, false)
        val layoutParams = pageIndicatorView.layoutParams as LinearLayout.LayoutParams

        layoutParams.apply {
            leftMargin = 20
            rightMargin = 20
            topMargin = 20
            bottomMargin = 20
        }

        Log.i("test4", "addPageIndicator: ")
        addView(pageIndicatorView)
    }

    /**
     * Обновляет видимость [PageIndicatorsView]
     *
     * @param displayedIndicatorCount количество отображаемых индикаторов
     */
    private fun updateVisibility(displayedIndicatorCount: Int) {
        isVisible = displayedIndicatorCount > 1
    }

    /**
     * Обновляет индикаторы в зависимости от позиции индикатора
     */
    private fun updateIndicatorsByIndicatorPosition() {
        val count: Int = childCount
        for (i in 0 until count) {
            val child: View = getChildAt(i)
            val needToSelect = i == currentIndicatorPosition
            val isSelected = child.isSelected
            val isSelectionChanged = needToSelect != isSelected
            if (!isSelectionChanged) {
                continue
            }
            child.isSelected = !isSelected

            child.requestLayout()
        }
    }

    /**
     * Уменьшает позицию индикатора
     */
    private fun decreaseIndicatorPosition() {
        if (currentIndicatorPosition > 1) {
            currentIndicatorPosition--
        }
    }

    /**
     * Увеличивает позицию индикатора
     */
    private fun increaseIndicatorPosition() {
        if (currentIndicatorPosition < maxIndicatorPosition - 1) {
            currentIndicatorPosition++
        }
    }

    /**
     * Увеличивает позицию индикатора до максимально возможной
     */
    private fun setMaxIndicatorPosition() {
        currentIndicatorPosition = maxIndicatorPosition
    }

    /**
     * Уменьшает позицию индикатора до минимально возможной
     */
    private fun setMinIndicatorPosition() {
        currentIndicatorPosition = 0
    }

    private companion object {
        /*todo to change the value after the logic is known*/
        const val MAX_DISPLAYED_INDICATORS = Int.MAX_VALUE
        const val PAGE_INDICATOR_SIDE_MARGIN = 10
    }
}