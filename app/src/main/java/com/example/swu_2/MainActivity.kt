package com.example.swu_2

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.dinuscxj.progressbar.CircleProgressBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
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


    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView =
            findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        naviView = findViewById(R.id.naviView)
        layout_drawer = findViewById(R.id.layout_drawer)

        var intent = intent
        var flag = intent.getIntExtra("flag", 0)
        if(flag != 0){
            layout_drawer.openDrawer(GravityCompat.START)
        }
        replaceFragment(Frag01())
        naviView.setNavigationItemSelectedListener(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(Frag01())
                return true
            }
            R.id.nav_timer -> {
                replaceFragment(Frag02())
                return true
            }
            R.id.nav_web -> {
                replaceFragment(Frag03())
                return true
            }
            R.id.accountInfo -> {
                //drawer 메뉴 내계정
                val intent = Intent(this, AccountInfo::class.java)
                startActivity(intent)
            }

            R.id.logout -> {
                //drawer 메뉴 로그아웃
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "로그아웃 완료", Toast.LENGTH_SHORT).show()

            }

            R.id.delete -> {
                //drawer 메뉴 회원 탈퇴
                FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(applicationContext, "계정삭제 완료", Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText( this,task.exception.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
            R.id.SWUInfo -> {
                //drawer 메뉴 개발자 정보
                val intent = Intent(this, SwuInfo::class.java)
                startActivity(intent)
            }
            R.id.gongji -> {
                //공지 편집창 이동
                val intent = Intent(this, Gonggi::class.java)
                startActivity(intent)
            }
        }
        layout_drawer.closeDrawers()
        return false
    }

    public fun replaceFragment(fragment: Fragment) {
        fragmentManager = getSupportFragmentManager()
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
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