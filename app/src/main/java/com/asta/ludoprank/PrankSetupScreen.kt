package com.asta.ludoprank

object PrankSetupScreen {

    fun show(activity: MainActivity) {

        activity.createScreen()
        activity.addTitle("PRANK SETUP")

        activity.addButton("RED WINNER") {
            activity.selectedWinner = 0
            activity.startLudo()
        }

        activity.addButton("GREEN WINNER") {
            activity.selectedWinner = 1
            activity.startLudo()
        }

        activity.addButton("YELLOW WINNER") {
            activity.selectedWinner = 2
            activity.startLudo()
        }

        activity.addButton("BLUE WINNER") {
            activity.selectedWinner = 3
            activity.startLudo()
        }

        activity.addButton("RANDOM WINNER") {
            activity.selectedWinner = -1
            activity.startLudo()
        }
    }
}
