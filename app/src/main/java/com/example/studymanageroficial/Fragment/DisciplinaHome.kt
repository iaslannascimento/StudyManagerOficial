package com.example.studymanageroficial.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room

import com.example.studymanageroficial.R
import com.example.studymanageroficial.adpters.AdpterDisciplina
import com.example.studymanageroficial.adpters.MyRecyclerViewClickListener
import com.example.studymanageroficial.conect.Conexao
import com.example.studymanageroficial.modelo.Disciplina
import com.example.studymanageroficial.shared.SecurityPreferences
import com.example.studymanageroficial.viewDisciplina.CadastroDisciplina
import com.example.studymanageroficial.viewDisciplina.DisciplinaDetalhada
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_disciplina_home.*

class DisciplinaHome : Fragment() {
    val conexao: Conexao by lazy{
        Room.databaseBuilder(context!!, Conexao::class.java,"DBstudyManager")
            .allowMainThreadQueries().build()
    }
    private lateinit var sharedPreferences:SecurityPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disciplina_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cadastroDisciplinas.setOnClickListener {
            startActivity(Intent(context,CadastroDisciplina::class.java))
        }

        sharedPreferences = SecurityPreferences(context!!)
        var user = sharedPreferences.getPreferences("LoginUser")
        var adpter = AdpterDisciplina(context!!,conexao.DisciplinaDAO().listDisciplinasUsers(user))
        recyclerDisciplina.adapter = adpter

        val layout = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        recyclerDisciplina.layoutManager = layout

    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = SecurityPreferences(context!!)
        var user = sharedPreferences.getPreferences("LoginUser")
        var adpter = AdpterDisciplina(context!!,conexao.DisciplinaDAO().listDisciplinasUsers(user))
        recyclerDisciplina.adapter = adpter

        var listaDisciplinas:MutableList<Disciplina> = conexao.DisciplinaDAO().listDisciplinasUsers(user)

        val layout = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        recyclerDisciplina.layoutManager = layout

        recyclerDisciplina.addOnItemTouchListener(
            MyRecyclerViewClickListener(
                this@DisciplinaHome.context!!,
                recyclerDisciplina,
                object : MyRecyclerViewClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        var i = Intent(context,DisciplinaDetalhada::class.java)
                        i.putExtra("id",position)
                        startActivity(i)
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                        val removida = listaDisciplinas[position]
                        listaDisciplinas.remove(removida)

                        conexao.DisciplinaDAO().deletar(removida)

                        recyclerDisciplina.adapter = AdpterDisciplina(context!!,conexao.DisciplinaDAO().listDisciplinasUsers(user))

                        recyclerDisciplina.adapter!!.notifyItemRemoved(position)

                        val snack = Snackbar.make(
                            recyclerDisciplina.parent as View,"Apagando... ",Snackbar.LENGTH_LONG )
                            .setAction("Cancelar") {
                                listaDisciplinas.add(position, removida)
                                conexao.DisciplinaDAO().inserir(removida)
                                recyclerDisciplina.adapter = AdpterDisciplina(context!!,conexao.DisciplinaDAO().listDisciplinasUsers(user))
                                recyclerDisciplina.adapter!!.notifyItemInserted(position)
                            }
                        snack.show()
                    }
                })
        )

        recyclerDisciplina.itemAnimator = DefaultItemAnimator()
        //recyclerview.itemAnimator = LandingAnimator()
        //recyclerview.itemAnimator = FlipInTopXAnimator()
    }
}



