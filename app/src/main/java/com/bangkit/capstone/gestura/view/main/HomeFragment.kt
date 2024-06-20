package com.bangkit.capstone.gestura.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.rv_learningmedia)
        recyclerView.layoutManager = LinearLayoutManager(context)    //adapternya belum connect
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        fetchLearningMedia()


//        binding.rv_learningmedia.layoutManager = LinearLayoutManager(this)
//        binding.rv_learningmedia.adapter = adapter
//        binding.rv_learningmedia.setHasFixedSize(true)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
                Log.d("UserInfo", "Username: ${user.username}, Profile Picture URL: ${user.profile_picture_url}")
                binding.tvUsername.text = user.username
                binding.tvSelamat.text = greetingmessage()
                Glide.with(this)
                    .load(user.profile_picture_url)
                    .error(R.drawable.ic_placeholder)
                    .into(binding.ivProfile)
                viewModel.fetchUserProfile(user.token)
            }
        }




//        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
//            binding.tvUsername.text =
//            Glide.with(this)
//                .load(userProfile.profilePictureUrl)
//                .into(binding.ivProfile)
//        }

        setupAction()
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


    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            requireActivity().finish()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivProfile, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val name = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.tvSelamat, View.ALPHA, 1f).setDuration(100)
        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(name, message, logout)
            startDelay = 100
        }.start()
    }
}
