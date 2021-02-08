package com.example.swu_2

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dinuscxj.progressbar.CircleProgressBar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Frag01 : Fragment(), CircleProgressBar.ProgressFormatter{

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

    lateinit var noticeview : TextView
/*
    //frag에서 뒤로가기 버튼
    private lateinit var callback: OnBackPressedCallback

 */

    // 원형 프로그레스 바 설정 변수
    private val DEFAULT_PATTERN = "%d%%"
    lateinit var circleProgressBar: CircleProgressBar
    var count: Int = 0

    //firebase,db 연결
    var fireEmail : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null


    //lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // frag02 xml로 화면 구성
        val view: View = inflater.inflate(R.layout.frag01, container, false)


        userID = view.findViewById(R.id.userID)
        userIDcheck = view.findViewById(R.id.userIDcheck)
        userPage = view.findViewById(R.id.userPage)
        myCalendar = view.findViewById(R.id.myCalendar)
        listEdit = view.findViewById(R.id.listEdit)
        todoListView = view.findViewById(R.id.todoListView)
        listItem = ArrayList<String>()
        layout_drawer = view.findViewById(R.id.layout_drawer)

        noticeview = view.findViewById(R.id.noticeview)

        //textView 계속 흐르도록...
        noticeview.setSelected( true );


        //caldendar 연결
        myCalendar.setOnClickListener {
            (activity as MainActivity).replaceFragment(Frag04())
        }

        fireEmail = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //firestore 도전!!!!!!!!!
        fireEmail = FirebaseAuth.getInstance()  //userInfo.storeUid = emailAuth?.uid
        firestore = FirebaseFirestore.getInstance()

        val store_uid = fireEmail?.uid

        val docRef = firestore?.collection("member")?.document(store_uid.toString())
        docRef?.get()?.addOnSuccessListener {
            val member = it.toObject(Member::class.java)
            userID.setText(member?.storeName)

            /////////////공지!!!!!!!!!!!


            //객체 내용 띄우기

            val phone = member?.storePhone

            var groupInfo = group_m()


            //member컬렉션에 일치하는 uid의 문서 객체로 가져오기
            val docRef1 = firestore?.collection("group")?.document(phone.toString())
            docRef1?.get()?.addOnSuccessListener {
                val group_m = it.toObject(group_m::class.java)
                noticeview.setText(group_m?.storeContent)
            }
        }











        circleProgressBar = view.findViewById(R.id.cpb_circlebar);

        // 어댑터 연결
        adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_multiple_choice,
            listItem
        )
        todoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        todoListView.setAdapter(adapter)

        // db에서 리스트뷰 데이터 로드
        dbManager = DBManager(context?.applicationContext, "itemDB", null, 1)
        sqlDB = dbManager.readableDatabase

        var cursor: Cursor
        var cnt = 0
        cursor = sqlDB.rawQuery("SELECT * FROM todolistTBL;", null)
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
        // 기존 체크된 상황대로 프로그레스 바 셋팅 반영
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
                sqlDB = dbManager.writableDatabase
                // 모든 리스트 뷰의 체크 박스를 UNCHECKED로 초기화함
                sqlDB.execSQL("UPDATE todolistTBL SET checked='UNCHECKED' WHERE item ='" + listItem.get(i) + "';")
                // 체크된 리스트만 CHECKED로 수정
                if (checkedItems.get(i)) {
                    sqlDB.execSQL("UPDATE todolistTBL SET checked='CHECKED' WHERE item ='" + listItem.get(i) + "';")
                }
                sqlDB.close()
            }

            // TodoList 편집 액티비티로 전환
            (activity as MainActivity).replaceFragment(TodoList2())
        }

        //유저 페이지로 이동
        userPage.setOnClickListener {
            var flag = 1
            val intent = Intent(getActivity(), MainActivity::class.java)
            intent.putExtra("flag", flag)
            startActivity(intent)
        }
        return view
    }

    override fun format(progress: Int, max: Int): CharSequence? {
        return String.format(
            DEFAULT_PATTERN,
            (progress.toFloat() / max.toFloat() * 100).toInt()
        )
    }

    private fun onSettingProgress() {
        var checkInt = 0.0
        checkedItems = todoListView.getCheckedItemPositions()

        for (i in count - 1 downTo 0) {
            if (checkedItems.get(i)) {
                checkInt++
            }
        }
        var percentInt = ((checkInt / count.toDouble())*100)
        circleProgressBar.setProgress(percentInt.toInt())
    }
}