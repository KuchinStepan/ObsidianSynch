package com.example.synchronizer

import android.app.Activity
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class MainActivity : ComponentActivity() {
    private val REQUEST_CODE_PICK_DIRECTORY = 123
    private var directory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                22)
        }



        val button = findViewById<Button>(R.id.selectFolderButton)

        button.setOnClickListener {
            val intent = Intent()
                .setAction(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, REQUEST_CODE_PICK_DIRECTORY)
        }

        val buttonZip = findViewById<Button>(R.id.createZipButton)

        buttonZip.setOnClickListener {
            createZipArchive()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)  {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_DIRECTORY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                directory = data.dataString.toString()
                Log.println(Log.INFO,"ASD" ,directory)

                val t = findViewById<TextView>(R.id.textView)
                t.text = directory
            }
        }
    }

//    fun createZipArchive(directory: File, rootLength: Int, zos: ZipOutputStream) {
//        val files = directory.listFiles()
//
//        files?.forEach { file ->
//            if (file.isFile && !file.isHidden) {
//                val entryName = file.absolutePath.substring(rootLength + 1) // сохраняем относительный путь
//                val zipEntry = ZipEntry(entryName)
//                zos.putNextEntry(zipEntry)
//
//                val fileInputStream = file.inputStream()
//                fileInputStream.copyTo(zos)
//
//                fileInputStream.close()
//                zos.closeEntry()
//            } else if (file.isDirectory) {
//                createZipArchive(file, rootLength, zos) // Вызываем рекурсивно для поддиректорий
//            }
//        }
//    }

    fun createZipArchive() {
        val hardDirectory = "/storage/emulated/0/Documents/Obsidian/First"
        val dir = File(hardDirectory)
        val files = dir.listFiles()

        val a = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        val outF = File(a, "/copy.zip" )
        val fos = FileOutputStream(outF)

        val zos = ZipOutputStream(fos)

//        createZipArchive(dir, dir.path.length, zos)

        files.forEach { file ->
            if (file.isFile && !file.isHidden) {
                val zipEntry = ZipEntry(file.name)
                zos.putNextEntry(zipEntry)

                val fileInputStream = file.inputStream()
                fileInputStream.copyTo(zos)

                fileInputStream.close()
                zos.closeEntry()
            }
        }

        zos.close()
        fos.close()
    }
}
