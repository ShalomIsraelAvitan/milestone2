package israela.milestone2;

import org.springframework.stereotype.Service;

@Service
public class UserServise {
    
    private UserRepository userRepo;

    public UserServise(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    
    public User getUserById(Long id)
    {
        return userRepo.findUserById(id);
    }

    public boolean addUser(User newUser)
    {
        try {
            
            if(newUser.getId()==111111111)
            {
                newUser.setAdmin(true);
            }
            else{
                newUser.setAdmin(false);
            }
            userRepo.insert(newUser);
            return true;
            
        } catch (Exception e) {
            
            System.out.println("\nERROR======>addUser\n");
            return false;
        }
    }

    public boolean deletUser(User user)
    {
        try {
            userRepo.delete(user);
            return true;
        } catch (Exception e) {
            
            return false;
        }
    }
    public boolean isUserExists(String name, Long id, int pw)
    {
        User user = userRepo.findUserById(id);
        //User user = userRepo.findByName(name);
        if(user!=null)
            if(user.getName()==name && user.getPw()==pw)
                return true;
        return false;
    }

    public boolean isIdUsd(Long id)
    {
        User user = userRepo.findUserById(id);

        if(user!=null)
        {
            return true;

        }
        return false;
    }


    public boolean isUserExistsLogin(String userName, Long idUser, int password) {
        User user = userRepo.findByName(userName);
        if(user!=null)
        {
            if(user.getName().equals(userName))
            {
                if(user.getPw()==password)
                return true;
            }
        }
        return false;

    }

}
