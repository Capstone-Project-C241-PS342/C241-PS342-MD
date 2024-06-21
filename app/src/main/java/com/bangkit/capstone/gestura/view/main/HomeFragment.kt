package com.bangkit.capstone.gestura.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.gestura.R
import com.bangkit.capstone.gestura.adapter.LearningMediaAdapter
import com.bangkit.capstone.gestura.data.pref.UserPreference
import com.bangkit.capstone.gestura.data.pref.dataStore
import com.bangkit.capstone.gestura.databinding.FragmentHomeBinding
import com.bangkit.capstone.gestura.remote.ApiConfig
import com.bangkit.capstone.gestura.view.ViewModelFactory
import com.bangkit.capstone.gestura.view.welcome.WelcomeActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.gestura.data.pref.UserModel
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LearningMediaAdapter
    private lateinit var userPreference: UserPreference

//    private val adapter by lazy {
//        UserAdapter {user ->
//            Intent(this, DetailActivity::class.java).apply {
//                putExtra("item", user)
//                startActivity(this)
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        fetchLearningMedia()


        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                requireActivity().finish()
            } else { }
        }

        binding.tvUsername.text = "Teman Dengar"
        binding.tvSelamat.text = greetingmessage()
        binding.ivProfile.setImageResource(R.drawable.ic_placeholder)

        binding.cardCover.setImageResource(R.drawable.thumbnail)
        binding.cardTitle.text = "Bahasa Isyarat Indonesia (BISINDO) \"ALPHABET\""
        binding.cardDesc.text = "Hai, saya terlahir Tuli, dan suka berbagi edukasi tentang Tuli, Dunia Tuli, Budaya Tuli dan Bahasa Isyarat"
        binding.buttoncard.setOnClickListener {
            val youtubeUrl = "https://www.youtube.com/watch?v=96j5Uv3rM0A"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))

            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "No application found to open YouTube link", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cardCover1.setImageResource(R.drawable.thumbnail1)
        binding.cardTitle1.text = "ABJAD JARI BISINDO"
        binding.cardDesc1.text = ""
        binding.buttoncard1.setOnClickListener {
            val youtubeUrl = "https://www.youtube.com/watch?v=Py6Ch1vBvL0"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))

            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "No application found to open YouTube link", Toast.LENGTH_SHORT).show()
            }
        }



        playAnimation()
    }



    private val _userProfile = MutableLiveData<UserModel>()



    private fun fetchLearningMedia() {
        lifecycleScope.launch {
            userPreference.getToken().collect { token ->
                if (token != null) {
                    try {
                        val response = ApiConfig.getApiService("Bearer $token").getLearningMedia()
                        adapter = LearningMediaAdapter(response)
                        recyclerView.adapter = adapter
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else { }
            }
        }
    }

    private fun greetingmessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Selamat Pagi,"
            in 12..15 -> "Selamat Siang,"
            in 16..18 -> "Selamat Sore,"
            else -> "Selamat Malam,"
        }
    }




    private fun playAnimation() {
        val name = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.tvSelamat, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(name, message)
            startDelay = 100
        }.start()
    }
}
