package com.example.crudapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var etNama : EditText
    private lateinit var etAbsen : EditText
    private lateinit var btnSave : Button

    private lateinit var listMhs : ListView
    private lateinit var ref : DatabaseReference
    private lateinit var mhsList: MutableList<Mahasiswa>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference("mahasiswa")

        etNama = findViewById(R.id.et_nama)
        etAbsen = findViewById(R.id.et_absen)
        btnSave = findViewById(R.id.btn_save)

        listMhs = findViewById(R.id.lv_mhs)

        btnSave.setOnClickListener(this)

        mhsList = mutableListOf()

        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0:DataSnapshot) {
                if (p0.exists()){
                    mhsList.clear()
                    for (h in p0.children){
                        val mahasiswa = h.getValue(Mahasiswa::class.java)
                        if (mahasiswa != null) {
                            mhsList.add(mahasiswa)
                        }
                    }
                    val adapter = MahasiswaAdapter(this@MainActivity,R.layout.item_mhs,mhsList)
                    listMhs.adapter = adapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData(){
        val nama = etNama.text.toString().trim()
        val absen = etAbsen.text.toString().trim()

        if(nama.isEmpty()){
            etNama.error = "isi nama!"
            return
        }
        if (absen.isEmpty()){
            etAbsen.error = "isi data!"
            return
        }


        val mhsId = ref.push().key

        val mhs = Mahasiswa(mhsId!!,nama,absen)

        if (mhsId !=null){
            ref.child(mhsId).setValue(mhs).addOnCompleteListener{
                Toast.makeText(applicationContext,"data berhasil Di tambahkan",Toast.LENGTH_SHORT).show()
            }
        }




    }
}