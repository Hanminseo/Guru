package com.example.swu_2

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ramijemli.percentagechartview.PercentageChartView
import java.util.*

class Frag01 : Fragment(){

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
    lateinit var myCalendar: MaterialCalendarView
    lateinit var listEdit: ImageButton
    lateinit var todoListView: ListView
    lateinit var adapter: ArrayAdapter<String>
    lateinit var listItem: ArrayList<String>
    lateinit var layout_drawer : DrawerLayout
    lateinit var naviView : NavigationView
    lateinit var mAuth: FirebaseAuth
    lateinit var noticeview : TextView

    // 원형 프로그레스 바 설정 변수
    lateinit var circleProgressBar : PercentageChartView
    var count: Int = 0

    //firebase,db 연결
    var fireEmail : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // frag01 xml로 화면 구성
        val view: View = inflater.inflate(R.layout.frag01, container, false)


        userID = view.findViewById(R.id.userID)
        userIDcheck = view.findViewById(R.id.userIDcheck)
        userPage = view.findViewById(R.id.userPage)
        myCalendar = view.findViewById(R.id.myCalendar)
        listEdit = view.findViewById(R.id.listEdit)
        todoListView = view.findViewById(R.id.todoListView)
        listItem = ArrayList<String>()
        dbManager = DBManager(context?.applicationContext, "itemDB", null, 1)
        circleProgressBar = view.findViewById(R.id.cpb_circlebar)

        noticeview = view.findViewById(R.id.noticeview)

        //textView(공지) 계속 흐르도록
        noticeview.setSelected( true );


        //캘린더 일정 동그라미 표시
        myCalendar.setSelectedDate(CalendarDay.today())
        onSettingCalDot()

        //캘린더뷰
        myCalendar.setOnDateChangedListener { widget, date, selected ->
            val year = date.year
            val month = date.month
            val day = date.day
            var strDay = String.format("%d.%02d.%02d", year, month+1, day)
            sqlDB = dbManager.writableDatabase
            // 마지막 쿼리 '1'은 날짜 선택 여부를 의미함('1'은 선택,'0'은 미선택(default))
            sqlDB.execSQL("INSERT INTO calTBL VALUES ('" + strDay + "', 'null', '1');")
            sqlDB.close()
            // Frag04로 이동
            (activity as MainActivity).replaceFragment(Frag04())
        }

        //firebase,firestore 접근
        fireEmail = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //현재 유저 uid 가져오기
        val store_uid = fireEmail?.uid

        //회원정보 내용
        val docRef = firestore?.collection("member")?.document(store_uid.toString())
        docRef?.get()?.addOnSuccessListener {

            //member객체에 회원정보 내용 저장 후 그중에 이름 띄우기
            val member = it.toObject(Member::class.java)
            userID.setText(member?.storeName)

            //member객체에서 그룹코드 받아오기
            val groupCode = member?.storeGroup

            // 공지가 디비에 이미 담겨있는지 구별하기 위한 변수
            var flag2 = 0

            //그룹코드 통해서 문서 접근
            val docRef1 = firestore?.collection("group")?.document(groupCode.toString())
            docRef1?.get()?.addOnSuccessListener {
                //객체로 저장 후 내용 띄우기
                val group_m = it.toObject(group_m::class.java)
                noticeview.setText(group_m?.storeContent)

                // 리스트에 추가
                sqlDB = dbManager.writableDatabase
                val noticeDB = group_m?.storeName+ " : "+group_m?.storeContent
                // 공지가 빈칸이 아닐 경우에 디비에 담음
                if(group_m?.storeContent.toString() != "") {
                    // noticeTBL에 있는 notice 내용을 모두 가져옴
                    var cursor: Cursor
                    cursor = sqlDB.rawQuery("SELECT notice FROM noticeTBL;", null)
                    while (cursor.moveToNext()) {
                        var getNotice = cursor.getString(0)
                        var idx = getNotice.indexOf(":")
                        var getNotice_idx = getNotice.substring(idx+2)

                        // 이미 디비에 담긴 공지는 flag2를 1로 설정함
                        if (group_m?.storeContent.toString()== getNotice_idx) {
                            flag2 = 1
                        }
                    }
                    // 디비 안에 없는 공지라면 insert
                    if (flag2 == 0){
                        sqlDB.execSQL("INSERT INTO noticeTBL VALUES ('" + noticeDB + "');")
                    }
                    cursor.close()
                    sqlDB.close()
                }
            }
        }

        // 어댑터 연결
        adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_multiple_choice,     // 다중 선택 박스
            listItem
        )
        todoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        todoListView.setAdapter(adapter)

        // db에서 리스트뷰(투두리스트) 데이터 로드
        sqlDB = dbManager.readableDatabase

        var cursor: Cursor
        var cnt = 0
        // todolistTBL에서 내용과 체크된 여부 모두 가지고 옴
        cursor = sqlDB.rawQuery("SELECT * FROM todolistTBL;", null)
        while (cursor.moveToNext()){
            getItemString = cursor.getString(0)
            getCheckedItem = cursor.getString(1)
            listItem.add(getItemString)
            // db에서 체크 상태도 가져와서 반영
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
            onSettingTodolist()
        }

        // 에디티 버튼 클릭 리스너
        listEdit.setOnClickListener {

            // TodoList 편집 액티비티로 전환
            (activity as MainActivity).replaceFragment(TodoList())
        }

        //유저 페이지로 이동
        // 하단 네비게이션 메뉴 fragment의 framelayout과 서랍 메뉴 drawerlayout의 동시 사용이 어려워
        // 서랍 메뉴를 메인 액티비티에서 실행하도록 구현
        userPage.setOnClickListener {
            var flag1 = 1
            val intent = Intent(getActivity(), MainActivity::class.java)
            intent.putExtra("flag", flag1)
            startActivity(intent)
        }
        return view
    }

    // 프로그레스바 퍼센테이지를 설정함
    private fun onSettingProgress() {
        var checkInt = 0.0
        checkedItems = todoListView.getCheckedItemPositions()

        for (i in count - 1 downTo 0) {
            if (checkedItems.get(i)) {
                checkInt++
            }
        }
        // 전체 항목 대비 체크된 항목의 비율을 퍼센테이지로 설정
        var percentInt = ((checkInt / count.toDouble())*100)
        circleProgressBar.setProgress(percentInt.toFloat(), true)
    }

    // 투두리스트 항목들의 체크 여부 db에 저장함
    private fun onSettingTodolist() {
        checkedItems = todoListView.getCheckedItemPositions()
        for (i in count - 1 downTo 0) {
            sqlDB = dbManager.writableDatabase
            // 모든 리스트 뷰의 체크 박스를 UNCHECKED로 초기화함
            sqlDB.execSQL(
                "UPDATE todolistTBL SET checked='UNCHECKED' WHERE item ='" + listItem.get(
                    i
                ) + "';"
            )
            // 체크된 리스트만 CHECKED로 수정
            if (checkedItems.get(i)) {
                sqlDB.execSQL(
                    "UPDATE todolistTBL SET checked='CHECKED' WHERE item ='" + listItem.get(
                        i) + "';"
                )
            }
            sqlDB.close()
        }
    }
    // 달력에 일정 표시하기
    private fun onSettingCalDot() {
        sqlDB = dbManager.readableDatabase
        var cursor: Cursor
        // calTBL 테이블에 있는 모든 date를 가져와서 달력에 점을 표시함 (등록된 날짜라면 일정표시하도록)
        cursor = sqlDB.rawQuery("SELECT date FROM calTBL;", null)
        while (cursor.moveToNext()) {
            val selDate = cursor.getString(0)
            val selY = selDate.substring(0, 4).toInt()
            val selM = selDate.substring(5, 7).toInt() - 1
            val selD = selDate.substring(8, 10).toInt()
            // 점 표시
            myCalendar.addDecorator(EventDecorator(
                Color.parseColor("#F093BD"),
                Collections.singleton(CalendarDay.from(selY, selM, selD))))
        }
        cursor.close()
        sqlDB.close()
    }

    //frag01에서 뒤로가기 버튼 누르면 frag01로 이동
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).replaceFragment(Frag01())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    //뒤로가기 버튼 필요 메소드
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}


