package com.giant.mindplates.biz.user.entity;

import com.giant.mindplates.framework.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Builder
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends AbstractEntity {

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

    @NotBlank
    @Length(min = 2, max = 100)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "activate_yn", nullable = false)
    private Boolean activateYn;

    @Column(name = "use_yn", nullable = false)
    private Boolean useYn;

    @Column(name = "delete_yn", nullable = false)
    private Boolean deleteYn;

    @Column(name = "picture_path")
    private String picturePath;
}
