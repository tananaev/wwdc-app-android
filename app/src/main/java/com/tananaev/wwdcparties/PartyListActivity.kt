package com.tananaev.wwdcparties

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.util.Locale

class PartyListActivity : LifecycleActivity() {

    private var viewModel: PartyListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_list)

        viewModel = ViewModelProviders.of(this)[PartyListViewModel::class.java]

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = title

        viewModel?.users?.observe(this, Observer { users ->
            if (users != null) {
                (findViewById(R.id.party_list) as RecyclerView).adapter = PartyListAdapter(users)
            }
        })
    }

    inner class PartyListAdapter(private val items: List<Party>) : RecyclerView.Adapter<PartyListAdapter.ViewHolder>() {

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            var header = view.findViewById(R.id.header) as TextView
            val icon = view.findViewById(R.id.icon) as ImageView
            val name = view.findViewById(R.id.name) as TextView
            val time = view.findViewById(R.id.time) as TextView
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
