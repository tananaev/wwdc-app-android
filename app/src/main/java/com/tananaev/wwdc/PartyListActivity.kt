package com.tananaev.wwdc

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class PartyListActivity : FragmentActivity() {

    private var viewModel: PartyListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_list)

        viewModel = ViewModelProviders.of(this)[PartyListViewModel::class.java]

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = title

        viewModel?.users?.observe(this, Observer { users ->
            if (users != null) {
                findViewById<RecyclerView>(R.id.party_list).adapter = PartyListAdapter(users)
            }
        })
    }

    inner class PartyListAdapter(private val items: List<Party>) : RecyclerView.Adapter<PartyListAdapter.ViewHolder>() {

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            var header = view.findViewById<TextView>(R.id.header)
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
                holder.header.visibility = View.VISIBLE
                holder.header.text = date
            } else {
                holder.header.visibility = View.GONE
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
