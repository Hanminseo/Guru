package com.example.swu_2


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val context: Context, val timeList: ArrayList<Time>) :
    RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // 리사이클뷰 아이템 항목의 레이아웃을 뷰에 담아 반환
        val view = LayoutInflater.from(context).inflate(R.layout.recycle_item, parent, false)
        return Holder(view)
    }

    // 인수로 전달받은 timeList의 항목의 수를 반환
    override fun getItemCount(): Int {
        return timeList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(timeList[position], context)
    }

    // 아이템 추가 메소드
    fun addItem(list: Time) {
        timeList.add(list)
        notifyDataSetChanged()
    }

    // 아이템 삭제 메소드
    fun removeItem(pos: Int) {
        timeList.removeAt(pos)

        notifyItemRemoved(pos)
        notifyDataSetChanged()
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        // 아이디 찾아서 연결
        val PhotoImg = itemView?.findViewById<ImageView>(R.id.PhotoImg)
        val subjectName = itemView?.findViewById<TextView>(R.id.subjectName)
        val studyTime = itemView?.findViewById<TextView>(R.id.studyTime)
        lateinit var dbManager: DBManager
        lateinit var sqlDB: SQLiteDatabase

        fun bind(Time: Time, context: Context) {
            // 포토 지정
            PhotoImg?.setImageResource(R.mipmap.ic_launcher_round)

            // 텍스트뷰에 String 값 전달
            subjectName?.text = Time.subject
            studyTime?.text = Time.time

            // 아이템 길게 클릭 시 삭제 메소드 호출
            itemView.setOnLongClickListener {
                // 취소 확인 팝업 메세지
                val builder = AlertDialog.Builder(context)
                builder.setMessage("${Time.subject}을(를) 삭제하시겠습니까?")
                builder.setPositiveButton(
                    "삭제"
                ) { dialogInterface: DialogInterface?, i: Int ->
                    // DB에서 제거
                    dbManager = DBManager(context?.applicationContext, "itemDB", null, 1)
                    sqlDB = dbManager.writableDatabase
                    sqlDB.execSQL("DELETE FROM stopwatchTBL WHERE subject='" + Time.subject + "';")
                    sqlDB.close()
                    // 리스트에서 제거
                    removeItem(adapterPosition)
                }
                builder.setNegativeButton("취소", null)
                builder?.show()

                true
            }
        }


    }
}