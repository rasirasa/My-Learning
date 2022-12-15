package com.ssrdi.co.id.myradboox.fragmentreseller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.ssrdi.co.id.myradboox.R
import kotlinx.android.synthetic.main.fragment_session.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SessionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SessionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // cara ambil nav controller
        val navController = navHostFragment.navController

//        navController.navigate(R.id.action_generateFragment_to_homeFragment)

//        btnSubmit.setOnClickListener {
//            // exitAnim = waktu pindah ke fragment tujuan
//            // enterAnim = waktu sampai di fragment tujuan
//            // popupExitAnim = waktu back dari fragment
//            // popupEnterAnim = waktu sampai di fragment sebelumnya
//
//            //pengecekan dari input user
//            if (edInput.text.toString().isNotEmpty()) {
//
//                //kalau ngk kosong
//                val input = edInput.text.toString()
//                val bundle = Bundle()
//                bundle.putString("args", input)
//
//               navController.navigate(R.id.action_generateFragment_to_homeFragment)
//                nav.navigate(R.id.action_sessionFragment_to_historyFragment, bundle)
//            }
//        }
    }
}