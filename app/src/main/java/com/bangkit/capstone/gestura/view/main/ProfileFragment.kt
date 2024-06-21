import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.capstone.gestura.R
import com.bangkit.capstone.gestura.databinding.FragmentProfileBinding
import com.bangkit.capstone.gestura.view.ViewModelFactory
import com.bangkit.capstone.gestura.view.main.MainViewModel
import com.bangkit.capstone.gestura.view.welcome.WelcomeActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentProfileBinding // atau sesuai binding yang Anda gunakan

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Contoh URL gambar profil, bisa diganti dengan URL asli dari user
        val profilePictureUrl: String? = getUserProfilePictureUrl() // Method yang mengembalikan URL gambar profil user

        binding.ivprofile.setImageResource(R.drawable.ic_placeholder)
        binding.profile.text = "Teman Dengar"
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(), WelcomeActivity::class.java)
            startActivity(intent)
        }

    }




    private fun getUserProfilePictureUrl(): String? {
        // Return URL profile picture user, bisa null atau string URL
        return null // Misalnya, untuk testing
    }
}
