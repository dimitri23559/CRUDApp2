package com.example.crudapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MahasiswaAdapter (val mCtx : Context,val layoutResId: Int,val mhsList:List<Mahasiswa>):ArrayAdapter<Mahasiswa>
    (mCtx,layoutResId,mhsList)
{
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
//
//        val view : View = LayoutInflater.inflate(layoutResId, null)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(layoutResId, null)

        val tvNama: TextView = view.findViewById(R.id.tv_nama)
        val tvAbsen: TextView = view.findViewById(R.id.tv_absen)
        val tvEdit : TextView = view.findViewById(R.id.tv_edit)



        val mahasiswa = mhsList[position]

        tvEdit.setOnClickListener{
            showUpdateDialog(mahasiswa)
        }

        tvNama.text = mahasiswa.nama
        tvAbsen.text = mahasiswa.absen

        return view
    }

    fun showUpdateDialog(mahasiswa: Mahasiswa) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("edit")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update_dialog,null)

        val et_Nama = view.findViewById<EditText>(R.id.et_nama)
        val et_Absen = view.findViewById<EditText>(R.id.tv_absen)

        et_Nama.setText(mahasiswa.nama)
        et_Absen.setText(mahasiswa.absen)



        builder.setView(view)

        builder.setPositiveButton("update"){p0,p1 ->
            val dbMhs = FirebaseDatabase.getInstance().getReference("mahasiswa")

            val nama = et_Nama.text.toString().trim()
            val absen = et_Absen.text.toString().trim()
            if (nama.isEmpty()){
                et_Nama.error = "isi nama"
                et_Nama.requestFocus()
                return@setPositiveButton
            }
            if (absen.isEmpty()){
                et_Absen.error = "isi data"
                et_Absen.requestFocus()
                return@setPositiveButton
            }
            val mahasiswa = Mahasiswa(mahasiswa.id,nama,absen)

            dbMhs.child(mahasiswa.id).setValue(mahasiswa)

            Toast.makeText(mCtx,"data berhasil di update",Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton("no"){p0,p1 ->

        }

        val alert = builder.create()
        alert.show()
    }
}