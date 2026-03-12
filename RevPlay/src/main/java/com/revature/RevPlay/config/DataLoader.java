package com.revature.RevPlay.config;

import com.revature.RevPlay.Enum.RoleName;
import com.revature.RevPlay.model.Role;
import com.revature.RevPlay.model.User;
import com.revature.RevPlay.repository.RoleRepository;
import com.revature.RevPlay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (roleRepository.count() == 0) {

            roleRepository.save(Role.builder().name(RoleName.USER).build());
            roleRepository.save(Role.builder().name(RoleName.ARTIST).build());
            roleRepository.save(Role.builder().name(RoleName.ADMIN).build());
        }

        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@taskmanager.com");
            admin.setPassword(passwordEncoder.encode("admin123"));


            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            Role userRole = roleRepository.findByName(RoleName.USER)
                    .orElseThrow(() -> new RuntimeException("USER role not found"));

            admin.setRoles(Set.of(adminRole, userRole));

            admin.setEnabled(true);

            userRepository.save(admin);

            System.out.println("Admin created → username: admin | password: admin123");
        }
    }
}
