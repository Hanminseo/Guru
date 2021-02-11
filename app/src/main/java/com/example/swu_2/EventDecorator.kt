package com.example.swu_2

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

//material calendar 일정 점표시 class
class EventDecorator(color:Int, dates:Collection<CalendarDay>): DayViewDecorator {

    private var color:Int = 0
    private val dates:HashSet<CalendarDay>
    init{
        this.color = color
        this.dates = HashSet(dates)
    }
    override fun shouldDecorate(day:CalendarDay):Boolean {
        return dates.contains(day)
    }
    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(8F, color))
    }
}

