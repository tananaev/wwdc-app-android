package com.tananaev.wwdcparties

import android.arch.lifecycle.LifecycleFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.SupportMapFragment
import com.squareup.picasso.Picasso
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng


class PartyDetailFragment : LifecycleFragment() {

    companion object {
        var EXTRA_PARTY = "party"
    }

    private var party: Party? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        party = arguments.getSerializable(EXTRA_PARTY) as Party
        activity.title = party?.title
    }

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.party_detail, container, false)

        Picasso.with(context).load(party?.logo).into(view?.findViewById(R.id.image) as ImageView)

        (view.findViewById(R.id.title) as TextView).text = party?.title
        (view.findViewById(R.id.details) as TextView).text = party?.details

        (view.findViewById(R.id.date) as TextView).text = party?.formatDate()
        (view.findViewById(R.id.time) as TextView).text = party?.formatTime()

        (view.findViewById(R.id.address1) as TextView).text = party?.address1
        (view.findViewById(R.id.address2) as TextView).text = party?.address2 + ", " + party?.address3

        view.findViewById(R.id.calendar).setOnClickListener(listenerCalendar)
        view.findViewById(R.id.location).setOnClickListener(listenerLocation)
        view.findViewById(R.id.mapOverlay).setOnClickListener(listenerLocation)
        view.findViewById(R.id.website).setOnClickListener(listenerWebsite)

        return view
    }

    private val listenerCalendar = View.OnClickListener {
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra(CalendarContract.Events.TITLE, party?.title)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, party?.startDate?.time)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, party?.endDate?.time)
        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, party?.address1)
        startActivity(intent)
    }

    private val listenerLocation = View.OnClickListener {
        val latitude = party?.latitude
        val longitude = party?.longitude
        val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude, $longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private val listenerWebsite = View.OnClickListener {
        val uri = Uri.parse(party?.url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            val location = LatLng(party?.latitude!!, party?.longitude!!)
            map.addMarker(MarkerOptions().position(location))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
        }
    }

}
