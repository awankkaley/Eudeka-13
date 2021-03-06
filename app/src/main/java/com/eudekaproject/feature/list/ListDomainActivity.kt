package com.eudekaproject.feature.list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.eudekaproject.DomainNavigator
import com.eudekaproject.Injection
import com.eudekaproject.R
import com.eudekaproject.adapter.DomainAdapter
import com.eudekaproject.DomainViewModel
import com.eudekaproject.feature.detail.DomainDetailActivity
import com.eudekaproject.model.DomainsItem
import com.eudekaproject.utils.gone
import com.eudekaproject.utils.invisible
import com.eudekaproject.utils.visible
import kotlinx.android.synthetic.main.activity_list_domain.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast

class ListDomainActivity : AppCompatActivity(), DomainNavigator {

    lateinit var domainViewModel: DomainViewModel
    lateinit var adapter: DomainAdapter
    var data: MutableList<DomainsItem?> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_domain)
        val key = intent.getStringExtra("key")

        supportActionBar?.title = resources.getString(R.string.result) + " " + key
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViewModel()
        domainViewModel.getListTeam(key)

        domain_swipe_refresh_id.onRefresh {
            domainViewModel.getListTeam(key)
        }


    }

    override fun loadListDomain(listDomain: List<DomainsItem?>) {
        domain_swipe_refresh_id.isRefreshing = false
        data.clear()
        data.addAll(listDomain)
        initAdapter()
    }

    override fun errorLoadListDomain(message: String) {
        domain_swipe_refresh_id.isRefreshing = false
        toast(message)
        rto_layout.visible()
    }

    override fun showProgress() {
        domain_progress_id.visible()
        domain_rv_id.invisible()
        domain_swipe_refresh_id.invisible()
    }

    override fun hideProgress() {
        domain_progress_id.gone()
        domain_rv_id.visible()
        domain_swipe_refresh_id.visible()
    }

    private fun initViewModel() {
        domainViewModel = DomainViewModel(Injection.provideDomainRepositorycontext(this))
        domainViewModel.setNavigator(this)
    }

    private fun initAdapter() {
        adapter = DomainAdapter(this, data) {
            startActivity(
                intentFor<DomainDetailActivity>(
                    "domain" to it?.domain.toString(),
                    "isdead" to it?.isDead.toString(),
                    "a" to it?.A.toString(),
                    "apps" to it?.apps.toString(),
                    "cname" to it?.cNAME.toString(),
                    "country" to it?.country.toString(),
                    "createdate" to it?.createDate.toString(),
                    "expirydate" to it?.expiryDate.toString(),
                    "ns" to it?.nS.toString(),
                    "parser" to it?.parser.toString(),
                    "parsererror" to it?.parserError.toString(),
                    "resolvable" to it?.resolvable.toString(),
                    "robotstxt" to it?.robotsTxt.toString(),
                    "suffix" to it?.suffix.toString(),
                    "txt" to it?.tXT.toString(),
                    "updatedate" to it?.updateDate.toString(),
                    "url" to it?.url.toString()
                )
            )
        }
        domain_rv_id.layoutManager = LinearLayoutManager(this)
        domain_rv_id.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
