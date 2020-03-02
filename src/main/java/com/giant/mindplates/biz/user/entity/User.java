package com.giant.mindplates.biz.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.common.data.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends CommonEntity {

    public User(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @NotBlank
    @Length(min = 2, max = 100)
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Length(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "salt", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    @NotBlank
    @Length(min = 2, max = 100)
    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "activate_yn", nullable = false)
    private Boolean activateYn;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "use_yn", nullable = false)
    private Boolean useYn;

    @Column(name = "picture_path")
    private String picturePath;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "activation_token")
    private String activationToken;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "activate_mail_send_result")
    private Boolean activateMailSendResult;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "recovery_token")
    private String recoveryToken;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "recovery_mail_send_result")
    private Boolean recoveryMailSendResult;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinTable(
            name = "organization_user",
            inverseJoinColumns = @JoinColumn(name = "organization_id"),
            joinColumns = @JoinColumn(name = "user_id")
    )
    List<Organization> organizations = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "allow_search__yn")
    private Boolean allowSearchYn;
}
