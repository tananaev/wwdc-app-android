package com.tananaev.wwdc.parties

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tananaev.wwdc.R

class PartyListActivity : AppCompatActivity() {

    private lateinit var viewModel: PartyListViewModel
    private var refreshItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_list)

        viewModel = ViewModelProviders.of(this)[PartyListViewModel::class.java]

        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel.parties.observe(this, Observer { parties ->
            if (parties != null) {
                refreshItem?.isEnabled = true
                refreshItem?.icon?.alpha = 0xff
                findViewById<RecyclerView>(R.id.party_list).adapter = PartyListAdapter(parties)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        refreshItem = menu?.getItem(0)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.refresh -> {
                item.isEnabled = false
                item.icon.alpha = 0xff / 2
                viewModel.refresh()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    inner class PartyListAdapter(private val items: List<Party>) : RecyclerView.Adapter<PartyListAdapter.ViewHolder>() {

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val divider = view.findViewById<View>(R.id.divider)
            val header = view.findViewById<TextView>(R.id.header)
            val icon = view.findViewById<ImageView>(R.id.icon)
            val name = view.findViewById<TextView>(R.id.name)
            val time = view.findViewById<TextView>(R.id.time)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.party_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]

            val date = item.formatDate()
            if (position == 0 || date != items[position - 1].formatDate()) {
                holder.divider.visibility = View.VISIBLE
                holder.header.visibility = View.VISIBLE
                holder.header.text = date
            } else {
                holder.divider.visibility = View.GONE
                holder.header.visibility = View.GONE
            }

            if (position == 0) {
                holder.divider.visibility = View.GONE
            }

            holder.name.text = item.title
            holder.time.text = item.formatTime()

            Picasso.with(holder.view.context).load(item.icon).into(holder.icon)

            holder.view.setOnClickListener { v ->
                val intent = Intent(v.context, PartyDetailActivity::class.java)
                intent.putExtra(PartyDetailFragment.EXTRA_PARTY, item)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int = items.size

    }

}
