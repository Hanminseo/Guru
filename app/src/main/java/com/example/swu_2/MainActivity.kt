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
    NavigationView.OnNavigationItemSelectedListener {

    //변수
    lateinit var layout_drawer : DrawerLayout
    lateinit var naviView : NavigationView

    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView =
            findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        naviView = findViewById(R.id.naviView)
        layout_drawer = findViewById(R.id.layout_drawer)

        // Frag01로 부터 flag를 전달받음
        var intent = intent
        var flag = intent.getIntExtra("flag", 0)
        // 받은 값이 0이 아닌 값이라면 옆 서랍 메뉴가 작동하도록 구현
        if(flag != 0){
            layout_drawer.openDrawer(GravityCompat.START)
        }

        // 처음 화면은 Frag01
        replaceFragment(Frag01())
        naviView.setNavigationItemSelectedListener(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

    }

    // 하단 네비게이션 메뉴 클릭시 화면을 변경하도록 하는 리스너
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // 디폴트 메인홈 메뉴
                replaceFragment(Frag01())
                return true
            }
            R.id.nav_timer -> {
                // 타이머 메뉴
                replaceFragment(Frag02())
                return true
            }
            R.id.nav_web -> {
                // 웹사이트 메뉴
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

    // fragment에서 다른 fragment로 화면을 전환하기 위한 메소드
    public fun replaceFragment(fragment: Fragment) {
        fragmentManager = getSupportFragmentManager()
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    // 뒤로 가기
    override fun onBackPressed() {
        if (layout_drawer.isDrawerOpen(GravityCompat.START)){
            layout_drawer.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }

}