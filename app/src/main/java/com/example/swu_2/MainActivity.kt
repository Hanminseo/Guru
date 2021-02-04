package com.example.swu_2


import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.dinuscxj.progressbar.CircleProgressBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    CircleProgressBar.ProgressFormatter, NavigationView.OnNavigationItemSelectedListener {

    // DB에서 리스트 정보 로드하는 변수
    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase
    lateinit var getItemString: String
    lateinit var getCheckedItem: String
    lateinit var checkedItems: SparseBooleanArray

    //변수
    lateinit var userID: TextView
    lateinit var userIDcheck: TextView
    lateinit var userPage: ImageButton
    lateinit var myCalendar: CalendarView
    lateinit var listEdit: ImageButton
    lateinit var todoListView: ListView
    lateinit var adapter: ArrayAdapter<String>
    lateinit var listItem: ArrayList<String>
    lateinit var layout_drawer : DrawerLayout
    lateinit var naviView : NavigationView
    lateinit var mAuth: FirebaseAuth

    // 원형 프로그레스 바 설정 변수
    private val DEFAULT_PATTERN = "%d%%"
    lateinit var circleProgressBar: CircleProgressBar
    var count: Int = 0
    var checkInt : Double = 0.0
    var percentInt : Double = 0.0

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


        // 어댑터 연결
        adapter = ArrayAdapter<String>(
            getApplicationContext(),
            android.R.layout.simple_list_item_multiple_choice,
            listItem
        )
        todoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        todoListView.setAdapter(adapter)

        // db에서 리스트뷰 데이터 로드
        dbManager = DBManager(this, "itemDB", null, 1)
        sqlDB = dbManager.readableDatabase

        var cursor: Cursor
        var cnt = 0
        cursor = sqlDB.rawQuery("SELECT * FROM itemTBL;", null)
        while (cursor.moveToNext()){
            getItemString = cursor.getString(0)
            getCheckedItem = cursor.getString(1)
            listItem.add(getItemString)
            // db에서 체크 상태도 로드해서 반영
            if(getCheckedItem == "CHECKED"){
                todoListView.setItemChecked(cnt, true)
            }
            cnt++
        }
        cursor.close()
        sqlDB.close()
        adapter.notifyDataSetChanged()

        count = adapter.getCount()
        onSettingProgress()



        // 체크박스 클릭 시 프로그레스바 퍼센테이지 변경
        todoListView.setOnItemClickListener { parent, view, position, id ->
            onSettingProgress()
        }

        // 에디티 버튼 클릭 리스너
        listEdit.setOnClickListener {

            // db에 리스트뷰 체크된 항목 여부 저장하기
            checkedItems = todoListView.getCheckedItemPositions()
            for (i in count - 1 downTo 0) {
                dbManager = DBManager(this, "itemDB", null, 1)
                sqlDB = dbManager.writableDatabase
                // 모든 리스트 뷰의 체크 박스를 UNCHECKED로 초기화함
                sqlDB.execSQL("UPDATE itemTBL SET checked='UNCHECKED' WHERE item ='" + listItem.get(i) + "';")
                // 체크된 리스트만 CHECKED로 수정
                if (checkedItems.get(i)) {
                    sqlDB.execSQL("UPDATE itemTBL SET checked='CHECKED' WHERE item ='" + listItem.get(i) + "';")
                }
                sqlDB.close()
            }

            // TodoList 편집 액티비티로 전환
            val OutIntent = Intent(this, TodoList::class.java)
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
            R.id.accountInfo -> {
                //drawer 메뉴 내계정
                val intent = Intent(this, AccountInfo::class.java)
                startActivity(intent)
            }

            R.id.logout -> {
                //drawer 메뉴 로그아웃
                FirebaseAuth.getInstance().signOut();
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "로그아웃 완료", Toast.LENGTH_SHORT).show()

            }

            R.id.delete -> {
                //drawer 메뉴 회원 탈퇴
                mAuth = FirebaseAuth.getInstance();
                mAuth.getCurrentUser()?.delete();
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "계정삭제 완료", Toast.LENGTH_SHORT).show()

            }

            R.id.SWUInfo -> {
                //drawer 메뉴 개발자 정보
                val intent = Intent(this, SwuInfo::class.java)
                startActivity(intent)
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

    private fun onSettingProgress() {
        checkInt = 0.0
        percentInt = 0.0
        checkedItems = todoListView.getCheckedItemPositions()

        for (i in count - 1 downTo 0) {
            if (checkedItems.get(i)) {
                checkInt++
            }
        }
        percentInt = ((checkInt / count.toDouble())*100)
        circleProgressBar.setProgress(percentInt.toInt())
    }
}