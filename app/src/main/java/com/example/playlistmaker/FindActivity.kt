package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.items.ITunesApiService
import com.example.playlistmaker.items.ITunesResponse
import com.example.playlistmaker.items.Track
import com.example.playlistmaker.items.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FindActivity : AppCompatActivity() {

    private var savedText = ""
    private lateinit var searchHistory: SearchHistory
    private lateinit var adapter: TrackAdapter
    private lateinit var emptyState: LinearLayout
    private lateinit var errorState: LinearLayout
    private lateinit var rvTrackList: RecyclerView
    private lateinit var btnClearHistory: Button // Добавляем переменную для кнопки
    private val iTunesApiService = createITunesApiService()
    private var lastSearchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)
        searchHistory = SearchHistory(this)

        emptyState = findViewById(R.id.empty_state)
        errorState = findViewById(R.id.error_state)
        rvTrackList = findViewById(R.id.rv_track_list)
        btnClearHistory = findViewById(R.id.btn_clearFindHistory) // Инициализация кнопки

        val backButton = findViewById<TextView>(R.id.search_top_bar)
        val searchEditText = findViewById<EditText>(R.id.find_music)
        val clearButton = findViewById<ImageView>(R.id.clear_button)
        val btnReload = findViewById<Button>(R.id.btn_reload)

        adapter = TrackAdapter(emptyList()) { track ->
            searchHistory.addTrack(track)
        }
        rvTrackList.layoutManager = LinearLayoutManager(this)
        rvTrackList.adapter = adapter

        backButton.setOnClickListener {
            finish()
        }

        btnClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            showHistory() // Обновляем отображение после очистки истории
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                savedText = s.toString()

                if (s.isNullOrEmpty()) {
                    showHistory()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch(searchEditText.text.toString())
                hideKeyboard(searchEditText)
                true
            } else {
                false
            }
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            searchEditText.clearFocus()
            hideKeyboard(searchEditText)
            adapter.updateTracks(emptyList())
            showHistory()
        }

        btnReload.setOnClickListener {
            if (lastSearchQuery.isNotEmpty()) {
                performSearch(lastSearchQuery)
            }
        }

        showHistory()
    }

    private fun performSearch(term: String) {
        if (term.isEmpty()) return
        lastSearchQuery = term

        showLoading()
        btnClearHistory.visibility = View.GONE

        iTunesApiService.searchSongs(term).enqueue(object : Callback<ITunesResponse> {
            override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.results?.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                        )
                    } ?: emptyList()

                    if (tracks.isEmpty()) {
                        showEmptyState()
                    } else {
                        showResults(tracks)
                    }
                } else {
                    showErrorState()
                }
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                showErrorState()
            }
        })
    }

    private fun showLoading() {
        rvTrackList.visibility = View.GONE
        emptyState.visibility = View.GONE
        errorState.visibility = View.GONE
        btnClearHistory.visibility = View.GONE
    }

    private fun showHistory() {
        val history = searchHistory.getHistory()
        if (history.isEmpty()) {
            showResults(emptyList())
            btnClearHistory.visibility = View.GONE
        } else {
            adapter.updateTracks(history)
            showResults(history)
            btnClearHistory.visibility = View.VISIBLE
        }
    }

    private fun showResults(tracks: List<Track>) {
        rvTrackList.visibility = View.VISIBLE
        emptyState.visibility = View.GONE
        errorState.visibility = View.GONE
        adapter.updateTracks(tracks)
        btnClearHistory.visibility = View.GONE
    }

    private fun showEmptyState() {
        rvTrackList.visibility = View.GONE
        emptyState.visibility = View.VISIBLE
        errorState.visibility = View.GONE
        btnClearHistory.visibility = View.GONE
    }

    private fun showErrorState() {
        rvTrackList.visibility = View.GONE
        emptyState.visibility = View.GONE
        errorState.visibility = View.VISIBLE
        btnClearHistory.visibility = View.GONE
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun createITunesApiService(): ITunesApiService {
        return Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }
}
