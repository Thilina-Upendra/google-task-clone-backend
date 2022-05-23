package lk.ijse.dep8.tasks.security;

import lk.ijse.dep8.tasks.dto.UserDTO;

public class SecurityContextHolder {

    private static volatile ThreadLocal<UserDTO> principle = new ThreadLocal<>();

    public static void setPrinciple(UserDTO user){
        principle.set(user);
    }

    public static UserDTO getPrinciple(){
        return principle.get();
    }
}
