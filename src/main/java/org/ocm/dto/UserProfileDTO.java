package org.ocm.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user_profile")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDTO implements Serializable {
    @Id
    @Column(name = "USERNAME")
    @Size(min=8, max = 255, message = "USERNAME must be between {min} and {max} characters")
    @NotBlank(message = "USERNAME is missing")
    private String username;

    @Transient
    private String password;

    @Column(name = "FIRST_NAME")
    //@Size(min=0, max = 255, message = "FIRST NAME  cannot have more than 255 characters")
    private String firstName;

    @Column(name = "LAST_NAME")
    //@Size(min=0, max = 255, message = "LAST NAME  cannot have more than 255 characters")
    private String lastName;

    //@Size(min=10, max = 255, message = "EMAIL cannot have more than 255 characters")
    @Column(name = "EMAIL")
    @Email(message = "Provide a valid email")
    private String email;

}

