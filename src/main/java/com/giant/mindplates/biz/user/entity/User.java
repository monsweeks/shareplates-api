package com.giant.mindplates.biz.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import com.giant.mindplates.common.data.domain.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends CommonEntity {

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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "delete_yn", nullable = false)
    private Boolean deleteYn;

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
}
