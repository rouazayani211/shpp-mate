import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop_mate.UserRepository
import com.example.shop_mate.LoginResponse
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Fonction de connexion
    fun loginUser(email: String, password: String, onResult: (Result<LoginResponse>) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.loginUser(email, password)
            onResult(result)
        }
    }
}
