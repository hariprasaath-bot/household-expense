package in.house.financial.entity;

import jakarta.persistence.*;

@Entity
@Table(name="user_roles")
public class UserRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}