package com.msws.shareplates.biz.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.msws.shareplates.biz.organization.entity.Organization;
import com.msws.shareplates.common.data.domain.CommonEntity;
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

    public interface ValidationCreate {

    }

    public interface ValidationUpdate {

    }

    public User(Long id, String email, String name, String info, String dateTimeFormat, String language ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.info = info;
        this.dateTimeFormat = dateTimeFormat;
        this.language = language;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @NotBlank(groups = ValidationCreate.class)
    @Length(min = 2, max = 100, groups = ValidationCreate.class)
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(groups = {ValidationCreate.class, ValidationUpdate.class})
    @Length(min = 2, max = 100, groups = {ValidationCreate.class, ValidationUpdate.class})
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "salt", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    @NotBlank(groups = ValidationCreate.class)
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

    @Column(columnDefinition = "text", name = "info")
    private String info;

    @Column(name = "date_time_format")
    private String dateTimeFormat;

    @Column(name = "language")
    private String language;

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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "allow_search__yn")
    private Boolean allowSearchYn;
}
