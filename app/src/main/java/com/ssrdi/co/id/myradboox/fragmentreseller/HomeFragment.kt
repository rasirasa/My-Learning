package com.ssrdi.co.id.myradboox.fragmentreseller


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.databinding.ActivityMainBinding
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.android.synthetic.main.fragment_home.*



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var nav : NavController
    val dropdownList = arrayOf("Option A","Option B", "Option C")
    private lateinit var binding:ActivityMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("Option  1", "Option 2", "Option 3")

        val adapter = ArrayAdapter(this,R.layout.list_item, items)
        binding.dropdown_field.setAdapter(adapter)

//        nav = Navigation.findNavController(view)
//        val spinner:Spinner= view.findViewById(com.ssrdi.co.id.myradboox.R.id.spinner_created)
//        val adapter = ArrayAdapter.createFromResource(this,
//            R.array.spinnerlist, android.R.layout.simple_spinner_item)
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        // Apply the adapter to the spinner
//        spinner.adapter = adapter


//        val spinner:Spinner= view.findViewById(com.ssrdi.co.id.myradboox.R.id.spinner_created)
//
//        if (spinner != null) {
//            val adapter = ArrayAdapter(this,
//                android.R.layout.simple_spinner_item, languages)
//            spinner.adapter = adapter
//
//            spinner.onItemSelectedListener = object :
//                AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>,
//                                            view: View, position: Int, id: Long) {
//                    Toast.makeText(this@HomeFragment,
//                        getString(com.ssrdi.co.id.myradboox.R.string.selected_item) + " " +
//                                "" + languages[position], Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>) {
//                    // write code to perform some action
//                }
//            }
//        }
//        btnAction.setOnClickListener {
//            // // idnya bisa dilihat pada mode Text nav_graph.xml
//            nav.navigate(R.id.action_homeFragment_to_sessionFragment3)
//        }
    }

    private fun setContentView(root: ConstraintLayout) {

    }


}


