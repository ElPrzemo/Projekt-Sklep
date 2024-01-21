
package com.example.projektsklep.model.entities.role;

import com.example.projektsklep.model.enums.AdminOrUser;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING) // Używamy EnumType.STRING, aby zapisywać nazwy enumów jako String
    private AdminOrUser roleType;

    public static Role fromAdminOrUser(AdminOrUser adminOrUser) {
        return Role.builder().roleType(AdminOrUser.valueOf(adminOrUser.name())).build();
    }
}


