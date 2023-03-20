package com.example.androidcontentprovider

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.contentValuesOf
import com.example.androidcontentprovider.databinding.ActivityMainBinding
import java.lang.reflect.Array.getDouble

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var bookId: String? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(this, "BookStore.db", 2)
        binding.createDatabaseButton.setOnClickListener {
            dbHelper.writableDatabase
        }
        binding.addDataButton.setOnClickListener {
            // add data
            val uri = Uri.parse("content://com.example.androidcontentprovider.provider/book")
            val values = contentValuesOf(
                "name" to "A Clash of Kings",
                "author" to "George Martin", "pages" to 1040, "price" to 22.85
            )
            val newUri = contentResolver.insert(uri, values)
            bookId = newUri?.pathSegments?.get(1)
        }
        binding.queryDataButton.setOnClickListener {
            // query data
            val uri = Uri.parse("content://com.example.androidcontentprovider.provider/book")
            contentResolver.query(uri, null, null, null, null)?.apply {
                while (moveToNext()) {
                    val name = getString(getColumnIndex("name"))
                    val author = getString(getColumnIndex("author"))
                    val pages = getInt(getColumnIndex("pages"))
                    val price = getDouble(getColumnIndex("price"))
                    Log.d("MainActivity", "book name is $name")
                    Log.d("MainActivity", "book author is $author")
                    Log.d("MainActivity", "book pages is $pages")
                    Log.d("MainActivity", "book price is $price")
                }
                close()
            }
        }
    }
}