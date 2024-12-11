package com.michredk.domian

/**
 * The Common Air Quality Index (CAQI) is a standardized index used to represent air quality.
 */
class CalculateCAQIUseCase() {

    data class CAQIBreakpoint(val low: Int, val high: Int, val aqiLow: Int, val aqiHigh: Int)

    private fun calculateCAQI(concentration: Int, breakpoints: List<CAQIBreakpoint>): Int {
        for (breakpoint in breakpoints) {
            if (concentration >= breakpoint.low && concentration <= breakpoint.high) {
                return ((breakpoint.aqiHigh - breakpoint.aqiLow) / (breakpoint.high - breakpoint.low) * (concentration - breakpoint.low) + breakpoint.aqiLow)
            }
        }
        throw IllegalArgumentException("Concentration out of range")
    }

    /**
     * For each pollutant, an individual sub-index is calculated based on its concentration
     * and associated health impact thresholds. The worst sub-index becomes the overall CAQI value.
     */
    operator fun invoke(pm25: Int, pm100: Int): Int {
        val pm25Breakpoints = listOf(
            CAQIBreakpoint(0, 15, 0, 25),
            CAQIBreakpoint(16, 30, 26, 50),
            CAQIBreakpoint(31, 55, 50, 75),
            CAQIBreakpoint(56, 110, 75, 100),
            CAQIBreakpoint(110, Int.MAX_VALUE, 100, Int.MAX_VALUE)
        )
        val pm100Breakpoints = listOf(
            CAQIBreakpoint(0, 25, 0, 25),
            CAQIBreakpoint(26, 50, 26, 50),
            CAQIBreakpoint(51, 90, 50, 75),
            CAQIBreakpoint(91, 180, 75, 100),
            CAQIBreakpoint(181, Int.MAX_VALUE, 100, Int.MAX_VALUE)
        )

        val caqiPm25 = calculateCAQI(pm25, pm25Breakpoints)
        val caqiPm10 = calculateCAQI(pm100, pm100Breakpoints)

        return maxOf(caqiPm25, caqiPm10)
    }
}