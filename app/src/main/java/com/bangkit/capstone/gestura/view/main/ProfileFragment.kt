import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.capstone.gestura.R
import com.bangkit.capstone.gestura.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProfileFragment : Fragment() {

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

        // Gunakan Glide untuk memuat gambar
        loadProfileImage(profilePictureUrl)
    }

    private fun loadProfileImage(url: String?) {
        if (!url.isNullOrEmpty()) {
            Log.d("ProfileFragment", "Loading profile image from URL: $url")
            Glide.with(this)
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                )
                .into(binding.ivprofile)
        } else {
            Log.d("ProfileFragment", "Profile picture URL is null or empty")
            // Tampilkan placeholder atau penanganan lainnya sesuai kebutuhan
        }
    }


    private fun getUserProfilePictureUrl(): String? {
        // Return URL profile picture user, bisa null atau string URL
        return null // Misalnya, untuk testing
    }
}
