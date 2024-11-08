package digitador.security;



import digitador.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;


@Getter
public class UserPrincipal {
    private Integer userId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream().map(role -> {
            return new SimpleGrantedAuthority("ROLE_".concat(role.getName()));
        }).collect(Collectors.toList());
    }

    public  static UserPrincipal create(User user){
        return new UserPrincipal(user);
    }
}
