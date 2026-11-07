package com.asta.ludoprank

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.*
import android.os.Handler
import android.os.Looper
import kotlin.math.min
import kotlin.random.Random

class MainActivity : Activity() {
    private val navy = Color.rgb(4, 28, 83)
    private val blue = Color.rgb(13, 83, 177)
    private val gold = Color.rgb(255, 190, 35)
    private val colors = intArrayOf(Color.rgb(225,35,45), Color.rgb(245,196,20), Color.rgb(30,170,80), Color.rgb(35,135,225))
    private val colorNames = listOf("RED", "GREEN", "YELLOW", "BLUE")
    private var mode = "FRIENDS"
    private var gameType = "CLASSIC"
    private var playerCount = 2
    private var firstPlayer = 0
    private var prankWinner = -1
    private var game: LudoGame? = null
    private var computerMode = false
    private lateinit var root: LinearLayout
    private lateinit var status: TextView
    private lateinit var diceButton: Button
    private lateinit var board: BoardView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(3, 20, 61)
        window.navigationBarColor = Color.rgb(2, 15, 48)
        showSplash()
    }

    private fun bg(): GradientDrawable = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(Color.rgb(4,27,78), Color.rgb(10,83,174), Color.rgb(3,29,84))).apply { cornerRadius = dp(18).toFloat() }
    private fun panel(): GradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(Color.rgb(14,104,202), Color.rgb(3,39,111))).apply { setStroke(dp(2), gold); cornerRadius = dp(18).toFloat() }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun base(titleText: String, scroll: Boolean = true) {
        root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL; setPadding(dp(16), dp(14), dp(16), dp(12)); background = bg() }
        val heading = TextView(this).apply { text = titleText; textSize = 27f; typeface = Typeface.create("sans-serif-black", Typeface.BOLD); setTextColor(gold); gravity = Gravity.CENTER; setShadowLayer(5f, 0f, 3f, Color.BLACK); setPadding(0, dp(10), 0, dp(16)) }
        root.addView(heading, LinearLayout.LayoutParams(-1, -2))
        if (scroll) {
            val sc = ScrollView(this).apply { isFillViewport = true; isVerticalScrollBarEnabled = false }
            val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL }
            sc.addView(content)
            root.addView(sc, LinearLayout.LayoutParams(-1, 0, 1f))
            root.tag = content
        } else root.tag = root
        setContentView(root)
    }
    private fun content(): LinearLayout = root.tag as LinearLayout

    private fun text(s: String, size: Float = 16f, color: Int = Color.WHITE): TextView = TextView(this).apply { text = s; textSize = size; setTextColor(color); gravity = Gravity.CENTER; setPadding(dp(8), dp(8), dp(8), dp(8)); typeface = Typeface.create("sans-serif", Typeface.BOLD) }
    private fun action(label: String, onClick: () -> Unit, enabled: Boolean = true, color: Int = blue): Button = Button(this).apply {
        text = label; textSize = 17f; isAllCaps = false; isEnabled = enabled; setTextColor(if (enabled) Color.WHITE else Color.LTGRAY); typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, if (enabled) intArrayOf(color, Color.rgb(2,35,105)) else intArrayOf(Color.DKGRAY, Color.GRAY)).apply { cornerRadius = dp(15).toFloat(); setStroke(dp(3), if (enabled) gold else Color.GRAY) }
        elevation = dp(5).toFloat(); setOnClickListener { onClick() }
        layoutParams = LinearLayout.LayoutParams(-1, dp(56)).apply { setMargins(dp(5), dp(6), dp(5), dp(6)) }
    }
    private fun addLabel(s: String, size: Float = 16f) { content().addView(text(s, size), LinearLayout.LayoutParams(-1, -2)) }
    private fun addButton(s: String, enabled: Boolean = true, onClick: () -> Unit) { content().addView(action(s, onClick, enabled)) }

    private fun showSplash() {
        base("", false)
        root.removeAllViews()
        val logo = TextView(this).apply { text = "GAMEMOTION"; textSize = 35f; letterSpacing = .08f; typeface = Typeface.create("serif", Typeface.BOLD); setTextColor(Color.WHITE); gravity = Gravity.CENTER; setShadowLayer(12f, 0f, 0f, gold) }
        root.addView(View(this), LinearLayout.LayoutParams(1, 0, 1f)); root.addView(logo, LinearLayout.LayoutParams(-1, dp(100))); root.addView(text("A CLASSIC BOARD GAME EXPERIENCE", 13f), LinearLayout.LayoutParams(-1, dp(55))); root.addView(View(this), LinearLayout.LayoutParams(1, 0, 1f))
        handler.postDelayed({ showLogo() }, 1400)
    }
    private fun showLogo() {
        base("", false); root.removeAllViews()
        root.addView(View(this), LinearLayout.LayoutParams(1, 0, 1f))
        root.addView(text("♛", 72f, gold)); root.addView(text("LUDO", 55f)); root.addView(text("KING", 32f, gold)); root.addView(text("OFFLINE EDITION", 14f))
        root.addView(View(this), LinearLayout.LayoutParams(1, 0, 1f))
        root.addView(action("CONTINUE", { showHome() }, true, Color.rgb(226,155,8)), LinearLayout.LayoutParams(-1, dp(58)))
    }

    private fun showHome() {
        base("LUDO KING")
        addLabel("♛  LUDO  •  ROLL • MOVE • WIN  ♛", 18f)
        addLabel("Offline board game", 14f)
        addButton("🌐  ONLINE", false) {}
        addButton("⚔  TEAM UP", false) {}
        addButton("❤️  FRIENDS  (OFFLINE)") { mode = "FRIENDS"; computerMode = false; showTheme() }
        addButton("🤖  COMPUTER") { mode = "COMPUTER"; computerMode = true; playerCount = 2; showTheme() }
        addButton("🎲  PASS N PLAY") { mode = "PASS N PLAY"; computerMode = false; showTheme() }
        addButton("🐍  SNAKES & LADDERS", false) {}
        addLabel("Offline prank game • No online matchmaking", 13f)
    }

    private fun showTheme() {
        base("SELECT THEME")
        addLabel("Choose your token style and game mode")
        addButton("🔵  CLASSIC BOARD") { gameType = "CLASSIC"; showPlayers() }
        addButton("🟡  TEAM UP MODE", false) {}
        addButton("⚡  QUICK MODE") { gameType = "QUICK"; showPlayers() }
        addButton("BACK") { showHome() }
    }

    private fun showPlayers() {
        base("CHOOSE PLAYERS")
        addLabel("Mode: $mode  •  ${gameType}")
        val counts = if (computerMode) listOf(2) else listOf(2, 3, 4)
        counts.forEach { n -> addButton("${n} PLAYERS${if (playerCount == n) "  ✓" else ""}") { playerCount = n; showPlayers() } }
        addLabel("Player names and colors")
        for (i in 0 until playerCount) {
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL; background = panel(); setPadding(dp(8), dp(4), dp(8), dp(4)); layoutParams = LinearLayout.LayoutParams(-1, dp(54)).apply { setMargins(0, dp(4), 0, dp(4)) } }
            row.addView(text("●", 22f, colors[i]), LinearLayout.LayoutParams(dp(38), -2))
            row.addView(text("Player ${i+1}  •  ${colorNames[i]}", 15f), LinearLayout.LayoutParams(0, -2, 1f))
            content().addView(row)
        }
        addButton("SELECT PRANK WINNER (OPTIONAL)") { showWinnerSelect() }
        addButton("START GAME") { firstPlayer = 0; startGame() }
        addButton("BACK") { showTheme() }
    }

    private fun showWinnerSelect() {
        base("PRANK SETUP")
        addLabel("Choose a player to receive a gameplay advantage. This is an offline prank setting.")
        for (i in 0 until playerCount) addButton("${colorNames[i]}  •  PLAYER ${i+1}${if (prankWinner == i) "  ✓" else ""}") { prankWinner = i; firstPlayer = i; showPlayers() }
        addButton("CLEAR SELECTION") { prankWinner = -1; firstPlayer = 0; showPlayers() }
        addButton("BACK") { showPlayers() }
    }

    private fun startGame() {
        game = LudoGame(playerCount, firstPlayer, prankWinner)
        base("LUDO BOARD", false)
        status = text("Turn: ${colorNames[firstPlayer]}  •  Roll the dice", 16f)
        root.addView(status, LinearLayout.LayoutParams(-1, dp(45)))
        board = BoardView(); root.addView(board, LinearLayout.LayoutParams(-1, 0, 1f))
        diceButton = action("🎲  ROLL DICE", { rollDice() }, true, Color.rgb(220,145,5))
        root.addView(diceButton, LinearLayout.LayoutParams(-1, dp(58)))
        root.addView(action("NEW GAME", { showPlayers() }), LinearLayout.LayoutParams(-1, dp(48)))
        root.addView(action("HOME", { showHome() }), LinearLayout.LayoutParams(-1, dp(48)))
        updateGameUi()
    }

    private fun rollDice() {
        val g = game ?: return
        if (g.dice != 0 || g.winner >= 0) return
        g.dice = Random.nextInt(1, 7)
        if (g.favored >= 0 && g.turn == g.favored && g.dice < 4) g.dice = 6
        val moves = g.validMoves()
        if (moves.isEmpty()) {
            status.text = "${colorNames[g.turn]} rolled ${g.dice} • no move"
            diceButton.isEnabled = false; board.invalidate()
            handler.postDelayed({ g.endTurn(); updateGameUi(); runComputerIfNeeded() }, 650)
        } else {
            status.text = "${colorNames[g.turn]} rolled ${g.dice} • tap a token"
            diceButton.isEnabled = false; board.invalidate()
            if (computerMode && g.turn == 1) handler.postDelayed({ g.move(moves.random()); updateGameUi(); runComputerIfNeeded() }, 550)
            else if (moves.size == 1) handler.postDelayed({ g.move(moves[0]); updateGameUi(); runComputerIfNeeded() }, 350)
        }
    }
    private fun runComputerIfNeeded() { if (computerMode && game?.turn == 1 && ((game?.winner ?: -1) < 0)) handler.postDelayed({ rollDice() }, 500) }
    private fun updateGameUi() {
        val g = game ?: return
        if (g.winner >= 0) { status.text = "🏆 ${colorNames[g.winner]} WINS!"; diceButton.isEnabled = false }
        else { status.text = "Turn: ${colorNames[g.turn]}  •  ${if (g.dice > 0) "Dice: ${g.dice} — select token" else "Roll the dice"}"; diceButton.isEnabled = g.dice == 0 && !(computerMode && g.turn == 1) }
        board.invalidate()
    }

    private inner class BoardView : View(this@MainActivity) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val route = ArrayList<Point>()
        private var cell = 0f; private var ox = 0f; private var oy = 0f
        init { for (x in 1..13) route.add(Point(x,1)); for (y in 2..13) route.add(Point(13,y)); for (x in 12 downTo 1) route.add(Point(x,13)); for (y in 12 downTo 2) route.add(Point(1,y)) }
        override fun onDraw(c: Canvas) { super.onDraw(c); val size = min(width, height).toFloat(); cell = size / 15f; ox = (width-size)/2f; oy=(height-size)/2f; drawBoard(c); drawTokens(c) }
        private fun rect(c: Canvas, x: Int, y: Int, w: Int, h: Int, color: Int) { paint.color=color; paint.style=Paint.Style.FILL; c.drawRect(ox+x*cell, oy+y*cell, ox+(x+w)*cell, oy+(y+h)*cell, paint) }
        private fun drawBoard(c: Canvas) {
            rect(c,0,0,15,15,Color.WHITE)
            val homes = arrayOf(intArrayOf(0,0),intArrayOf(10,0),intArrayOf(10,10),intArrayOf(0,10))
            for (i in 0..3) { val h=homes[i]; rect(c,h[0],h[1],5,5,colors[i]); paint.color=Color.WHITE; c.drawRoundRect(ox+(h[0]+.65f)*cell,oy+(h[1]+.65f)*cell,ox+(h[0]+4.35f)*cell,oy+(h[1]+4.35f)*cell,cell*.25f,cell*.25f,paint); for (a in 0..1) for (b in 0..1) { paint.color=colors[i]; c.drawCircle(ox+(h[0]+1.5f+a*2)*cell,oy+(h[1]+1.5f+b*2)*cell,cell*.38f,paint) } }
            for (pt in route) { paint.color=Color.LTGRAY; paint.style=Paint.Style.STROKE; paint.strokeWidth=1f; c.drawRect(ox+pt.x*cell,oy+pt.y*cell,ox+(pt.x+1)*cell,oy+(pt.y+1)*cell,paint); paint.style=Paint.Style.FILL; paint.color=Color.WHITE; c.drawRect(ox+pt.x*cell+1,oy+pt.y*cell+1,ox+(pt.x+1)*cell-1,oy+(pt.y+1)*cell-1,paint) }
            val lanes = arrayOf(Triple(6,1,1),Triple(13,6,2),Triple(6,13,3),Triple(1,6,0))
            for ((x,y,i) in lanes) for (k in 0..4) { val xx=if(i==0) x else if(i==1) x else if(i==2) x else x; val yy=if(i==0) y+k else if(i==1) y+k else if(i==2) y-k else y-k; if(xx in 0..14 && yy in 0..14) rect(c,xx,yy,1,1,colors[i]) }
            paint.color=Color.rgb(245,190,30); paint.style=Paint.Style.FILL; val path=Path().apply { moveTo(ox+7.5f*cell,oy+5*cell); lineTo(ox+10*cell,oy+7.5f*cell); lineTo(ox+7.5f*cell,oy+10*cell); lineTo(ox+5*cell,oy+7.5f*cell); close() }; c.drawPath(path,paint)
            paint.color=Color.rgb(20,45,90); paint.style=Paint.Style.STROKE; paint.strokeWidth=1.2f; c.drawRect(ox,oy,ox+15*cell,oy+15*cell,paint)
        }
        private fun tokenPoint(player:Int, token:Int, progress:Int): PointF {
            val homes=arrayOf(intArrayOf(2,2),intArrayOf(12,2),intArrayOf(12,12),intArrayOf(2,12)); val starts=intArrayOf(0,13,26,39); val h=homes[player]
            if(progress<0) return PointF((h[0]+(token%2)*2).toFloat(),(h[1]+(token/2)*2).toFloat())
            if(progress>=57) return PointF(6.7f+(token%2)*1.3f,6.7f+(token/2)*1.3f)
            if(progress>=52) { val n=progress-52; return when(player){0->PointF(1f+n,7f);1->PointF(7f,1f+n);2->PointF(13f-n,7f);else->PointF(7f,13f-n)} }
            val q=route[(starts[player]+progress)%route.size]; return PointF(q.x.toFloat(),q.y.toFloat())
        }
        private fun drawTokens(c:Canvas) { val g=game?:return; for(pl in 0 until g.players) for(t in 0..3) { val pt=tokenPoint(pl,t,g.pos[pl][t]); val x=ox+(pt.x+.5f)*cell; val y=oy+(pt.y+.5f)*cell; paint.style=Paint.Style.FILL; paint.color=colors[pl]; c.drawCircle(x,y,cell*.34f,paint); paint.color=Color.WHITE; paint.style=Paint.Style.STROKE; paint.strokeWidth=2.5f; c.drawCircle(x,y,cell*.34f,paint); if(g.turn==pl && g.dice>0 && g.validMoves().contains(t)) { paint.color=gold; paint.strokeWidth=4f; c.drawCircle(x,y,cell*.45f,paint) } } }
        override fun onTouchEvent(e:MotionEvent):Boolean { if(e.action!=MotionEvent.ACTION_UP)return true; val g=game?:return true; if(g.dice<=0||g.winner>=0)return true; for(t in g.validMoves()) { val pt=tokenPoint(g.turn,t,g.pos[g.turn][t]); val x=ox+(pt.x+.5f)*cell; val y=oy+(pt.y+.5f)*cell; if((e.x-x)*(e.x-x)+(e.y-y)*(e.y-y)<cell*cell*.8f) { g.move(t); updateGameUi(); runComputerIfNeeded(); break } }; return true }
    }

    private class LudoGame(val players:Int, start:Int, val favored:Int) {
        val pos=Array(4){IntArray(4){-1}}; var turn=start; var dice=0; var winner=-1
        private val starts=intArrayOf(0,13,26,39); private val safe=setOf(0,8,13,21,26,34,39,47)
        fun validMoves():List<Int> { if(dice==0)return emptyList(); val out=mutableListOf<Int>(); for(t in 0..3){val p=pos[turn][t]; if(p<0&&dice==6)out.add(t) else if(p>=0&&p+dice<=57)out.add(t)}; return out }
        fun move(t:Int) { if(t !in validMoves())return; val p=pos[turn][t]; val next=if(p<0)0 else p+dice; pos[turn][t]=next; if(next in 0..51)capture(turn,(starts[turn]+next)%52); if(pos[turn].all{it>=57}){winner=turn;dice=0;return}; val again=dice==6; dice=0; if(!again)endTurn() }
        private fun capture(player:Int,global:Int){for(o in 0 until players)if(o!=player)for(t in 0..3){val p=pos[o][t];if(p in 0..51&&(starts[o]+p)%52==global&&global !in safe)pos[o][t]=-1}}
        fun endTurn(){turn=(turn+1)%players;dice=0}
    }
}
