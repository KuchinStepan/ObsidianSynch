package com.example.synchronizer.zip

import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipMaker {
    companion object {
        private fun createZipArchiveRecursive(directory: File, rootLength: Int, zos: ZipOutputStream) {
            val files = directory.listFiles()

            files?.forEach { file ->
                if (file.isFile) {
                    val entryName = file.absolutePath.substring(rootLength + 1)
                    val zipEntry = ZipEntry(entryName)
                    zos.putNextEntry(zipEntry)

                    val fileInputStream = file.inputStream()
                    fileInputStream.copyTo(zos)

                    fileInputStream.close()
                    zos.closeEntry()
                } else if (file.isDirectory) {
                    createZipArchiveRecursive(file, rootLength, zos) // Вызываем рекурсивно для поддиректорий
                }
            }
        }

         fun createZipArchive() {
            val hardDirectory = "/storage/emulated/0/Documents/Obsidian/First"
            val dir = File(hardDirectory)

            val a = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

            val outF = File(a, "copy.zip" )
            val fos = FileOutputStream(outF)

            val zos = ZipOutputStream(fos)

            createZipArchiveRecursive(dir, dir.path.length, zos)

            zos.close()
            fos.close()
        }
    }
}