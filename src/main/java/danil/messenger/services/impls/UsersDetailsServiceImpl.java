package danil.messenger.services.impls;

import danil.messenger.models.User;
import danil.messenger.repositories.UserRepository;
import danil.messenger.secuirty.UsersDetails;
import danil.messenger.services.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UsersDetailsServiceImpl implements UsersDetailsService {

    private final UserRepository userRepo;

    @Autowired
    public UsersDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepo.findByUsername(username);
        if (user.isEmpty())
        {
            throw new UsernameNotFoundException("User not found");
        }
        return new UsersDetails(user.get());
    }
}
