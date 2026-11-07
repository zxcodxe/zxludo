package com.asta.ludoprank

object PlayerSelectionScreen {

    fun show(activity: MainActivity) {

        activity.createScreen()
        activity.addTitle("SELECT PLAYERS")

        activity.addButton("2 PLAYERS") {
            activity.selectedPlayers = 2
            activity.showPrankSetup()
        }

        activity.addButton("3 PLAYERS") {
            activity.selectedPlayers = 3
            activity.showPrankSetup()
        }

        activity.addButton("4 PLAYERS") {
            activity.selectedPlayers = 4
            activity.showPrankSetup()
        }

        activity.addButton("BACK") {
            activity.showGameSelection()
        }
    }
}
