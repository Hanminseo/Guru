package com.example.swu_2

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dinuscxj.progressbar.CircleProgressBar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.collection.LLRBNode
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
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
    lateinit var myCalendar: MaterialCalendarView
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
        dbManager = DBManager(context?.applicationContext, "itemDB", null, 1)

        noticeview = view.findViewById(R.id.noticeview)

        //textView(공지) 계속 흐르도록
        noticeview.setSelected( true );

        //캘린더 동그라미 표시
        myCalendar.setSelectedDate(CalendarDay.today())
        onSettingCalDot()

        //캘린더뷰
        myCalendar.setOnDateChangedListener { widget, date, selected ->
            val year = date.year
            val month = date.month
            val day = date.day
            var strDay = String.format("%d.%02d.%02d", year, month+1, day)
            sqlDB = dbManager.writableDatabase
            // 마지막 쿼리 '1'은 선택 여부를 의미/ '1'은 선택,'0'은 미선택(default)
            sqlDB.execSQL("INSERT INTO calTBL VALUES ('" + strDay + "', 'null', '1');")
            sqlDB.close()
            (activity as MainActivity).replaceFragment(Frag04())
            //myCalendar.addDecorator(EventDecorator(Color.RED, Collections.singleton(CalendarDay.today())))
        }

        //firebase,firestore 접근
        fireEmail = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //현재 유저 uid 가져오기
        val store_uid = fireEmail?.uid

        //회원정보 내용
        val docRef = firestore?.collection("member")?.document(store_uid.toString())
        docRef?.get()?.addOnSuccessListener {

            //member객체에 회원정보 내용 저장 후 이름 띄우기
            val member = it.toObject(Member::class.java)
            userID.setText(member?.storeName)

            //member객체에서 그룹코드 받아오기
            val groupCode = member?.storeGroup

        //    var groupInfo = group_m()


            //그룹코드 통해서 공지 가져오기
            val docRef1 = firestore?.collection("group")?.document(groupCode.toString())
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
            onSettingTodolist()
        }

        // 에디티 버튼 클릭 리스너
        listEdit.setOnClickListener {

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

    private fun onSettingTodolist() {
        // db에 리스트뷰 체크된 항목 여부 저장하기
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
                        i
                    ) + "';"
                )
            }
            sqlDB.close()
        }
    }
    // 달력에 일정 표시하기
    private fun onSettingCalDot() {
        sqlDB = dbManager.readableDatabase
        var cursor: Cursor
        // calTBL 테이블에 있는 모든 행의 date를 가져와서 달력에 점을 표시함
        cursor = sqlDB.rawQuery("SELECT date FROM calTBL;", null)
        while (cursor.moveToNext()) {
            val selDate = cursor.getString(0)
            val selY = selDate.substring(0, 4).toInt()
            val selM = selDate.substring(5, 7).toInt() - 1
            val selD = selDate.substring(8, 10).toInt()
            // 점 표시+ 점 색이 이상해~
            myCalendar.addDecorator(EventDecorator(R.color.purple_200,
                Collections.singleton(CalendarDay.from(selY, selM, selD))))
        }
        cursor.close()
        sqlDB.close()

    }

}


