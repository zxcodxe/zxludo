package com.asta.ludoprank

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.*
import kotlin.math.min
import kotlin.random.Random

class MainActivity : Activity() {
    private var mode = "4 PLAYERS"
    private var firstPlayer = 0
    private var prankWinner = -1
    private var game: LudoGame? = null
    private lateinit var root: LinearLayout
    private lateinit var gameView: LudoView
    private lateinit var status: TextView
    private lateinit var diceButton: Button
    private val purple = Color.rgb(123,44,191)

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); showHome() }

    private fun base(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL
        setPadding(22, 24, 22, 18); setBackgroundColor(Color.rgb(245,242,250))
        root = this
    }
    private fun title(t:String) = TextView(this).apply { text=t; textSize=30f; setTextColor(Color.rgb(35,20,45)); gravity=Gravity.CENTER; setPadding(0,0,0,18) }
    private fun button(t:String, action:()->Unit) = Button(this).apply {
        text=t; textSize=16f; setOnClickListener{action()}; layoutParams=LinearLayout.LayoutParams(-1,58).apply{setMargins(0,7,0,7)}
    }
    private fun showHome(){
        base(); root.addView(title("ASTA LUDO")); root.addView(TextView(this).apply{text="Fair offline Ludo • 1–4 players";textSize=16f;gravity=Gravity.CENTER;setPadding(0,0,0,20)})
        root.addView(button("PLAY LUDO"){showModes()})
        root.addView(button("HOW TO PLAY"){showHelp()}); setContentView(root)
    }
    private fun showHelp(){
        base(); root.addView(title("HOW TO PLAY")); root.addView(TextView(this).apply{text="• Roll a 6 to bring a token out.\n• Tap a highlighted token to move it.\n• Landing on an opponent captures it (except safe squares).\n• Roll a 6 to play again.\n• Get all 4 tokens home to win.\n• In 2 VS 2, teammates share the win.\n\nASTA SELECT only chooses who starts; it does not rig dice or movement.";textSize=17f;setPadding(8,5,8,15)})
        root.addView(button("BACK"){showHome()}); setContentView(root)
    }
    private fun showModes(){
        base(); root.addView(title("SELECT MODE"));
        root.addView(button("1 VS 1"){mode="1 VS 1";showStartSelect()});
        root.addView(button("2 VS 2 TEAM"){mode="2 VS 2 TEAM";showStartSelect()});
        root.addView(button("4 PLAYERS"){mode="4 PLAYERS";showStartSelect()});
        root.addView(button("BACK"){showHome()}); setContentView(root)
    }
    private fun showStartSelect(){
        base(); root.addView(title("ASTA SELECT")); root.addView(TextView(this).apply{text="Choose who starts. Optional prank mode can secretly favor a player.";textSize=16f;gravity=Gravity.CENTER;setPadding(0,0,0,18)})
        val names=when(mode){"1 VS 1"->listOf("🔴 RED","🟡 YELLOW");"2 VS 2 TEAM"->listOf("🔴 RED + 🟡 YELLOW","🟢 GREEN + 🔵 BLUE");else->listOf("🔴 RED","🟡 YELLOW","🟢 GREEN","🔵 BLUE")}
        names.forEachIndexed{i,n->root.addView(button(n){firstPlayer=if(mode=="2 VS 2 TEAM") i*2 else i; prankWinner=-1; startGame()})}
        root.addView(button("PRANK: SECRET WINNER"){showPrankSelect(names)})
        root.addView(button("BACK"){showModes()}); setContentView(root)
    }
    private fun showPrankSelect(names:List<String>){
        base(); root.addView(title("SECRET WINNER")); root.addView(TextView(this).apply{text="Pick the player who should have a strong advantage. Keep this screen private.";textSize=16f;gravity=Gravity.CENTER;setPadding(0,0,0,18)})
        names.forEachIndexed{i,n->root.addView(button(n){prankWinner=if(mode=="2 VS 2 TEAM") i*2 else i; firstPlayer=prankWinner; startGame()})}
        root.addView(button("BACK"){showStartSelect()}); setContentView(root)
    }
    private fun startGame(){
        val count=when(mode){"1 VS 1"->2;"2 VS 2 TEAM"->4;else->4}
        game=LudoGame(count, firstPlayer, prankWinner, mode=="2 VS 2 TEAM")
        base(); root.addView(title("ASTA LUDO"))
        status=TextView(this).apply{textSize=16f;gravity=Gravity.CENTER;setPadding(0,0,0,8)}; root.addView(status)
        gameView=LudoView(); root.addView(gameView,LinearLayout.LayoutParams(-1,0,1f))
        diceButton=button("🎲 ROLL DICE"){roll()}; root.addView(diceButton)
        root.addView(button("NEW GAME"){showStartSelect()}); root.addView(button("HOME"){showHome()})
        updateUi(); setContentView(root)
    }
    private fun roll(){
        val g=game?:return; if(g.dice!=0||g.winner>=0)return
        g.dice=Random.nextInt(1,7); if(g.favored>=0 && g.turn==g.favored && g.dice<4) g.dice=6; val moves=g.validMoves()
        if(moves.isEmpty()){
            status.text="${g.name(g.turn)} rolled ${g.dice} • no move"; diceButton.isEnabled=false; gameView.invalidate()
            window.decorView.postDelayed({g.endTurn();updateUi()},650)
        } else { status.text="${g.name(g.turn)} rolled ${g.dice} • tap a glowing token"; diceButton.isEnabled=false; gameView.invalidate(); if(moves.size==1) window.decorView.postDelayed({g.move(moves[0]);updateUi()},300) }
    }
    private fun updateUi(){
        val g=game?:return
        if(g.winner>=0){status.text="🏆 ${g.winnerText()} wins!";diceButton.isEnabled=false}
        else {status.text="Turn: ${g.name(g.turn)}${if(g.dice>0) " • Dice: ${g.dice}" else " • Roll the dice"}";diceButton.isEnabled=g.dice==0}
        gameView.invalidate()
    }

    private inner class LudoView: View(this@MainActivity){
        private val p=Paint(Paint.ANTI_ALIAS_FLAG); private val colors=intArrayOf(Color.rgb(225,45,45),Color.rgb(238,185,25),Color.rgb(35,170,85),Color.rgb(55,105,220));
        private val path=ArrayList<Point>(); private var cell=0f; private var ox=0f; private var oy=0f
        init { buildPath(); setBackgroundColor(Color.WHITE) }
        private fun buildPath(){
            // 52-cell rectangular loop around the board.
            for(x in 1..13) path.add(Point(x,1)); for(y in 2..13) path.add(Point(13,y)); for(x in 12 downTo 1) path.add(Point(x,13)); for(y in 12 downTo 2) path.add(Point(1,y));
            // 4 cells still needed to reach 52: inner connectors.
            path.add(Point(2,2)); path.add(Point(3,2)); path.add(Point(4,2)); path.add(Point(5,2))
        }
        override fun onDraw(c:Canvas){super.onDraw(c); val size=min(width,height).toFloat(); cell=size/15f; ox=(width-size)/2f; oy=(height-size)/2f; drawBoard(c); drawTokens(c)}
        private fun rect(c:Canvas,x:Int,y:Int,color:Int){p.color=color;p.style=Paint.Style.FILL;c.drawRect(ox+x*cell,oy+y*cell,ox+(x+1)*cell,oy+(y+1)*cell,p)}
        private fun drawBoard(c:Canvas){
            p.style=Paint.Style.FILL; p.color=Color.rgb(250,250,250);c.drawRect(ox,oy,ox+15*cell,oy+15*cell,p)
            // Homes
            val homes=arrayOf(intArrayOf(0,0),intArrayOf(10,0),intArrayOf(0,10),intArrayOf(10,10))
            for(i in 0..3){p.color=colors[i];p.alpha=45;val h=homes[i];c.drawRoundRect(ox+h[0]*cell+3,oy+h[1]*cell+3,ox+(h[0]+5)*cell-3,oy+(h[1]+5)*cell-3,18f,18f,p);p.alpha=255}
            // Track ring and grid
            for((i,q) in path.withIndex()){p.color=Color.WHITE;p.style=Paint.Style.FILL;c.drawRect(ox+q.x*cell,oy+q.y*cell,ox+(q.x+1)*cell,oy+(q.y+1)*cell,p);p.color=Color.LTGRAY;p.style=Paint.Style.STROKE;p.strokeWidth=1f;c.drawRect(ox+q.x*cell,oy+q.y*cell,ox+(q.x+1)*cell,oy+(q.y+1)*cell,p)}
            // Starts
            val starts=intArrayOf(0,13,26,39); for(i in 0 until 4){val q=path[starts[i]];p.style=Paint.Style.FILL;p.color=colors[i];c.drawRect(ox+q.x*cell,oy+q.y*cell,ox+(q.x+1)*cell,oy+(q.y+1)*cell,p)}
            // Home center
            p.color=Color.WHITE;p.style=Paint.Style.FILL;c.drawCircle(ox+7.5f*cell,oy+7.5f*cell,1.25f*cell,p);p.color=purple;p.style=Paint.Style.STROKE;p.strokeWidth=3f;c.drawCircle(ox+7.5f*cell,oy+7.5f*cell,1.25f*cell,p)
        }
        private fun drawTokens(c:Canvas){
            val g=game?:return; val homes=arrayOf(intArrayOf(2,2),intArrayOf(12,2),intArrayOf(2,12),intArrayOf(12,12)); val starts=intArrayOf(0,13,26,39)
            for(player in 0 until 4){if(player>=g.players)continue; for(t in 0..3){val prog=g.pos[player][t];val pt=tokenPoint(player,t,prog,homes[player],starts);p.color=colors[player];p.style=Paint.Style.FILL;c.drawCircle(ox+(pt.x+.5f)*cell,oy+(pt.y+.5f)*cell,cell*.31f,p);p.color=Color.WHITE;p.style=Paint.Style.STROKE;p.strokeWidth=2f;c.drawCircle(ox+(pt.x+.5f)*cell,oy+(pt.y+.5f)*cell,cell*.31f,p)
                if(g.turn==player && g.dice>0 && g.validMoves().contains(t)){p.color=Color.WHITE;p.style=Paint.Style.STROKE;p.strokeWidth=4f;c.drawCircle(ox+(pt.x+.5f)*cell,oy+(pt.y+.5f)*cell,cell*.42f,p)}}
            }
        }
        private fun tokenPoint(player:Int,t:Int,prog:Int,home:IntArray,start:Int):PointF{
            if(prog<0){val spots=arrayOf(Point(home[0],home[1]),Point(home[0]+2,home[1]),Point(home[0],home[1]+2),Point(home[0]+2,home[1]+2));return PointF(spots[t].x.toFloat(),spots[t].y.toFloat())}
            if(prog>=57){return PointF(6.9f+(t%2)*1.2f,6.9f+(t/2)*1.2f)}
            if(prog>=52){val lane=prog-52;val center=7;return when(player){0->PointF(6f+lane,6f);1->PointF(8f,6f+lane);2->PointF(8f-lane,8f);else->PointF(6f,8f-lane)}}
            val q=path[(start+prog)%path.size];return PointF(q.x.toFloat(),q.y.toFloat())
        }
        override fun onTouchEvent(e:android.view.MotionEvent):Boolean{if(e.action!=MotionEvent.ACTION_UP)return true;val g=game?:return true;if(g.dice<=0||g.winner>=0)return true;val moves=g.validMoves();for(t in moves){val pt=tokenPoint(g.turn,t,g.pos[g.turn][t],arrayOf(intArrayOf(2,2),intArrayOf(12,2),intArrayOf(2,12),intArrayOf(12,12))[g.turn],intArrayOf(0,13,26,39)[g.turn]);val cx=ox+(pt.x+.5f)*cell;val cy=oy+(pt.y+.5f)*cell;if((e.x-cx)*(e.x-cx)+(e.y-cy)*(e.y-cy)<(cell*.65f)*(cell*.65f)){g.move(t);updateUi();break}};return true}
    }

    private class LudoGame(val players:Int,val start:Int,val favored:Int,val teamMode:Boolean){
        val pos=Array(4){IntArray(4){-1}};var turn=start;var dice=0;var winner=-1
        private val starts=intArrayOf(0,13,26,39);private val path=ArrayList<Point>().apply{for(x in 1..13)add(Point(x,1));for(y in 2..13)add(Point(13,y));for(x in 12 downTo 1)add(Point(x,13));for(y in 12 downTo 2)add(Point(1,y));add(Point(2,2));add(Point(3,2));add(Point(4,2));add(Point(5,2))};private val safe=setOf(0,8,13,21,26,34,39,47)
        fun validMoves():List<Int>{if(dice==0)return emptyList();val out=mutableListOf<Int>();for(t in 0..3){val p=pos[turn][t];if(p<0&&dice==6)out.add(t) else if(p>=0&&p+dice<=57)out.add(t)};return out}
        fun move(t:Int){if(!validMoves().contains(t))return;var p=pos[turn][t];p=if(p<0)0 else p+dice;pos[turn][t]=p;if(p in 0..51)capture(turn,t,(starts[turn]+p)%52);if(pos[turn].all{it>=57}){winner=turn;dice=0;return};val again=dice==6;dice=0;if(!again)endTurn()}
        private fun capture(player:Int,token:Int,global:Int){for(o in 0 until players)if(o!=player){for(t in 0..3){val p=pos[o][t];if(p in 0..51 && (starts[o]+p)%52==global && global !in safe)pos[o][t]=-1}}}
        fun endTurn(){turn=(turn+1)%players;dice=0}
        fun name(i:Int)=when(i){0->"RED";1->"YELLOW";2->"GREEN";else->"BLUE"}
        fun winnerText()=if(teamMode) { if(winner==0 || winner==1) "RED + YELLOW TEAM" else "GREEN + BLUE TEAM" } else name(winner)
    }
}
