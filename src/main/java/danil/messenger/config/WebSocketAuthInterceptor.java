package danil.messenger.config;

import danil.messenger.services.JwtService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSocketAuthInterceptor(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand()))
        {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer "))
            {
                String token = authHeader.substring(7);
                if (jwtService.isTokenValid(token))
                {
                    String username = jwtService.extractUsername(token);
                    String role = jwtService.extractRole(token);

                    List<GrantedAuthority> authorities = role != null ? List.of(new SimpleGrantedAuthority("ROLE_" + role)) : Collections.emptyList();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    accessor.setUser(authentication);
                }
            }
        }
        return message;
    }
}
