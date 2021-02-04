package com.example.swu_2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import java.util.*

class Frag03 : Fragment(){

    lateinit var allcon : ImageButton
    lateinit var specup : ImageButton
    lateinit var linkareer : ImageButton
    lateinit var wevity : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag03, container, false)
    }

    //frag에서 뒤로가기 버튼
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.remove(this@Frag03)?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        allcon = getView()?.findViewById(R.id.allcon)!!
        specup = getView()?.findViewById(R.id.specup)!!
        linkareer = getView()?.findViewById(R.id.linkareer)!!
        wevity = getView()?.findViewById(R.id.wevity)!!

        allcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.all-con.co.kr"))
            startActivity(intent)
        }

        specup.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com/specup"))
            startActivity(intent)
        }

        linkareer.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkareer.com/"))
            startActivity(intent)
        }

        wevity.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wevity.com/"))
            startActivity(intent)
        }
        super.onActivityCreated(savedInstanceState)

    }
}