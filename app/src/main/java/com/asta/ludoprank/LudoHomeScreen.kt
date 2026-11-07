package com.asta.ludoprank

object LudoHomeScreen {

    fun show(activity: MainActivity) {

        activity.createScreen()
        activity.addTitle("LUDO")

        activity.addButton("FRIENDS") {
            activity.selectedMode = "Friends"
            activity.showGameSelection()
        }

        activity.addButton("COMPUTER") {
            activity.selectedMode = "Computer"
            activity.showGameSelection()
        }

        activity.addButton("PASS N PLAY") {
            activity.selectedMode = "Pass N Play"
            activity.showGameSelection()
        }

        activity.addButton("HOW TO PLAY") {
            activity.createScreen()
            activity.addTitle("HOW TO PLAY")
            activity.addButton("BACK") {
                activity.showHome()
            }
        }
    }
}
