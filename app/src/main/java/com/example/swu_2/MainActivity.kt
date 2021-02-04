package com.example.swu_2


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.dinuscxj.progressbar.CircleProgressBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.util.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    CircleProgressBar.ProgressFormatter, NavigationView.OnNavigationItemSelectedListener {

    //변수
    lateinit var userID: TextView
    lateinit var userIDcheck: TextView
    lateinit var userPage: ImageButton
    lateinit var myCalendar: CalendarView
    lateinit var listEdit: ImageButton
    lateinit var todoListView: ListView
    lateinit var adapter1: ArrayAdapter<String>
    lateinit var listItem: ArrayList<String>
    lateinit var layout_drawer : DrawerLayout
    var count : Int = 0
    lateinit var naviView : NavigationView


    // 원형 프로그레스 바 설정 변수
    private val DEFAULT_PATTERN = "%d%%"
    lateinit var circleProgressBar: CircleProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userID = findViewById(R.id.userID)
        userIDcheck = findViewById(R.id.userIDcheck)
        userPage = findViewById(R.id.userPage)
        myCalendar = findViewById(R.id.myCalendar)
        listEdit = findViewById(R.id.listEdit)
        todoListView = findViewById(R.id.todoListView)
        listItem = ArrayList<String>()
        layout_drawer = findViewById(R.id.layout_drawer)
        naviView = findViewById(R.id.naviView)

        val bottomNavigationView =
            findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        circleProgressBar = findViewById(R.id.cpb_circlebar);
        //circleProgressBar.setProgress(percentInt*checkInt);  // 해당 퍼센트를 적용


        // TodoList로부터 intent 받기
        val InIntent = getIntent()
        val item = InIntent.getSerializableExtra("ArrayList") as? ArrayList<String>

        // itemlist가 null 일 경우 adapter를 연결하지 않음
        if (item != null) {
            adapter1 = ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice,
                item)
            todoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
            todoListView.setAdapter(adapter1)

            count = adapter1.getCount()

            var listview_adapter = ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice,
                item)

            todoListView.adapter = listview_adapter

            // 체크박스 클릭 시 프로그레스바 퍼센테이지 변경
            todoListView.setOnItemClickListener { parent, view, position, id ->

                var checkInt : Double = 0.0
                var percentInt : Double = 0.0

                val checkedItems = todoListView.getCheckedItemPositions()
                for (i in count - 1 downTo 0) {
                    if (checkedItems.get(i)) {
                        checkInt++
                    }
                }

                percentInt = ((checkInt.toDouble() / count.toDouble())*100)
                circleProgressBar.setProgress(percentInt.toInt())
            }

        }

        // 에디티 버튼 클릭시 TodoList로 intent 전달
        listEdit.setOnClickListener {
            val OutIntent = Intent(this, TodoList::class.java)
            OutIntent.putExtra("ArrayList2", item)
            startActivity(OutIntent)
        }

        //유저 페이지로 이동
        userPage.setOnClickListener {
            layout_drawer.openDrawer(GravityCompat.START)
        }
        naviView.setNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, Frag01())
                    .commit()
                return true
            }
            R.id.nav_timer -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, Frag02())
                    .commit()
                return true
            }
            R.id.nav_web -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, Frag03())
                    .commit()
                return true
            }
            R.id.access -> {
                Toast.makeText(applicationContext, "일단 구현전", Toast.LENGTH_SHORT).show()
            }
            R.id.logout -> {
                Toast.makeText(applicationContext, "일단 구현전", Toast.LENGTH_SHORT).show()
            }
        }
        layout_drawer.closeDrawers()
        return false
    }

    override fun onBackPressed() {

        if (layout_drawer.isDrawerOpen(GravityCompat.START)){
            layout_drawer.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }


    override fun format(progress: Int, max: Int): CharSequence? {
        return String.format(
            DEFAULT_PATTERN,
            (progress.toFloat() / max.toFloat() * 100).toInt()
        )
    }
}