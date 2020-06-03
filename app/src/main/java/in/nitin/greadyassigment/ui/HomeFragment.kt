package `in`.nitin.greadyassigment.ui

import `in`.nitin.greadyassigment.R
import `in`.nitin.greadyassigment.databinding.FragmentHomeBinding
import `in`.nitin.greadyassigment.datasource.helper.Result
import `in`.nitin.greadyassigment.datasource.model.Data_
import `in`.nitin.greadyassigment.ui.adapater.ImageAdapter
import `in`.nitin.greadyassigment.ui.utils.ItemDecorationColumns
import `in`.nitin.greadyassigment.ui.utils.snack
import `in`.nitin.greadyassigment.ui.utils.toTransitionGroup
import `in`.nitin.greadyassigment.ui.utils.waitForTransition
import `in`.nitin.greadyassigment.viewmodel.RedditViewModel
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController

class HomeFragment : BaseFragment(), ImageAdapter.ClickListener {


    lateinit var viewModel: RedditViewModel
    lateinit var binding: FragmentHomeBinding
    lateinit var imageAdapter: ImageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        initViews()
        subscribeData()
        return binding.root
    }


    /**
     * initialisation of views
     * */
    private fun initViews() {
        imageAdapter = ImageAdapter(this)
        binding.lifecycleOwner = this
        viewModel =
            ViewModelProvider(this, providerFactory).get(RedditViewModel::class.java)

        binding.contentLayout.rv.apply {
            adapter = imageAdapter
            addItemDecoration(
                ItemDecorationColumns(
                    resources.getInteger(R.integer.list_columns),
                    resources.getDimensionPixelSize(R.dimen.list_spacing),
                    true
                )
            )
        }

        waitForTransition(binding.contentLayout.rv)

    }

    /*
      * data observer
      * */
    private fun subscribeData() {

        /**
         * getting saved post data after configuration changes
         * */
        viewModel.getSavedPostData().observe(viewLifecycleOwner, Observer {
//            if(it!=null){
            imageAdapter.submitList(it)
        })


        viewModel.getPostData().observe(viewLifecycleOwner, Observer {

            when (it.status) {
                Result.Status.LOADING -> {
                }
                Result.Status.SUCCESS -> {
                    imageAdapter.submitList(it.data!!.data!!.children)

                    /*
                    * saving post data to handle when configuration change
                    * */
                    viewModel.savePostInStateHandle(it.data.data!!.children!!)

                }
                Result.Status.ERROR -> {
                    it.message!!.snack(Color.RED, binding.root)

                }
            }

        })
    }

    override fun onItemClick(postData: Data_, imageView: ImageView) {
        val destination =
            HomeFragmentDirections.actionHomeFragmentToImagePreviewFragment(postData.url!!)
        val extras = FragmentNavigatorExtras(
            /**
             * Returns the name of the View to be used to identify Views in Transitions.
             * */
            imageView.toTransitionGroup()
        )

        /**
         * @param extras for shared element transition
         * @param destination to open [ImagePreviewFragment]
         * */

        findNavController().navigate(destination, extras)
    }
}