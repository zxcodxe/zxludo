package com.asta.ludoprank

object GameSelectionScreen {

    fun show(activity: MainActivity) {

        activity.createScreen()
        activity.addTitle("GAME MODE")

        activity.addButton("CLASSIC LUDO") {
            activity.showPlayerSelection()
        }

        activity.addButton("QUICK LUDO") {
            activity.showPlayerSelection()
        }

        activity.addButton("BACK") {
            activity.showHome()
        }
    }
}
