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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.items.TrackAdapter


class FindActivity : AppCompatActivity() {

    private lateinit var viewModel: FindViewModel
    private lateinit var adapter: TrackAdapter
    private lateinit var emptyState: LinearLayout
    private lateinit var errorState: LinearLayout
    private lateinit var rvTrackList: RecyclerView
    private lateinit var btnClearHistory: Button
    private lateinit var tvHeader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        viewModel = ViewModelProvider(this).get(FindViewModel::class.java)
        viewModel.initSearchHistory(this)

        emptyState = findViewById(R.id.empty_state)
        errorState = findViewById(R.id.error_state)
        rvTrackList = findViewById(R.id.rv_track_list)
        btnClearHistory = findViewById(R.id.btn_clearFindHistory)
        tvHeader = findViewById(R.id.header_text)

        adapter = TrackAdapter(emptyList()) { track ->
            viewModel.addTrackToHistory(track)
            //viewModel.getSearchHistory()
        }
        rvTrackList.layoutManager = LinearLayoutManager(this)
        rvTrackList.adapter = adapter

        val backButton = findViewById<TextView>(R.id.search_top_bar)
        val searchEditText = findViewById<EditText>(R.id.find_music)
        val clearButton = findViewById<ImageView>(R.id.clear_button)
        val btnReload = findViewById<Button>(R.id.btn_reload)

        backButton.setOnClickListener {
            finish()
        }

        btnClearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                if (s.isNullOrEmpty()) {
                    viewModel.getSearchHistory()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchSongs(searchEditText.text.toString())
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
            viewModel.getSearchHistory()
        }

        btnReload.setOnClickListener {
            viewModel.searchSongs(searchEditText.text.toString())
        }

        observeViewModel()
        viewModel.getSearchHistory()
    }

    private fun observeViewModel() {
        viewModel.tracks.observe(this) { tracks ->
            tracks?.let { adapter.updateTracks(it) }
        }

        viewModel.state.observe(this) { state ->
            when (state) {
                FindViewModel.FindState.Loading -> showLoadingState()
                FindViewModel.FindState.Success -> showResultsState()
                FindViewModel.FindState.Empty -> showEmptyState()
                FindViewModel.FindState.Error -> showErrorState()
                FindViewModel.FindState.Hystory -> showHistoryState()
            }
        }
    }

    private fun showHistoryState() {
        rvTrackList.visibility = View.VISIBLE
        emptyState.visibility = View.GONE
        errorState.visibility = View.GONE
        btnClearHistory.visibility = View.VISIBLE
        tvHeader.visibility = View.VISIBLE
    }

    private fun showLoadingState() {
        rvTrackList.visibility = View.GONE
        emptyState.visibility = View.GONE
        errorState.visibility = View.GONE
        btnClearHistory.visibility = View.GONE
        tvHeader.visibility = View.GONE
    }

    private fun showResultsState() {
        rvTrackList.visibility = View.VISIBLE
        emptyState.visibility = View.GONE
        errorState.visibility = View.GONE
        btnClearHistory.visibility = View.GONE
        tvHeader.visibility = View.GONE
    }

    private fun showEmptyState() {
        rvTrackList.visibility = View.GONE
        emptyState.visibility = View.VISIBLE
        errorState.visibility = View.GONE
        btnClearHistory.visibility = View.GONE
        tvHeader.visibility = View.GONE
    }

    private fun showErrorState() {
        rvTrackList.visibility = View.GONE
        emptyState.visibility = View.GONE
        errorState.visibility = View.VISIBLE
        btnClearHistory.visibility = View.GONE
        tvHeader.visibility = View.GONE
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
